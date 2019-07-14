package com.niken.tkalfiizzah.fragment.masterdata;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;

import com.niken.tkalfiizzah.activity.detailsiswa.DetailSiswaActivity;
import com.niken.tkalfiizzah.base.BaseRecyclerAdapter;
import com.niken.tkalfiizzah.base.BaseViewHolder;
import com.niken.tkalfiizzah.databinding.ListSiswaBinding;
import com.niken.tkalfiizzah.model.Siswa;

import java.util.List;

/**
 * Created by Firman on 4/17/2019.
 */
public class SiswaAdapter extends BaseRecyclerAdapter<Siswa> {

    private boolean clickable = true;
    private boolean editable = false;

    public SiswaAdapter(List<Siswa> originalList) {
        super(originalList);
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
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
            binding.setEditable(editable);
            if (clickable) {
                binding.getRoot().setOnClickListener(v ->
                        DetailSiswaActivity.start(binding.getRoot().getContext(), showList.get(position).getC_SISWA()));
            }

            if (editable) {
                binding.hubungan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int p, long id) {
                        showList.get(position).setC_HUBUNGAN(p + 1);
                        Log.d("test", "onItemSelected: pos=" + position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        }
    }
}
