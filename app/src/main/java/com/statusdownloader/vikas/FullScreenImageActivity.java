package com.statusdownloader.vikas;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.statusdownloader.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FullScreenImageActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = FullScreenImageActivity.class.getSimpleName();
    private ImageView image, back, download, share;
    private RelativeLayout rl_back, rl_download, rl_share,actionBar;
    private Bitmap myBitmap;
    private String imagePath;
    private ArrayList<String> imageUrlList;
    private int imagePosition;
    private InterstitialAd interstitialAd;
    private Animation slideDown,zoomIn;
    private AdView mAdView;
    private static ViewPager mPager;
    private SlidingImage_Adapter slidingImage_adapter;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private int noOfSwipePage = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_scrren_image);


        imageUrlList = new ArrayList<>();

        slideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
        actionBar = (RelativeLayout)findViewById(R.id.actionBar);
        actionBar.startAnimation(slideDown);

        zoomIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);



        initData();


        // Initialize the Mobile Ads SDK
        MobileAds.initialize(this, getString(R.string.admob_app_id));

        // Find Banner ad
        mAdView = findViewById(R.id.adView);


        AdRequest adRequest = new AdRequest
                .Builder()
                .build();
        mAdView.loadAd(adRequest);


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
        interstitialAd = new InterstitialAd(FullScreenImageActivity.this);

        //insert interstitial id
        interstitialAd.setAdUnitId(getResources().getString(R.string.admob_interstitial_id));

        interstitialAd.loadAd(adRequest);

        interstitialAd.setAdListener(new AdListener( )
        {
            public void onAdLoaded() {
                //displayInterstitialAds();
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }
        });


    }


    private void initData() {

        image = (ImageView) findViewById(R.id.image);

        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);

        rl_download = (RelativeLayout) findViewById(R.id.rl_download);
        rl_download.setOnClickListener(this);

        rl_share = (RelativeLayout) findViewById(R.id.rl_share);
        rl_share.setOnClickListener(this);

        Intent intent = getIntent();
        if (intent != null) {

            imageUrlList = intent.getStringArrayListExtra("imageUrlList");
            imagePosition = Integer.valueOf(intent.getStringExtra("position"));
            //imagePath = intent.getStringExtra("imageUrl");
            myBitmap = BitmapFactory.decodeFile(imagePath);

            image.setImageBitmap(myBitmap);
        }
        image.startAnimation(zoomIn);

        image.setOnTouchListener(new ImageMatrixTouchHandler(this));

        mPager = (ViewPager) findViewById(R.id.pager);
        slidingImage_adapter = new SlidingImage_Adapter(this,this, imageUrlList);
        mPager.setAdapter(slidingImage_adapter);
        mPager.setCurrentItem(imagePosition);




       /* CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.indicator);

        indicator.setViewPager(mPager);*/

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.rl_back:
                finish();
                onBackPressed();
                break;

            case R.id.rl_download:
                downloadImage();
                break;

            case R.id.rl_share:
                shareImage();
                break;
        }
    }

    public void downloadImage() {
        FileOutputStream fos = null;
        File file = getDisc();
        if (!file.exists() && !file.mkdirs()) {
            //Toast.makeText(this, "Can't create directory to store image", Toast.LENGTH_LONG).show();
            //return;
            System.out.println("file not created");
            return;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyymmsshhmmss");
        String date = simpleDateFormat.format(new Date());
        String name = "FileName" + date + ".jpg";
        String file_name = file.getAbsolutePath() + "/" + name;
        File new_file = new File(file_name);
        System.out.println("new_file created");

        Drawable current =  slidingImage_adapter.views.get(mPager.getCurrentItem()).getDrawable();


        try {
            fos = new FileOutputStream(new_file);
            Bitmap bitmap = viewToBitmap(current, current.getBounds().width(), current.getBounds().height());
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

    public void refreshGallery(File file) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        sendBroadcast(intent);
    }

    private File getDisc() {
        String t = getCurrentDateAndTime();
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        return new File(file, getResources().getString(R.string.app_name));
    }

    private String getCurrentDateAndTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public static Bitmap viewToBitmap(Drawable view, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public void shareImage() {

        Log.d(TAG, "share image clicked");
        File imageFileToShare = new File(imageUrlList.get(mPager.getCurrentItem()));

        Log.d(TAG, "package name= " + getApplicationContext().getPackageName());
        //Toast.makeText(this, "share image clicked ", Toast.LENGTH_SHORT).show();

        Uri imgUri = Uri.parse(imageFileToShare.getAbsolutePath());
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.putExtra(Intent.EXTRA_STREAM, imgUri);
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, "If you want to save or share your friend's status, please click on this link \n https://play.google.com/store/apps/details?id=com.statusdownloader.vikas&hl=en");
        whatsappIntent.setType("image/*");
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
