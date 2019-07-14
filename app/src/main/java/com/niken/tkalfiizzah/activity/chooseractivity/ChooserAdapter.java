package com.niken.tkalfiizzah.activity.chooseractivity;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.niken.tkalfiizzah.base.BaseRecyclerAdapter;
import com.niken.tkalfiizzah.base.BaseViewHolder;
import com.niken.tkalfiizzah.databinding.ListChooserSiswaBinding;
import com.niken.tkalfiizzah.databinding.ListChooserWaliBinding;
import com.niken.tkalfiizzah.model.Siswa;
import com.niken.tkalfiizzah.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Firman on 7/7/2019.
 */
public class ChooserAdapter extends BaseRecyclerAdapter<Object> {

    private final SparseBooleanArray selected = new SparseBooleanArray();


    public ChooserAdapter(List<Object> originalList) {
        super(originalList);
    }

    @Override
    public boolean onSearch(String filter, Object o) throws Exception {
        return false;
    }

    @Override
    public int getItemViewType(int position) {
        return showList.get(position) instanceof User ? 0 : 1;
    }

    @NonNull
    @Override
    public BaseViewHolder<?> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            ListChooserWaliBinding binding =
                    ListChooserWaliBinding.inflate(LayoutInflater.from(parent.getContext()),
                            parent,
                            false);
            return new WaliViewHolder(binding);
        }
        ListChooserSiswaBinding binding =
                ListChooserSiswaBinding.inflate(LayoutInflater.from(parent.getContext()),
                        parent,
                        false);
        return new SiswaViewHolder(binding);
    }

    public ArrayList<User> getSelectedUser() {
        ArrayList<User> selectedItem = new ArrayList<>();
        for (int i = 0; i < showList.size(); i++) {
            if (selected.get(i)) {
                selectedItem.add((User) showList.get(i));
            }
        }
        selected.clear();
        return selectedItem;
    }

    public ArrayList<Siswa> getSelectedSiswa() {
        ArrayList<Siswa> selectedItem = new ArrayList<>();
        for (int i = 0; i < showList.size(); i++) {
            if (selected.get(i)) {
                selectedItem.add((Siswa) showList.get(i));
            }
        }
        selected.clear();
        return selectedItem;
    }

    @Override
    public void updateDataSet() {
        super.updateDataSet();
        selected.clear();
    }

    class WaliViewHolder extends BaseViewHolder<ListChooserWaliBinding> {

        public WaliViewHolder(ListChooserWaliBinding binding) {
            super(binding);
        }

        @Override
        public void onBind(int position) {
            binding.setItem((User) showList.get(position));
            binding.setClickListener(v -> {
                boolean current = selected.get(position);
                if (current) {
                    selected.put(position, false);
                    binding.setSelected(false);
                } else {
                    selected.put(position, true);
                    binding.setSelected(true);
                }

                binding.executePendingBindings();
            });
            binding.setSelected(selected.get(position));
            binding.executePendingBindings();
        }
    }

    class SiswaViewHolder extends BaseViewHolder<ListChooserSiswaBinding> {

        public SiswaViewHolder(ListChooserSiswaBinding binding) {
            super(binding);
        }

        @Override
        public void onBind(int position) {
            binding.setItem((Siswa) showList.get(position));
            binding.setClickListener(v -> {
                boolean current = selected.get(position);
                if (current) {
                    selected.put(position, false);
                    binding.setSelected(false);
                } else {
                    selected.put(position, true);
                    binding.setSelected(true);
                }

                binding.executePendingBindings();
            });
            binding.setSelected(selected.get(position));
            binding.executePendingBindings();
        }
    }

}
