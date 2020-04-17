package com.statusdownloader.vikas;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.statusdownloader.BuildConfig;
import com.statusdownloader.R;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class TabActivity extends AppCompatActivity {

    private static final String TAG = TabActivity.class.getSimpleName();
    private RelativeLayout rl_image, rl_video, rl_action_bar, rl_instagram, rl_share, rl_menu;
    private ImageView imageView;
    private AdView mAdView;
    private Animation leftToRight, rightToLeft, slideDown, zoomIn;
    private Button tab;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ActionBar actionBar;
    private Dialog dialog;
    // tab titles
    private String[] tabTitles = new String[]{"Images", "Videos"};
    private InterstitialAd mInterstitialAd;
    private ProgressBar progressBar;

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

    private TextView app_version;

    File fileList;


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page_activity);
        //verifyStoragePermissions(TabActivity.this);

        rl_share = (RelativeLayout) findViewById(R.id.rl_share);
        rl_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareAppLink();
            }
        });


        dl = (DrawerLayout) findViewById(R.id.activity_main);
        t = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);

        dl.addDrawerListener(t);
        t.syncState();




        nv = (NavigationView) findViewById(R.id.nv);


        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.how_use:
                        dl.closeDrawers();
                        Dialog helpDailog = new Dialog(TabActivity.this);
                        helpDailog.setContentView(R.layout.dailo_help);
                        helpDailog.show();

                        TextView ok = helpDailog.findViewById(R.id.ok);
                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                helpDailog.dismiss();
                            }
                        });

                        break;
                    case R.id.more_app:
                        try {
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:AmazeApp")));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/developer?id=AmazeApp")));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;

                    case R.id.whatsapp_normal_business :
                        dl.closeDrawers();
                        fileList = new File(Environment.getExternalStorageDirectory() + "/WhatsApp/Media/.Statuses/");
                        fragmentCall(fileList);
                        break;

                    case R.id.whatsapp_business:
                        dl.closeDrawers();

                        fileList = new File(Environment.getExternalStorageDirectory() + "/WhatsApp Business/Media/.Statuses/");
                        fragmentCall(fileList);
                        break;

                    case R.id.rate:
                        final String appPackageName1 = getPackageName(); // getPackageName() from Context or Activity object
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName1)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName1)));
                        }
                        break;


                    case R.id.share:
                        shareAppLink();
                        break;
                    default:
                        return true;
                }


                return true;

            }
        });

        rl_menu = (RelativeLayout) findViewById(R.id.rl_menu);
        rl_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!dl.isDrawerOpen(Gravity.LEFT))
                    dl.openDrawer(Gravity.LEFT);
                else dl.closeDrawer(Gravity.RIGHT);
            }
        });


        progressBar = findViewById(R.id.progressBar1);

        fragmentCall(new File(Environment.getExternalStorageDirectory() + "/WhatsApp/Media/.Statuses/"));

        mInterstitialAd = new InterstitialAd(this);

        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.admob_interstitial_video_id));

        ScheduledExecutorService scheduler =
                Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {

            public void run() {
                Log.i("hello", "world");
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                        } else {
                            Log.d("TAG", " Interstitial not loaded");
                        }

                        prepareAd();


                    }
                });

            }
        }, 45, 30, TimeUnit.SECONDS);

    }


    private void fragmentCall(File fileList) {
        if (verifyStoragePermissions(TabActivity.this)) {
            // Find the view pager that will allow the user to swipe between fragments
            ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

            progressBar.setVisibility(View.GONE);
            // Create an adapter that knows which fragment should be shown on each page
            SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(this, getSupportFragmentManager(),fileList);

            // Set the adapter onto the view pager
            viewPager.setAdapter(adapter);

            // Give the TabLayout the ViewPager
            TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
            tabLayout.setupWithViewPager(viewPager);
        } else {
            dialog = new Dialog(this);
            dialog.setContentView(R.layout.storage_permission_dialog);

            dialog.setCancelable(false);

            Button btnAllow = dialog.findViewById(R.id.btnAllow);
            btnAllow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    if (verifyStoragePermissions(TabActivity.this)) {

                    } else {
                        dialog.show();
                    }
                }
            });
            dialog.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (t.onOptionsItemSelected(item))
            return true;


        return super.onOptionsItemSelected(item);
    }

    private void prepareAd() {


        AdRequest adRequest = new AdRequest.Builder().build();

        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });

    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    @Override
    public void onBackPressed() {
        InterstitialAd mInterstitialAd1 = new InterstitialAd(this);
        ;
        // set the ad unit ID
        mInterstitialAd1.setAdUnitId(getString(R.string.admob_interstitial_video_id));
        AdRequest adRequest1 = new AdRequest.Builder().build();

        // Load ads into Interstitial Ads
        mInterstitialAd1.loadAd(adRequest1);

       /* mInterstitialAd1.setAdListener(new AdListener() {
            public void onAdLoaded() {
                if (mInterstitialAd1.isLoaded()) {
                    mInterstitialAd1.show();
                }
            }
        });*/
        if (mInterstitialAd1.isLoaded()) {
            mInterstitialAd1.show();
        }
        super.onBackPressed();
    }



//setupViewPager(viewPager);


    class SimpleFragmentPagerAdapter extends FragmentStatePagerAdapter {
        private Context mContext;
        private File file;

        public SimpleFragmentPagerAdapter(Context context, FragmentManager fm, File file) {
            super(fm);
            mContext = context;
            this.file = file;
        }

        // This determines the fragment for each tab
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new ImageFragment(TabActivity.this,file);
            } else {
                return new VideoFragment(TabActivity.this, file);
            }
        }

        // This determines the number of tabs
        @Override
        public int getCount() {
            return 2;
        }

        // This determines the title for each tab
        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            switch (position) {
                case 0:
                    return mContext.getString(R.string.images);
                case 1:
                    return mContext.getString(R.string.videos);
                default:
                    return null;
            }
        }
    }


    /**
     * Checks if the app has permission to write to device storage
     * <p>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static boolean verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
            return false;
        } else
            return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void shareAppLink() {

        Log.d(TAG, "share image clicked");
        File imageFileToShare = new File(String.valueOf(getDrawable(R.drawable.app_icon)));

        Log.d(TAG, "package name= " + getApplicationContext().getPackageName());
        //Toast.makeText(this, "share image clicked ", Toast.LENGTH_SHORT).show();
        Uri imageUri = null;
        try {
            imageUri = Uri.parse(MediaStore.Images.Media.insertImage(this.getContentResolver(),
                    BitmapFactory.decodeResource(getResources(), R.drawable.app_icon), null, null));
        } catch (NullPointerException e) {
        }

        //Uri imgUri = Uri.parse(imageFileToShare.getAbsolutePath());
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, "If you want to save or share your friend's status, please click on this link \n https://play.google.com/store/apps/details?id=com.statusdownloader.vikas&hl=en");
        whatsappIntent.setType("image/*");
        whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(Intent.createChooser(whatsappIntent, ""));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Whatsapp have not been installed.", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    dialog.dismiss();
                    // Find the view pager that will allow the user to swipe between fragments
                    ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

                    progressBar.setVisibility(View.GONE);
                    // Create an adapter that knows which fragment should be shown on each page
                    SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(TabActivity.this, getSupportFragmentManager(), fileList);

                    // Set the adapter onto the view pager
                    viewPager.setAdapter(adapter);

                    // Give the TabLayout the ViewPager
                    TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
                    tabLayout.setupWithViewPager(viewPager);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
