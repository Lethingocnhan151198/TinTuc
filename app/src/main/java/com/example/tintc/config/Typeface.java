package com.example.tintc.config;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

public class Typeface {

    public static Typeface typeface;

    public static Typeface getInstance(){
        if(typeface==null){
            typeface = new Typeface();
        }
        return typeface;
    }

    public Spannable getTypeface(String text,int start, int end){
        Spannable wordtoSpan = new SpannableString(text);
        wordtoSpan.setSpan(new ForegroundColorSpan(Color.BLUE), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return wordtoSpan;
    }

}
