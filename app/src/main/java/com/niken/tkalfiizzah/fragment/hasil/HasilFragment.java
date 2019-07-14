package com.niken.tkalfiizzah.fragment.hasil;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.niken.tkalfiizzah.BR;
import com.niken.tkalfiizzah.R;
import com.niken.tkalfiizzah.base.BaseFragment;
import com.niken.tkalfiizzah.base.OnExecuteListener;
import com.niken.tkalfiizzah.databinding.FragmentHasilBinding;
import com.niken.tkalfiizzah.model.Siswa;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Firman on 4/17/2019.
 */
public class HasilFragment extends BaseFragment<FragmentHasilBinding, HasilViewModel>
        implements OnExecuteListener<List<Siswa>> {


    private List<Siswa> siswaList = new ArrayList<>();
    private HasilSiswaAdapter adapter;
    private HasilViewModel viewModel;

    @Override
    protected int getBindingVariable() {
        return BR.vm;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_hasil;
    }

    @Override
    protected HasilViewModel getViewModel() {
        if (viewModel == null)
            viewModel = new HasilViewModel(getActivity(), this);
        return viewModel;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new HasilSiswaAdapter(siswaList);
        getBinding().listData.setAdapter(adapter);
        viewModel.refresh();
    }

    @Override
    public void onExecuted(List<Siswa> siswas) {
        Log.d(getTag(), "data size=" + siswas.size());
        this.siswaList.clear();
        this.siswaList.addAll(siswas);
        adapter.updateDataSet();
    }

    @Override
    public void onError(Throwable throwable) {

        throwable.printStackTrace();
    }
}
