package com.example.tintc.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.tintc.R;
import com.example.tintc.adapter.AdapterCategories;
import com.example.tintc.adapter.AdapterHome;
import com.example.tintc.model.Article;
import com.example.tintc.model.Categories;
import com.example.tintc.model.Headline;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ArrayList<Categories> categories;
    private AdapterCategories adapterCategories;
    private Spinner spCategories;
    private AdapterHome adapterHome;
    private RecyclerView recyclerHome;
    private ArrayList<Article> arrayList;
    private int Current = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        addCategories();
        init();
        setupAdapter();
        setUpRecycler();

    }

    private void setUpRecycler() {
        recyclerHome.setHasFixedSize(true);
        recyclerHome.setLayoutManager(new LinearLayoutManager(this));
        adapterHome = new AdapterHome(this,arrayList);
        recyclerHome.setAdapter(adapterHome);

    }

    private void addCategories() {
        categories = new ArrayList<>();
        categories.add(new Categories("Thể loại",0));
       categories.add(new Categories("Thể thao",R.drawable.ic_sport_64dp));
       categories.add(new Categories("Sức khỏe",0));

    }

    private void setupAdapter() {
        adapterCategories = new AdapterCategories(this,categories);
        spCategories.setAdapter(adapterCategories);
        spCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (Current == position){
                    return;
                }
                else {
                    // nếu click vào vị trí nào , thì query lên và hiển thị dữ liệu ra .
                    // hoặc chuyển sang màn hình khác hiển thị dữ liệu lên

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void init() {
        spCategories   = findViewById(R.id.spCategories);
        drawerLayout   = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        recyclerHome   = findViewById(R.id.recyclerHome);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.vnExpress:
               // call Api vn Express
                break;
            case R.id.cnn:
                //call Api Cnn
        }
        return true;
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
}
