package com.niken.tkalfiizzah.activity.login;


import android.content.Context;

import androidx.databinding.ObservableField;

import com.niken.tkalfiizzah.Constant;
import com.niken.tkalfiizzah.base.BaseViewModel;
import com.niken.tkalfiizzah.base.OnExecuteListener;
import com.rx2androidnetworking.Rx2AndroidNetworking;

import org.json.JSONObject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class LoginViewModel extends BaseViewModel {


    private final OnExecuteListener<Boolean> loginListener;
    private ObservableField<String> username = new ObservableField<>();
    private ObservableField<String> password = new ObservableField<>();

    public LoginViewModel(Context context, OnExecuteListener<Boolean> loginListener) {
        super(context);
        this.loginListener = loginListener;
    }

    public ObservableField<String> getUsername() {
        return username;
    }

    public void setUsername(ObservableField<String> username) {
        this.username = username;
    }

    public ObservableField<String> getPassword() {
        return password;
    }

    public void setPassword(ObservableField<String> password) {
        this.password = password;
    }

    public void register() {

    }

    public void login() {
        getCompositeDisposable().clear();

        getCompositeDisposable().add(
                doLogin(username.get(), password.get())
                        .subscribe(aBoolean -> {
                            if (aBoolean)
                                loginListener.onExecuted(true);
                            else
                                loginListener.onExecuted(false);
                        }, throwable -> {
                            loginListener.onError(throwable);
                        })
        );
    }

    private Observable<Boolean> doLogin(String username, String password) {

        return Rx2AndroidNetworking.post(Constant.URL_LOGIN)
                .addBodyParameter("C_LOGIN", username)
                .addBodyParameter("V_PASSWORD", password)
                .build()
                .getJSONObjectObservable()
                .map(jsonObject -> {
                    if (jsonObject.optBoolean("success")) {
                        JSONObject profile = jsonObject.getJSONObject("profile");
                        getSessionHandler().login(profile.optString("C_USER"),
                                profile.optString("C_LOGIN"),
                                profile.optString("V_PASSWORD"),
                                profile.optString("V_NAMA"),
                                profile.optString("V_ALAMAT"),
                                profile.optInt("C_GROUP"));
                    }
                    return jsonObject.optBoolean("success");
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
