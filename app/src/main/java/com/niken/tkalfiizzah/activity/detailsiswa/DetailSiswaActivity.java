package com.niken.tkalfiizzah.activity.detailsiswa;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.niken.tkalfiizzah.BR;
import com.niken.tkalfiizzah.CustomDialog;
import com.niken.tkalfiizzah.R;
import com.niken.tkalfiizzah.activity.chooseractivity.ChooserActivity;
import com.niken.tkalfiizzah.base.BaseAccessActivity;
import com.niken.tkalfiizzah.base.OnExecuteListener;
import com.niken.tkalfiizzah.databinding.ActivityDetailSiswaBinding;
import com.niken.tkalfiizzah.fragment.masterdata.WaliAdapter;
import com.niken.tkalfiizzah.model.User;

import java.util.ArrayList;
import java.util.List;


public class DetailSiswaActivity extends BaseAccessActivity<ActivityDetailSiswaBinding, DetailSiswaViewModel>
        implements OnExecuteListener<List<User>> {

    private static final String TAG = DetailSiswaActivity.class.getSimpleName();

    private DetailSiswaViewModel viewModel;
    private List<User> userList = new ArrayList<>();
    private WaliAdapter adapter = new WaliAdapter(userList);

    public static void start(Context context, String c_SISWA) {
        Intent starter = new Intent(context, DetailSiswaActivity.class);
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
        return R.layout.activity_detail_siswa;
    }

    @Override
    public DetailSiswaViewModel getViewModel() {
        if (viewModel == null)
            viewModel = new DetailSiswaViewModel(this, this);
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

        if (getIntent().hasExtra("C_SISWA")) {
            String c_SISWA = getIntent().getStringExtra("C_SISWA");
            viewModel.setSiswa(c_SISWA);
            adapter.setClickable(false);
        }

        getBinding().button3.setOnClickListener(v -> {
            ChooserActivity.startActivityForResult(this, 1, 0);
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (getIntent().hasExtra("C_SISWA")) {
            String c_SISWA = getIntent().getStringExtra("C_SISWA");
            viewModel.setSiswa(c_SISWA);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            List<User> users = data.getParcelableArrayListExtra("data");
            this.userList.clear();
            this.userList.addAll(users);
            adapter.updateDataSet();
        }
    }

    @Override
    public void onExecuted(List<User> userList) {
        this.userList.clear();
        this.userList.addAll(userList);
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
                if (getIntent().hasExtra("C_SISWA")) {
                    String c_SISWA = getIntent().getStringExtra("C_SISWA");
                    viewModel.setSiswa(c_SISWA);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void validateBeforeSave() {
        if (TextUtils.isEmpty(getBinding().namaUserET.getText().toString())) {
            getBinding().namaUserET.setError("Nama siswa harus diisi.");
            getBinding().namaUserET.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(getBinding().tempatLahirEt.getText().toString())) {
            getBinding().tempatLahirEt.setError("Tempat lahir siswa harus diisi.");
            getBinding().tempatLahirEt.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(getBinding().tanggalLahirEt.getText().toString())) {
            getBinding().tanggalLahirEt.setError("Tanggal lahir siswa harus diisi.");
            getBinding().tanggalLahirEt.requestFocus();
            return;
        } else
            getBinding().tanggalLahirEt.setError(null);


        if (getBinding().labelJenkel.getCheckedRadioButtonId() == -1) {
            getBinding().jenkelL.setError("Mohon pilih jenis kelamin siswa.");
            getBinding().jenkelL.requestFocus();
            return;
        } else
            getBinding().jenkelL.setError(null);

        if (TextUtils.isEmpty(getBinding().tanggalMasukEt.getText().toString())) {
            getBinding().tanggalMasukEt.setError("Tanggal masuk siswa harus diisi.");
            getBinding().tanggalMasukEt.requestFocus();
            return;
        } else
            getBinding().tanggalMasukEt.setError(null);

        if (getBinding().spinner2.getSelectedItemPosition() == AdapterView.INVALID_POSITION) {
            Toast.makeText(this, "Mohon pilih kelas siswa", Toast.LENGTH_SHORT).show();
            getBinding().spinner2.requestFocus();
            return;
        }

        if (userList.isEmpty()) {
            CustomDialog.get(this)
                    .title(R.string.konfirmasi)
                    .message("Anda belum memilih wali/orang tua untuk siswa ini, apa anda yakin ingin menyimpan data ini?")
                    .addPositiveButton(R.string.ya, (dialog, which) -> {
                        viewModel.saveSiswa(userList);
                    }).addNegativeButton(R.string.cancel, null)
                    .show();
        } else {
            viewModel.saveSiswa(userList);
        }

    }
}
