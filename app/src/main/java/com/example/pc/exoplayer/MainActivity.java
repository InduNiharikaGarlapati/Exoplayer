package com.example.pc.exoplayer;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class MainActivity extends AppCompatActivity {
    SimpleExoPlayerView simpleExoPlayerView;
    SimpleExoPlayer simpleExoPlayer;
    long currentposition;
    boolean play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startplayer();
    }
    public void startplayer()
    {
        simpleExoPlayerView=findViewById(R.id.player);
        RenderersFactory renderersFactory=new DefaultRenderersFactory(this);
        TrackSelector trackSelector=new DefaultTrackSelector();
        LoadControl loadControl=new DefaultLoadControl();
        Uri uri=Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
        String Useragent= Util.getUserAgent(this,"exoplayerdemo");
        MediaSource mediaSource=new ExtractorMediaSource(uri,new DefaultDataSourceFactory(this,Useragent),new DefaultExtractorsFactory(),null,null);
        simpleExoPlayer=ExoPlayerFactory.newSimpleInstance(renderersFactory,trackSelector,loadControl);
        simpleExoPlayer.prepare(mediaSource);
        simpleExoPlayer.seekTo(currentposition);
        simpleExoPlayer.setPlayWhenReady(play);
        simpleExoPlayerView.setPlayer(simpleExoPlayer);
    }

    public void stopplayer(){
        if(simpleExoPlayer!=null){
            currentposition=simpleExoPlayer.getCurrentPosition();
            simpleExoPlayer.release();
            simpleExoPlayer.stop();
            simpleExoPlayer=null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(Util.SDK_INT>23)
        {
            startplayer();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(Util.SDK_INT>23)
        {
            stopplayer();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(Util.SDK_INT<=23)
        {
            stopplayer();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopplayer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Util.SDK_INT<=23||simpleExoPlayer==null)
        {
            startplayer();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState!=null){
            currentposition=savedInstanceState.getLong("current");
            play=savedInstanceState.getBoolean("play");
        }
    }
}
