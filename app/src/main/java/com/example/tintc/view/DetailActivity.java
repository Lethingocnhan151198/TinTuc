package com.example.tintc.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tintc.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DetailActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView tvSourceTl,tvTime;
    private WebView webView;
    private ProgressBar loader;
    private FloatingActionButton fabShare;
    private static final String SHARE_INTENT_TYPE = "text/plain";
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
        webView.getSettings().setAppCacheMaxSize( 5 * 1024 * 1024 ); // 5MB
        webView.getSettings().setAppCachePath( getApplicationContext().getCacheDir().getAbsolutePath() );
        webView.getSettings().setAllowFileAccess( true );
        webView.getSettings().setAppCacheEnabled( true );
        webView.getSettings().setJavaScriptEnabled( true );
        webView.getSettings().setCacheMode( WebSettings.LOAD_DEFAULT ); // load online by default

        if ( !isNetworkAvailable() ) { // loading offline
            webView.getSettings().setCacheMode( WebSettings.LOAD_CACHE_ELSE_NETWORK );
        }
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
        if (webView.isShown()){
            loader.setVisibility(View.INVISIBLE);
        }
    fabShare.setOnClickListener(v -> {
        onShareClicked(url);
    });

    }

    private void init() {
        fabShare    = findViewById(R.id.fabShare);
        tvTime      = findViewById(R.id.tvTime);
        tvSourceTl  = findViewById(R.id.tvSourceTl);
        imageView   = findViewById(R.id.imageView);
        webView     = findViewById(R.id.webView);
        loader      = findViewById(R.id.webViewLoader);
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService( CONNECTIVITY_SERVICE );
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void onShareClicked(String videoLink) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType(SHARE_INTENT_TYPE);
        shareIntent.putExtra(Intent.EXTRA_TEXT, videoLink);
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.check_data));
        startActivity(Intent.createChooser(shareIntent,getString(R.string.share_with_home)));
    }
}
