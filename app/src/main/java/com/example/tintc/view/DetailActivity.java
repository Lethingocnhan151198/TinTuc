package com.example.tintc.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tintc.R;

public class DetailActivity extends AppCompatActivity {
    private ImageView imageView,imgShare;
    private TextView tvSourceTl,tvTime;
    private WebView webView;
    private ProgressBar loader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        init();
        getData();
    }

    private void getData() {
        Intent intent = getIntent();
        String source = intent.getStringExtra("source");
        String time = intent.getStringExtra("time");
        String imageUrl = intent.getStringExtra("image");
        String url = intent.getStringExtra("link");
        loader.setVisibility(View.VISIBLE);

        Glide.with(this)
                .load(imageUrl)
                .into(imageView);
        tvSourceTl.setText(source);
        tvTime.setText(time);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
        if (webView.isShown()){
            loader.setVisibility(View.INVISIBLE);
        }

    }

    private void init() {
        tvTime      = findViewById(R.id.tvTime);
        tvSourceTl  = findViewById(R.id.tvSourceTl);
        imageView   = findViewById(R.id.imageView);
        imgShare    = findViewById(R.id.imgShare);
        webView     = findViewById(R.id.webView);
        loader      = findViewById(R.id.webViewLoader);
    }
}
