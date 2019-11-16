package com.statusdownloader;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class VideoviewAdapter extends RecyclerView.Adapter<VideoviewAdapter.MyViewHolder> {

    private static final String TAG = VideoviewAdapter.class.getSimpleName();

    private Activity activity;
    private ArrayList<String> videoList;

    public VideoviewAdapter(Activity activity, ArrayList<String> imageList) {

        this.activity = activity;
        this.videoList = imageList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.activity_single_video, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int position) {

        Glide.with(activity)
                .load(videoList.get(position))
                .into(myViewHolder.image);

/*
        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(videoList.get(position),
                MediaStore.Images.Thumbnails.MINI_KIND);

        BitmapDrawable bitmapDrawable = new BitmapDrawable(thumb);
        myViewHolder.image.setBackgroundDrawable(bitmapDrawable);*/

        //Bitmap myBitmap = BitmapFactory.decodeFile(imageList.get(position));

        //ImageView myImage = (ImageView) find ViewById(R.id.imageviewTest);

        //imageView.setImageBitmap(myBitmap);

        //myViewHolder.video.setVideoPath(videoList.get(position));

        /*MediaPlayer.OnErrorListener mOnErrorListener = new MediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                // Your code goes here
                return true;
            }
        };*/

        /*String videoUrl = videoList.get(position);
        videoUrl = videoUrl.replace("upload/","upload/vs_20,dl_200,h_200,e_loop/").replace(".mp4",".gif").replace(".mov",".gif");


        Glide.with(activity)
                .asGif()
                .load(videoUrl)
                .placeholder(R.drawable.app_icon)
                .into(myViewHolder.video_gif);
*/
       // Uri uri = Uri.parse(videoList.get(position));



       /* Bitmap thumb = ThumbnailUtils.createVideoThumbnail(videoList.get(position), MediaStore.Images.Thumbnails.MINI_KIND);

        BitmapDrawable bitmapDrawable = new BitmapDrawable(thumb);
        myViewHolder.video.setBackgroundDrawable(bitmapDrawable);

        myViewHolder.video.setVideoURI(uri);*/
        //myViewHolder.video.seekTo(1);
       //myViewHolder.video.start();
       //myViewHolder.video.setOnErrorListener(mOnErrorListener);
        /*myViewHolder.video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {

                //mp.seekTo(2000);
                //mp.setVolume(0, 0);
                //mp.setLooping(true);
                myViewHolder.tv_duration.setText("00." + (mp.getDuration()/1000));
                //mp.getDuration();

                Log.d(TAG, "onPrepared: " + mp.getDuration());
            }
        });*/

        //myViewHolder.video.seekTo(1);


        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, FullScreenVideoActivity.class);
                intent.putExtra("videoUrl",videoList.get(position));
                activity.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private VideoView video;
        private TextView tv_duration;
        private ImageView video_gif;
        private AppCompatImageView image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_duration = (TextView) itemView.findViewById(R.id.tv_duration);
            video = (VideoView) itemView.findViewById(R.id.video);
            video_gif = (ImageView) itemView.findViewById(R.id.video_gif);
            image = (AppCompatImageView) itemView.findViewById(R.id.image);
        }
    }

}
