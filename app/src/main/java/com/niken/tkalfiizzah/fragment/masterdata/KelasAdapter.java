package com.niken.tkalfiizzah.fragment.masterdata;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.niken.tkalfiizzah.activity.detailkelas.DetailKelasActivity;
import com.niken.tkalfiizzah.base.BaseRecyclerAdapter;
import com.niken.tkalfiizzah.base.BaseViewHolder;
import com.niken.tkalfiizzah.databinding.ListKelasBinding;
import com.niken.tkalfiizzah.model.Kelas;

import java.util.List;

/**
 * Created by Firman on 4/17/2019.
 */
public class KelasAdapter extends BaseRecyclerAdapter<Kelas> {


    public KelasAdapter(List<Kelas> originalList) {
        super(originalList);
    }

    @Override
    public boolean onSearch(String filter, Kelas jadwal) throws Exception {
        return false;
    }

    @NonNull
    @Override
    public BaseViewHolder<?> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListKelasBinding binding = ListKelasBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new KelasViewHolder(binding);
    }

    class KelasViewHolder extends BaseViewHolder<ListKelasBinding> {

        public KelasViewHolder(ListKelasBinding binding) {
            super(binding);
        }

        @Override
        public void onBind(int position) {
            binding.setItem(showList.get(position));
            binding.getRoot().setOnClickListener(v -> DetailKelasActivity.start(binding.getRoot().getContext(), showList.get(position).getC_KELAS()));
        }
    }
}
