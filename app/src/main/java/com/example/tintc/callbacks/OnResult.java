package com.example.tintc.callbacks;

import android.graphics.Bitmap;

import com.example.tintc.model.Article;

public interface OnResult {
    default void onFinish(String string){};
    default void onFinish(Article article[]){};
}
