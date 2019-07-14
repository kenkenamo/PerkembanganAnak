package com.niken.tkalfiizzah.activity.hasiljadwalsiswa;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.niken.tkalfiizzah.Constant;
import com.niken.tkalfiizzah.CustomDialog;
import com.niken.tkalfiizzah.R;
import com.niken.tkalfiizzah.Utils;
import com.niken.tkalfiizzah.base.BaseViewModel;
import com.niken.tkalfiizzah.base.OnExecuteListener;
import com.rx2androidnetworking.Rx2AndroidNetworking;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Firman on 7/6/2019.
 */
public class HasilJadwalSiswaViewModel extends BaseViewModel {

    private static final String TAG = HasilJadwalSiswaActivity.class.getSimpleName();
    private ObservableField<String> C_JADWAL = new ObservableField<>();
    private ObservableField<String> C_SISWA = new ObservableField<>();
    private ObservableField<String> namaSiswa = new ObservableField<>();

    private ObservableInt nilai = new ObservableInt();
    private OnExecuteListener<Boolean> listener;


    public HasilJadwalSiswaViewModel(Context context, OnExecuteListener<Boolean> listener) {
        super(context);
        this.listener = listener;
    }

    public ObservableInt getNilai() {
        return nilai;
    }

    public ObservableField<String> getC_JADWAL() {
        return C_JADWAL;
    }

    public ObservableField<String> getC_SISWA() {
        return C_SISWA;
    }

    public ObservableField<String> getNamaSiswa() {
        return namaSiswa;
    }

    public void setData(String c_jadwal, String c_siswa, String namaSiswa) {
        C_JADWAL.set(c_jadwal);
        C_SISWA.set(c_siswa);
        this.namaSiswa.set(namaSiswa);

    }


}
