package com.statusdownloader.vikas;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.statusdownloader.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FullScreenVideoActivity extends AppCompatActivity{

    private static final String TAG = FullScreenImageActivity.class.getSimpleName();
    private ImageView  back, download, share;
    private VideoView  video;
    private Bitmap myBitmap;
    private String videoPath;
    private Animation slideDown;
    private AdView mAdView;
    private InterstitialAd interstitialAd;

    private boolean isRotate = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_video);


        initData();

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



        // prepare for interstitial ad
        interstitialAd = new InterstitialAd(FullScreenVideoActivity.this);

        //insert interstitial id
        interstitialAd.setAdUnitId(getResources().getString(R.string.admob_interstitial_id));

        interstitialAd.loadAd(adRequest);




    }




    @SuppressLint("RestrictedApi")
    private void initData() {


        video = (VideoView) findViewById(R.id.video);



        FloatingActionButton fabAdd = (FloatingActionButton) findViewById(R.id.fabAdd);

        FloatingActionButton fabShare = (FloatingActionButton) findViewById(R.id.fabShare);
        FloatingActionButton fabDownload = (FloatingActionButton) findViewById(R.id.fabDownload);

        LinearLayout rl_download_fab = (LinearLayout) findViewById(R.id.rl_download_fab);
        LinearLayout rl_share_fab = (LinearLayout) findViewById(R.id.rl_share_fab);

        Intent intent = getIntent();

            videoPath = intent.getStringExtra("videoUrl");
            Uri uri = Uri.parse(videoPath);


            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(video);
            video.setMediaController(mediaController);
            video.setVideoURI(uri);
            video.requestFocus();
            video.start();

            mediaController.setPadding(0,0,0,25);

            if(mediaController.isShowing()) {
                fabAdd.setVisibility(View.GONE);
                fabDownload.setVisibility(View.GONE);
                fabShare.setVisibility(View.GONE);
            } else {
                fabAdd.setVisibility(View.VISIBLE);
                fabDownload.setVisibility(View.VISIBLE);
                fabShare.setVisibility(View.VISIBLE);
            }




        init(rl_download_fab);
        init(rl_share_fab);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRotate = rotateFab(v, !isRotate);
                if(isRotate){
                    showIn(rl_share_fab);
                    showIn(rl_download_fab);
                }else{
                    showOut(rl_share_fab);
                    showOut(rl_download_fab);
                }

            }
        });

        rl_share_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareImage();
            }
        });

        rl_download_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadVideo(videoPath);
            }
        });



    }


    private   boolean rotateFab(final View v, boolean rotate) {
        v.animate().setDuration(200)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                })
                .rotation(rotate ? 135f : 0f);
        return rotate;
    }

    private  void init(final View v) {
        v.setVisibility(View.GONE);
        v.setTranslationY(v.getHeight());
        v.setAlpha(0f);
    }


    private   void showOut(final View v) {
        v.setVisibility(View.VISIBLE);
        v.setAlpha(1f);
        v.setTranslationY(0);
        v.animate()
                .setDuration(200)
                .translationY(v.getHeight())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        v.setVisibility(View.GONE);
                        super.onAnimationEnd(animation);
                    }
                }).alpha(0f)
                .start();
    }

    private void showIn(final View v) {
        v.setVisibility(View.VISIBLE);
        v.setAlpha(0f);
        v.setTranslationY(v.getHeight());
        v.animate()
                .setDuration(200)
                .translationY(0)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                })
                .alpha(1f)
                .start();
    }




    private void saveVideoToInternalStorage (String filePath) {
        downloadVideo(filePath);
    }






    public void downloadVideo(String filepath) {

        InputStream in = null;

        FileOutputStream fos= null;
        File file = getDisc();
        if(!file.exists() && !file.mkdirs()) {
            //Toast.makeText(this, "Can't create directory to store image", Toast.LENGTH_LONG).show();
            //return;
            System.out.println("file not created");
            return;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyymmsshhmmss");
        String date = simpleDateFormat.format(new Date());
        String name = "FileName"+date+".mp4";
        String file_name = file.getAbsolutePath()+"/"+name;
        File new_file = new File(file_name);
        System.out.println("new_file created");
        try {

            in = new FileInputStream(filepath);
            fos= new FileOutputStream(new_file);

            byte[] buf = new byte[1024];
            int len;

            while ((len = in.read(buf)) > 0) {
                fos.write(buf, 0, len);
            }

            Bitmap bitmap = viewToBitmap(video, video.getWidth(), video.getHeight() );
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            Toast.makeText(this, "Save success", Toast.LENGTH_LONG).show();
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            System.out.println("FNF");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        refreshGallery(new_file);

        if(interstitialAd.isLoaded()) {
            Log.d(TAG, "downloadImage: df");
            interstitialAd.show();
        } else {
            Log.d(TAG, "downloadImage: dfsd");
        }
    }
    public void refreshGallery(File file){
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        sendBroadcast(intent);
    }

    private File getDisc(){
        String t= getCurrentDateAndTime();
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        return new File(file, getResources().getString(R.string.app_name));
    }

    private String getCurrentDateAndTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public static Bitmap viewToBitmap(View view, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public void shareImage() {

        File videoFile = new File(videoPath);
        Log.d(TAG, "share image clicked");

        Uri imgUri = Uri.parse(videoFile.getAbsolutePath());
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.putExtra(Intent.EXTRA_STREAM, imgUri);
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, "If you want to save or share your friend's status, please click on this link \n https://play.google.com/store/apps/details?id=com.statusdownloader.vikas&hl=en");
        whatsappIntent.setType("video/*");
        whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        if(interstitialAd.isLoaded()) {
            Log.d(TAG, "downloadImage: df");
            interstitialAd.show();
        } else {
            Log.d(TAG, "downloadImage: dfsd");
        }
        try {
            startActivity(Intent.createChooser(whatsappIntent, ""));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Whatsapp have not been installed.", Toast.LENGTH_LONG).show();
        }

    }
}
