package com.niken.tkalfiizzah.activity.chooseractivity;

import android.content.Context;

import androidx.appcompat.widget.SearchView;

import com.niken.tkalfiizzah.Constant;
import com.niken.tkalfiizzah.base.BaseViewModel;
import com.niken.tkalfiizzah.base.OnExecuteListener;
import com.niken.tkalfiizzah.model.Siswa;
import com.niken.tkalfiizzah.model.User;
import com.rx2androidnetworking.Rx2AndroidNetworking;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Firman on 7/7/2019.
 */
public class ChooserViewModel extends BaseViewModel {

    private String query = "";
    private int type;

    private final OnExecuteListener<List> listener;

    public ChooserViewModel(Context context, OnExecuteListener<List> listener) {
        super(context);
        this.listener = listener;
    }

    public void setType(int type) {
        this.type = type;
        download();
    }

    public SearchView.OnQueryTextListener queryTextListener() {
        return new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String q) {
                query = q.trim();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                query = newText.trim();
                return false;
            }
        };
    }

    public void download() {
        getCompositeDisposable().clear();
        if (type == 0) {
            getCompositeDisposable().add(
                    getListObservableUser().subscribe(users -> {
                        listener.onExecuted(users);
                    }, throwable -> {

                    })
            );
        } else {
            getCompositeDisposable().add(
                    getListObservableSiswa().subscribe(siswa -> {
                        listener.onExecuted(siswa);
                    }, throwable -> {

                    })
            );
        }

    }


    private Observable<List<Siswa>> getListObservableSiswa() {
        Map<String, String> param = new HashMap<>();

        if (!query.isEmpty()) {
            param.put("query", query);
        }
        return Rx2AndroidNetworking.post(Constant.URL_SISWA)
                .addBodyParameter(query)
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

    private Observable<List<User>> getListObservableUser() {
        Map<String, String> param = new HashMap<>();
        param.put("C_GROUP", "2");
        if (!query.isEmpty()) {
            param.put("query", query);
        }
        return Rx2AndroidNetworking.post(Constant.URL_WALI)
                .addBodyParameter(param)
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
}
