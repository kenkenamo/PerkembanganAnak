package com.niken.tkalfiizzah.activity.detailkelas;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.niken.tkalfiizzah.BR;
import com.niken.tkalfiizzah.R;
import com.niken.tkalfiizzah.base.BaseAccessActivity;
import com.niken.tkalfiizzah.base.OnExecuteListener;
import com.niken.tkalfiizzah.databinding.ActivityDetailKelasBinding;


public class DetailKelasActivity extends BaseAccessActivity<ActivityDetailKelasBinding, DetailKelasViewModel>
        implements OnExecuteListener<Boolean> {

    private DetailKelasViewModel viewModel;

    public static void start(Context context, String c_KELAS) {
        Intent starter = new Intent(context, DetailKelasActivity.class);
        if (c_KELAS != null)
            starter.putExtra("C_KELAS", c_KELAS);
        context.startActivity(starter);
    }

    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_detail_kelas;
    }

    @Override
    public DetailKelasViewModel getViewModel() {
        if (viewModel == null)
            viewModel = new DetailKelasViewModel(this, this);
        return viewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(getBinding().toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().hasExtra("C_KELAS")) {
            String c_KELAS = getIntent().getStringExtra("C_KELAS");
            viewModel.setKelas(c_KELAS);
        }
    }

    @Override
    public void onExecuted(Boolean aBoolean) {

    }

    @Override
    public void onError(Throwable throwable) {

    }
}
