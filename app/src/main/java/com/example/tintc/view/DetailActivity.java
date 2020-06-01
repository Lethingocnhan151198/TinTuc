package com.example.tintc.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.tintc.R;
import com.example.tintc.callbacks.OnResult;
import com.example.tintc.database.NewsModify;
import com.example.tintc.model.Article;
import com.example.tintc.model.News;
import com.example.tintc.utils.BitmapUtils;
import com.example.tintc.utils.CheckNetwork;
import com.example.tintc.utils.RetrieveHtml;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;

public class DetailActivity extends AppCompatActivity implements OnResult {
    private ImageView imageView;
    private TextView tvSourceTl, tvTime;
    private WebView webView;
    private ProgressBar loader;
    private FloatingActionButton fabShare;
    private static final String SHARE_INTENT_TYPE = "text/plain";

    private String mCurrentURL;
    private Article mCurrentArticle;
    private String mCurrentHtml;

    private NewsModify mInstanceDatabase;
    private RetrieveHtml retrieveHtml;

    boolean isOffline = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mInstanceDatabase = NewsModify.getInstance(this);
        retrieveHtml = new RetrieveHtml(this);
        mCurrentHtml = null;
        init();
        getData();

    }

    private void getData() {
        Intent intent = getIntent();
        isOffline = intent.getBooleanExtra("offline", false);
        String source = intent.getStringExtra("source");
        String time = intent.getStringExtra("time");
        mCurrentURL = intent.getStringExtra("link");
        mCurrentArticle = (Article) intent.getSerializableExtra("article");

        if (isOffline) {
            mCurrentHtml = intent.getStringExtra("html");
            Bitmap bitmapImage = BitmapUtils.convertByteToBitmap(intent.getByteArrayExtra("image"));
            Glide.with(this)
                    .load(bitmapImage)
                    .into(imageView);
        } else {
            String imageUrl = intent.getStringExtra("image");
            Glide.with(this)
                    .load(imageUrl)
                    .into(imageView);
        }
        loader.setVisibility(View.VISIBLE);

        tvSourceTl.setText(source);
        tvTime.setText(time);

        retrieveHtml.execute(mCurrentURL);

        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.getSettings().setAppCacheMaxSize(5 * 1024 * 1024); // 5MB
        webView.getSettings().setAppCachePath(getApplicationContext().getCacheDir().getAbsolutePath());
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT); // load online by default

        webView.setWebViewClient(new WebViewClient());

        if (isOffline) { // loading offline
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            webView.loadUrl(mCurrentHtml);
        } else {
            webView.loadUrl(mCurrentURL);
        }

        if (webView.isShown()) {
            loader.setVisibility(View.INVISIBLE);
        }

        fabShare.setOnClickListener(v -> {
            if(isOffline){
                Toast.makeText(this, "cannot share in history news", Toast.LENGTH_SHORT).show();
                return;
            }

            if (CheckNetwork.isConnectInternet(DetailActivity.this)) {
                onShareClicked(mCurrentURL);
            } else {
                Toast.makeText(this, "Please connect wifi", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void init() {
        fabShare = findViewById(R.id.fabShare);
        tvTime = findViewById(R.id.tvTime);
        tvSourceTl = findViewById(R.id.tvSourceTl);
        imageView = findViewById(R.id.imageView);
        webView = findViewById(R.id.webView);
        loader = findViewById(R.id.webViewLoader);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void onShareClicked(String videoLink) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType(SHARE_INTENT_TYPE);
        shareIntent.putExtra(Intent.EXTRA_TEXT, videoLink);
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.check_data));
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_with_home)));
    }

    private static final String TAG = "LOG_DetailActivity";

    @Override
    public void onFinish(String html) {
        Log.d(TAG, "onFinish: " + mCurrentURL);
        Log.d(TAG, "onFinish: " + mCurrentURL);

        if(!isOffline){
            try {
                mInstanceDatabase.insertNewNews(new News(mCurrentURL, html), mCurrentArticle);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
