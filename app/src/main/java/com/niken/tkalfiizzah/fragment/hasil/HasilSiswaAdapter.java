package com.niken.tkalfiizzah.fragment.hasil;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.niken.tkalfiizzah.activity.hasiljadwal.HasilJadwalActivity;
import com.niken.tkalfiizzah.base.BaseRecyclerAdapter;
import com.niken.tkalfiizzah.base.BaseViewHolder;
import com.niken.tkalfiizzah.databinding.ListSiswaBinding;
import com.niken.tkalfiizzah.model.Siswa;

import java.util.List;

/**
 * Created by Firman on 4/17/2019.
 */
public class HasilSiswaAdapter extends BaseRecyclerAdapter<Siswa> {

    public HasilSiswaAdapter(List<Siswa> originalList) {
        super(originalList);
    }

    @Override
    public boolean onSearch(String filter, Siswa jadwal) throws Exception {
        return false;
    }

    @NonNull
    @Override
    public BaseViewHolder<?> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListSiswaBinding binding = ListSiswaBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SiswaViewHolder(binding);
    }

    class SiswaViewHolder extends BaseViewHolder<ListSiswaBinding> {

        public SiswaViewHolder(ListSiswaBinding binding) {
            super(binding);
        }

        @Override
        public void onBind(int position) {
            binding.setItem(showList.get(position));
            binding.setEditable(false);
            binding.getRoot().setOnClickListener(v ->
                    HasilJadwalActivity.start(binding.getRoot().getContext(), showList.get(position).getC_SISWA()));
        }
    }
}
