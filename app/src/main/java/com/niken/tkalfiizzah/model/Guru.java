package com.niken.tkalfiizzah.model;


import androidx.databinding.Bindable;

import com.niken.tkalfiizzah.BR;

import org.json.JSONObject;

import java.util.Objects;

/**
 * Created by Firman on 4/17/2019.
 */
public class Guru extends Profile {

    private int C_STATUS;

    public static Guru fromJson(JSONObject jsonObject) {
        Guru guru = new Guru();
        guru.setV_NAMA(jsonObject.optString("V_NAMA"));
        guru.setC_JENKEL(jsonObject.optString("C_JENKEL"));

        guru.setC_USER(jsonObject.optString("C_USER"));
        guru.setC_LOGIN(jsonObject.optString("C_LOGIN"));
        guru.setV_PASSWORD(jsonObject.optString("V_PASSWORD"));
        guru.setC_JENKEL(jsonObject.optString("C_JENKEL"));


        if (!jsonObject.isNull("V_ALAMAT"))
            guru.setV_ALAMAT(jsonObject.optString("V_ALAMAT"));

        if (!jsonObject.isNull("V_PHONE1"))
            guru.setV_PHONE1(jsonObject.optString("V_PHONE1"));

        if (!jsonObject.isNull("V_PHONE2"))
            guru.setV_PHONE2(jsonObject.optString("V_PHONE2"));

        if (!jsonObject.isNull("V_TELP"))
            guru.setV_TELP(jsonObject.optString("V_TELP"));

        if (!jsonObject.isNull("V_MAIL"))
            guru.setV_MAIL(jsonObject.optString("V_MAIL"));

        guru.setC_GROUP(jsonObject.optInt("C_GROUP"));
        guru.setC_STATUS(jsonObject.optInt("C_STATUS"));
        return guru;
    }


    @Bindable
    public int getC_STATUS() {
        return C_STATUS;
    }

    public void setC_STATUS(int c_STATUS) {
        C_STATUS = c_STATUS;
        notifyPropertyChanged(BR.c_STATUS);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Guru guru = (Guru) o;
        return getC_USER().equals(guru.getC_USER());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getC_USER());
    }

}
