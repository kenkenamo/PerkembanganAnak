package com.niken.tkalfiizzah;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.niken.tkalfiizzah.interceptors.AddCookiesInterceptor;
import com.niken.tkalfiizzah.interceptors.ReceivedCookiesInterceptor;

import okhttp3.OkHttpClient;


public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        SessionHandler sessionHandler = new SessionHandler(this);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new AddCookiesInterceptor(sessionHandler))
                .addInterceptor(new ReceivedCookiesInterceptor(sessionHandler))
                .build();

        AndroidNetworking.initialize(this, client);
        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);
    }
}
