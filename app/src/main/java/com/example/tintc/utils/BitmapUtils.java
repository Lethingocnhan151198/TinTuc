package com.example.tintc.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.tintc.callbacks.OnResult;
import com.example.tintc.model.Article;
import com.google.android.gms.common.util.IOUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by TranTien
 * Date 06/02/2020.
 */
public class BitmapUtils {
    public static byte[] convertBitmapToByte(@NonNull Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        return bos.toByteArray();
    }

    public static Bitmap convertByteToBitmap(@NonNull byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    private static final String TAG = "LOG_BitmapUtils";
}
