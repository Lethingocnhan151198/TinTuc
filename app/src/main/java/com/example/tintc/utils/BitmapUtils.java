package com.example.tintc.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;

import androidx.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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
        return BitmapFactory.decodeByteArray(image, 100, image.length);
    }
}
