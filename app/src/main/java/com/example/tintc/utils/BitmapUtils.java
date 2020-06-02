package com.example.tintc.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.tintc.callbacks.OnResult;
import com.example.tintc.model.Article;

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
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    private static final String TAG = "LOG_BitmapUtils";

    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.d(TAG, "getBitmapFromURL: " + src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    public static class GetBitmapFromUrl extends AsyncTask<Article, Void, Article[]> {

        private OnResult listener;

        public GetBitmapFromUrl(OnResult resultListener){
            listener = resultListener;
        }

        @Override
        protected Article[] doInBackground(Article... articles) {
            try {
                for(int i = 0 ; i < articles.length ; i++){
                    Article article = articles[i];

                    Log.d(TAG, "getBitmapFromURL: " + article.getUrlToImage());
                    URL url = new URL(article.getUrlToImage());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();

                    article.setImageBitmap(convertBitmapToByte(BitmapFactory.decodeStream(input)));
                    articles[i] = article;
                }
                return articles;
            } catch (IOException e) {
                // Log exception
                return null;
            }
        }

        @Override
        protected void onPostExecute(Article[] articles) {
            listener.onFinish(articles);
        }
    }
}
