package com.niken.tkalfiizzah.fragment.masterdata;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;

import com.niken.tkalfiizzah.activity.detailuser.DetaiUseriActivity;
import com.niken.tkalfiizzah.base.BaseRecyclerAdapter;
import com.niken.tkalfiizzah.base.BaseViewHolder;
import com.niken.tkalfiizzah.databinding.ListWaliBinding;
import com.niken.tkalfiizzah.model.User;

import java.util.List;

/**
 * Created by Firman on 4/17/2019.
 */
public class WaliAdapter extends BaseRecyclerAdapter<User> {

    private boolean clickable = true;
    private boolean editable = false;

    public WaliAdapter(List<User> originalList) {
        super(originalList);
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    @Override
    public boolean onSearch(String filter, User jadwal) throws Exception {
        return false;
    }

    @NonNull
    @Override
    public BaseViewHolder<?> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListWaliBinding binding = ListWaliBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new WaliViewHolder(binding);
    }

    class WaliViewHolder extends BaseViewHolder<ListWaliBinding> {

        public WaliViewHolder(ListWaliBinding binding) {
            super(binding);
        }

        @Override
        public void onBind(int position) {
            binding.setItem(showList.get(position));
            binding.setEditable(editable);
            if (clickable) {
                binding.getRoot().setOnClickListener(v ->
                        DetaiUseriActivity.start(binding.getRoot().getContext(), binding.getItem().getC_USER()));
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
