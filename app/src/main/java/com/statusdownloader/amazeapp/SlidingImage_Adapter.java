package com.statusdownloader.amazeapp;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.viewpager.widget.PagerAdapter;

import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.statusdownloader.R;

public class SlidingImage_Adapter extends PagerAdapter {

    private ArrayList<String> IMAGES;
    private LayoutInflater inflater;
    private Context context;
    private Bitmap myBitmap;
    private AdView mAdView;
    private Activity activity;
    private ImageView currentImage;
    HashMap<Integer, ImageView> views = new HashMap();

    // The number of native ads to load and display.
    public static final int NUMBER_OF_ADS = 1;

    // The AdLoader used to load ads.
    private AdLoader adLoader;
    private UnifiedNativeAdView ad_view;

    // List of native ads that have been successfully loaded.
    private List<UnifiedNativeAd> mNativeAds = new ArrayList<>();



    public SlidingImage_Adapter(Context context,Activity activity, ArrayList<String> IMAGES) {
        this.context = context;
        this.IMAGES=IMAGES;
        this.activity = activity;
        inflater = LayoutInflater.from(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        views.remove(position, currentImage);
    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.image);

        //Animation zoomIn = AnimationUtils.loadAnimation(activity.getApplicationContext(), R.anim.zoom_in);


        //imageView.setImageResource(IMAGES.get(position));
        myBitmap = BitmapFactory.decodeFile(IMAGES.get(position));
        imageView.setImageBitmap(myBitmap);

        imageView.setOnTouchListener(new ImageMatrixTouchHandler(context));

       // imageView.setAnimation(zoomIn);


       // loadNativeAds(imageLayout);
        view.addView(imageLayout, 0);
        views.put(position, imageView);

        currentImage = imageView;

        // Initialize the Mobile Ads SDK
        MobileAds.initialize(context, activity.getResources().getString(R.string.admob_app_id));

        // Find Banner ad
        mAdView = imageLayout.findViewById(R.id.adView);


        AdRequest adRequest = new AdRequest
                .Builder()
                .build();
        mAdView.loadAd(adRequest);



        return imageLayout;
    }

    public ImageView getCurrentImageView() {
        return currentImage;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }




}
