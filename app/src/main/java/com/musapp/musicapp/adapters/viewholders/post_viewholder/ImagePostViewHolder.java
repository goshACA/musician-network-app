package com.musapp.musicapp.adapters.viewholders.post_viewholder;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.musapp.musicapp.R;
import com.musapp.musicapp.utils.GlideUtil;

public class ImagePostViewHolder extends BasePostViewHolder {

    private ImageView imageView;
    public ImagePostViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.image_post_item_upload_image);

    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImage(String url) {
        GlideUtil.setImageGlideSquare(url, imageView);
    }

}
