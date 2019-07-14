package com.niken.tkalfiizzah.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

/**
 * Created by firmansyah on 4/25/2018.
 */

public abstract class BaseFragment<T extends ViewDataBinding, V extends BaseViewModel>
        extends Fragment {

    private T binding;

    protected abstract int getBindingVariable();

    @LayoutRes
    protected abstract int getLayoutId();

    protected abstract V getViewModel();

    public T getBinding() {
        return binding;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getLayoutId() == -1 || getBindingVariable() == -1)
            return super.onCreateView(inflater, container, savedInstanceState);

        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);

        if (getViewModel() != null) {
            binding.setVariable(getBindingVariable(), getViewModel());
            binding.executePendingBindings();
        }
        return binding.getRoot();
    }

    protected void setTitle(@StringRes int resId) {
        if (getActivity() != null) {
            ActionBar bar = ((BaseActivity) getActivity()).getSupportActionBar();

            if (bar != null)
                bar.setTitle(resId);
        }
    }

    protected void setTitle(CharSequence charSequence) {
        if (getActivity() != null) {
            ActionBar bar = ((BaseActivity) getActivity()).getSupportActionBar();

            if (bar != null)
                bar.setTitle(charSequence);
        }
    }

    protected void setSubTitle(@StringRes int resId) {
        if (getActivity() != null) {
            ActionBar bar = ((BaseActivity) getActivity()).getSupportActionBar();

            if (bar != null)
                bar.setSubtitle(resId);
        }
    }

    protected void setSubTitle(CharSequence charSequence) {
        if (getActivity() != null) {
            ActionBar bar = ((BaseActivity) getActivity()).getSupportActionBar();

            if (bar != null)
                bar.setSubtitle(charSequence);
        }
    }

}
