package com.niken.tkalfiizzah.activity.hasiljadwal;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.niken.tkalfiizzah.activity.hasiljadwalsiswa.HasilJadwalSiswaActivity;
import com.niken.tkalfiizzah.base.BaseRecyclerAdapter;
import com.niken.tkalfiizzah.base.BaseViewHolder;
import com.niken.tkalfiizzah.databinding.ListHasilJadwalBinding;
import com.niken.tkalfiizzah.model.HasilJadwal;

import java.util.List;

/**
 * Created by Firman on 4/17/2019.
 */
public class HasilJadwalAdapter extends BaseRecyclerAdapter<HasilJadwal> {

    private String C_SISWA;
    private String V_NAMA;

    public HasilJadwalAdapter(List<HasilJadwal> originalList) {
        super(originalList);
    }

    public void setC_SISWA(String c_SISWA) {
        C_SISWA = c_SISWA;
    }

    public void setV_NAMA(String v_NAMA) {
        V_NAMA = v_NAMA;
    }

    @Override
    public boolean onSearch(String filter, HasilJadwal jadwal) throws Exception {
        return false;
    }

    @NonNull
    @Override
    public BaseViewHolder<?> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListHasilJadwalBinding binding = ListHasilJadwalBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new HasilKegiatanSiswaViewHolder(binding);
    }

    class HasilKegiatanSiswaViewHolder extends BaseViewHolder<ListHasilJadwalBinding> {

        public HasilKegiatanSiswaViewHolder(ListHasilJadwalBinding binding) {
            super(binding);
        }

        @Override
        public void onBind(int position) {
            HasilJadwal hasilJadwalSiswa = showList.get(position);
            binding.setItem(hasilJadwalSiswa);
            binding.constraintLayout.setOnClickListener(v -> {
                HasilJadwalSiswaActivity.start(v.getContext(),
                        hasilJadwalSiswa.getC_JADWAL(),
                        C_SISWA,
                        V_NAMA,
                        hasilJadwalSiswa.getC_NILAI());
            });
        }
    }
}
