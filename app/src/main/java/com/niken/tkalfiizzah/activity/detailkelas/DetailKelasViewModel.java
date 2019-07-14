package com.niken.tkalfiizzah.activity.detailkelas;

import android.content.Context;
import android.widget.Toast;

import com.niken.tkalfiizzah.Constant;
import com.niken.tkalfiizzah.base.BaseViewModel;
import com.niken.tkalfiizzah.base.OnExecuteListener;
import com.niken.tkalfiizzah.model.Kelas;
import com.rx2androidnetworking.Rx2AndroidNetworking;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class DetailKelasViewModel extends BaseViewModel {

    private final OnExecuteListener<Boolean> listener;
    private final Kelas kelas = new Kelas();

    public DetailKelasViewModel(Context context, OnExecuteListener<Boolean> listener) {
        super(context);
        this.listener = listener;
    }

    public void setKelas(String c_KELAS) {
        kelas.setC_KELAS(c_KELAS);
        getCompositeDisposable().add(
                downloadKelas()
                        .subscribe(aBoolean -> {
                        }, throwable -> throwable.printStackTrace())
        );
    }

    public void onNameChanged(CharSequence s, int start, int before, int count) {
        kelas.setC_NAMA(s.toString());
    }

    public Kelas getKelas() {
        return kelas;
    }

    public void saveKelas() {
        getCompositeDisposable().add(
                uploadKelas()
                        .subscribe(aBoolean -> {
                        }, throwable -> throwable.printStackTrace())
        );
        Toast.makeText(getContext(), "Tersimpan", Toast.LENGTH_SHORT).show();
    }

    private Observable<Boolean> uploadKelas() {
        Map<String, String> param = new HashMap<>();
        param.put("C_NAMA", kelas.getC_NAMA());
        param.put("N_TINGKAT", String.valueOf(kelas.getN_TINGKAT()));
        param.put("C_STATUS", String.valueOf(kelas.getC_STATUS()));

        if (kelas.getC_KELAS() != null)
            param.put("C_KELAS", kelas.getC_KELAS());


        return Rx2AndroidNetworking.post(Constant.URL_UBAH_KELAS)
                .addBodyParameter(kelas)
                .build()
                .getJSONObjectObservable()
                .map(object -> object.optBoolean("success"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<Boolean> downloadKelas() {
        return Rx2AndroidNetworking.post(Constant.URL_KELAS)
                .addBodyParameter("C_KELAS", kelas.getC_KELAS())
                .build()
                .getJSONObjectObservable()
                .map(object -> {
                    JSONArray array = object.getJSONArray("DataRow");
                    Kelas kelas = Kelas.fromJson(array.getJSONObject(0));
                    this.kelas.setC_NAMA(kelas.getC_NAMA());
                    this.kelas.setC_STATUS(kelas.getC_STATUS());
                    this.kelas.setN_JUMLAH_SISWA(kelas.getN_JUMLAH_SISWA());
                    this.kelas.setN_TINGKAT(kelas.getN_TINGKAT());
                    return true;
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
