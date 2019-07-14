package com.niken.tkalfiizzah.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.niken.tkalfiizzah.BR;

import org.json.JSONObject;

/**
 * Created by Firman on 4/21/2019.
 */
public class Profile extends BaseObservable {

    private String C_USER;
    private String C_LOGIN;
    private String V_PASSWORD;
    private String V_NAMA;
    private String V_ALAMAT = "";
    private int C_GROUP;
    private String C_JENKEL;

    private String V_PHONE1 = "";
    private String V_PHONE2 = "";
    private String V_TELP = "";
    private String V_MAIL = "";

    public static Profile fromJson(JSONObject object) {
        Profile profile = new Profile();
        profile.setC_USER(object.optString("C_USER"));
        profile.setC_LOGIN(object.optString("C_LOGIN"));
        profile.setV_PASSWORD(object.optString("V_PASSWORD"));

        if (!object.isNull("V_NAMA"))
            profile.setV_NAMA(object.optString("V_NAMA"));

        if (!object.isNull("V_ALAMAT"))
            profile.setV_ALAMAT(object.optString("V_ALAMAT"));

        profile.setC_JENKEL(object.optString("C_JENKEL"));
        profile.setC_GROUP(object.optInt("C_GROUP"));

        if (!object.isNull("V_PHONE1"))
            profile.setV_PHONE1(object.optString("V_PHONE1"));

        if (!object.isNull("V_PHONE2"))
            profile.setV_PHONE2(object.optString("V_PHONE2"));

        if (!object.isNull("V_TELP"))
            profile.setV_TELP(object.optString("V_TELP"));

        if (!object.isNull("V_MAIL"))
            profile.setV_MAIL(object.optString("V_MAIL"));


        return profile;
    }

    public void copy(Profile profile) {
        setC_USER(profile.C_USER);
        setC_LOGIN(profile.C_LOGIN);
        setV_PASSWORD(profile.V_PASSWORD);
        setV_NAMA(profile.V_NAMA);
        setV_ALAMAT(profile.V_ALAMAT);
        setC_GROUP(profile.C_GROUP);
        setC_JENKEL(profile.C_JENKEL);
        setV_PHONE1(profile.V_PHONE1);
        setV_PHONE2(profile.V_PHONE2);
        setV_TELP(profile.V_TELP);
        setV_MAIL(profile.V_MAIL);

    }

    @Bindable
    public String getC_USER() {
        return C_USER;
    }

    public void setC_USER(String C_USER) {
        this.C_USER = C_USER;
        notifyPropertyChanged(BR.c_USER);
    }

    @Bindable
    public String getC_LOGIN() {
        return C_LOGIN;
    }

    public void setC_LOGIN(String C_LOGIN) {
        this.C_LOGIN = C_LOGIN;
        notifyPropertyChanged(BR.c_LOGIN);
    }

    @Bindable
    public String getV_PASSWORD() {
        return V_PASSWORD;
    }

    public void setV_PASSWORD(String V_PASSWORD) {
        this.V_PASSWORD = V_PASSWORD;
        notifyPropertyChanged(BR.v_PASSWORD);
    }

    @Bindable
    public String getV_NAMA() {
        return V_NAMA;
    }

    public void setV_NAMA(String V_NAMA) {
        this.V_NAMA = V_NAMA;
        notifyPropertyChanged(BR.v_NAMA);
    }

    @Bindable
    public String getV_ALAMAT() {
        return V_ALAMAT;
    }

    public void setV_ALAMAT(String V_ALAMAT) {
        this.V_ALAMAT = V_ALAMAT;
        notifyPropertyChanged(BR.v_ALAMAT);
    }

    @Bindable
    public int getC_GROUP() {
        return C_GROUP;
    }

    public void setC_GROUP(int C_GROUP) {
        this.C_GROUP = C_GROUP;
        notifyPropertyChanged(BR.c_GROUP);
    }

    @Bindable
    public String getC_JENKEL() {
        return C_JENKEL;
    }

    public void setC_JENKEL(String c_JENKEL) {
        C_JENKEL = c_JENKEL;
        notifyPropertyChanged(BR.c_JENKEL);
    }

    @Bindable
    public String getV_PHONE1() {
        return V_PHONE1;
    }

    public void setV_PHONE1(String v_PHONE1) {
        V_PHONE1 = v_PHONE1;
        notifyPropertyChanged(BR.v_PHONE1);
    }

    @Bindable
    public String getV_PHONE2() {
        return V_PHONE2;
    }

    public void setV_PHONE2(String v_PHONE2) {
        V_PHONE2 = v_PHONE2;
        notifyPropertyChanged(BR.v_PHONE2);
    }

    @Bindable
    public String getV_TELP() {
        return V_TELP;
    }

    public void setV_TELP(String v_TELP) {
        V_TELP = v_TELP;
        notifyPropertyChanged(BR.v_TELP);
    }

    @Bindable
    public String getV_MAIL() {
        return V_MAIL;
    }

    public void setV_MAIL(String v_MAIL) {
        V_MAIL = v_MAIL;
        notifyPropertyChanged(BR.v_MAIL);
    }

    @Override
    public String toString() {
        return "Profile{" +
                "C_USER='" + C_USER + '\'' +
                ", C_LOGIN='" + C_LOGIN + '\'' +
                ", V_PASSWORD='" + V_PASSWORD + '\'' +
                ", V_NAMA='" + V_NAMA + '\'' +
                ", V_ALAMAT='" + V_ALAMAT + '\'' +
                ", C_GROUP=" + C_GROUP +
                ", C_JENKEL='" + C_JENKEL + '\'' +
                ", V_PHONE1='" + V_PHONE1 + '\'' +
                ", V_PHONE2='" + V_PHONE2 + '\'' +
                ", V_TELP='" + V_TELP + '\'' +
                ", V_MAIL='" + V_MAIL + '\'' +
                '}';
    }
}
