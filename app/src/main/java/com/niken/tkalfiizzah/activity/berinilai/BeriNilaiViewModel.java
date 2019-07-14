package com.niken.tkalfiizzah.activity.berinilai;

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
public class BeriNilaiViewModel extends BaseViewModel {

    private static final String TAG = BeriNilaiActivity.class.getSimpleName();
    private ObservableField<String> C_JADWAL = new ObservableField<>();
    private ObservableField<String> C_SISWA = new ObservableField<>();
    private ObservableField<String> namaSiswa = new ObservableField<>();

    private ObservableInt nilai = new ObservableInt();
    private ObservableField<Uri> image = new ObservableField<>();
    private OnExecuteListener<Boolean> listener;


    public BeriNilaiViewModel(Context context, OnExecuteListener<Boolean> listener) {
        super(context);
        this.listener = listener;
    }

    public ObservableInt getNilai() {
        return nilai;
    }

    public ObservableField<Uri> getImage() {
        return image;
    }

    public void setImage(Uri imageUri) {
        this.image.set(imageUri);
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

    public void nilaiChanged(CharSequence s, int start, int before, int count) {
        String input = s.toString();
        input = input.replaceFirst("^0+(?!$)", "");

        if (TextUtils.isEmpty(input))
            input = "0";

        int nilai = Integer.valueOf(input);

        if (nilai < 0)
            nilai = 0;
        else if (nilai > 100)
            nilai = 100;

        this.nilai.set(nilai);
    }

    public void onCancelClick(View view) {
        listener.onExecuted(false);
    }

    public void onConfirmClick(View view) {

        if (nilai.get() < 0) {
            Toast.makeText(getContext(), "Nilai harus diisi antara 0 dan 100.", Toast.LENGTH_SHORT).show();
            return;
        }

        CustomDialog.get(getContext(), R.style.AppTheme_DialogTheme)
                .title(R.string.konfirmasi)
                .message("Apa anda yakin ingin menyimpan nilai ini?")
                .addPositiveButton(R.string.ya, (dialog, which) -> {
                    getCompositeDisposable().clear();
                    getCompositeDisposable().add(uploadJadwal()
                            .subscribe(aBoolean -> {
                                listener.onExecuted(aBoolean);

                            }, throwable -> {

                            })
                    );
                }).addNegativeButton(R.string.cancel, null)
                .show();
    }

    private Observable<Boolean> uploadJadwal() {
        JSONObject param = new JSONObject();
        try {
            param.put("C_SISWA", C_SISWA.get());
            param.put("C_JADWAL", C_JADWAL.get());
            param.put("C_NILAI", String.valueOf(nilai.get()));

            if (image.get() != null && !image.get().equals(Uri.EMPTY))
                param.put("C_FOTO", Utils.encodeImage(image.get()));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "uploadJadwal: " + param.toString());

        return Rx2AndroidNetworking.post(Constant.URL_UBAH_HASIL_JADWAL)
                .addJSONObjectBody(param)
                .build()
                .getJSONObjectObservable()
                .map(object -> object.optBoolean("success"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


}
