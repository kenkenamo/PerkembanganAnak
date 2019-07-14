package com.niken.tkalfiizzah.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.Bindable;

import com.niken.tkalfiizzah.BR;

import org.json.JSONObject;

/**
 * Created by Firman on 4/21/2019.
 */
public class User extends Profile implements Parcelable {

    private int C_HUBUNGAN;
    private int C_STATUS;

    public static User fromJson(JSONObject object) {
        User user = new User();
        user.setC_USER(object.optString("C_USER"));
        user.setC_LOGIN(object.optString("C_LOGIN"));
        user.setV_PASSWORD(object.optString("V_PASSWORD"));
        user.setC_JENKEL(object.optString("C_JENKEL"));

        if (!object.isNull("V_NAMA"))
            user.setV_NAMA(object.optString("V_NAMA"));


        if (!object.isNull("V_ALAMAT"))
            user.setV_ALAMAT(object.optString("V_ALAMAT"));

        if (!object.isNull("V_PHONE1"))
            user.setV_PHONE1(object.optString("V_PHONE1"));

        if (!object.isNull("V_PHONE2"))
            user.setV_PHONE2(object.optString("V_PHONE2"));

        if (!object.isNull("V_TELP"))
            user.setV_TELP(object.optString("V_TELP"));

        if (!object.isNull("V_MAIL"))
            user.setV_MAIL(object.optString("V_MAIL"));

        user.setC_GROUP(object.optInt("C_GROUP"));
        user.setC_STATUS(object.optInt("C_STATUS"));
        user.setC_HUBUNGAN(object.optInt("C_HUBUNGAN"));
        return user;
    }

    public void copy(User user) {
        setC_STATUS(user.getC_STATUS());
        setC_USER(user.getC_USER());
        setC_LOGIN(user.getC_LOGIN());
        setV_PASSWORD(user.getV_PASSWORD());
        setV_NAMA(user.getV_NAMA());
        setV_ALAMAT(user.getV_ALAMAT());
        setC_GROUP(user.getC_GROUP());
        setC_JENKEL(user.getC_JENKEL());
        setV_PHONE1(user.getV_PHONE1());
        setV_PHONE2(user.getV_PHONE2());
        setV_TELP(user.getV_TELP());
        setV_MAIL(user.getV_MAIL());

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
    public int getC_STATUS() {
        return C_STATUS;
    }

    public void setC_STATUS(int c_STATUS) {
        C_STATUS = c_STATUS;
        notifyPropertyChanged(BR.c_STATUS);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.C_HUBUNGAN);
        dest.writeInt(this.C_STATUS);
        dest.writeString(this.getC_USER());
        dest.writeString(this.getC_LOGIN());
        dest.writeString(this.getV_PASSWORD());
        dest.writeString(this.getV_NAMA());
        dest.writeString(this.getV_ALAMAT());
        dest.writeInt(this.getC_GROUP());
        dest.writeString(this.getC_JENKEL());
        dest.writeString(this.getV_PHONE1());
        dest.writeString(this.getV_PHONE2());
        dest.writeString(this.getV_TELP());
        dest.writeString(this.getV_MAIL());
    }

    public User() {
    }

    protected User(Parcel in) {
        this.C_HUBUNGAN = in.readInt();
        this.C_STATUS = in.readInt();
        this.setC_USER(in.readString());
        this.setC_LOGIN(in.readString());
        this.setV_PASSWORD(in.readString());
        this.setV_NAMA(in.readString());
        this.setV_ALAMAT(in.readString());
        this.setC_GROUP(in.readInt());
        this.setC_JENKEL(in.readString());
        this.setV_PHONE1(in.readString());
        this.setV_PHONE2(in.readString());
        this.setV_TELP(in.readString());
        this.setV_MAIL(in.readString());
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
