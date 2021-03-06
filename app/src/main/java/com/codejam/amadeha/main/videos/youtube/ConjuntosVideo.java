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

public class ConjuntosVideo extends YouTubeBaseActivity {

    private static final String TAG = "MainActivity";

    private YouTubePlayerView mYouTubePlayerView;
    private YouTubePlayer.OnInitializedListener mOnInicializedListener;
    private boolean isPlaying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_uno);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Log.d(TAG, "onCreate: Starting.");
        mYouTubePlayerView = findViewById(R.id.youtubePlayer);
        mOnInicializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

                Log.d(TAG, "onClick: Intializing YouTube Player.");
                mYouTubePlayerView.initialize(YouTubeConfig.getApi_Key(), mOnInicializedListener);
                Log.d(TAG, "onClick: Done initializing.");

                Log.d(TAG, "onClick: Done initializing.");
                List<String> videoList = new ArrayList<>();
                videoList.add("60_fj9YtDvY");
                videoList.add("RzjR2b8aK1k");
                videoList.add("3o4nF1a_0tY");
                videoList.add("Pm_PYhnJe1w");
                videoList.add("0Muc0Vl_PCE");
                youTubePlayer.loadVideos(videoList);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d(TAG, "onClick: Failed to initializing.");
            }
        };

        if(!isPlaying) {
            mYouTubePlayerView.initialize(YouTubeConfig.getApi_Key(), mOnInicializedListener);
            isPlaying = true;
        }
    }
}
