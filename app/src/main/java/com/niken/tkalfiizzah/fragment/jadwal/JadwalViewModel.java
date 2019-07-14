package com.niken.tkalfiizzah.fragment.jadwal;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.niken.tkalfiizzah.Constant;
import com.niken.tkalfiizzah.activity.detailjadwal.DetailJadwalActivity;
import com.niken.tkalfiizzah.base.BaseViewModel;
import com.niken.tkalfiizzah.base.OnExecuteListener;
import com.niken.tkalfiizzah.model.Jadwal;
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
public class JadwalViewModel extends BaseViewModel {

    private final OnExecuteListener<List<Jadwal>> listener;

    public JadwalViewModel(Context context, OnExecuteListener<List<Jadwal>> listener) {
        super(context);
        this.listener = listener;
    }

    public void buatJadwal(View view) {
        DetailJadwalActivity.start(view.getContext(), null);
    }

    public void refresh() {
        getCompositeDisposable().clear();
        getCompositeDisposable().add(
                getJadwals().subscribe(jadwals -> {
                    Log.d(getClass().getSimpleName(), "size = " + jadwals.size());
                    listener.onExecuted(jadwals);
                }, throwable -> {
                    listener.onError(throwable);
                })
        );
    }


    private Observable<List<Jadwal>> getJadwals() {
        return Rx2AndroidNetworking.post(Constant.URL_JADWAL)
                .build()
                .getJSONObjectObservable()
                .map(jsonObject -> {
                    JSONArray array = jsonObject.getJSONArray("DataRow");
                    List<Jadwal> jadwals = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        jadwals.add(Jadwal.fromJson(array.getJSONObject(i)));
                    }
                    return jadwals;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
