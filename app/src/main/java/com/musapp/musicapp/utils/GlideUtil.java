package com.musapp.musicapp.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.musapp.musicapp.R;

import java.net.URL;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import java.net.URL;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public final class GlideUtil {

    private static Context context;

    public static void setContext(Context ctx){
        context = ctx;
    }

    public static void setImageGlide(String src, ImageView view){

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.image_placeholder);

        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(src)
                .apply(RequestOptions.circleCropTransform())
                .into(view);
    }

    public static  void setImageGlideSquare(String src, ImageView view){
        Glide.with(context).load(src)
                .into(view);
    }
    public static void setFullScreenImage(String src, ImageView view){
        RequestOptions requestOptions = new RequestOptions().fitCenter();//or .centerCrop()
        Glide.with(context).load(src).apply(requestOptions).into(view);
    }

    public static void setResource(int source, ImageView view){
        RequestOptions requestOptions = new RequestOptions().fitCenter();

        Glide.with(context).load(source).apply(requestOptions).into(view);
    }
}
