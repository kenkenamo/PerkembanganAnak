package com.niken.tkalfiizzah.activity.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.niken.tkalfiizzah.BR;
import com.niken.tkalfiizzah.R;
import com.niken.tkalfiizzah.base.BaseAccessActivity;
import com.niken.tkalfiizzah.base.BaseFragment;
import com.niken.tkalfiizzah.databinding.ActivityHomeBinding;
import com.niken.tkalfiizzah.fragment.hasil.HasilFragment;
import com.niken.tkalfiizzah.fragment.jadwal.JadwalFragment;
import com.niken.tkalfiizzah.fragment.jadwalpending.JadwalPendingFragment;
import com.niken.tkalfiizzah.fragment.jadwalsiswa.JadwalSiswaFragment;
import com.niken.tkalfiizzah.fragment.masterdata.MasterDataFragment;
import com.niken.tkalfiizzah.fragment.profile.ProfileFragment;


public class HomeActivity extends BaseAccessActivity<ActivityHomeBinding, HomeViewModel>
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    private HomeViewModel viewModel;

    public static void start(Context context) {
        context.startActivity(new Intent(context, HomeActivity.class));
    }

    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    public HomeViewModel getViewModel() {
        if (viewModel == null)
            viewModel = new HomeViewModel(this);
        return viewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(getBinding().toolbar);
        getBinding().bottomNavigation.setOnNavigationItemSelectedListener(this);

        if (viewModel.getSessionHandler().get("GROUP", 0) == 2) {
            getBinding().bottomNavigation.getMenu().clear();
            getBinding().bottomNavigation.inflateMenu(R.menu.menu_nav_wali);
        }
    }


    private void updateFragment() {
        Fragment root = getSupportFragmentManager().findFragmentById(R.id.fragment_root);

        if (root == null) {
            if (viewModel.getSessionHandler().get("GROUP", 0) == 2)
                loadFragment(new JadwalSiswaFragment());
            else
                loadFragment(new JadwalFragment());
            return;
        }

        switch (getBinding().bottomNavigation.getSelectedItemId()) {
            case R.id.menu_jadwal:
                if (viewModel.getSessionHandler().get("GROUP", 0) == 2)
                    loadFragment(new JadwalSiswaFragment());
                else
                    loadFragment(new JadwalFragment());
                break;
            case R.id.menu_hasil:
                loadFragment(new JadwalPendingFragment());
                break;
            case R.id.menu_kelas:
                loadFragment(new HasilFragment());
                break;
            case R.id.menu_wali:
                loadFragment(new MasterDataFragment());
                break;
            case R.id.menu_account:
                loadFragment(new ProfileFragment());
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateFragment();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_jadwal:
                if (viewModel.getSessionHandler().get("GROUP", 0) == 2)
                    loadFragment(new JadwalSiswaFragment());
                else
                    loadFragment(new JadwalFragment());
                break;
            case R.id.menu_hasil:
                loadFragment(new JadwalPendingFragment());
                break;
            case R.id.menu_kelas:
                loadFragment(new HasilFragment());
                break;
            case R.id.menu_wali:
                loadFragment(new MasterDataFragment());
                break;
            case R.id.menu_account:
                loadFragment(new ProfileFragment());
                break;
        }
        return true;
    }

    private void loadFragment(BaseFragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_root, fragment)
                .commit();
    }
}
