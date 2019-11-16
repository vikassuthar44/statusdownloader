package com.statusdownloader;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaPlayer;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_video);

        /*getSupportActionBar().setTitle("Image");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
        //getSupportActionBar().hide();
        initData();



    }




    private void initData() {

        slideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
        actionBar = (RelativeLayout)findViewById(R.id.actionBar);
        actionBar.startAnimation(slideDown);

        video = (VideoView) findViewById(R.id.video);

        back = (ImageView) findViewById(R.id.iv_back);
        back.setOnClickListener(this);
        download = (ImageView) findViewById(R.id.iv_download);
        download.setOnClickListener(this);
        share = (ImageView) findViewById(R.id.iv_share);
        share.setOnClickListener(this);

        rl_back = (RelativeLayout)findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);

        rl_download = (RelativeLayout)findViewById(R.id.rl_download);
        rl_download.setOnClickListener(this);

        rl_share = (RelativeLayout)findViewById(R.id.rl_share);
        rl_share.setOnClickListener(this);

        Intent intent = getIntent();
        if(intent != null) {

            /*MediaPlayer.OnErrorListener mOnErrorListener = new MediaPlayer.OnErrorListener() {

                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    // Your code goes here
                    return true;
                }
            };*/
            videoPath = intent.getStringExtra("videoUrl");
            Uri uri = Uri.parse(videoPath);

            //video.setVideoPath(videoPath);
           // video.setVideoURI(uri);
            //video.start();
            /*video.setOnErrorListener(mOnErrorListener);

            video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setVolume(0f,100f);
                    mp.setLooping(true);
                }
            });*/
            //myBitmap = BitmapFactory.decodeFile(imagePath);

            //image.setImageBitmap(myBitmap);


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
       /* Uri videoURI = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                ? FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName(), videoFile)
                : Uri.fromFile(videoFile);
        ShareCompat.IntentBuilder.from(this)
                .setStream(videoURI)
                .setType("video/*")
                .setChooserTitle("Share video...")
                .startChooser();*/
        Log.d(TAG, "share image clicked");

        Uri imgUri = Uri.parse(videoFile.getAbsolutePath());
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.putExtra(Intent.EXTRA_STREAM, imgUri);
        whatsappIntent.setType("video/*");
        whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(Intent.createChooser(whatsappIntent, ""));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Whatsapp have not been installed.", Toast.LENGTH_LONG).show();
        }

    }
}
