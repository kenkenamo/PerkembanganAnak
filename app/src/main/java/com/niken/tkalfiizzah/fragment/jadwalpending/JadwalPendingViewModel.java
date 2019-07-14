package com.niken.tkalfiizzah.fragment.jadwalpending;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.niken.tkalfiizzah.Constant;
import com.niken.tkalfiizzah.activity.detailjadwalpending.DetailJadwalPendingActivity;
import com.niken.tkalfiizzah.base.BaseViewModel;
import com.niken.tkalfiizzah.base.OnExecuteListener;
import com.niken.tkalfiizzah.model.HasilJadwal;
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
public class JadwalPendingViewModel extends BaseViewModel {

    private final OnExecuteListener<List<HasilJadwal>> listener;

    public JadwalPendingViewModel(Context context, OnExecuteListener<List<HasilJadwal>> listener) {
        super(context);
        this.listener = listener;
    }

    public void buatKelas(View view) {
        DetailJadwalPendingActivity.start(view.getContext(), null);
    }

    public void refresh() {
        getCompositeDisposable().clear();
        getCompositeDisposable().add(
                getJadwal().subscribe(hasilKegiatanSiswas -> {
                    Log.d(getClass().getSimpleName(), "size = " + hasilKegiatanSiswas.size());
                    listener.onExecuted(hasilKegiatanSiswas);
                }, throwable -> {
                    listener.onError(throwable);
                })
        );
    }


    private Observable<List<HasilJadwal>> getJadwal() {
        return Rx2AndroidNetworking.post(Constant.URL_HASIL_KEGIATAN)
                .build()
                .getJSONObjectObservable()
                .map(jsonObject -> {
                    JSONArray array = jsonObject.getJSONArray("DataRow");
                    List<HasilJadwal> jadwals = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        jadwals.add(HasilJadwal.fromJson(array.getJSONObject(i)));
                    }
                    return jadwals;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
