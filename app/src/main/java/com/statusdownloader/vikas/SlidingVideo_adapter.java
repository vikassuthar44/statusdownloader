package com.statusdownloader.vikas;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.RequiresApi;
import androidx.viewpager.widget.PagerAdapter;

import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.statusdownloader.R;

import java.util.ArrayList;
import java.util.HashMap;

public class SlidingVideo_adapter extends PagerAdapter {

    private ArrayList<String> videoUrlList;
    private LayoutInflater inflater;
    private Context context;
    private Bitmap myBitmap;
    private AdView mAdView;
    private Activity activity;
    private VideoView currentVideo;
    HashMap<Integer, VideoView> views = new HashMap();


    public SlidingVideo_adapter(Context context,Activity activity, ArrayList<String> IMAGES) {
        this.context = context;
        this.videoUrlList=IMAGES;
        this.activity = activity;
        inflater = LayoutInflater.from(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        views.remove(position, currentVideo);
    }

    @Override
    public int getCount() {
        return videoUrlList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.slidingvideo_layout, view, false);

        assert imageLayout != null;
        final VideoView video = (VideoView) imageLayout
                .findViewById(R.id.video);
/*
        if(position > 0) {
            //imageView.setImageResource(IMAGES.get(position));
            Uri uri = Uri.parse(videoUrlList.get(position - 1));


            MediaController mediaController = new MediaController(context);
            mediaController.setAnchorView(video);
            video.setMediaController(mediaController);
            video.setVideoURI(uri);
            video.pause();
        }


        if(position > 0) {
            //imageView.setImageResource(IMAGES.get(position));
            Uri uri = Uri.parse(videoUrlList.get(position));


            MediaController mediaController = new MediaController(context);
            mediaController.setAnchorView(video);
            video.setMediaController(mediaController);
            video.setVideoURI(uri);
            video.pause();
            //video.suspend();
        }*/

        //imageView.setImageResource(IMAGES.get(position));
        Uri uri = Uri.parse(videoUrlList.get(position));


        MediaController mediaController = new MediaController(context);
        mediaController.setAnchorView(video);
        video.setMediaController(mediaController);
        video.setVideoURI(uri);
        video.requestFocus();
        video.start();


        view.addView(imageLayout, 0);
        views.put(position, video);

        currentVideo = video;

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

    public VideoView getCurrentImageView() {
        return currentVideo;
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
