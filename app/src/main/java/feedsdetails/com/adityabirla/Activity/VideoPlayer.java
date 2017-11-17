package feedsdetails.com.adityabirla.Activity;

import android.app.ProgressDialog;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.MediaController;
import android.widget.VideoView;
import feedsdetails.com.adityabirla.R;

public class VideoPlayer extends AppCompatActivity  {
    String path;
    private VideoView videoView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            path = extras.getString("PATH");
            // and get whatever type user account id is
        }
        videoView = (VideoView) findViewById(R.id.videoView);

        Uri video = Uri.parse(path);
        videoView.setVideoURI(video);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(video);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                videoView.start();
            }
        });
    }



}
