package com.musapp.musicapp.adapters.viewholders.post_viewholder;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageMetadata;
import com.musapp.musicapp.R;
import com.musapp.musicapp.adapters.inner_post_adapter.BaseUploadsAdapter;
import com.musapp.musicapp.firebase_repository.FirebaseRepository;
import com.musapp.musicapp.fragments.main_fragments.AddPostFragment;

public class MusicPostViewHolder extends BasePostViewHolder {

    private Button mPlayButton;
    private TextView mSongTitle;
    private TextView mArtistName;
    private TextView mSongDuration;
    private SeekBar mDurationSeekBar;
    private boolean flag = false;
    public OnMusicItemClickListener mListener;
    private BaseUploadsAdapter.OnMusicSeekBarListener mOnSeekBarListener;

    public void setOnSeekBarListener(BaseUploadsAdapter.OnMusicSeekBarListener onSeekBarListener) {
        mOnSeekBarListener = onSeekBarListener;
    }

    public MusicPostViewHolder(@NonNull View itemView) {
        super(itemView);
        mPlayButton = itemView.findViewById(R.id.action_music_view_play_pause_button);
        mSongTitle = itemView.findViewById(R.id.text_music_title);
        mArtistName = itemView.findViewById(R.id.text_music_author);
        mSongDuration = itemView.findViewById(R.id.text_music_duration);
        mDurationSeekBar = itemView.findViewById(R.id.seek_bar_music_view_post_inner_recycler_view);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag){
                    setButtonBackground(R.drawable.ic_play_arrow_white_24dp);
                    flag = false;
                }else{
                    setButtonBackground(R.drawable.ic_pause_black_24dp);
                    flag = true;
                }
                if (mListener != null){
                    mListener.onClick();
                    mListener.onStartSeekBarHandle( mDurationSeekBar, mPlayButton);

                }

            }
        });

        mDurationSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    if (mListener != null){
                        mListener.onSeekBarChange(progress);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void setPlayButton(Button playButton) {
        mPlayButton = playButton;
    }

    public void setSongTitle(String songTitle) {
        mSongTitle.setText(songTitle);
    }

    public void setArtistName(String artistName) {
        mArtistName.setText(artistName);
    }

    public void setSongDuration(int songDuration) {
        mSongDuration.setText(songDuration);
    }

    public SeekBar getSeekBar(){
        return mDurationSeekBar;
    }

    public void setMusic(String artist, String title, String duration){
        mSongTitle.setText(title);
        mArtistName.setText(artist);
        mSongDuration.setText(castMillisecondToMinuteAndSecond(duration));

    }
    public String castMillisecondToMinuteAndSecond(String millisecondsString){
        if(millisecondsString != null && !millisecondsString.equals("")){
            int milliseconds = Integer.parseInt(millisecondsString);
            mDurationSeekBar.setMax(milliseconds);

            long minutes = (milliseconds) / 1000 / 60;
            long sec = ( milliseconds / 1000 ) % 60;
            if(sec>=10){
                return (String.valueOf(minutes) + ":" + String.valueOf(sec) );
            }else{
                return (String.valueOf(minutes) + ":0" + String.valueOf(sec) );
            }
        }
        return "0:00";
    }

    public void setPlayState(){
        setButtonBackground(R.drawable.ic_pause_black_24dp);
        flag = true;
        mOnSeekBarListener.onStartHandle(mDurationSeekBar, mPlayButton);
    }

    public void setButtonBackground(int resource){
        mPlayButton.setBackgroundResource(resource);
    }

    public interface OnMusicItemClickListener{
        void onClick();
        void onSeekBarChange(int position);
        void onStartSeekBarHandle(SeekBar seekBar, Button button);
    }

}
