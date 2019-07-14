package com.niken.tkalfiizzah.fragment.masterdata;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.niken.tkalfiizzah.BR;
import com.niken.tkalfiizzah.R;
import com.niken.tkalfiizzah.base.BaseFragment;
import com.niken.tkalfiizzah.base.OnExecuteListener;
import com.niken.tkalfiizzah.databinding.FragmentMasterDataBinding;
import com.niken.tkalfiizzah.model.Kelas;
import com.niken.tkalfiizzah.model.Siswa;
import com.niken.tkalfiizzah.model.User;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Firman on 4/17/2019.
 */
public class MasterDataFragment extends BaseFragment<FragmentMasterDataBinding, MasterDataViewModel> {


    private final List<Siswa> siswaList = new ArrayList<>();
    private final List<User> userList = new ArrayList<>();
    private final List<Kelas> kelasList = new ArrayList<>();


    private MasterDataViewModel viewModel;
    private SiswaAdapter siswaAdapter;
    private WaliAdapter waliAdapter;
    private KelasAdapter kelasAdapter;


    @Override
    protected int getBindingVariable() {
        return BR.vm;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_master_data;
    }

    @Override
    protected MasterDataViewModel getViewModel() {
        if (viewModel == null)
            viewModel = new MasterDataViewModel(getActivity(), siswaListener, waliListener, kelasListener);
        return viewModel;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        siswaAdapter = new SiswaAdapter(siswaList);
        waliAdapter = new WaliAdapter(userList);
        kelasAdapter = new KelasAdapter(kelasList);

        getBinding().listData.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration decoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.divider_item));
        getBinding().listData.addItemDecoration(decoration);
    }

    @Override
    public void onResume() {
        super.onResume();

        viewModel.onMasterSelected(getBinding().spinner.getSelectedItemPosition());
    }

    private final OnExecuteListener<List<Siswa>> siswaListener = new OnExecuteListener<List<Siswa>>() {
        @Override
        public void onExecuted(List<Siswa> siswa) {
            siswaList.clear();
            siswaList.addAll(siswa);
            siswaAdapter.updateDataSet();
            getBinding().listData.setAdapter(siswaAdapter);
        }

        @Override
        public void onError(Throwable throwable) {
            throwable.printStackTrace();
            Toast.makeText(getContext(), "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
        }
    };

    private final OnExecuteListener<List<User>> waliListener = new OnExecuteListener<List<User>>() {
        @Override
        public void onExecuted(List<User> user) {
            userList.clear();
            userList.addAll(user);
            waliAdapter.updateDataSet();
            getBinding().listData.setAdapter(waliAdapter);
        }

        @Override
        public void onError(Throwable throwable) {
            throwable.printStackTrace();
            Toast.makeText(getContext(), "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
        }
    };

    private final OnExecuteListener<List<Kelas>> kelasListener = new OnExecuteListener<List<Kelas>>() {
        @Override
        public void onExecuted(List<Kelas> kelas) {
            kelasList.clear();
            kelasList.addAll(kelas);
            kelasAdapter.updateDataSet();
            getBinding().listData.setAdapter(kelasAdapter);
        }

        @Override
        public void onError(Throwable throwable) {
            throwable.printStackTrace();
            Toast.makeText(getContext(), "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
        }
    };
}
