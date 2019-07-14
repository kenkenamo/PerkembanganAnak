package com.niken.tkalfiizzah.fragment.jadwalsiswa;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.niken.tkalfiizzah.Constant;
import com.niken.tkalfiizzah.Utils;
import com.niken.tkalfiizzah.base.BaseViewModel;
import com.niken.tkalfiizzah.base.OnExecuteListener;
import com.niken.tkalfiizzah.model.Jadwal;
import com.niken.tkalfiizzah.model.Siswa;
import com.rx2androidnetworking.Rx2AndroidNetworking;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Firman on 4/17/2019.
 */
public class JadwalSiswaViewModel extends BaseViewModel {

    private final OnExecuteListener<List<Jadwal>> listener;
    private final OnExecuteListener<Date> gantiTanggalListener;
    private Calendar selectedDate = Calendar.getInstance();

    private int selectedPos = 0;
    private List<Siswa> siswas = new ArrayList<>();
    private ArrayAdapter<Siswa> siswaAdapter;


    public JadwalSiswaViewModel(Context context, OnExecuteListener<List<Jadwal>> listener, OnExecuteListener<Date> gantiTanggalListener) {
        super(context);
        this.listener = listener;
        this.gantiTanggalListener = gantiTanggalListener;
        gantiTanggalListener.onExecuted(selectedDate.getTime());

        siswaAdapter = new ArrayAdapter<Siswa>(getContext(), android.R.layout.simple_list_item_1, siswas) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
                Siswa siswa = getItem(position);
                ((TextView) convertView.findViewById(android.R.id.text1)).setText(siswa.getC_SISWA() + " - " + siswa.getV_NAMA());
                return convertView;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
                Siswa siswa = getItem(position);
                ((TextView) convertView.findViewById(android.R.id.text1)).setText(siswa.getC_SISWA() + " - " + siswa.getV_NAMA());
                return convertView;
            }
        };
        getCompositeDisposable().add(
                downloadSiswa().subscribe(aBoolean -> siswaAdapter.notifyDataSetChanged(),
                        Throwable::printStackTrace)
        );
    }

    public boolean onSpinnerClick(View v, MotionEvent event) {
        if (siswas.isEmpty()) {

            getCompositeDisposable().clear();
            getCompositeDisposable().add(
                    downloadSiswa().subscribe(aBoolean -> siswaAdapter.notifyDataSetChanged(),
                            Throwable::printStackTrace)
            );
        }
        return false;
    }

    public ArrayAdapter<Siswa> getSiswaAdapter() {
        return siswaAdapter;
    }

    public void onSiswaSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedPos = position;
        refresh();
    }

    public void gantiTanggal(View v) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), 0,
                (view, year, month, dayOfMonth) -> {
                    selectedDate.set(Calendar.YEAR, year);
                    selectedDate.set(Calendar.MONTH, month);
                    selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    gantiTanggalListener.onExecuted(selectedDate.getTime());
                    refresh();
                }, selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    public void refresh() {
        if (selectedPos >= siswas.size()) {
            selectedPos = 0;
            Toast.makeText(getContext(), "Mohon pilih siswa dahulu.", Toast.LENGTH_SHORT).show();
            return;
        }

        getCompositeDisposable().clear();
        getCompositeDisposable().add(
                getJadwals().subscribe(jadwals -> {
                    Log.d(getClass().getSimpleName(), "size = " + jadwals.size());
                    listener.onExecuted(jadwals);
                }, throwable -> {
                    listener.onError(throwable);
                })
        );
    }


    private Observable<List<Jadwal>> getJadwals() {

        Map<String, String> param = new HashMap<>();
        param.put("D_TGL", Utils.formatDate(selectedDate.getTime(), "yyyy-MM-dd"));
        param.put("C_KELAS", siswas.get(selectedPos).getKelas().getC_KELAS());

        return Rx2AndroidNetworking.post(Constant.URL_JADWAL)
                .addBodyParameter(param)
                .build()
                .getJSONObjectObservable()
                .map(jsonObject -> {
                    JSONArray array = jsonObject.getJSONArray("DataRow");
                    List<Jadwal> jadwals = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        jadwals.add(Jadwal.fromJson(array.getJSONObject(i)));
                    }
                    return jadwals;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<Boolean> downloadSiswa() {
        return Rx2AndroidNetworking.post(Constant.URL_WALI_STATUS)
                .addBodyParameter("C_USER", getSessionHandler().getString(Constant.KEY_CUSER, null))
                .addBodyParameter("ModeSiswa", "1")
                .build()
                .getJSONObjectObservable()
                .map(object -> {
                    JSONArray array = object.getJSONArray("DataRow");
                    siswas.clear();
                    for (int i = 0; i < array.length(); i++) {
                        Siswa siswa = Siswa.fromJson(array.getJSONObject(i));
                        siswas.add(siswa);
                    }
                    return true;
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
