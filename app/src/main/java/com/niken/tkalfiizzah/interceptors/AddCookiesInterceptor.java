package com.niken.tkalfiizzah.interceptors;

import androidx.annotation.NonNull;

import com.niken.tkalfiizzah.Constant;
import com.niken.tkalfiizzah.SessionHandler;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Firman on 4/15/2019.
 */
public class AddCookiesInterceptor implements Interceptor {

    private final SessionHandler sessionHandler;

    public AddCookiesInterceptor(SessionHandler sessionHandler) {
        this.sessionHandler = sessionHandler;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {

        Set<String> cookies = sessionHandler.get(Constant.KEY_COOKIES, new HashSet<>());

        Request.Builder builder = chain.request().newBuilder();
        for (String cookie : cookies) {
            builder.addHeader("Cookie", cookie);
        }

        Request originalRequest = builder.build();

        Response originalResponse = chain.proceed(originalRequest);

        if (!originalResponse.isSuccessful()) {
            RequestBody body = new FormBody.Builder()
                    .add("C_LOGIN", sessionHandler.getString(Constant.KEY_LOGIN, null))
                    .add("V_PASSWORD", sessionHandler.getString(Constant.KEY_PASSWORD, null))
                    .build();


            //prepare an authentication request
            Request authRequest = originalRequest.newBuilder()
                    .url(Constant.URL_LOGIN)
                    .post(body)
                    .build();

            Response authResponse = chain.proceed(authRequest);

            if (authResponse.isSuccessful()) {
                originalResponse = chain.proceed(originalRequest);
            } else
                sessionHandler.put(Constant.KEY_ISLOGIN, false);
        }


        return originalResponse;
    }
}
