package com.example.tintc.view;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.tintc.R;
import com.example.tintc.SplashActivity;
import com.example.tintc.adapter.AdapterCategories;
import com.example.tintc.adapter.AdapterHome;
import com.example.tintc.api.ApiClient;
import com.example.tintc.callbacks.OnResult;
import com.example.tintc.database.NewsModify;
import com.example.tintc.model.Article;
import com.example.tintc.model.Categories;
import com.example.tintc.model.Headline;
import com.example.tintc.model.News;
import com.example.tintc.model.User;
import com.example.tintc.utils.AccountUtils;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,OnResult {
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ArrayList<Categories> categories;
    private AdapterCategories adapterCategories;
    private Spinner spCategories;
    private AdapterHome adapterHome;
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView recyclerHome;
    private Toolbar toolbarHome;
    private ArrayList<Article> arrayList = new ArrayList<>();
    final String API_KEY = "a8000ce54cd44528afee2faa7be1a385";
    private int Current = 0;
    private User user;
    private TextView tvName, tvSdt;

    private Map<Article, News> mapHistory = new HashMap<>();
    private NewsModify mInstanceDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mInstanceDatabase = NewsModify.getInstance(this);
        getData();
        addCategories();
        init();
        setUpToolbar();
        setupAdapter();
        vnExpress();

        setUpRecycler(false);

    }

    private void getData() {
        user = AccountUtils.getInstance(this).getUser();
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbarHome);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
    }

    private void setUpRecycler(boolean isHistory) {
        recyclerHome.setHasFixedSize(true);
        recyclerHome.setLayoutManager(new LinearLayoutManager(this));

        adapterHome = new AdapterHome(this, arrayList, mapHistory, isHistory);
        recyclerHome.setAdapter(adapterHome);
        swipeRefresh.setRefreshing(false);

    }

    private void addCategories() {
        categories = new ArrayList<>();

        categories.add(new Categories("Thể loại", R.drawable.ic_list_128dp));
        categories.add(new Categories("Tin nóng", R.drawable.ic_newsletter_128dp));
        categories.add(new Categories("Thời sự", R.drawable.ic_news_128dp));
        categories.add(new Categories("Sức khỏe", R.drawable.ic_healthy_128dp));
        categories.add(new Categories("Du lịch", R.drawable.ic_journey_128dp));
        categories.add(new Categories("Thể thao", R.drawable.ic_sport_64dp));
        categories.add(new Categories("Thế giới", R.drawable.ic_globe_128dp));

    }

    private void setupAdapter() {
        adapterCategories = new AdapterCategories(this, categories);
        spCategories.setAdapter(adapterCategories);
        spCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (Current == position) {
                    return;
                } else {
                    switch ((int) id) {
                        case 1:
                            QueryData("tin nóng");
                            break;
                        case 2:
                            QueryData("thời sự");
                            break;
                        case 3:
                            QueryData("sức khỏe");
                            break;
                        case 4:
                            QueryData("Du lịch");
                            break;
                        case 5:
                            QueryData("Thể thao");
                            break;
                        case 6:
                            QueryData("Thế giới");
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void init() {
        toolbarHome = findViewById(R.id.toolbarHome);
        spCategories = findViewById(R.id.spCategories);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        recyclerHome = findViewById(R.id.recyclerView);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        tvName = navigationView.getHeaderView(0).findViewById(R.id.tvName);
        tvSdt = navigationView.getHeaderView(0).findViewById(R.id.tvSdt);
        tvName.setText(user.getFullName());
        tvSdt.setText(user.getPhoneNumber());

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.vnExpress:
                vnExpress();
                break;
            case R.id.wsj:
                Wsj();
                break;
            case R.id.cnn:
                Cnn();
                break;

            case R.id.history:
                loadHistory();
                break;
            case R.id.logout:
                AccountUtils.getInstance(this).logout();
                Intent intent = new Intent(this, SplashActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                break;

        }
        drawerLayout.closeDrawers();
        return false;
    }

    private void loadHistory() {
        arrayList.clear();
        swipeRefresh.setRefreshing(true);

        List<News> newsList = mInstanceDatabase.queryAllNews();
        List<Article> articleList = mInstanceDatabase.queryAllArticle();

        Log.d(TAG, "loadHistory: " + newsList);
        Log.d(TAG, "loadHistory: " + articleList);

        for (News news : newsList) {
            for (Article article : articleList) {
                if (news.getUrl().equals(article.getUrl())) {
                    if (news.getId() <10) {

                        mapHistory.put(article, news);
                        break;
                    }
                }
            }
        }

        arrayList.addAll(articleList);
        setUpRecycler(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    public void vnExpress() {

        // call Api vn Express
        swipeRefresh.setRefreshing(true);
        Call<Headline> call = ApiClient.getInstance().getData().getDataDomain("vnexpress.net", API_KEY);
        call.enqueue(new Callback<Headline>() {
            @Override
            public void onResponse(Call<Headline> call, Response<Headline> response) {
                arrayList.clear();
                arrayList = (ArrayList<Article>) response.body().getArticles();
                setUpRecycler(false);
                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<Headline> call, Throwable t) {
                swipeRefresh.setRefreshing(false);
                Toast.makeText(HomeActivity.this, "Dữ liệu lỗi,Kiểm tra lại mạng!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Cnn() {
        swipeRefresh.setRefreshing(true);
        Call<Headline> call = ApiClient.getInstance().getData().getDataDomain("cnn.com", API_KEY);
        call.enqueue(new Callback<Headline>() {
            @Override
            public void onResponse(Call<Headline> call, Response<Headline> response) {
                swipeRefresh.setRefreshing(false);
                arrayList.clear();
                arrayList = (ArrayList<Article>) response.body().getArticles();
                setUpRecycler(false);

            }

            @Override
            public void onFailure(Call<Headline> call, Throwable t) {
                swipeRefresh.setRefreshing(false);
                Toast.makeText(HomeActivity.this, "Dữ liệu lỗi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Wsj() {
        swipeRefresh.setRefreshing(true);
        Call<Headline> call = ApiClient.getInstance().getData().getDataDomain("wsj.com", API_KEY);
        call.enqueue(new Callback<Headline>() {
            @Override
            public void onResponse(Call<Headline> call, Response<Headline> response) {
                swipeRefresh.setRefreshing(false);
                arrayList.clear();
                arrayList = (ArrayList<Article>) response.body().getArticles();
                setUpRecycler(false);

            }

            @Override
            public void onFailure(Call<Headline> call, Throwable t) {
                swipeRefresh.setRefreshing(false);
                Toast.makeText(HomeActivity.this, "Dữ liệu lỗi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.dashboard_search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) HomeActivity.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(HomeActivity.this.getComponentName()));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    if (query != null) {
                        QueryData(query);
                    }
                    return false;

                }

                @Override
                public boolean onQueryTextChange(String newText) {

                    return false;
                }
            });


        }
        return super.onCreateOptionsMenu(menu);
    }

    private void QueryData(String query) {
        swipeRefresh.setRefreshing(true);
        Call<Headline> call = ApiClient.getInstance().getData().getEverythingData(query, API_KEY);
        call.enqueue(new Callback<Headline>() {
            @Override
            public void onResponse(Call<Headline> call, Response<Headline> response) {

                arrayList.clear();
                arrayList = (ArrayList<Article>) response.body().getArticles();

                setUpRecycler(false);
            }

            @Override
            public void onFailure(Call<Headline> call, Throwable t) {

            }
        });
    }

    private static final String TAG = "LOG_HomeActivity";
}
