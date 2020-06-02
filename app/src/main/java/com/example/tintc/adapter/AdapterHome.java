package com.example.tintc.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tintc.R;
import com.example.tintc.config.DateUtils;
import com.example.tintc.model.Article;
import com.example.tintc.model.News;
import com.example.tintc.view.DetailActivity;

import java.util.ArrayList;
import java.util.Map;

public class AdapterHome extends RecyclerView.Adapter<AdapterHome.ViewHolder> {
    private Context context;
    private ArrayList<Article> articles;

    private Map<Article, News> mMapHistory;
    private boolean isHistory;

    public AdapterHome(Context context, ArrayList<Article> articles, @Nullable Map<Article, News> mapHistory, boolean isHistory) {
        this.context = context;
        this.articles = articles;

        mMapHistory = mapHistory;
        this.isHistory = isHistory;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: " + position + " - " + isHistory);
        Glide.with(context)
                .load(articles.get(position).getUrlToImage())
                .into(holder.imgBanner);

        holder.tvSource.setText(articles.get(position).getSource().getName());
        holder.tvTitle.setText(articles.get(position).getTitle());
        holder.tvTime.setText(DateUtils.getInstance().dateTime(articles.get(position).getPublishedAt()));
        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), DetailActivity.class);

            intent.putExtra("time", DateUtils.getInstance().dateTime(articles.get(position).getPublishedAt()));
            intent.putExtra("source", articles.get(position).getSource().getName());
            intent.putExtra("link", articles.get(position).getUrl());

            intent.putExtra("article", articles.get(position));
            intent.putExtra("image", articles.get(position).getUrlToImage());
            intent.putExtra("offline", isHistory);
            if (isHistory) {
                News currentNews = mMapHistory.get(articles.get(position));
                if (currentNews != null) {
                    intent.putExtra("html", currentNews.getHtml());
                }
            }

            holder.itemView.getContext().startActivity(intent);

        });
    }

    private static final String TAG = "LOG_AdapterHome";

    @Override
    public int getItemCount() {
        return articles.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView imgBanner;
        TextView tvTitle, tvSource, tvTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            imgBanner = itemView.findViewById(R.id.imgBanner);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvSource = itemView.findViewById(R.id.tvSource);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }
}
