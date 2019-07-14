package com.niken.tkalfiizzah.base;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.niken.tkalfiizzah.Utils;

import java.io.IOException;

public class BindingHolder {

    @BindingAdapter("keyListener")
    public static void setOnKeyListener(View view, View.OnKeyListener keyListener) {
        view.setOnKeyListener(keyListener);
    }

    @SuppressLint("ClickableViewAccessibility")
    @BindingAdapter("endDrawableClick")
    public static void setOnCompoundDrawableClickListener(EditText view,
                                                          View.OnClickListener listener) {
        view.setOnTouchListener((v, event) -> {
            boolean hasConsumed = false;
            if (v instanceof EditText) {
                if (event.getRawX() >= v.getRight() - view.getCompoundDrawablesRelative()[2].getBounds().width()) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        listener.onClick(v);
                    }
                    hasConsumed = true;
                }
            }
            return hasConsumed;
        });
    }

    @BindingAdapter("setOnTouchListener")
    public static void setOnTouchListener(View view, View.OnTouchListener listener) {
        view.setOnTouchListener(listener);
    }

    @BindingAdapter("recyclerAdapter")
    public static void setRecyclerAdapter(RecyclerView recyclerView, BaseRecyclerAdapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    @BindingAdapter("imageUri")
    public static void setImageUri(ImageView imageView, @Nullable Uri uri) {
        if (uri != null && !uri.equals(Uri.EMPTY)) {
            Bitmap bitmap = null;
            try {
                bitmap = Utils.modifyOrientation(BitmapFactory.decodeFile(uri.getPath()), uri.getPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageView.setImageBitmap(Utils.getResizedBitmap(bitmap, 800));
        }
    }
}
