package com.niken.tkalfiizzah.activity.hasiljadwal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.niken.tkalfiizzah.BR;
import com.niken.tkalfiizzah.R;
import com.niken.tkalfiizzah.base.BaseAccessActivity;
import com.niken.tkalfiizzah.base.OnExecuteListener;
import com.niken.tkalfiizzah.databinding.ActivityDetailHasilJadwalBinding;
import com.niken.tkalfiizzah.model.HasilJadwal;
import com.niken.tkalfiizzah.model.Siswa;

import java.util.ArrayList;
import java.util.List;


public class HasilJadwalActivity
        extends BaseAccessActivity<ActivityDetailHasilJadwalBinding, HasilJadwalViewModel>
        implements OnExecuteListener<List<HasilJadwal>> {

    private HasilJadwalViewModel viewModel;
    private List<HasilJadwal> hasilJadwalSiswas = new ArrayList<>();
    private HasilJadwalAdapter adapter;

    public static void start(Context context, String c_SISWA) {
        Intent starter = new Intent(context, HasilJadwalActivity.class);
        if (c_SISWA != null)
            starter.putExtra("C_SISWA", c_SISWA);
        context.startActivity(starter);
    }

    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_detail_hasil_jadwal;
    }

    @Override
    public HasilJadwalViewModel getViewModel() {
        if (viewModel == null) {
            viewModel = new HasilJadwalViewModel(this, this);
            viewModel.setSiswaListener(new OnExecuteListener<Siswa>() {
                @Override
                public void onExecuted(Siswa siswa) {
                    adapter.setC_SISWA(siswa.getC_SISWA());
                    adapter.setV_NAMA(siswa.getV_NAMA());
                }

                @Override
                public void onError(Throwable throwable) {

                }
            });
        }


        return viewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!getIntent().hasExtra("C_SISWA")) {
            finish();
            return;
        }
        setSupportActionBar(getBinding().toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new HasilJadwalAdapter(hasilJadwalSiswas);
        getBinding().listData.setAdapter(adapter);


        String c_SISWA = getIntent().getStringExtra("C_SISWA");
        viewModel.setHasilKegiatanSiswa(c_SISWA);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String c_SISWA = getIntent().getStringExtra("C_SISWA");
        viewModel.setHasilKegiatanSiswa(c_SISWA);
    }

    @Override
    public void onExecuted(List<HasilJadwal> hasilJadwals) {
        this.hasilJadwalSiswas.clear();
        this.hasilJadwalSiswas.addAll(hasilJadwals);
        adapter.updateDataSet();
    }

    @Override
    public void onError(Throwable throwable) {

    }
}
