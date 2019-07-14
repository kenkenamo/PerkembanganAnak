package com.niken.tkalfiizzah.model;

import androidx.databinding.Bindable;

import com.niken.tkalfiizzah.BR;
import com.niken.tkalfiizzah.Utils;

import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by Firman on 4/18/2019.
 */
public class HasilJadwal extends Jadwal {

    private int C_NILAI;
    private Date D_TIME;


    public static HasilJadwal fromJson(JSONObject jsonObject) {
        HasilJadwal jadwal = new HasilJadwal();
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

        jadwal.setC_NILAI(jsonObject.optInt("C_NILAI"));
        try {
            jadwal.setD_TIME(Utils.parseDate(jsonObject.optString("D_TIME"), "yyyy-MM-dd HH:mm:ss"));
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
        guru.setV_NAMA(jsonObject.optString("C_NAMAGURU"));

        jadwal.setGuru(guru);
        return jadwal;
    }

    @Bindable
    public int getC_NILAI() {
        return C_NILAI;
    }

    public void setC_NILAI(int c_NILAI) {
        C_NILAI = c_NILAI;
        notifyPropertyChanged(BR.c_NILAI);
    }

    @Bindable
    public Date getD_TIME() {
        return D_TIME;
    }

    public void setD_TIME(Date d_TIME) {
        D_TIME = d_TIME;
    }
}
