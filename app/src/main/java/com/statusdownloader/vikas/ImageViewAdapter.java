package com.statusdownloader.vikas;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.statusdownloader.R;

import java.util.ArrayList;

public class ImageViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int IMAGE_VIEW = 0;
    private static final int NATIVE_AD_VIEW = 1;
    private Activity activity;
    private ArrayList<String> imageList;

    private static String LOG_TAG = "EXAMPLE";
    private NativeExpressAdView mAdView;
    private VideoController mVideoController;
    private OnClickListerner onClickListerner;

    public ImageViewAdapter(Activity activity, ArrayList<String> imageList, OnClickListerner onClickListerner) {

        this.activity = activity;
        this.imageList = imageList;
        this.onClickListerner = onClickListerner;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;

        if (i == IMAGE_VIEW) {
            view = LayoutInflater.from(activity).inflate(R.layout.single_image, viewGroup, false);
            return new MyViewHolder(view);
        } else {

            View unifiedNativeLayoutView = LayoutInflater.from(
                    viewGroup.getContext()).inflate(R.layout.native_ad_layout,
                    viewGroup, false);
            return new UnifiedNativeAdViewHolder(unifiedNativeLayoutView);

        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {

        if(getItemViewType(position) == IMAGE_VIEW) {

            MyViewHolder myViewHolder = (MyViewHolder) viewHolder;
            Bitmap myBitmap = BitmapFactory.decodeFile(imageList.get(position));

            //ImageView myImage = (ImageView) findViewById(R.id.imageviewTest);

            //imageView.setImageBitmap(myBitmap);


            myViewHolder.image.setImageBitmap(myBitmap);

            myViewHolder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListerner.onlick(position);
                }
            });

            Animation zoomIn = AnimationUtils.loadAnimation(activity.getApplicationContext(), R.anim.zoom_in);
            myViewHolder.image.startAnimation(zoomIn);
        } else {

        }
        /*} else {

            UnifiedNativeAd nativeAd = (UnifiedNativeAd) mRecyclerViewItems.get(position);
            populateNativeAdView(nativeAd, ((UnifiedNativeAdViewHolder) viewHolder).getAdView());

          /*  // Set its video options.
            mAdView.setVideoOptions(new VideoOptions.Builder()
                    .setStartMuted(true)
                    .build());

// The VideoController can be used to get lifecycle events and info about an ad's video
// asset. One will always be returned by getVideoController, even if the ad has no video
// asset.
            mVideoController = mAdView.getVideoController();
            mVideoController.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    Log.d(LOG_TAG, "Video playback is finished.");
                    super.onVideoEnd();
                }
            });

              // Set an AdListener for the AdView, so the Activity can take action when an ad has finished
              // loading.
            mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    if (mVideoController.hasVideoContent()) {
                        Log.d(LOG_TAG, "Received an ad that contains a video asset.");
                    } else {
                        Log.d(LOG_TAG, "Received an ad that does not contain a video asset.");
                    }
                }
            });

            mAdView.loadAd(new AdRequest.Builder().build());*/






    }

    @Override
    public int getItemViewType(int position) {
        /*if (position != 0) {
            if (position / 9 == 0)
                return NATIVE_AD_VIEW;
            else
                return IMAGE_VIEW;
        } else*/
            return IMAGE_VIEW;
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.iv_image);
        }
    }


    public class NaticeAdHolder extends RecyclerView.ViewHolder {

        private ImageView image;

        public NaticeAdHolder(@NonNull View itemView) {
            super(itemView);

            // Locate the NativeExpressAdView.
            mAdView = (NativeExpressAdView) itemView.findViewById(R.id.adView);
        }
    }


    private void populateNativeAdView(UnifiedNativeAd nativeAd,
                                      UnifiedNativeAdView adView) {
        // Some assets are guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        NativeAd.Image icon = nativeAd.getIcon();

        if (icon == null) {
            adView.getIconView().setVisibility(View.INVISIBLE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(icon.getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // Assign native ad object to the native view.
        adView.setNativeAd(nativeAd);
    }


    interface  OnClickListerner {
        void onlick(int position);
    }
}
