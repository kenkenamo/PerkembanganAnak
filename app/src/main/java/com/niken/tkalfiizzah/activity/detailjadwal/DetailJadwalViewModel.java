package com.niken.tkalfiizzah.activity.detailjadwal;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableInt;

import com.google.android.material.textfield.TextInputEditText;
import com.niken.tkalfiizzah.Constant;
import com.niken.tkalfiizzah.CustomDialog;
import com.niken.tkalfiizzah.R;
import com.niken.tkalfiizzah.Utils;
import com.niken.tkalfiizzah.base.BaseViewModel;
import com.niken.tkalfiizzah.base.OnExecuteListener;
import com.niken.tkalfiizzah.model.Guru;
import com.niken.tkalfiizzah.model.Jadwal;
import com.niken.tkalfiizzah.model.Kelas;
import com.rx2androidnetworking.Rx2AndroidNetworking;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class DetailJadwalViewModel extends BaseViewModel {

    private static final String TAG = DetailJadwalViewModel.class.getSimpleName();
    private final OnExecuteListener<Boolean> listener;
    private final Jadwal jadwal = new Jadwal();
    private ArrayAdapter<Guru> guruAdapter;
    private ArrayAdapter<Kelas> kelasAdapter;
    private List<Guru> guruList = new ArrayList<>();
    private List<Kelas> kelasList = new ArrayList<>();

    private ObservableInt selectedGuru = new ObservableInt();
    private ObservableInt selectedKelas = new ObservableInt();

    public DetailJadwalViewModel(Context context, OnExecuteListener<Boolean> listener) {
        super(context);
        this.listener = listener;
        initAdapter();
        getCompositeDisposable().add(
                getGuru().subscribe(aBoolean -> guruAdapter.notifyDataSetChanged(),
                        Throwable::printStackTrace)
        );
        getCompositeDisposable().add(
                getKelas().subscribe(aBoolean -> kelasAdapter.notifyDataSetChanged(),
                        Throwable::printStackTrace)
        );
    }

    private void initAdapter() {
        guruAdapter = new ArrayAdapter<Guru>(getContext(), android.R.layout.simple_list_item_1, guruList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
                ((TextView) convertView.findViewById(android.R.id.text1)).setText(getItem(position).getV_NAMA());
                return convertView;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
                ((TextView) convertView.findViewById(android.R.id.text1)).setText(getItem(position).getV_NAMA());
                return convertView;
            }
        };
        kelasAdapter = new ArrayAdapter<Kelas>(getContext(), android.R.layout.simple_list_item_1, kelasList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
                ((TextView) convertView.findViewById(android.R.id.text1)).setText(getItem(position).getC_NAMA());
                return convertView;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
                ((TextView) convertView.findViewById(android.R.id.text1)).setText(getItem(position).getC_NAMA());
                return convertView;
            }
        };
    }

    public ObservableInt getSelectedGuru() {
        return selectedGuru;
    }

    public ObservableInt getSelectedKelas() {
        return selectedKelas;
    }

    public Jadwal getJadwal() {
        return jadwal;
    }

    public void setJadwal(String c_JADWAL) {
        jadwal.setC_JADWAL(c_JADWAL);
        getCompositeDisposable().clear();
        getCompositeDisposable().add(
                downloadJadwal()
                        .subscribe(aBoolean -> {
                            guruList.clear();
                            guruList.add(jadwal.getGuru());
                            guruAdapter.notifyDataSetChanged();
                            kelasList.clear();
                            kelasList.add(jadwal.getKelas());
                            kelasAdapter.notifyDataSetChanged();
                        }, throwable -> throwable.printStackTrace())
        );
    }

    public ArrayAdapter<Guru> getGuruAdapter() {
        return guruAdapter;
    }

    public ArrayAdapter<Kelas> getKelasAdapter() {
        return kelasAdapter;
    }

    public void setTanggal(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(getContext(),
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    jadwal.setD_TANGGAL(calendar.getTime());

//                    editText.setText(Utils.formatDate(calendar.getTime(), "dd MMMM yyyy"));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        dialog.show();
    }

    public void setMulai(TextInputEditText editText) {
        if (jadwal.getD_TANGGAL() == null) {
            editText.setError("Mohon pilih tanggal dahulu");
            return;
        }

        TimePickerDialog dialog =
                new TimePickerDialog(getContext(), (view, hourOfDay, minute) -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(jadwal.getD_TANGGAL());
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    jadwal.setD_MULAI(calendar.getTime());
//                    editText.setText(Utils.addZeroTime(hourOfDay) + ":" + Utils.addZeroTime(minute));
                }, 0, 0, true);
        dialog.show();
    }

    public void setSelesai(EditText editText) {
        if (jadwal.getD_TANGGAL() == null) {
            editText.setError("Mohon pilih tanggal dahulu");
            return;
        }

        if (jadwal.getD_MULAI() == null) {
            editText.setError("Mohon pilih waktu mulai dahulu");
            return;
        }
        TimePickerDialog dialog =
                new TimePickerDialog(getContext(), (view, hourOfDay, minute) -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(jadwal.getD_TANGGAL());
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    jadwal.setD_SELESAI(calendar.getTime());
//                    editText.setText(Utils.addZeroTime(hourOfDay) + ":" + Utils.addZeroTime(minute));
                }, 0, 0, true);

        dialog.show();
    }

    public void onGuruSelected(AdapterView<?> parent, View view, int position, long id) {
        jadwal.setGuru(guruList.get(position));
    }

    public void onKelasSelected(AdapterView<?> parent, View view, int position, long id) {
        jadwal.setKelas(kelasList.get(position));
    }

    public void simpanJadwal() {
        Log.d(getClass().getSimpleName(), jadwal.toString());

        getCompositeDisposable().clear();
        getCompositeDisposable().add(
                uploadJadwal()
                        .subscribe(aBoolean -> {
                            if (aBoolean) {
                                ((Activity) getContext()).finish();
                                Toast.makeText(getContext(), "Data Tersimpan", Toast.LENGTH_SHORT).show();
                            }
                        }, throwable -> {
                            throwable.printStackTrace();
                        })
        );
    }

    public void selesaikanJadwal() {
        CustomDialog dialog = CustomDialog.get(getContext())
                .title("Konfimasi")
                .message("Apa anda yakin ingin selesaikan jadwal ini?");
        dialog.addPositiveButton(R.string.ya, (d, which) -> {
            getCompositeDisposable().clear();
            getCompositeDisposable().add(
                    updateJadwal("1")
                            .subscribe(aBoolean -> {
                                ((Activity) getContext()).finish();
                                Toast.makeText(getContext(), "Jadwal telah diselesaikan", Toast.LENGTH_SHORT).show();
                            }, throwable -> {
                                throwable.printStackTrace();
                            })
            );
        });
        dialog.addNegativeButton(R.string.cancel, null);
        dialog.show();
    }

    public void batalkanJadwal() {
        CustomDialog dialog = CustomDialog.get(getContext())
                .title(R.string.konfirmasi)
                .message("Apa anda yakin ingin batalkan jadwal ini?");
        dialog.addPositiveButton(R.string.ya, (d, which) -> {
            getCompositeDisposable().clear();
            getCompositeDisposable().add(
                    updateJadwal("0")
                            .subscribe(aBoolean -> {
                                ((Activity) getContext()).finish();
                                Toast.makeText(getContext(), "Jadwal telah dibatalkan", Toast.LENGTH_SHORT).show();
                            }, throwable -> {
                                throwable.printStackTrace();
                            })
            );
        });
        dialog.addNegativeButton(R.string.cancel, null);
        dialog.show();
    }


    private Observable<Boolean> getGuru() {
        return Rx2AndroidNetworking.post(Constant.URL_GURU)
                .build()
                .getJSONObjectObservable()
                .map(object -> {
                    JSONArray array = object.getJSONArray("DataRow");
                    guruList.clear();
                    for (int i = 0; i < array.length(); i++) {
                        Guru guru = Guru.fromJson(array.getJSONObject(i));

                        if (guru.equals(jadwal.getGuru()))
                            selectedGuru.set(i);
                        guruList.add(guru);
                    }
                    return true;
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<Boolean> getKelas() {
        return Rx2AndroidNetworking.post(Constant.URL_KELAS)
                .build()
                .getJSONObjectObservable()
                .map(object -> {
                    JSONArray array = object.getJSONArray("DataRow");
                    kelasList.clear();
                    for (int i = 0; i < array.length(); i++) {
                        Kelas kelas = Kelas.fromJson(array.getJSONObject(i));
                        if (kelas.equals(jadwal.getKelas()))
                            selectedKelas.set(i);
                        kelasList.add(kelas);
                    }
                    return true;
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<Boolean> downloadJadwal() {
        return Rx2AndroidNetworking.post(Constant.URL_JADWAL)
                .addBodyParameter("C_JADWAL", jadwal.getC_JADWAL())
                .build()
                .getJSONObjectObservable()
                .map(object -> {
                    JSONArray array = object.getJSONArray("DataRow");
                    Jadwal jadwal = Jadwal.fromJson(array.getJSONObject(0));
                    this.jadwal.setC_NAMA(jadwal.getC_NAMA());
                    this.jadwal.setD_TANGGAL(jadwal.getD_TANGGAL());
                    this.jadwal.setD_SELESAI(jadwal.getD_SELESAI());
                    this.jadwal.setD_MULAI(jadwal.getD_MULAI());
                    this.jadwal.setC_STATUS(jadwal.getC_STATUS());
                    this.jadwal.setKelas(jadwal.getKelas());
                    this.jadwal.setGuru(jadwal.getGuru());
                    return true;
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<Boolean> uploadJadwal() {
        Map<String, String> param = new HashMap<>();
        param.put("C_NAMA", jadwal.getC_NAMA());
        param.put("D_TGL", Utils.formatDate(jadwal.getD_TANGGAL(), "yyyy-MM-dd"));
        param.put("D_MULAI", Utils.formatDate(jadwal.getD_MULAI(), "HH:mm:ss"));
        param.put("D_SELESAI", Utils.formatDate(jadwal.getD_SELESAI(), "HH:mm:ss"));
        param.put("C_STATUS", String.valueOf(jadwal.getC_STATUS()));
        param.put("C_KELAS", jadwal.getKelas().getC_KELAS());
        param.put("C_NIP", jadwal.getGuru().getC_USER());


        if (jadwal.getC_JADWAL() != null)
            param.put("C_JADWAL", jadwal.getC_JADWAL());


        return Rx2AndroidNetworking.post(Constant.URL_UBAH_JADWAL)
                .addBodyParameter(param)
                .build()
                .getJSONObjectObservable()
                .map(object -> object.optBoolean("success"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<Boolean> updateJadwal(String status) {
        Map<String, String> param = new HashMap<>();
        param.put("C_STATUS", status);
        param.put("C_JADWAL", jadwal.getC_JADWAL());


        return Rx2AndroidNetworking.post(Constant.URL_UBAH_JADWAL)
                .addBodyParameter(param)
                .build()
                .getJSONObjectObservable()
                .map(object -> object.optBoolean("success"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
