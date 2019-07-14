package com.niken.tkalfiizzah.fragment.jadwal;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.niken.tkalfiizzah.activity.detailjadwal.DetailJadwalActivity;
import com.niken.tkalfiizzah.activity.detailjadwalpending.DetailJadwalPendingActivity;
import com.niken.tkalfiizzah.base.BaseRecyclerAdapter;
import com.niken.tkalfiizzah.base.BaseViewHolder;
import com.niken.tkalfiizzah.databinding.ListJadwalBinding;
import com.niken.tkalfiizzah.model.Jadwal;

import java.util.List;

/**
 * Created by Firman on 4/17/2019.
 */
public class JadwalAdapter extends BaseRecyclerAdapter<Jadwal> {

    private boolean tambahNilai;

    public JadwalAdapter(List<Jadwal> originalList) {
        super(originalList);
    }

    public void setTambahNilai(boolean tambahNilai) {
        this.tambahNilai = tambahNilai;
    }

    @Override
    public boolean onSearch(String filter, Jadwal jadwal) throws Exception {
        return false;
    }

    @NonNull
    @Override
    public BaseViewHolder<?> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListJadwalBinding binding = ListJadwalBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new JadwalViewHolder(binding);
    }

    class JadwalViewHolder extends BaseViewHolder<ListJadwalBinding> {

        public JadwalViewHolder(ListJadwalBinding binding) {
            super(binding);
        }

        @Override
        public void onBind(int position) {
            binding.setItem(showList.get(position));
            binding.cardView.setOnClickListener(v -> {
                if (tambahNilai)
                    DetailJadwalPendingActivity.start(v.getContext(), showList.get(position).getC_JADWAL());
                else
                    DetailJadwalActivity.start(v.getContext(), showList.get(position).getC_JADWAL());
            });

            binding.executePendingBindings();
        }
    }
}
