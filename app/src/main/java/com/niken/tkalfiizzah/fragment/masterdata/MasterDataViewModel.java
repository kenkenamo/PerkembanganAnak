package com.niken.tkalfiizzah.fragment.masterdata;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.niken.tkalfiizzah.Constant;
import com.niken.tkalfiizzah.activity.detailkelas.DetailKelasActivity;
import com.niken.tkalfiizzah.activity.detailsiswa.DetailSiswaActivity;
import com.niken.tkalfiizzah.activity.detailuser.DetaiUseriActivity;
import com.niken.tkalfiizzah.base.BaseViewModel;
import com.niken.tkalfiizzah.base.OnExecuteListener;
import com.niken.tkalfiizzah.model.Kelas;
import com.niken.tkalfiizzah.model.Siswa;
import com.niken.tkalfiizzah.model.User;
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
public class MasterDataViewModel extends BaseViewModel {

    private final OnExecuteListener<List<Siswa>> siswaListener;
    private final OnExecuteListener<List<User>> waliListener;
    private final OnExecuteListener<List<Kelas>> kelasListener;
    private int selected;


    public MasterDataViewModel(Context context, OnExecuteListener<List<Siswa>> siswaListener,
                               OnExecuteListener<List<User>> waliListener,
                               OnExecuteListener<List<Kelas>> kelasListener) {
        super(context);
        this.siswaListener = siswaListener;
        this.waliListener = waliListener;
        this.kelasListener = kelasListener;
        refresh();
    }

    public void onMasterSelected(int position) {
        this.selected = position;
        refresh();
    }

    public void onClick(View view) {
        if (selected == 0)
            DetailSiswaActivity.start(getContext(), null);
        else if (selected == 1)
            DetaiUseriActivity.start(getContext(), null);
        else
            DetailKelasActivity.start(getContext(), null);
    }

    public void refresh() {
        getCompositeDisposable().clear();

        if (selected == 0) {
            getCompositeDisposable().add(
                    getSiswa().subscribe(siswas -> {
                        Log.d(getClass().getSimpleName(), "size = " + siswas.size());
                        siswaListener.onExecuted(siswas);
                    }, throwable -> {
                        siswaListener.onError(throwable);
                    })
            );
        } else if (selected == 1) {
            getCompositeDisposable().add(
                    getWali().subscribe(walis -> {
                        Log.d(getClass().getSimpleName(), "size = " + walis.size());
                        waliListener.onExecuted(walis);
                    }, throwable -> {
                        waliListener.onError(throwable);
                    })
            );
        } else {
            getCompositeDisposable().add(
                    getKelas().subscribe(kelas -> {
                        Log.d(getClass().getSimpleName(), "size = " + kelas.size());
                        kelasListener.onExecuted(kelas);
                    }, throwable -> {
                        kelasListener.onError(throwable);
                    })
            );
        }


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

    private Observable<List<User>> getWali() {
        return Rx2AndroidNetworking.post(Constant.URL_WALI)
                .build()
                .getJSONObjectObservable()
                .map(jsonObject -> {
                    JSONArray array = jsonObject.getJSONArray("DataRow");
                    List<User> users = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        users.add(User.fromJson(array.getJSONObject(i)));
                    }
                    return users;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<List<Kelas>> getKelas() {
        return Rx2AndroidNetworking.post(Constant.URL_KELAS)
                .build()
                .getJSONObjectObservable()
                .map(jsonObject -> {
                    JSONArray array = jsonObject.getJSONArray("DataRow");
                    List<Kelas> kelas = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        kelas.add(Kelas.fromJson(array.getJSONObject(i)));
                    }
                    return kelas;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
