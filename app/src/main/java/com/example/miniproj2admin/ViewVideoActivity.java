package com.example.miniproj2admin;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class ViewVideoActivity extends AppCompatActivity {
String key,videouri;
private VideoView videoView;
private Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_video);
        key=getIntent().getStringExtra("key").toString();
        videouri=getIntent().getStringExtra("videourl").toString();
       // Toast.makeText(this,videouri.toString(),Toast.LENGTH_SHORT).show();

        videoView=findViewById(R.id.videoView);
        uri= Uri.parse(videouri);
        videoView.setVideoURI(uri);
        MediaController mediaController=new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.start();


    }
}