package com.niken.tkalfiizzah.fragment.profile;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.ObservableBoolean;

import com.niken.tkalfiizzah.Constant;
import com.niken.tkalfiizzah.CustomDialog;
import com.niken.tkalfiizzah.base.BaseViewModel;
import com.niken.tkalfiizzah.databinding.DialogChangePassBinding;
import com.niken.tkalfiizzah.model.Profile;
import com.rx2androidnetworking.Rx2AndroidNetworking;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Firman on 4/17/2019.
 */
public class ProfileViewModel extends BaseViewModel {


    private final Profile profile = new Profile();
    private ObservableBoolean changed = new ObservableBoolean();

    public ProfileViewModel(Context context) {
        super(context);

        refresh();
    }

    private void refresh() {
        getCompositeDisposable().add(
                getProfileObservsble()
                        .subscribe(profile1 -> profile.copy(profile1), throwable -> throwable.printStackTrace())
        );
    }

    public ObservableBoolean getChanged() {
        return changed;
    }

    public Profile getProfile() {
        return profile;
    }

    public void onSaveClick(View view) {
        getCompositeDisposable().add(
                saveObservable()
                        .subscribe(aBoolean -> {
                            if (aBoolean)
                                Toast.makeText(getContext(), "Profil Berhasil Diubah", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(getContext(), "Profil Gagal Diubah", Toast.LENGTH_SHORT).show();
                            refresh();
                        }, throwable -> {
                            Toast.makeText(getContext(), "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
                        })
        );
    }

    public void onChangePassClick(View view) {
        DialogChangePassBinding binding =
                DialogChangePassBinding.inflate(LayoutInflater.from(getContext()), null, false);

        CustomDialog dialog = CustomDialog.get(getContext())
                .title("Ubah Password")
                .addView(binding.getRoot())
                .cancelable(false);

        binding.setCancelClickListener(v -> {
            dialog.dismiss();
        });
        binding.setConfirmClickListener(v -> {
            String oldPassord = getSessionHandler().getString(Constant.KEY_PASSWORD, null);
            String inputPassword = binding.oldPasswordEt.getText().toString();
            String newPassword = binding.newPassword.getText().toString();
            String confirmPassword = binding.confirmPassword.getText().toString();

            if (TextUtils.isEmpty(inputPassword)) {
                binding.oldPasswordEt.setError("Password lama harus diisi.");
                binding.oldPasswordEt.requestFocus();
                return;
            }

            if (!TextUtils.equals(oldPassord, inputPassword)) {
                binding.oldPasswordEt.setError("Password salah.");
                binding.oldPasswordEt.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(newPassword) || TextUtils.getTrimmedLength(newPassword) < 8) {
                binding.newPassword.setError("Password baru harus diisi dan terdiri dari 8 karakter.");
                binding.newPassword.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(confirmPassword)) {
                binding.confirmPassword.setError("Password baru harus diisi.");
                binding.confirmPassword.requestFocus();
                return;
            }

            if (!TextUtils.equals(oldPassord, inputPassword)) {
                binding.confirmPassword.setError("Password tidak sama.");
                binding.confirmPassword.requestFocus();
                return;
            }

            getCompositeDisposable().add(
                    changePassObservable(newPassword)
                            .subscribe(aBoolean -> {
                                Toast.makeText(getContext(), "Password berhasil diubah.", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }, throwable -> {

                            })
            );
        });

        dialog.show();
    }

    public void onKeluarClick(View view) {
        getSessionHandler().logout();
    }

    private Observable<Profile> getProfileObservsble() {
        return Rx2AndroidNetworking.post(Constant.URL_PROFILE)
                .build()
                .getJSONObjectObservable()
                .map(jsonObject -> Profile.fromJson(jsonObject.getJSONObject("DataRow")))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<Boolean> saveObservable() {
        Map<String, String> param = new HashMap<>();
        param.put("C_LOGIN", profile.getC_LOGIN());
        param.put("V_NAMA", profile.getV_NAMA());
        param.put("V_ALAMAT", profile.getV_ALAMAT());
        param.put("V_PHONE1", profile.getV_PHONE1());
        param.put("V_PHONE2", profile.getV_PHONE2());
        param.put("V_TELP", profile.getV_TELP());
        param.put("V_MAIL", profile.getV_MAIL());
        param.put("C_USER", profile.getC_USER());

        return Rx2AndroidNetworking.post(Constant.URL_UBAH_PROFILE)
                .addBodyParameter(param)
                .build()
                .getJSONObjectObservable()
                .map(jsonObject -> jsonObject.optBoolean("success"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<Boolean> changePassObservable(String newPassword) {
        Map<String, String> param = new HashMap<>();
        param.put("V_PASSWORD", newPassword);
        param.put("C_USER", profile.getC_USER());

        return Rx2AndroidNetworking.post(Constant.URL_UBAH_PROFILE)
                .addBodyParameter(param)
                .build()
                .getJSONObjectObservable()
                .map(jsonObject -> {
                    getSessionHandler().putString(Constant.KEY_PASSWORD, newPassword);
                    return jsonObject.optBoolean("success");
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


}
