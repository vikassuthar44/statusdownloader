package com.statusdownloader;

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

import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageActivity extends AppCompatActivity implements View.OnClickListener {

    private static String TAG = ImageActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private ImageViewAdapter imageviewAdapter;
    private RelativeLayout rl_back_arrow,actionBar;
    private ArrayList<String> imageList;
    private Animation slideDown;
    private LinearLayout rl_no_data_found;

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

                imageviewAdapter = new ImageViewAdapter(this, imageList);

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
}
