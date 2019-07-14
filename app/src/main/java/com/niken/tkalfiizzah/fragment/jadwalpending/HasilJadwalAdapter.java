package com.niken.tkalfiizzah.fragment.jadwalpending;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.niken.tkalfiizzah.activity.detailjadwalpending.DetailJadwalPendingActivity;
import com.niken.tkalfiizzah.base.BaseRecyclerAdapter;
import com.niken.tkalfiizzah.base.BaseViewHolder;
import com.niken.tkalfiizzah.databinding.ListJadwalBinding;
import com.niken.tkalfiizzah.model.HasilJadwal;

import java.util.List;

/**
 * Created by Firman on 4/17/2019.
 */
public class HasilJadwalAdapter extends BaseRecyclerAdapter<HasilJadwal> {


    public HasilJadwalAdapter(List<HasilJadwal> originalList) {
        super(originalList);
    }

    @Override
    public boolean onSearch(String filter, HasilJadwal jadwal) throws Exception {
        return false;
    }

    @NonNull
    @Override
    public BaseViewHolder<?> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListJadwalBinding binding = ListJadwalBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new HasilKegiatanViewHolder(binding);
    }

    class HasilKegiatanViewHolder extends BaseViewHolder<ListJadwalBinding> {

        public HasilKegiatanViewHolder(ListJadwalBinding binding) {
            super(binding);
        }

        @Override
        public void onBind(int position) {
            binding.setItem(showList.get(position));
            binding.cardView.setOnClickListener(v -> {
                DetailJadwalPendingActivity.start(v.getContext(), showList.get(position).getC_JADWAL());
            });
        }
    }
}
