package com.musapp.musicapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.musapp.musicapp.utils.GlideUtil;

import java.util.ArrayList;
import java.util.List;

public class FullScreenPagerAdapter extends PagerAdapter {

    private List<String> urls;
    private Context mContext;

    public FullScreenPagerAdapter() {
        urls = new ArrayList<>();
    }

    public FullScreenPagerAdapter(Context contex, List<String> urls) {
        this.urls = urls;
        mContext = contex;
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        GlideUtil.setFullScreenImage(urls.get(position), imageView);
        container.addView(imageView);

        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
