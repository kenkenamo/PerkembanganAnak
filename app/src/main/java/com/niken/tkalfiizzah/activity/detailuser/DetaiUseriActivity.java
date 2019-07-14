package com.niken.tkalfiizzah.activity.detailuser;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.niken.tkalfiizzah.BR;
import com.niken.tkalfiizzah.Constant;
import com.niken.tkalfiizzah.CustomDialog;
import com.niken.tkalfiizzah.R;
import com.niken.tkalfiizzah.activity.chooseractivity.ChooserActivity;
import com.niken.tkalfiizzah.base.BaseAccessActivity;
import com.niken.tkalfiizzah.base.OnExecuteListener;
import com.niken.tkalfiizzah.databinding.ActivityDetailUserBinding;
import com.niken.tkalfiizzah.fragment.masterdata.SiswaAdapter;
import com.niken.tkalfiizzah.model.Siswa;

import java.util.ArrayList;
import java.util.List;


public class DetaiUseriActivity extends BaseAccessActivity<ActivityDetailUserBinding, DetailUserViewModel>
        implements OnExecuteListener<List<Siswa>> {

    private DetailUserViewModel viewModel;
    private List<Siswa> siswaList = new ArrayList<>();
    private SiswaAdapter adapter = new SiswaAdapter(siswaList);

    public static void start(Context context, String c_USER) {
        Intent starter = new Intent(context, DetaiUseriActivity.class);
        if (c_USER != null)
            starter.putExtra("C_USER", c_USER);
        context.startActivity(starter);
    }

    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_detail_user;
    }

    @Override
    public DetailUserViewModel getViewModel() {
        if (viewModel == null)
            viewModel = new DetailUserViewModel(this, this);
        return viewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(getBinding().toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getBinding().listData.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.divider_item));
        getBinding().listData.addItemDecoration(decoration);
        getBinding().listData.setAdapter(adapter);

        adapter.setEditable(true);

        if (getIntent().hasExtra("C_USER")) {
            String c_user = getIntent().getStringExtra("C_USER");
            viewModel.setUser(c_user);
            adapter.setClickable(false);
        }

        getBinding().button3.setOnClickListener(v -> {
            ChooserActivity.startActivityForResult(this, 1, 1);
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (getIntent().hasExtra("C_USER")) {
            String c_SISWA = getIntent().getStringExtra("C_USER");
            viewModel.setUser(c_SISWA);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            List<Siswa> siswas = data.getParcelableArrayListExtra("data");
            this.siswaList.clear();
            this.siswaList.addAll(siswas);
            adapter.updateDataSet();
        }
    }

    @Override
    public void onExecuted(List<Siswa> siswaList) {
        this.siswaList.clear();
        this.siswaList.addAll(siswaList);
        adapter.updateDataSet();
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                validateBeforeSave();
                break;
            case R.id.action_refresh:
                if (getIntent().hasExtra("C_USER")) {
                    String c_user = getIntent().getStringExtra("C_USER");
                    viewModel.setUser(c_user);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void validateBeforeSave() {
        if (TextUtils.isEmpty(getBinding().namaUserET.getText().toString())) {
            getBinding().namaUserET.setError("Nama user harus diisi.");
            getBinding().namaUserET.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(getBinding().passwordEt.getText().toString())) {
            getBinding().passwordEt.setError("Password user harus diisi.");
            getBinding().passwordEt.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(getBinding().fullnameEt.getText().toString())) {
            getBinding().fullnameEt.setError("Nama lengkap user harus diisi.");
            getBinding().fullnameEt.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(getBinding().fullnameEt.getText().toString())) {
            getBinding().fullnameEt.setError("Alamat lengkap user harus diisi.");
            getBinding().fullnameEt.requestFocus();
            return;
        }

        if (getBinding().labelGroup.getCheckedRadioButtonId() == -1) {
            getBinding().groupWali.setError("Mohon pilih group user.");
            getBinding().groupWali.requestFocus();
            return;
        } else
            getBinding().groupWali.setError(null);

        if (getBinding().labelJenkel.getCheckedRadioButtonId() == -1) {
            getBinding().jenkelL.setError("Mohon pilih jenis kelamin user.");
            getBinding().jenkelL.requestFocus();
            return;
        } else
            getBinding().jenkelL.setError(null);

        if (siswaList.isEmpty() && viewModel.getSessionHandler().get(Constant.KEY_GROUP, 0) == 2) {
            CustomDialog.get(this)
                    .title(R.string.konfirmasi)
                    .message("Anda belum memilih siswa untuk user ini, apa anda yakin ingin menyimpan data ini?")
                    .addPositiveButton(R.string.ya, (dialog, which) -> {
                        viewModel.saveWali(siswaList);
                    }).addNegativeButton(R.string.cancel, null)
                    .show();
        } else {
            viewModel.saveWali(siswaList);
        }

    }
}
