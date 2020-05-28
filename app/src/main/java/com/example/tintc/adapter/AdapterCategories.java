package com.example.tintc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tintc.R;
import com.example.tintc.model.Categories;

import java.util.ArrayList;

public class AdapterCategories extends ArrayAdapter<Categories> {
    public AdapterCategories(@NonNull Context context, ArrayList<Categories> categories) {
        super(context, 0,categories);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }
  private View initView(int position,View convertView,ViewGroup parent){
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.items_spinner_categories,parent,false);
        }
      ImageView imageView = convertView.findViewById(R.id.image_view_flag);
      TextView textView   = convertView.findViewById(R.id.text_view_name);
      Categories current  = getItem(position);
      if(current != null){
          imageView.setImageResource(current.getmFlagImage());
          textView.setText(current.getNameCategories());
      }
        return  convertView;
  }
}
