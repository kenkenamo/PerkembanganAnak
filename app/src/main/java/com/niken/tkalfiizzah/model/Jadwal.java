package com.niken.tkalfiizzah.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.niken.tkalfiizzah.BR;
import com.niken.tkalfiizzah.Utils;

import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;
import java.util.Objects;

/**
 * Created by Firman on 4/17/2019.
 */
public class Jadwal extends BaseObservable {

    private String C_JADWAL;
    private String C_NAMA;

    private Date D_TANGGAL;
    private Date D_MULAI;
    private Date D_SELESAI;
    private int C_STATUS;
    private Kelas kelas;
    private Guru guru;

    public static Jadwal fromJson(JSONObject jsonObject) {
        Jadwal jadwal = new Jadwal();
        jadwal.setC_JADWAL(jsonObject.optString("C_JADWAL"));
        jadwal.setC_NAMA(jsonObject.optString("C_NAMA"));

        try {
            jadwal.setD_TANGGAL(Utils.parseDate(jsonObject.optString("D_TGL"), "yyyy-MM-dd"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            jadwal.setD_MULAI(Utils.parseDate(jsonObject.optString("D_TGL") + " " + jsonObject.optString("D_MULAI"), "yyyy-MM-dd HH:mm:ss"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            jadwal.setD_SELESAI(Utils.parseDate(jsonObject.optString("D_TGL") + " " + jsonObject.optString("D_SELESAI"), "yyyy-MM-dd HH:mm:ss"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        jadwal.setC_STATUS(jsonObject.optInt("C_STATUS"));


        Kelas kelas = new Kelas();
        kelas.setC_KELAS(jsonObject.optString("C_KELAS"));
        kelas.setC_NAMA(jsonObject.optString("C_NAMAKELAS"));
        jadwal.setKelas(kelas);

        Guru guru = new Guru();
        guru.setC_USER(jsonObject.optString("C_NIP"));
        guru.setV_NAMA(jsonObject.optString("V_NAMA"));

        jadwal.setGuru(guru);
        return jadwal;
    }


    @Bindable
    public String getC_JADWAL() {
        return C_JADWAL;
    }

    public void setC_JADWAL(String C_JADWAL) {
        this.C_JADWAL = C_JADWAL;
        notifyPropertyChanged(BR.c_JADWAL);
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
    public Date getD_MULAI() {
        return D_MULAI;
    }

    public void setD_MULAI(Date d_MULAI) {
        this.D_MULAI = d_MULAI;
        notifyPropertyChanged(BR.d_MULAI);
    }

    @Bindable
    public Date getD_SELESAI() {
        return D_SELESAI;
    }

    public void setD_SELESAI(Date D_SELESAI) {
        this.D_SELESAI = D_SELESAI;
        notifyPropertyChanged(BR.d_SELESAI);
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
    public Kelas getKelas() {
        return kelas;
    }

    public void setKelas(Kelas kelas) {
        this.kelas = kelas;
        notifyPropertyChanged(BR.kelas);
    }

    @Bindable
    public Guru getGuru() {
        return guru;
    }

    public void setGuru(Guru guru) {
        this.guru = guru;
        notifyPropertyChanged(BR.guru);
    }

    @Bindable
    public Date getD_TANGGAL() {
        return D_TANGGAL;
    }

    public void setD_TANGGAL(Date d_TANGGAL) {
        D_TANGGAL = d_TANGGAL;
        notifyPropertyChanged(BR.d_TANGGAL);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Jadwal jadwal = (Jadwal) o;
        return C_JADWAL.equals(jadwal.C_JADWAL);
    }

    @Override
    public int hashCode() {
        return Objects.hash(C_JADWAL);
    }

    @Override
    public String toString() {
        return "Jadwal{" +
                "C_JADWAL='" + C_JADWAL + '\'' +
                ", C_NAMA='" + C_NAMA + '\'' +
                ", D_TANGGAL=" + D_TANGGAL +
                ", D_MULAI=" + D_MULAI +
                ", D_SELESAI=" + D_SELESAI +
                ", C_STATUS=" + C_STATUS +
                ", kelas=" + kelas +
                ", guru=" + guru +
                '}';
    }
}
