package com.example.tintc.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.tintc.R;
import com.example.tintc.adapter.AdapterHome;
import com.example.tintc.model.Article;

import java.util.ArrayList;

public class ListCateActivity extends AppCompatActivity {
    private RecyclerView recyclerCate;
    private AdapterHome adapterHome;
    private ArrayList<Article> articles = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_cate);
        init();
        getData();
        setUpRecycler();
    }

    private void getData() {
    }

    private void setUpRecycler() {
        recyclerCate.setHasFixedSize(true);
        recyclerCate.setLayoutManager(new LinearLayoutManager(this));
        adapterHome = new AdapterHome(this,articles);
        recyclerCate.setAdapter(adapterHome);
    }

    private void init() {
        recyclerCate = findViewById(R.id.recyclerCate);

    }
}
