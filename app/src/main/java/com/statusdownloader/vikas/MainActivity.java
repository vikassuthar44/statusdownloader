package com.statusdownloader.vikas;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.statusdownloader.R;


import java.io.File;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();
    private RelativeLayout rl_image,rl_video,rl_action_bar,rl_instagram;
    private ImageView imageView;
    private AdView mAdView;
    private Animation leftToRight, rightToLeft, slideDown, zoomIn;
    private Button tab;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        leftToRight = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_to_right);
        rightToLeft = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.right_to_left);
        slideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
        zoomIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);

        // Initialize the Mobile Ads SDK
        MobileAds.initialize(this, getString(R.string.admob_app_id));

        // Find Banner ad
        mAdView = findViewById(R.id.adView);

       // for(int i=0;i<1000;i++) {
            AdRequest adRequest = new AdRequest
                    .Builder()
                    .build();
            mAdView.loadAd(adRequest);
        //}

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.d(TAG, "onAdLoaded: ");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.d(TAG, "onAdFailedToLoad: error code " + errorCode);
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Log.d(TAG, "onAdOpened: ");
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                Log.d(TAG, "onAdClicked: ");
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Log.d(TAG, "onAdLeftApplication: ");
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
                Log.d(TAG, "onAdClosed: ");
            }
        });


        //AdRequest adRequest = new AdRequest.Builder().build();
        // Display Banner ad
        //mAdView.loadAd(adRequest);

        //Log.d(TAG, " adRequest " + adRequest.getKeywords());

        //action bar layout
        rl_action_bar = (RelativeLayout) findViewById(R.id.rl_action_bar);
        rl_action_bar.startAnimation(slideDown);

        //tab activity
        tab = (Button) findViewById(R.id.tab);
        tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TabActivity.class);
                startActivity(intent);
            }
        });



        rl_image = (RelativeLayout) findViewById(R.id.rl_image);
        imageView = (ImageView) findViewById(R.id.image);
        imageView.startAnimation(zoomIn);
        rl_image.startAnimation(leftToRight);



        rl_video = (RelativeLayout) findViewById(R.id.rl_video);
        rl_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), VideoActivity.class);
                startActivity(intent);
            }
        });
        rl_video.startAnimation(rightToLeft);

        verifyStoragePermissions(MainActivity.this);

        rl_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(v.getId() == R.id.rl_image) {

                    File fileList = new File(Environment.getExternalStorageDirectory() + "/WhatsApp/Media/.Statuses/");

                    File list[] = fileList.listFiles();
                   /* for(int i = 0; i < fileList.listFiles().length; i++) {

                        System.out.println(TAG + list[i].getName() + " value = " + i);
                    }*/

                    Intent intent = new Intent(getApplicationContext(), ImageActivity.class);
                    startActivity(intent);


                }
            }
        });
    }
    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
