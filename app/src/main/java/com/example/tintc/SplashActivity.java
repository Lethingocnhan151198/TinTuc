package com.example.tintc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.example.tintc.view.HomeActivity;
import com.example.tintc.view.LoginActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class SplashActivity extends AppCompatActivity {
    private CircleImageView imgSplash;
    private ConstraintLayout lnSplash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgSplash = findViewById(R.id.imgSplash);
        lnSplash  = findViewById(R.id.lnSplash);
        start();
    }

    private void start() {
        TranslateAnimation animation = new TranslateAnimation(0,-1000,0,500,0,0,0,0);
        animation.setDuration(3000);
        imgSplash.startAnimation(animation);
        new Thread(){
            @Override
            public void run() {
                try {
                    sleep(4000);
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    finish();
                }
            }
        }.start();
    }
}
