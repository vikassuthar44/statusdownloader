package com.statusdownloader.vikas;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.google.android.gms.ads.AdView;
import com.google.android.material.tabs.TabLayout;
import com.statusdownloader.R;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.io.File;


public class TabActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RelativeLayout rl_image,rl_video,rl_action_bar,rl_instagram,rl_share;
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

        if(verifyStoragePermissions(TabActivity.this)) {
            // Find the view pager that will allow the user to swipe between fragments
            ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

            // Create an adapter that knows which fragment should be shown on each page
            SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(this, getSupportFragmentManager());

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
                    if(verifyStoragePermissions(TabActivity.this)){

                    } else {
                        dialog.show();
                    }
                }
            });
            dialog.show();
        }
    }


        //setupViewPager(viewPager);





    class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {
        private Context mContext;

        public SimpleFragmentPagerAdapter(Context context, FragmentManager fm) {
            super(fm);
            mContext = context;
        }

        // This determines the fragment for each tab
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new ImageFragment(TabActivity.this);
            } else {
                return new VideoFragment(TabActivity.this);
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
     *
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

                    // Create an adapter that knows which fragment should be shown on each page
                    SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(TabActivity.this, getSupportFragmentManager());

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
