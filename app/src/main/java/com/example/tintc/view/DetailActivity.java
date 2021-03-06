package com.example.tintc.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
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

import com.example.tintc.R;
import com.example.tintc.callbacks.OnResult;
import com.example.tintc.database.NewsModify;
import com.example.tintc.model.Article;
import com.example.tintc.model.News;
import com.example.tintc.utils.CheckNetwork;
import com.example.tintc.utils.RetrieveHtml;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

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
        Log.d(TAG, "onCreate: ");
        mInstanceDatabase = NewsModify.getInstance(this);
        retrieveHtml = new RetrieveHtml(this);
        mCurrentHtml = null;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        init();
        getData();

    }

    private void getData() {
        Log.d(TAG, "getData: ");
        Intent intent = getIntent();

        isOffline = intent.getBooleanExtra("offline", false);
        String source = intent.getStringExtra("source");
        String time = intent.getStringExtra("time");
        mCurrentURL = intent.getStringExtra("link");
        mCurrentArticle = (Article) intent.getSerializableExtra("article");
        String imageUrl = intent.getStringExtra("image");


        if (isOffline) {
            mCurrentHtml = intent.getStringExtra("html");
        }

        Picasso.get()
                .load(imageUrl)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .fit()
                .into(imageView);
        loader.setVisibility(View.VISIBLE);

        tvSourceTl.setText(source);
        tvTime.setText(time);

        if (!isOffline) {
            retrieveHtml.execute(mCurrentURL);
        }

        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setAppCacheMaxSize(1024*1024*8);
        webView.getSettings().setAppCachePath("/data/data/com.your.package.appname/cache");
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

        Log.d(TAG, "getData: " + isOffline);
        Log.d(TAG, "getData: " + mCurrentHtml);

        if (isOffline && mCurrentHtml != null) { // loading offline
            webView.loadDataWithBaseURL(null, mCurrentHtml, "text/html", "UTF-8", null);
        } else {
            webView.loadUrl(mCurrentURL);
        }

        if (webView.isShown()) {
            loader.setVisibility(View.INVISIBLE);
        }

        fabShare.setOnClickListener(v -> {
            if (isOffline) {
                Toast.makeText(this, "Cannot share in history news", Toast.LENGTH_SHORT).show();
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
        if (!isOffline) {
            try {
                final int position = getIntent().getIntExtra("postion", -1);
                mInstanceDatabase.insertNewNews(new News(position, mCurrentURL, html), mCurrentArticle);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
