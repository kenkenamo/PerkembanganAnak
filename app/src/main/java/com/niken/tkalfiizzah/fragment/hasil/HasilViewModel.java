package com.niken.tkalfiizzah.fragment.hasil;

import android.content.Context;
import android.util.Log;

import com.niken.tkalfiizzah.Constant;
import com.niken.tkalfiizzah.base.BaseViewModel;
import com.niken.tkalfiizzah.base.OnExecuteListener;
import com.niken.tkalfiizzah.model.Siswa;
import com.rx2androidnetworking.Rx2AndroidNetworking;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Firman on 4/17/2019.
 */
public class HasilViewModel extends BaseViewModel {


    private final OnExecuteListener<List<Siswa>> listener;

    public HasilViewModel(Context context, OnExecuteListener<List<Siswa>> listener) {
        super(context);
        this.listener = listener;
    }


    public void refresh() {
        getCompositeDisposable().clear();
        getCompositeDisposable().add(
                downloadSiswa().subscribe(kegiatans -> {
                    Log.d(getClass().getSimpleName(), "size = " + kegiatans.size());
                    listener.onExecuted(kegiatans);
                }, throwable -> {
                    listener.onError(throwable);
                })
        );
    }


    private Observable<List<Siswa>> downloadSiswa() {

        if (getSessionHandler().get(Constant.KEY_GROUP, 0) == 1)
            return getSiswa();

        return Rx2AndroidNetworking.post(Constant.URL_WALI_STATUS)
                .addBodyParameter("C_USER", getSessionHandler().getString(Constant.KEY_CUSER, null))
                .addBodyParameter("ModeSiswa", "1")
                .build()
                .getJSONObjectObservable()
                .map(object -> {
                    JSONArray array = object.getJSONArray("DataRow");
                    List<Siswa> siswas = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        siswas.add(Siswa.fromJson(array.getJSONObject(i)));
                    }
                    return siswas;
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<List<Siswa>> getSiswa() {
        return Rx2AndroidNetworking.post(Constant.URL_SISWA)
                .build()
                .getJSONObjectObservable()
                .map(jsonObject -> {
                    JSONArray array = jsonObject.getJSONArray("DataRow");
                    List<Siswa> siswas = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        siswas.add(Siswa.fromJson(array.getJSONObject(i)));
                    }
                    return siswas;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
