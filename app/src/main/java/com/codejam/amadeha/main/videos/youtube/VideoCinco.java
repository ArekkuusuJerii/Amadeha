package com.codejam.amadeha.main.videos.youtube;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.codejam.amadeha.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.ArrayList;
import java.util.List;

public class VideoCinco extends YouTubeBaseActivity {

    private static final String TAG = "MainActivity";

    YouTubePlayerView mYouTubePlayerView;
    Button buttonPlay;
    YouTubePlayer.OnInitializedListener mOnInicializedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Ocupa toda la pantalla... escondiento la barra de notificaciones
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.video_cinco);

        Toolbar toolbar = findViewById(R.id.toolbar);

        // initCollapsingToolbar();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Log.d(TAG, "onCreate: Starting.");
        buttonPlay = findViewById(R.id.buttonPlay5);
        mYouTubePlayerView = findViewById(R.id.youtubePlayer5);

        mOnInicializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

                Log.d(TAG, "onClick: Intializing YouTube Player.");
                mYouTubePlayerView.initialize(YouTubeConfig.getApi_Key(), mOnInicializedListener);
                Log.d(TAG, "onClick: Done initializing.");

                Log.d(TAG, "onClick: Done initializing.");
                List<String> videoList = new ArrayList<>();
                videoList.add("Qjn5dkROJXw");
                videoList.add("Qjn5dkROJXw");
                youTubePlayer.loadVideos(videoList);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d(TAG, "onClick: Failed to initializing.");
            }
        };

        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Intializing YouTube Player.");
                mYouTubePlayerView.initialize(YouTubeConfig.getApi_Key(), mOnInicializedListener);
                Log.d(TAG, "onClick: Done initializing.");
            }
        });
    }
}
