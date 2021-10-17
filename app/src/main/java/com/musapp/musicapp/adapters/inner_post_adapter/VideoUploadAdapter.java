package com.musapp.musicapp.adapters.inner_post_adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.musapp.musicapp.R;
import com.musapp.musicapp.adapters.viewholders.post_viewholder.VideoPostViewHolder;
import com.musapp.musicapp.uploads.BaseUpload;
import com.musapp.musicapp.uploads.VideoUpload;

public class VideoUploadAdapter extends BaseUploadsAdapter<VideoUpload, VideoPostViewHolder> {

    @NonNull
    @Override
    public VideoPostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_post_inner_recycler_view_video_item, viewGroup, false);
        return new VideoPostViewHolder(view, viewGroup.getContext(), uploads.get(i).getUrl());
    }

    @Override
    public void onBindViewHolder(@NonNull VideoPostViewHolder videoPostViewHolder, int i) {
        //super.onBindViewHolder(videoPostViewHolder, i);
      //  videoPostViewHolder.setVideo(uploads.get(i).getUrl());
    }
}
