package com.niken.tkalfiizzah.base;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.niken.tkalfiizzah.SessionHandler;

import java.lang.ref.WeakReference;

import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseViewModel extends ViewModel {


    private CompositeDisposable mCompositeDisposable;

    private WeakReference<Context> mContext;
    private final SessionHandler sessionHandler;

    public BaseViewModel(Context context) {
        this.mContext = new WeakReference<>(context);
        this.mCompositeDisposable = new CompositeDisposable();
        sessionHandler = new SessionHandler(context);
    }

    @Override
    protected void onCleared() {
        mCompositeDisposable.dispose();
        super.onCleared();
    }


    public CompositeDisposable getCompositeDisposable() {
        return mCompositeDisposable;
    }

    protected Context getContext() {
        return this.mContext.get();
    }

    public SessionHandler getSessionHandler() {
        return sessionHandler;
    }

}
