package com.example.tintc.callbacks;

import android.graphics.Bitmap;

import com.example.tintc.model.Article;

/**
 * Created by TranTien
 * Date 06/01/2020.
 */
public interface OnResult {
    default void onFinish(String string){};
    default void onFinish(Article article[]){};
}
