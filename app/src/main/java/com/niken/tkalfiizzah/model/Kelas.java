package com.niken.tkalfiizzah.model;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.niken.tkalfiizzah.BR;

import org.json.JSONObject;

import java.util.Objects;

/**
 * Created by Firman on 4/17/2019.
 */
public class Kelas extends BaseObservable implements Parcelable {

    private String C_KELAS;
    private String C_NAMA;
    private int N_TINGKAT;
    private int C_STATUS;
    private int N_JUMLAH_SISWA;

    public static Kelas fromJson(JSONObject jsonObject) {
        Kelas kelas = new Kelas();
        kelas.setC_KELAS(jsonObject.optString("C_KELAS"));
        kelas.setC_NAMA(jsonObject.optString("C_NAMA"));
        kelas.setN_TINGKAT(jsonObject.optInt("N_TINGKAT"));
        kelas.setC_STATUS(jsonObject.optInt("C_STATUS"));
        kelas.setN_JUMLAH_SISWA(jsonObject.optInt("N_JUMLAH_SISWA"));
        return kelas;
    }


    @Bindable
    public String getC_KELAS() {
        return C_KELAS;
    }

    public void setC_KELAS(String C_KELAS) {
        this.C_KELAS = C_KELAS;
        notifyPropertyChanged(BR.c_KELAS);
    }

    @Bindable
    public String getC_NAMA() {
        return C_NAMA;
    }

    public void setC_NAMA(String C_NAMA) {
        this.C_NAMA = C_NAMA;
        notifyPropertyChanged(BR.c_NAMA);
    }

    @Bindable
    public int getN_TINGKAT() {
        return N_TINGKAT;
    }

    public void setN_TINGKAT(int N_TINGKAT) {
        this.N_TINGKAT = N_TINGKAT;
        notifyPropertyChanged(BR.n_TINGKAT);
    }

    @Bindable
    public int getC_STATUS() {
        return C_STATUS;
    }

    public void setC_STATUS(int C_STATUS) {
        this.C_STATUS = C_STATUS;
        notifyPropertyChanged(BR.c_STATUS);
    }

    @Bindable
    public int getN_JUMLAH_SISWA() {
        return N_JUMLAH_SISWA;
    }

    public void setN_JUMLAH_SISWA(int N_JUMLAH_SISWA) {
        this.N_JUMLAH_SISWA = N_JUMLAH_SISWA;
        notifyPropertyChanged(BR.n_JUMLAH_SISWA);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Kelas kelas = (Kelas) o;
        return C_KELAS.equals(kelas.C_KELAS);
    }

    @Override
    public int hashCode() {
        return Objects.hash(C_KELAS);
    }


    @Override
    public String toString() {
        return "Kelas{" +
                "C_KELAS='" + C_KELAS + '\'' +
                ", C_NAMA='" + C_NAMA + '\'' +
                ", N_TINGKAT=" + N_TINGKAT +
                ", C_STATUS='" + C_STATUS + '\'' +
                ", N_JUMLAH_SISWA=" + N_JUMLAH_SISWA +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.C_KELAS);
        dest.writeString(this.C_NAMA);
        dest.writeInt(this.N_TINGKAT);
        dest.writeInt(this.C_STATUS);
        dest.writeInt(this.N_JUMLAH_SISWA);
    }

    public Kelas() {
    }

    protected Kelas(Parcel in) {
        this.C_KELAS = in.readString();
        this.C_NAMA = in.readString();
        this.N_TINGKAT = in.readInt();
        this.C_STATUS = in.readInt();
        this.N_JUMLAH_SISWA = in.readInt();
    }

    public static final Parcelable.Creator<Kelas> CREATOR = new Parcelable.Creator<Kelas>() {
        @Override
        public Kelas createFromParcel(Parcel source) {
            return new Kelas(source);
        }

        @Override
        public Kelas[] newArray(int size) {
            return new Kelas[size];
        }
    };
}
