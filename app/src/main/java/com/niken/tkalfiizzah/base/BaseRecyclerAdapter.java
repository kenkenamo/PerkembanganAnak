package com.niken.tkalfiizzah.base;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableField;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by firmansyah on 5/22/2018.
 */

public abstract class BaseRecyclerAdapter<E> extends RecyclerView.Adapter<BaseViewHolder<?>> {

    private static final String TAG = BaseRecyclerAdapter.class.getSimpleName();

    private Disposable disposable;
    protected final List<E> showList = new ArrayList<>();
    private final List<E> originalList;
    private ObservableField<String> filterText = new ObservableField<>("");

    public BaseRecyclerAdapter(List<E> originalList) {
        Log.d(TAG, "Instantiated");
        this.originalList = originalList;
        this.showList.addAll(this.originalList);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<?> holder, int position) {
        holder.onBind(position);
        Log.d(TAG, " position = " + position);
    }

    @Override
    public int getItemCount() {
        return showList.size();
    }

    @Override
    public long getItemId(int position) {
        return showList.get(position).hashCode();
    }


    public void updateDataSet() {
        this.showList.clear();
        this.showList.addAll(originalList);
        notifyDataSetChanged();
    }

    public void updateDataChanged(int position) {
        this.showList.clear();
        this.showList.addAll(originalList);
        notifyItemChanged(position);
    }

    public void updateDataRange(int start, int itemCount) {
        this.showList.clear();
        this.showList.addAll(originalList);
        notifyItemRangeChanged(start, itemCount);
    }

    public boolean isOnFilter() {
        return filterText != null;
    }

    /**
     * Untuk filter adapter
     *
     * @param filterText untuk mencari data tertentu
     *                   passing null untuk membatalkan pencarian
     */

    public void filter(@Nullable String filterText) {
        this.filterText.set(filterText);
        filterNow();
    }

    private void filterNow() {

        if (disposable != null)
            disposable.dispose();

        disposable = Observable.create(e1 -> {
            for (E item : showList) {
                e1.onNext(item);
            }
        }).filter(item -> onSearch(filterText.get(), (E) item))
                .toList()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                    showList.clear();
                    showList.addAll((Collection<? extends E>) data);
                }, throwable -> {
                    Log.e(TAG, "error while Searching " + throwable.toString());
                });
    }


    public abstract boolean onSearch(String filter, E e) throws Exception;
}
