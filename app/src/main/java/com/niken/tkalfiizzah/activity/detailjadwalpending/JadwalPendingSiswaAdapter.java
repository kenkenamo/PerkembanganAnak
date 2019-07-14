package com.niken.tkalfiizzah.activity.detailjadwalpending;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.niken.tkalfiizzah.activity.berinilai.BeriNilaiActivity;
import com.niken.tkalfiizzah.base.BaseRecyclerAdapter;
import com.niken.tkalfiizzah.base.BaseViewHolder;
import com.niken.tkalfiizzah.databinding.ListJadwalPendingBinding;
import com.niken.tkalfiizzah.model.HasilJadwalSiswa;

import java.util.List;

/**
 * Created by Firman on 4/17/2019.
 */
public class JadwalPendingSiswaAdapter extends BaseRecyclerAdapter<HasilJadwalSiswa> {

    private String C_JADWAL;

    public JadwalPendingSiswaAdapter(List<HasilJadwalSiswa> originalList) {
        super(originalList);
    }

    public void setC_JADWAL(String c_JADWAL) {
        C_JADWAL = c_JADWAL;
    }

    @Override
    public boolean onSearch(String filter, HasilJadwalSiswa jadwal) throws Exception {
        return false;
    }

    @NonNull
    @Override
    public BaseViewHolder<?> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListJadwalPendingBinding binding = ListJadwalPendingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new HasilKegiatanSiswaViewHolder(binding);
    }

    class HasilKegiatanSiswaViewHolder extends BaseViewHolder<ListJadwalPendingBinding> {

        public HasilKegiatanSiswaViewHolder(ListJadwalPendingBinding binding) {
            super(binding);
        }

        @Override
        public void onBind(int position) {
            HasilJadwalSiswa hasilJadwalSiswa = showList.get(position);
            binding.setItem(hasilJadwalSiswa);
            binding.button.setOnClickListener(v -> {
                BeriNilaiActivity.start(v.getContext(), C_JADWAL, hasilJadwalSiswa.getC_SISWA(), hasilJadwalSiswa.getV_NAMA(), hasilJadwalSiswa.getC_NILAI());
            });
        }
    }
}
