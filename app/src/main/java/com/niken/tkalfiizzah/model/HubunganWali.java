package com.niken.tkalfiizzah.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.niken.tkalfiizzah.BR;

import org.json.JSONObject;

/**
 * Created by Firman on 4/28/2019.
 */
public class HubunganWali extends BaseObservable {

    private User user;
    private Siswa siswa;
    private int C_HUBUNGAN;
    private String C_HUBUNGAN_NAME;

    public static HubunganWali fromJson(JSONObject object) {
        HubunganWali hubunganWali = new HubunganWali();
        hubunganWali.setC_HUBUNGAN(object.optInt("C_HUBUNGAN"));
        hubunganWali.setC_HUBUNGAN_NAME(object.optString("C_HUBUNGAN_NAME"));
        hubunganWali.setSiswa(Siswa.fromJson(object));
        hubunganWali.setUser(User.fromJson(object));
        return hubunganWali;
    }

    @Bindable
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        notifyPropertyChanged(BR.user);
    }

    @Bindable
    public Siswa getSiswa() {
        return siswa;
    }

    public void setSiswa(Siswa siswa) {
        this.siswa = siswa;
        notifyPropertyChanged(BR.siswa);
    }

    @Bindable
    public int getC_HUBUNGAN() {
        return C_HUBUNGAN;
    }

    public void setC_HUBUNGAN(int c_HUBUNGAN) {
        C_HUBUNGAN = c_HUBUNGAN;
        notifyPropertyChanged(BR.c_HUBUNGAN);
    }

    @Bindable
    public String getC_HUBUNGAN_NAME() {
        return C_HUBUNGAN_NAME;
    }

    public void setC_HUBUNGAN_NAME(String c_HUBUNGAN_NAME) {
        C_HUBUNGAN_NAME = c_HUBUNGAN_NAME;
        notifyPropertyChanged(BR.c_HUBUNGAN_NAME);
    }
}
