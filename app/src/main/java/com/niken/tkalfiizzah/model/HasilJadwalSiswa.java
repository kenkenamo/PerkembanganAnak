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
public class HasilJadwalSiswa extends Siswa {

    private int C_NILAI;
    private Date D_TIME;

    public static HasilJadwalSiswa fromJson(JSONObject jsonObject) {
        HasilJadwalSiswa hasilJadwalSiswa = new HasilJadwalSiswa();
        hasilJadwalSiswa.setC_SISWA(jsonObject.optString("C_SISWA"));
        hasilJadwalSiswa.setV_NAMA(jsonObject.optString("V_NAMA"));

        hasilJadwalSiswa.setC_NILAI(jsonObject.optInt("C_NILAI"));
        try {
            hasilJadwalSiswa.setD_TIME(Utils.parseDate(jsonObject.optString("D_TIME"), "yyyy-MM-dd HH:mm:ss"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Kelas kelas = new Kelas();
        kelas.setC_KELAS(jsonObject.optString("C_KELAS"));
        kelas.setC_NAMA(jsonObject.optString("C_NAMA"));
        hasilJadwalSiswa.setKelas(kelas);

        return hasilJadwalSiswa;
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
