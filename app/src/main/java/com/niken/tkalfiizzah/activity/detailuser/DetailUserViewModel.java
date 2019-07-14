package com.niken.tkalfiizzah.activity.detailuser;

import android.app.Activity;
import android.content.Context;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.niken.tkalfiizzah.Constant;
import com.niken.tkalfiizzah.R;
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


public class DetailUserViewModel extends BaseViewModel {

    private final OnExecuteListener<List<Siswa>> listener;
    private final User user = new User();

    public DetailUserViewModel(Context context, OnExecuteListener<List<Siswa>> listener) {
        super(context);
        this.listener = listener;
    }

    public void setUser(String c_USER) {
        user.setC_USER(c_USER);
        getCompositeDisposable().add(
                downloadWali()
                        .subscribe(aBoolean -> {
                        }, throwable -> throwable.printStackTrace())
        );
        getCompositeDisposable().add(
                downloadSiswa()
                        .subscribe(listener::onExecuted,
                                throwable -> throwable.printStackTrace())
        );
    }

    public void onUserGroupChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.group_guru)
            user.setC_GROUP(1);
        else
            user.setC_GROUP(2);
    }

    public void onJenkelChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.jenkel_l)
            user.setC_JENKEL("L");
        else
            user.setC_JENKEL("P");
    }

    public void onStatusChanged(CompoundButton buttonView, boolean isChecked) {
        user.setC_STATUS(isChecked ? 1 : 0);
    }

    public User getUser() {
        return user;
    }

    public void saveWali(List<Siswa> siswas) {
        getCompositeDisposable().add(
                uploadUser(siswas)
                        .subscribe(result -> {
                            if (result) {
                                ((Activity) getContext()).finish();
                                Toast.makeText(getContext(), "Data Tersimpan", Toast.LENGTH_SHORT).show();
                            }
                        }, throwable -> throwable.printStackTrace())
        );
        Toast.makeText(getContext(), "Tersimpan", Toast.LENGTH_SHORT).show();
    }

    private Observable<Boolean> uploadUser(List<Siswa> siswas) {
        Map<String, String> param = new HashMap<>();
        param.put("C_LOGIN", user.getC_LOGIN());
        param.put("V_PASSWORD", user.getV_PASSWORD());
        param.put("V_NAMA", user.getV_NAMA());
        param.put("V_ALAMAT", user.getV_ALAMAT());
        param.put("C_GROUP", String.valueOf(user.getC_GROUP()));
        param.put("C_STATUS", String.valueOf(user.getC_STATUS()));

        param.put("V_PHONE1", user.getV_PHONE1());
        param.put("V_PHONE2", user.getV_PHONE2());
        param.put("V_TELP", user.getV_TELP());
        param.put("V_MAIL", user.getV_MAIL());


        if (user.getC_USER() != null)
            param.put("C_USER", user.getC_USER());

        return Rx2AndroidNetworking.post(Constant.URL_UBAH_USER)
                .addBodyParameter(param)
                .build()
                .getJSONObjectObservable()
                .flatMap(object -> {
                    String c_user = user.getC_USER() != null ?
                            user.getC_USER() :
                            object.getJSONArray("LastInsertId").getJSONObject(0)
                                    .getString("C_USER");
                    return uploadHubunganSiswa(c_user, siswas);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<Boolean> uploadHubunganSiswa(String c_USER, List<Siswa> siswas) {
        List<Observable<Boolean>> obsList = new ArrayList<>();

        for (int i = 0; i < siswas.size(); i++) {
            Map<String, String> param = new HashMap<>();
            param.put("C_SISWA", siswas.get(i).getC_SISWA());
            param.put("C_USER", c_USER);
            param.put("C_HUBUNGAN", String.valueOf(user.getC_HUBUNGAN()));


            Observable<Boolean> booleanObservable = Rx2AndroidNetworking.post(Constant.URL_UBAH_STATUS)
                    .addBodyParameter(param)
                    .build()
                    .getJSONObjectObservable()
                    .map(object -> object.optBoolean("success"))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

            obsList.add(booleanObservable);
        }

        return Observable.zip(obsList, objects -> {

            boolean results = true;
            for (Object object : objects) {
                boolean result = (boolean) object;
                if (!result) {
                    results = false;
                    break;
                }
            }

            return results;
        });
    }

    private Observable<Boolean> downloadWali() {
        return Rx2AndroidNetworking.post(Constant.URL_WALI)
                .addBodyParameter("C_USER", user.getC_USER())
                .build()
                .getJSONObjectObservable()
                .map(object -> {
                    JSONArray array = object.getJSONArray("DataRow");
                    User user = User.fromJson(array.getJSONObject(0));
                    this.user.copy(user);
                    return true;
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<List<Siswa>> downloadSiswa() {
        return Rx2AndroidNetworking.post(Constant.URL_WALI_STATUS)
                .addBodyParameter("C_USER", user.getC_USER())
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


}
