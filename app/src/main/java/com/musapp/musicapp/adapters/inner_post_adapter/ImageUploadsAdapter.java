package com.musapp.musicapp.adapters.inner_post_adapter;


import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.musapp.musicapp.R;
import com.musapp.musicapp.adapters.viewholders.post_viewholder.ImagePostViewHolder;
import com.musapp.musicapp.uploads.ImageUpload;

public class ImageUploadsAdapter extends BaseUploadsAdapter<ImageUpload, ImagePostViewHolder> {


    @NonNull
    @Override
    public ImagePostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_post_inner_recycler_view_image_item, viewGroup, false);
        final ImagePostViewHolder holder = new ImagePostViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItemSelectedListener != null){
                    mOnItemSelectedListener.onItemSelected(uploads.get(holder.getAdapterPosition()).getUrl());
                }
            }
        });


        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImagePostViewHolder imagePostViewHolder, int i) {
        imagePostViewHolder.setImage(uploads.get(i).getUrl());
    }
}
