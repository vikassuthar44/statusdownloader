package com.statusdownloader.vikas;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.statusdownloader.R;

import java.io.File;
import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class VideoFragment extends Fragment {

    private static String TAG = VideoFragment.class.getSimpleName();
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
    private Activity activity;


    public VideoFragment(Activity activity) {
        // Required empty public constructor

        this.activity = activity;
        activity.setTitle("Two");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        videoList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        initdata(view);
        return view;
    }

    private void initdata(View view) {


        // Initialize the Mobile Ads SDK
        MobileAds.initialize(activity, getString(R.string.admob_app_id));

        // Find Banner ad
        mAdView = view.findViewById(R.id.adView);

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



        rl_no_data_found = (LinearLayout) view.findViewById(R.id.rl_no_data_found);

        File fileList = new File(Environment.getExternalStorageDirectory() + "/WhatsApp/Media/.Statuses/");


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
            recyclerView = (RecyclerView) view.findViewById(R.id.rv_image);


            GridLayoutManager layoutManager = new GridLayoutManager(activity, 2);
            recyclerView.setLayoutManager(layoutManager);

            if(k != 0) {

                imageviewAdapter = new VideoviewAdapter(activity, videoList);

                recyclerView.setAdapter(imageviewAdapter);

            } else {
                rl_no_data_found.setVisibility(View.VISIBLE);
            }
        } else {
            rl_no_data_found.setVisibility(View.VISIBLE);
        }


    }


}
