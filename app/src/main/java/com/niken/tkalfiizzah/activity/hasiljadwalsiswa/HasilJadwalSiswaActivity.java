package com.niken.tkalfiizzah.activity.hasiljadwalsiswa;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.BitmapRequestListener;
import com.niken.tkalfiizzah.BR;
import com.niken.tkalfiizzah.Constant;
import com.niken.tkalfiizzah.R;
import com.niken.tkalfiizzah.base.BaseActivity;
import com.niken.tkalfiizzah.base.OnExecuteListener;
import com.niken.tkalfiizzah.databinding.ActivityHasilJadwalSiswaBinding;

/**
 * Created by Firman on 7/6/2019.
 */
public class HasilJadwalSiswaActivity extends BaseActivity<ActivityHasilJadwalSiswaBinding, HasilJadwalSiswaViewModel> implements OnExecuteListener<Boolean> {
    private static final String TAG = HasilJadwalSiswaActivity.class.getSimpleName();

    private HasilJadwalSiswaViewModel viewModel;

    public static void start(Context context, String c_JADWAL, String c_SISWA, String namaSiswa, int nilai) {
        Intent starter = new Intent(context, HasilJadwalSiswaActivity.class);
        starter.putExtra("C_JADWAL", c_JADWAL);
        starter.putExtra("C_SISWA", c_SISWA);
        starter.putExtra("V_NAMA", namaSiswa);
        starter.putExtra("C_NILAI", nilai);
        context.startActivity(starter);
    }


    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_hasil_jadwal_siswa;
    }

    @Override
    public HasilJadwalSiswaViewModel getViewModel() {
        if (viewModel == null)
            viewModel = new HasilJadwalSiswaViewModel(this, this);
        return viewModel;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setSupportActionBar(getBinding().toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();

        String c_JADWAL = i.getStringExtra("C_JADWAL");
        String c_SISWA = i.getStringExtra("C_SISWA");
        String namaSiswa = i.getStringExtra("V_NAMA");
        int nilai = i.getIntExtra("C_NILAI", 0);
        viewModel.setData(c_JADWAL, c_SISWA, namaSiswa);
        viewModel.getNilai().set(nilai);

        AndroidNetworking.get(Constant.URL_IMAGE)
                .addQueryParameter("C_JADWAL", c_JADWAL)
                .addQueryParameter("C_SISWA", c_SISWA)
                .build()
                .getAsBitmap(new BitmapRequestListener() {
                    @Override
                    public void onResponse(Bitmap response) {
                        if (!isDestroyed())
                            getBinding().image.setImageBitmap(response);
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });

    }

    @Override
    public void onExecuted(Boolean aBoolean) {
        Toast.makeText(this, "Data Tersimpan", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onError(Throwable throwable) {

    }

//    public String BitMapToString(Bitmap userImage1) {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        userImage1.compress(Bitmap.CompressFormat.PNG, 60, baos);
//        byte[] b = baos.toByteArray();
//        String Document_img1 = Base64.encodeToString(b, Base64.DEFAULT);
//        return Document_img1;
//    }


}
