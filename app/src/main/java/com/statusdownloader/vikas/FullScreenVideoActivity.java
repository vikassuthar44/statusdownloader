package com.statusdownloader.vikas;

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
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
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

public class FullScreenVideoActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = FullScreenImageActivity.class.getSimpleName();
    private ImageView  back, download, share;
    private VideoView  video;
    private RelativeLayout rl_back, rl_download, rl_share,actionBar;
    private Bitmap myBitmap;
    private String videoPath;
    private Animation slideDown;
    private AdView mAdView;
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_video);

        /*getSupportActionBar().setTitle("Image");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
        //getSupportActionBar().hide();
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




    private void initData() {

        slideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
        actionBar = (RelativeLayout)findViewById(R.id.actionBar);
        actionBar.startAnimation(slideDown);

        video = (VideoView) findViewById(R.id.video);


        rl_back = (RelativeLayout)findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);

        rl_download = (RelativeLayout)findViewById(R.id.rl_download);
        rl_download.setOnClickListener(this);

        rl_share = (RelativeLayout)findViewById(R.id.rl_share);
        rl_share.setOnClickListener(this);

        Intent intent = getIntent();
        if(intent != null) {


            videoPath = intent.getStringExtra("videoUrl");
            Uri uri = Uri.parse(videoPath);


            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(video);
            video.setMediaController(mediaController);
            video.setVideoURI(uri);
            video.requestFocus();
            video.start();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.rl_back :
                onBackPressed();
                break;

            case R.id.rl_download :
                saveVideoToInternalStorage(videoPath);
                break;

            case R.id.rl_share :
                shareImage();
                break;
        }
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
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, "If you want to save or share your friend's status, please click on this link \n https://status-donwloaders.web.app/");
        whatsappIntent.setType("video/*");
        whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(Intent.createChooser(whatsappIntent, ""));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Whatsapp have not been installed.", Toast.LENGTH_LONG).show();
        }

    }
}
