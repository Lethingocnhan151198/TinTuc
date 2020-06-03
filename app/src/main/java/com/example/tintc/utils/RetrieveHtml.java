package com.example.tintc.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.example.tintc.callbacks.OnResult;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Arrays;

public class RetrieveHtml extends AsyncTask<String, Void, String> {

    private OnResult listener;

    public RetrieveHtml(OnResult resultListener) {
        listener = resultListener;
    }

    @Override
    protected String doInBackground(String... url) {
        try {
            Document document = Jsoup.connect(url[0])
                    .userAgent("Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Mobile Safari/537.36")
                    .get();

            return document.outerHtml();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static final String TAG = "LOG_RetrieveHtml";

    @Override
    protected void onPostExecute(String s) {
        Log.d(TAG, "onPostExecute: " + s);
        listener.onFinish(s);
    }
}
