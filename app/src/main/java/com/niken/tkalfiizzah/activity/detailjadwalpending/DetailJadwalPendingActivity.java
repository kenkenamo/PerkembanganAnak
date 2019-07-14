package com.niken.tkalfiizzah.activity.detailjadwalpending;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.niken.tkalfiizzah.BR;
import com.niken.tkalfiizzah.R;
import com.niken.tkalfiizzah.base.BaseAccessActivity;
import com.niken.tkalfiizzah.base.OnExecuteListener;
import com.niken.tkalfiizzah.databinding.ActivityDetailJadwalPendingBinding;
import com.niken.tkalfiizzah.model.HasilJadwalSiswa;

import java.util.ArrayList;
import java.util.List;


public class DetailJadwalPendingActivity
        extends BaseAccessActivity<ActivityDetailJadwalPendingBinding, DetailJadwalPendingViewModel>
        implements OnExecuteListener<List<HasilJadwalSiswa>> {

    private DetailJadwalPendingViewModel viewModel;
    private List<HasilJadwalSiswa> hasilJadwalSiswas = new ArrayList<>();
    private JadwalPendingSiswaAdapter adapter;

    public static void start(Context context, String c_JADWAL) {
        Intent starter = new Intent(context, DetailJadwalPendingActivity.class);
        if (c_JADWAL != null)
            starter.putExtra("C_JADWAL", c_JADWAL);
        context.startActivity(starter);
    }

    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_detail_jadwal_pending;
    }

    @Override
    public DetailJadwalPendingViewModel getViewModel() {
        if (viewModel == null)
            viewModel = new DetailJadwalPendingViewModel(this, this);
        return viewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!getIntent().hasExtra("C_JADWAL")) {
            finish();
            return;
        }
        setSupportActionBar(getBinding().toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new JadwalPendingSiswaAdapter(hasilJadwalSiswas);
        getBinding().listData.setAdapter(adapter);


        String c_JADWAL = getIntent().getStringExtra("C_JADWAL");
        adapter.setC_JADWAL(c_JADWAL);
        viewModel.setHasilKegiatanSiswa(c_JADWAL);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String c_JADWAL = getIntent().getStringExtra("C_JADWAL");
        viewModel.setHasilKegiatanSiswa(c_JADWAL);
    }

    @Override
    public void onExecuted(List<HasilJadwalSiswa> hasilJadwalSiswas) {
        this.hasilJadwalSiswas.clear();
        this.hasilJadwalSiswas.addAll(hasilJadwalSiswas);
        adapter.updateDataSet();
    }

    @Override
    public void onError(Throwable throwable) {

    }
}
