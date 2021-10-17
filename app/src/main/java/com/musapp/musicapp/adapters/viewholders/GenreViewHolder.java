package com.musapp.musicapp.adapters.viewholders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.musapp.musicapp.R;
import com.musapp.musicapp.utils.GlideUtil;


public class GenreViewHolder extends RecyclerView.ViewHolder {

    private ImageView img;
    private TextView mName;

    public GenreViewHolder(@NonNull View itemView) {
        super(itemView);

        img = itemView.findViewById(R.id.image_genre_item_grid_view_genre_image);
        mName = itemView.findViewById(R.id.text_genre_grid_fragment_genre_name);
    }

    public void setImage(int source) {
        GlideUtil.setResource(source, img);
        //img.setImageResource(source);
    }

    public void setName(String Name) {
        this.mName.setText(Name);
    }

    public void setNameBackground(int color) {
        mName.setBackgroundColor(color);
    }
}

