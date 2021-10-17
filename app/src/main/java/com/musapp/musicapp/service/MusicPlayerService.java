package com.musapp.musicapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Button;
import android.widget.SeekBar;

import com.musapp.musicapp.R;


public class MusicPlayerService extends IntentService {

    private MediaPlayer mMediaPlayer;
    private LocalBinder mLocalBinder;
    private String currentSongUrl;
    private SeekBar mSeekBar;
    private Button mButton;
    private Handler handler;

    private MediaPlayer.OnPreparedListener mOnPreparedListener =
            new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mMediaPlayer.start();
                    startSeekBarHandle();

                }
            };

    private MediaPlayer.OnCompletionListener mOnCompletionListener =
            new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mLocalBinder.repeatSong();
                }
            };
    private MediaPlayer.OnErrorListener mOnErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            return false;
        }
    };

    private MediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            Log.d("buffering", "onBufferingUpdate: percent: " + percent);
        }
    };

    public void startSeekBarHandle(){
        if (mSeekBar != null){
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mLocalBinder.isPlaying()) {
                        int currentPosition = mLocalBinder.getCurrentDuration();
                        mSeekBar.setProgress(currentPosition);
                        if (mSeekBar.getMax() == currentPosition){
                            mSeekBar.setProgress(0);
                            mLocalBinder.isPrepared = true;
                        }
                        handler.postDelayed(this, 1000);
                    }else if (mLocalBinder.isPrepared ){
                        handler.postDelayed(this, 10);
                    }

                }
            }, 10);
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        initMediaPlayer();

    }


    public MusicPlayerService(String name) {
        super(name);
    }

    public MusicPlayerService() {
        super("MusicPlayerService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    public void initMediaPlayer(){
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnPreparedListener(mOnPreparedListener);
        mMediaPlayer.setOnCompletionListener(mOnCompletionListener);
        mMediaPlayer.setOnErrorListener(mOnErrorListener);
        mMediaPlayer.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK);

    }
    private void release(){
        if(mMediaPlayer != null){
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void playSong(String url){
        try{
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.prepareAsync();
            currentSongUrl = url;
            if(mButton != null) {
                mButton.setBackgroundResource(R.drawable.ic_pause_black_24dp);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private boolean isPlayerPlaying(){
        return mMediaPlayer.isPlaying();
    }
    private int getDuration(){
        return mMediaPlayer.isPlaying() ? mMediaPlayer.getCurrentPosition() : 0;
    }
    private void resume(){
        if(mMediaPlayer != null && !mMediaPlayer.isPlaying()){
            mMediaPlayer.start();
            if(mButton != null) {
                mButton.setBackgroundResource(R.drawable.ic_pause_black_24dp);
            }

        }
    }

    private void pause(){
        if(mMediaPlayer != null && mMediaPlayer.isPlaying()){
            mMediaPlayer.pause();
//            if(mButton != null) {
//                mButton.setBackgroundResource(R.drawable.ic_play_arrow_white_24dp);
//            }
        }
    }

    private void stop(){
        if(mMediaPlayer != null && mMediaPlayer.isPlaying()){
            mMediaPlayer.stop();

        }
    }
    private void seekTo(int position){
        if(mMediaPlayer != null ){
            mMediaPlayer.seekTo(position);
        }
    }
    private int getMediaPlayerDuration(){
        return  mMediaPlayer.getDuration();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        release();
    }

    @Override
    public IBinder onBind(Intent intent) {
        if(mLocalBinder == null){
            mLocalBinder = new LocalBinder();
        }
        return mLocalBinder;
    }

    public class LocalBinder extends Binder{
        public boolean isPrepared = false;

        public void play(String path){
            if(path.equals(currentSongUrl)){
                if(isPlaying()){
                    pause();
                }else{
                    MusicPlayerService.this.startSeekBarHandle();
                    resume();
                }
                isPrepared = false;
            }else{
                playSong(path);
                isPrepared = true;
            }

        }
        public void pause(){
            MusicPlayerService.this.pause();

        }
        public void resume(){
            MusicPlayerService.this.resume();
        }
        public void stop(){
            MusicPlayerService.this.stop();
        }
        public void seekTo(int position){
            MusicPlayerService.this.seekTo(position);
        }
        public void repeatSong(){
            playSong(currentSongUrl);
        }

        public boolean isPlaying(){
            return isPlayerPlaying();
        }
        public int getCurrentDuration(){
            return getDuration();
        }
        public int getPlayerDuration(){
            return getMediaPlayerDuration();
        }
        public String getCurrentUrl(){
            return currentSongUrl;
        }
        public void setSeekBar(SeekBar bar){
            mSeekBar = bar;
        }
        public void setPlayPauseButton(Button btn){
            if(mButton != null && btn != mButton){
                mButton.setBackgroundResource(R.drawable.ic_play_arrow_white_24dp);
            }
            mButton = btn;
        }
        public void startSeekBarHandle(){
            MusicPlayerService.this.startSeekBarHandle();
        }

    }
}
