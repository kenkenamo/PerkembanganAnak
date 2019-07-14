package com.niken.tkalfiizzah.activity.hasiljadwal;

import android.content.Context;

import com.niken.tkalfiizzah.Constant;
import com.niken.tkalfiizzah.Utils;
import com.niken.tkalfiizzah.base.BaseViewModel;
import com.niken.tkalfiizzah.base.OnExecuteListener;
import com.niken.tkalfiizzah.model.HasilJadwal;
import com.niken.tkalfiizzah.model.Siswa;
import com.rx2androidnetworking.Rx2AndroidNetworking;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class HasilJadwalViewModel extends BaseViewModel {

    private static final String TAG = HasilJadwalViewModel.class.getSimpleName();
    private final OnExecuteListener<List<HasilJadwal>> listener;
    private OnExecuteListener<Siswa> siswaListener;

    private final Siswa siswa = new Siswa();


    public HasilJadwalViewModel(Context context, OnExecuteListener<List<HasilJadwal>> listener) {
        super(context);
        this.listener = listener;
    }

    public void setSiswaListener(OnExecuteListener<Siswa> siswaListener) {
        this.siswaListener = siswaListener;
    }

    public void setHasilKegiatanSiswa(String c_SISWA) {
        siswa.setC_SISWA(c_SISWA);
        getCompositeDisposable().add(
                downloadSiswa()
                        .subscribe(aBoolean -> {
                            siswaListener.onExecuted(siswa);
                        }, Throwable::printStackTrace)
        );

        getCompositeDisposable().add(
                downloadHasilJadwalSiswa()
                        .subscribe(listener::onExecuted,
                                listener::onError)
        );
    }

    public Siswa getSiswa() {
        return siswa;
    }

    private Observable<Boolean> downloadSiswa() {
        return Rx2AndroidNetworking.post(Constant.URL_SISWA)
                .addBodyParameter("C_SISWA", siswa.getC_SISWA())
                .build()
                .getJSONObjectObservable()
                .map(object -> {
                    JSONArray array = object.getJSONArray("DataRow");
                    Siswa siswa = Siswa.fromJson(array.getJSONObject(0));
                    this.siswa.setV_NAMA(siswa.getV_NAMA());
                    this.siswa.setD_TGL_LAHIR(Utils.reformatDate(siswa.getD_TGL_LAHIR(), "yyyy-MM-dd", "dd MMMM yyyy"));
                    this.siswa.setC_TEMPAT_LAHIR(siswa.getC_TEMPAT_LAHIR());
                    this.siswa.setC_JENKEL(siswa.getC_JENKEL());
                    this.siswa.setKelas(siswa.getKelas());
                    this.siswa.setD_TGL_MASUK(Utils.reformatDate(siswa.getD_TGL_MASUK(), "yyyy-MM-dd", "dd MMMM yyyy"));

                    return true;
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<List<HasilJadwal>> downloadHasilJadwalSiswa() {
        return Rx2AndroidNetworking.post(Constant.URL_HASIL_JADWAL_SISWA)
                .addBodyParameter("C_SISWA", siswa.getC_SISWA())
                .build()
                .getJSONObjectObservable()
                .map(object -> {
                    JSONArray array = object.getJSONArray("DataRow");
                    List<HasilJadwal> hasilJadwals = new ArrayList<>();

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject item = array.getJSONObject(i);
                        HasilJadwal hasilJadwal = HasilJadwal.fromJson(item);
                        hasilJadwals.add(hasilJadwal);
                    }

                    return hasilJadwals;
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
