package com.niken.tkalfiizzah.base;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.niken.tkalfiizzah.Utils;


public abstract class BaseActivity<T extends ViewDataBinding, V extends BaseViewModel>
        extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    private T mViewDataBinding;
    private V mViewModel;

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    public abstract int getBindingVariable();

    /**
     * @return layout resource id
     */
    public abstract
    @LayoutRes
    int getLayoutId();

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    public abstract V getViewModel();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        performDataBinding();
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//        hideKeyboard();

    }

    public T getBinding() {
        return mViewDataBinding;
    }

    public void hideKeyboard() {
        Utils.hideKeyboard(getCurrentFocus());
    }

    public void hideKeyboard(View view) {
        Utils.hideKeyboard(view);
    }

    public boolean isNetworkConnected() {
        return Utils.isNetworkConnected(getApplicationContext());
    }

    private void performDataBinding() {
        if (getLayoutId() == -1 || getViewModel() == null || getBindingVariable() == -1)
            return;
        mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId());
        mViewModel = getViewModel();
        mViewDataBinding.setVariable(getBindingVariable(), mViewModel);
        mViewDataBinding.executePendingBindings();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.mViewModel != null)
            this.mViewModel.onCleared();
    }
}