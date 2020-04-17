package com.statusdownloader.vikas;


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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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


        loadNativeAds(imageLayout);
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


    private void loadNativeAds(View nativeView) {

        ad_view = (UnifiedNativeAdView) nativeView.findViewById(R.id.ad_view);

        // The MediaView will display a video asset if one is present in the ad, and the
        // first image asset otherwise.
        ad_view.setMediaView((MediaView) ad_view.findViewById(R.id.ad_media));

        // Register the view used for each individual asset.
        ad_view.setHeadlineView(ad_view.findViewById(R.id.ad_headline));
        ad_view.setBodyView(ad_view.findViewById(R.id.ad_body));
        ad_view.setCallToActionView(ad_view.findViewById(R.id.ad_call_to_action));
        ad_view.setIconView(ad_view.findViewById(R.id.ad_icon));
        ad_view.setPriceView(ad_view.findViewById(R.id.ad_price));
        ad_view.setStarRatingView(ad_view.findViewById(R.id.ad_stars));
        ad_view.setStoreView(ad_view.findViewById(R.id.ad_store));
        ad_view.setAdvertiserView(ad_view.findViewById(R.id.ad_advertiser));


        AdLoader.Builder builder = new AdLoader.Builder(activity, activity.getResources().getString(R.string.ad_unit_id));
        adLoader = builder.forUnifiedNativeAd(
                new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        // A native ad loaded successfully, check if the ad loader has finished loading
                        // and if so, insert the ads into the list.
                        mNativeAds.add(unifiedNativeAd);
                        if (!adLoader.isLoading()) {
                            // Some assets are guaranteed to be in every UnifiedNativeAd.
                            ((TextView) nativeView.findViewById(R.id.ad_headline)).setText(unifiedNativeAd.getHeadline());
                            ((TextView) nativeView.findViewById(R.id.ad_body)).setText(unifiedNativeAd.getBody());
                            ((Button) nativeView.findViewById(R.id.ad_call_to_action)).setText(unifiedNativeAd.getCallToAction());

                            // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
                            // check before trying to display them.
                            NativeAd.Image icon = unifiedNativeAd.getIcon();
                            ImageView ad_icon = (ImageView) nativeView.findViewById(R.id.ad_icon);

                            if (icon == null) {
                                ad_icon.setVisibility(View.INVISIBLE);
                            } else {
                                ad_icon.setImageDrawable(icon.getDrawable());
                                ad_icon.setVisibility(View.VISIBLE);
                            }

                            TextView ad_price = (TextView) nativeView.findViewById(R.id.ad_price);
                            if (unifiedNativeAd.getPrice() == null) {
                                ad_price.setVisibility(View.INVISIBLE);
                            } else {
                                ad_price.setVisibility(View.VISIBLE);
                                ad_price.setText(unifiedNativeAd.getPrice());
                            }

                            TextView ad_store = (TextView) nativeView.findViewById(R.id.ad_store);
                            if (unifiedNativeAd.getStore() == null) {
                                ad_store.setVisibility(View.INVISIBLE);
                            } else {
                                ad_store.setVisibility(View.VISIBLE);
                                ad_store.setText(unifiedNativeAd.getStore());
                            }

                            RatingBar ad_stars = (RatingBar) nativeView.findViewById(R.id.ad_stars);
                            if (unifiedNativeAd.getStarRating() == null) {
                                ad_stars.setVisibility(View.INVISIBLE);
                            } else {
                                ad_stars.setRating(unifiedNativeAd.getStarRating().floatValue());
                                ad_stars.setVisibility(View.VISIBLE);
                            }

                            TextView ad_advertiser = (TextView) nativeView.findViewById(R.id.ad_advertiser);
                            if (unifiedNativeAd.getAdvertiser() == null) {
                                ad_advertiser.setVisibility(View.INVISIBLE);
                            } else {
                                ad_advertiser.setText(unifiedNativeAd.getAdvertiser());
                                ad_advertiser.setVisibility(View.VISIBLE);
                            }

                            // Assign native ad object to the native view.
                            ad_view.setNativeAd(unifiedNativeAd);
                        }
                    }
                }).withAdListener(
                new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        // A native ad failed to load, check if the ad loader has finished loading
                        // and if so, insert the ads into the list.
                        Log.e("MainActivity", "The previous native ad failed to load. Attempting to"
                                + " load another.");
                        if (!adLoader.isLoading()) {
                            //insertAdsInMenuItems();
                        }
                    }
                }).build();

        // Load the Native ads.
        adLoader.loadAds(new AdRequest.Builder().build(), NUMBER_OF_ADS);
    }

}
