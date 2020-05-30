package com.example.tintc.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tintc.R;
import com.example.tintc.config.DateUtils;
import com.example.tintc.config.Typeface;
import com.example.tintc.model.Article;
import com.example.tintc.view.DetailActivity;

import java.util.ArrayList;

public class AdapterHome extends RecyclerView.Adapter<AdapterHome.ViewHolder> {
    private Context context;
    private ArrayList<Article> articles;
    public AdapterHome(Context context, ArrayList<Article> articles) {
        this.context = context;
        this.articles = articles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article,parent,false);
       return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context)
                .load(articles.get(position).getUrlToImage())
                .into(holder.imgBanner);
        holder.tvSource.setText(articles.get(position).getSource().getName());
        holder.tvTitle.setText(articles.get(position).getTitle());
        holder.tvTime.setText(DateUtils.getInstance().dateTime(articles.get(position).getPublishedAt()));
        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), DetailActivity.class);
            intent.putExtra("image",articles.get(position).getUrlToImage());
            intent.putExtra("link",articles.get(position).getUrl());
            intent.putExtra("time",DateUtils.getInstance().dateTime(articles.get(position).getPublishedAt()));
            intent.putExtra("source",articles.get(position).getSource());
            v.getContext().startActivity(intent);

        });
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView imgBanner;
        TextView tvTitle,tvSource,tvTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView    = itemView.findViewById(R.id.cardView);
            imgBanner   = itemView.findViewById(R.id.imgBanner);
            tvTitle     = itemView.findViewById(R.id.tvTitle);
            tvSource    = itemView.findViewById(R.id.tvSource);
            tvTime      = itemView.findViewById(R.id.tvTime);
        }
    }
}
