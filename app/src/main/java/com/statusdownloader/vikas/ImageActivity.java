package com.statusdownloader.vikas;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.statusdownloader.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageActivity extends AppCompatActivity implements View.OnClickListener, ImageViewAdapter.OnClickListerner {

    private static String TAG = ImageActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private ImageViewAdapter imageviewAdapter;
    private RelativeLayout rl_back_arrow,actionBar;
    private ArrayList<String> imageList;
    private Animation slideDown;
    private LinearLayout rl_no_data_found;

    private AdView mAdView;

    // The number of native ads to load and display.
    public static final int NUMBER_OF_ADS = 5;

    // The AdLoader used to load ads.
    private AdLoader adLoader;

    // List of native ads that have been successfully loaded.
    private List<UnifiedNativeAd> mNativeAds = new ArrayList<>();




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        imageList = new ArrayList<>();

        initdata();
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
    }


    private void initdata() {



        slideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
        actionBar = (RelativeLayout)findViewById(R.id.rl_action_bar);
        actionBar.startAnimation(slideDown);

        rl_back_arrow = (RelativeLayout)findViewById(R.id.rl_back_arrow);
        rl_back_arrow.setOnClickListener(this);


        rl_no_data_found = (LinearLayout) findViewById(R.id.rl_no_data_found);

        File fileList = new File(Environment.getExternalStorageDirectory() + "/WhatsApp/Media/.Statuses/");

        Log.d(TAG, "initdata: fileList " + fileList);


        File list[] = fileList.listFiles();

        Log.d(TAG, "initdata: list " + list.toString());

        if(list != null) {
            int k =0;
            rl_no_data_found.setVisibility(View.GONE);
            for (int i = 0; i < list.length; i++) {

                int length = list[i].getName().length();
                if ((list[i].getName().substring(length - 3).equals("mp4"))) {

                } else {
                    imageList.add(fileList + "/" + list[i].getName());
                    k++;
                }


            }
            recyclerView = (RecyclerView) findViewById(R.id.rv_image);


            GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
            recyclerView.setLayoutManager(layoutManager);

            if (k != 0) {

                Log.d(TAG, "initdata: imagelist size= " + imageList.size());
                imageviewAdapter = new ImageViewAdapter(this, imageList,this);

                recyclerView.setAdapter(imageviewAdapter);
            } else {
             rl_no_data_found.setVisibility(View.VISIBLE);
            }
        } else {
            rl_no_data_found.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.rl_back_arrow :
                onBackPressed();
                break;
        }
    }

    @Override
    public void onlick(int position) {
        Intent intent = new Intent(this, FullScreenImageActivity.class);
        //intent.putExtra("imageUrl", imageList.get(position));
        intent.putStringArrayListExtra("imageUrlList", imageList);
        intent.putExtra("position", String.valueOf(position));
        startActivity(intent);
    }
}
