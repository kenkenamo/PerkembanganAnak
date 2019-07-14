package com.niken.tkalfiizzah.fragment.jadwalpending;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.niken.tkalfiizzah.BR;
import com.niken.tkalfiizzah.R;
import com.niken.tkalfiizzah.base.BaseFragment;
import com.niken.tkalfiizzah.base.OnExecuteListener;
import com.niken.tkalfiizzah.databinding.FragmentJadwalPendingBinding;
import com.niken.tkalfiizzah.model.HasilJadwal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Firman on 4/17/2019.
 */
public class JadwalPendingFragment extends BaseFragment<FragmentJadwalPendingBinding, JadwalPendingViewModel>
        implements OnExecuteListener<List<HasilJadwal>> {

    private List<HasilJadwal> jadwalList = new ArrayList<>();
    private HasilJadwalAdapter adapter;
    private JadwalPendingViewModel viewModel;

    @Override
    protected int getBindingVariable() {
        return BR.vm;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_jadwal_pending;
    }

    @Override
    protected JadwalPendingViewModel getViewModel() {
        if (viewModel == null)
            viewModel = new JadwalPendingViewModel(getActivity(), this);
        return viewModel;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new HasilJadwalAdapter(jadwalList);
        getBinding().listData.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration decoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.divider_item));
        getBinding().listData.addItemDecoration(decoration);
        getBinding().listData.setAdapter(adapter);


        viewModel.refresh();
    }

    @Override
    public void onExecuted(List<HasilJadwal> hasilJadwals) {
        Log.d(getTag(), "data size=" + hasilJadwals.size());
        this.jadwalList.clear();
        this.jadwalList.addAll(hasilJadwals);
        adapter.updateDataSet();
    }

    @Override
    public void onError(Throwable throwable) {

        throwable.printStackTrace();
    }
}
