package com.musapp.musicapp.adapters.inner_post_adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import com.musapp.musicapp.R;
import com.musapp.musicapp.adapters.viewholders.post_viewholder.MusicPostViewHolder;
import com.musapp.musicapp.uploads.MusicUpload;

public class MusicUploadAdapter extends BaseUploadsAdapter<MusicUpload, MusicPostViewHolder> {

    private MusicPostViewHolder.OnMusicItemClickListener mMusicItemClickListener;


    @NonNull
    @Override
    public MusicPostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_post_inner_recycler_view_music_item, viewGroup, false);
        final MusicPostViewHolder holder = new MusicPostViewHolder(view);

        holder.setOnSeekBarListener(mOnSeekBarListener);

        mMusicItemClickListener = new MusicPostViewHolder.OnMusicItemClickListener() {
            @Override
            public void onClick() {
                if(mOnItemSelectedListener != null){
                    mOnItemSelectedListener.onItemSelected(getSongUri(uploads.get(holder.getAdapterPosition()).getUrl()));
                }

            }

            @Override
            public void onSeekBarChange(int position) {
                if(mOnSeekBarListener != null)
                    mOnSeekBarListener.onSeekBarChanged( position);
            }

            @Override
            public void onStartSeekBarHandle(SeekBar seekBar, Button button) {
                if(mOnSeekBarListener != null)
                    mOnSeekBarListener.onStartHandle(seekBar, button);
            }
        };
        holder.mListener = mMusicItemClickListener;
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MusicPostViewHolder musicPostViewHolder, int i) {
        //musicPostViewHolder.setMusic(uploads.get(i).getUrl());
        String artist = uploads.get(i).getUrl().substring(0, uploads.get(i).getUrl().indexOf("$"));
        String title = uploads.get(i).getUrl().substring(uploads.get(i).getUrl().indexOf("$")+1, uploads.get(i).getUrl().indexOf("$", uploads.get(i).getUrl().indexOf("$")+1));
        String duration = uploads.get(i).getUrl().substring(uploads.get(i).getUrl().indexOf("$", uploads.get(i).getUrl().indexOf("$")+1)+1, uploads.get(i).getUrl().indexOf("$", uploads.get(i).getUrl().indexOf("$", uploads.get(i).getUrl().indexOf("$")+1)+1));
        musicPostViewHolder.setMusic(artist, title, duration);

        if (mPlayerServiceConnection.isPlayerPlaying(getSongUri(uploads.get(musicPostViewHolder.getAdapterPosition()).getUrl()))){
            musicPostViewHolder.setPlayState();
            mPlayerServiceConnection.startSeekBarHandle();
        }
    }

    public String getSongUri(String fullName){
        return fullName.substring(fullName.lastIndexOf("$")+1);
    }
}
