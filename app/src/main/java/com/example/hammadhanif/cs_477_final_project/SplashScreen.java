package com.example.hammadhanif.cs_477_final_project;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.VideoView;


public class SplashScreen extends AppCompatActivity {

    VideoView videoView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);

        //getSupportActionBar().hide();

        videoView1 = (VideoView) findViewById(R.id.video_View);

        videoView1.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.try3);

        videoView1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(isFinishing())
                    return;

                startActivity(new Intent(SplashScreen.this, MainActivity.class));
                finish();
            }
        });

        videoView1.start();
    }
}