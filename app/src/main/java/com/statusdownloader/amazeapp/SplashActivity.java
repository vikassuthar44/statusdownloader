package com.statusdownloader.amazeapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;


import com.statusdownloader.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView imageView = (ImageView) findViewById(R.id.splash_image);

        /*Glide.with(this)
                .load(R.drawable.splash)
                .placeholder(R.drawable.splash)
                .into(imageView);*/

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                finish();
               startActivity(new Intent(getApplicationContext(), TabActivity.class));
            }

        }, 2000L);
    }
}
