package com.niken.tkalfiizzah.activity.detailjadwalpending;

import android.content.Context;

import com.niken.tkalfiizzah.Constant;
import com.niken.tkalfiizzah.Utils;
import com.niken.tkalfiizzah.base.BaseViewModel;
import com.niken.tkalfiizzah.base.OnExecuteListener;
import com.niken.tkalfiizzah.model.HasilJadwal;
import com.niken.tkalfiizzah.model.HasilJadwalSiswa;
import com.niken.tkalfiizzah.model.Jadwal;
import com.rx2androidnetworking.Rx2AndroidNetworking;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class DetailJadwalPendingViewModel extends BaseViewModel {

    private static final String TAG = DetailJadwalPendingViewModel.class.getSimpleName();
    private final OnExecuteListener<List<HasilJadwalSiswa>> listener;
    private final HasilJadwal hasilJadwal = new HasilJadwal();


    public DetailJadwalPendingViewModel(Context context, OnExecuteListener<List<HasilJadwalSiswa>> listener) {
        super(context);
        this.listener = listener;

    }

    public void setHasilKegiatanSiswa(String c_JADWAL) {
        hasilJadwal.setC_JADWAL(c_JADWAL);
        getCompositeDisposable().add(
                downloadHasilKegiatan()
                        .subscribe(aBoolean -> {
                        }, Throwable::printStackTrace)
        );

        getCompositeDisposable().add(
                downloadHasilKegiatanSiswa()
                        .subscribe(listener::onExecuted,
                                listener::onError)
        );
    }

    public HasilJadwal getHasilJadwal() {
        return hasilJadwal;
    }

    private Observable<Boolean> downloadHasilKegiatan() {
        return Rx2AndroidNetworking.post(Constant.URL_JADWAL)
                .addBodyParameter("C_JADWAL", hasilJadwal.getC_JADWAL())
                .build()
                .getJSONObjectObservable()
                .map(object -> {
                    JSONArray array = object.getJSONArray("DataRow");
                    Jadwal jadwal = Jadwal.fromJson(array.getJSONObject(0));
                    this.hasilJadwal.setC_NAMA(jadwal.getC_NAMA());
                    this.hasilJadwal.setD_TANGGAL(jadwal.getD_TANGGAL());
                    this.hasilJadwal.setD_SELESAI(jadwal.getD_SELESAI());
                    this.hasilJadwal.setD_MULAI(jadwal.getD_MULAI());
                    this.hasilJadwal.setC_STATUS(jadwal.getC_STATUS());
                    this.hasilJadwal.setKelas(jadwal.getKelas());
                    this.hasilJadwal.setGuru(jadwal.getGuru());
                    return true;
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<List<HasilJadwalSiswa>> downloadHasilKegiatanSiswa() {
        return Rx2AndroidNetworking.post(Constant.URL_HASIL_KEGIATAN)
                .addBodyParameter("C_JADWAL", hasilJadwal.getC_JADWAL())
                .build()
                .getJSONObjectObservable()
                .map(object -> {
                    JSONArray array = object.getJSONArray("DataRow");
                    List<HasilJadwalSiswa> hasilJadwalSiswas = new ArrayList<>();

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject item = array.getJSONObject(i);
                        HasilJadwalSiswa hasilJadwalSiswa = HasilJadwalSiswa.fromJson(item);
                        hasilJadwalSiswas.add(hasilJadwalSiswa);
                    }

                    return hasilJadwalSiswas;
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<Boolean> uploadJadwal() {
        Map<String, String> param = new HashMap<>();
        param.put("C_NAMA", hasilJadwal.getC_NAMA());
        param.put("D_TANGGAL", Utils.formatDate(hasilJadwal.getD_TANGGAL(), "yyyy-MM-dd"));
        param.put("D_MULAI", Utils.formatDate(hasilJadwal.getD_MULAI(), "HH:mm:ss"));
        param.put("D_SELESAI", Utils.formatDate(hasilJadwal.getD_SELESAI(), "HH:mm:ss"));
        param.put("C_STATUS", String.valueOf(hasilJadwal.getC_STATUS()));
        param.put("C_KELAS", hasilJadwal.getKelas().getC_KELAS());
        param.put("C_USER", hasilJadwal.getGuru().getC_USER());


        if (hasilJadwal.getC_JADWAL() != null)
            param.put("C_JADWAL", hasilJadwal.getC_JADWAL());


        return Rx2AndroidNetworking.post(Constant.URL_UBAH_JADWAL)
                .build()
                .getJSONObjectObservable()
                .map(object -> object.optBoolean("success"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
