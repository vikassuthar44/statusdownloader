package com.statusdownloader.amazeapp;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.statusdownloader.R;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

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


    // The number of native ads to load and display.
    //public static final int NUMBER_OF_ADS = 5;

    // The AdLoader used to load ads.
    //private AdLoader adLoader;

    // List of native ads that have been successfully loaded.
    //private List<UnifiedNativeAd> mNativeAds = new ArrayList<>();


    // List of MenuItems and native ads that populate the RecyclerView.
    private List<Object> mRecyclerViewItems = new ArrayList<>();

    private File file;


    private ProgressBar progressBar1;
    private AVLoadingIndicatorView avLoadingIndicatorView;

    public VideoFragment(Activity activity, File file) {
        // Required empty public constructor

        this.activity = activity;
        activity.setTitle("Two");
        this.file = file;
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

        progressBar1 = (ProgressBar) view.findViewById(R.id.progressBar1);
        avLoadingIndicatorView =  view.findViewById(R.id.avLoadingIndicatorView);
        if(videoList != null && !videoList.isEmpty()) {
            videoList.clear();
            imageviewAdapter.notifyDataSetChanged();
            progressBar1.setVisibility(View.VISIBLE);
            avLoadingIndicatorView.setVisibility(View.VISIBLE);
        }

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

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_image);

        rl_no_data_found = (LinearLayout) view.findViewById(R.id.rl_no_data_found);

        //File fileList = new File(Environment.getExternalStorageDirectory() + "/WhatsApp/Media/.Statuses/");

        //loadNativeAds();

        File list[] = file.listFiles();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(list != null) {
                    progressBar1.setVisibility(View.GONE);
                    avLoadingIndicatorView.setVisibility(View.GONE);
                    int k =0;
                    rl_no_data_found.setVisibility(View.GONE);
                    for (int i = 0; i < list.length; i++) {
                        data1++;
                        int length = list[i].getName().length();
                        if ((list[i].getName().substring(length - 3).equals("mp4"))) {
                            videoList.add(file + "/" + list[i].getName());
                            ImageListData imageListData = new ImageListData(file + "/" + list[i].getName());
                            mRecyclerViewItems.add(imageListData);
                            k++;
                            data++;
                    /*if(data == 10)
                        break;*/
                        } else {

                        }


                    }



                    StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);


                    //GridLayoutManager layoutManager = new GridLayoutManager(activity, 2);
                    recyclerView.setLayoutManager(staggeredGridLayoutManager);

                    if(k != 0) {

                        imageviewAdapter = new VideoviewAdapter(activity, videoList,mRecyclerViewItems);

                        recyclerView.setAdapter(imageviewAdapter);

                    } else {
                        rl_no_data_found.setVisibility(View.VISIBLE);
                    }
                } else {
                    progressBar1.setVisibility(View.GONE);
                    avLoadingIndicatorView.setVisibility(View.GONE);
                    rl_no_data_found.setVisibility(View.VISIBLE);
                }
            }
        },2000);



    }





}
