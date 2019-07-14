package com.niken.tkalfiizzah.fragment.jadwalsiswa;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;

import com.google.android.material.snackbar.Snackbar;
import com.niken.tkalfiizzah.BR;
import com.niken.tkalfiizzah.R;
import com.niken.tkalfiizzah.Utils;
import com.niken.tkalfiizzah.base.BaseFragment;
import com.niken.tkalfiizzah.base.OnExecuteListener;
import com.niken.tkalfiizzah.databinding.FragmentJadwalSiswaBinding;
import com.niken.tkalfiizzah.model.Jadwal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Firman on 4/17/2019.
 */
public class JadwalSiswaFragment extends BaseFragment<FragmentJadwalSiswaBinding, JadwalSiswaViewModel>
        implements OnExecuteListener<List<Jadwal>> {

    private Snackbar snackbar;

    private final OnExecuteListener<Date> gantiTanggalListener = new OnExecuteListener<Date>() {
        @Override
        public void onExecuted(Date date) {
            if (isVisible()) {
                setSubTitle(Utils.formatDate(date, "EEEE, dd MMM yyyy"));

            }
        }

        @Override
        public void onError(Throwable throwable) {

        }
    };
    private List<Jadwal> jadwalList = new ArrayList<>();
    private JadwalSiswaAdapter adapter;
    private JadwalSiswaViewModel viewModel;

    @Override
    protected int getBindingVariable() {
        return BR.vm;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_jadwal_siswa;
    }

    @Override
    protected JadwalSiswaViewModel getViewModel() {
        if (viewModel == null)
            viewModel = new JadwalSiswaViewModel(getActivity(), this, gantiTanggalListener);
        return viewModel;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new JadwalSiswaAdapter(jadwalList);
        getBinding().listData.setHasFixedSize(true);
        getBinding().listData.setItemAnimator(new DefaultItemAnimator());
        getBinding().listData.setAdapter(adapter);
        viewModel.refresh();
    }

    @Override
    public void onExecuted(List<Jadwal> jadwals) {
        Log.d(getTag(), "data size=" + jadwals.size());
        this.jadwalList.clear();
        this.jadwalList.addAll(jadwals);
        adapter.updateDataSet();
    }

    @Override
    public void onError(Throwable throwable) {

        throwable.printStackTrace();
    }
}
