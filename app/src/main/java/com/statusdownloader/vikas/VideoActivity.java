package com.statusdownloader.vikas;

import android.os.Bundle;
import android.os.Environment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.statusdownloader.R;

import java.io.File;
import java.util.ArrayList;

public class VideoActivity extends AppCompatActivity implements View.OnClickListener {

    private static String TAG = ImageActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private VideoviewAdapter imageviewAdapter;
    private RelativeLayout rl_back_arrow,actionBar;
    private ArrayList<String> videoList;
    private Animation slideDown;
    private LinearLayout rl_no_data_found;
    private boolean loading = true;
    private int pastVisibleItems, visibleItemCount, totalItemcount;
    private int mPreviousTotal = 0;
    private int mLoadedItems = 0;
    private int data = 0;
    private int data1 = 0;
    private AdView mAdView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        setTitle("Video's");

        videoList = new ArrayList<>();

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

        /*String selection= MediaStore.Video.Media.DATA +" like?";
        String[] selectionArgs=new String[]{"%/WhatsApp/Media/.Statuses/%"};
        String[] parameters = { MediaStore.Video.Media._ID};
        Cursor videocursor = managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                parameters, selection, selectionArgs, MediaStore.Video.Media.DATE_TAKEN + " DESC");
*/
        File list[] = fileList.listFiles();
        if(list != null) {
            int k =0;
            rl_no_data_found.setVisibility(View.GONE);
            for (int i = 0; i < list.length; i++) {
                data1++;
                int length = list[i].getName().length();
                if ((list[i].getName().substring(length - 3).equals("mp4"))) {
                    videoList.add(fileList + "/" + list[i].getName());
                    k++;
                    data++;
                    /*if(data == 10)
                        break;*/
                } else {

                }


            }
            recyclerView = (RecyclerView) findViewById(R.id.rv_image);


            GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
            recyclerView.setLayoutManager(layoutManager);

            if(k != 0) {

                imageviewAdapter = new VideoviewAdapter(this, videoList);

                recyclerView.setAdapter(imageviewAdapter);

                /*recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                    }

                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        visibleItemCount = layoutManager.getChildCount();
                        totalItemcount = layoutManager.getItemCount();
                        pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                        if(loading) {
                            if((visibleItemCount + pastVisibleItems) >= totalItemcount) {
                                loading = false;
                            }
                        }

                        int visibleItemCount = layoutManager.getChildCount();
                        int totalItemCount = layoutManager.getItemCount();
                        int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

                        if (loading) {
                            if (totalItemCount > mPreviousTotal) {
                                loading = false;
                                mPreviousTotal = totalItemCount;
                            }
                        }
                        int visibleThreshold = 5;
                        if (!loading && (totalItemCount - visibleItemCount)
                                <= (firstVisibleItem + visibleThreshold)) {
                            // End has been reached

                            onLoadMore();

                            loading = true;
                        }
                    }
                });*/
            } else {
                rl_no_data_found.setVisibility(View.VISIBLE);
            }
        } else {
            rl_no_data_found.setVisibility(View.VISIBLE);
        }


    }

    private void onLoadMore() {
        //itemProgressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = data1; i <= videoList.size(); i++) {
                    videoList.add("SampleText : " + mLoadedItems);
                    mLoadedItems++;
                }
                imageviewAdapter.notifyDataSetChanged();
                //mActivityMainBinding.itemProgressBar.setVisibility(View.GONE);
            }
        }, 1500);

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
