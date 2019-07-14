package com.niken.tkalfiizzah.interceptors;

import androidx.annotation.NonNull;

import com.niken.tkalfiizzah.Constant;
import com.niken.tkalfiizzah.SessionHandler;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by Firman on 4/16/2019.
 */
public class ReceivedCookiesInterceptor implements Interceptor {

    private final SessionHandler sessionHandler;

    public ReceivedCookiesInterceptor(SessionHandler sessionHandler) {
        this.sessionHandler = sessionHandler;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            Set<String> cookies = new HashSet<>(originalResponse.headers("Set-Cookie"));
            sessionHandler.put(Constant.KEY_COOKIES, cookies);
        }

        return originalResponse;
    }
}