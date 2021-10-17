package com.musapp.musicapp.adapters.inner_post_adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.musapp.musicapp.activities.AppMainActivity;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseUploadsAdapter<T, Y extends RecyclerView.ViewHolder > extends RecyclerView.Adapter<Y> {

    protected List<T> uploads;
    protected OnItemSelectedListener mOnItemSelectedListener;
    OnMusicSeekBarListener mOnSeekBarListener;
    //I know it's bad solution, we have deadline...
    AppMainActivity.MusicPlayerServiceConnection mPlayerServiceConnection;

    @Override
    public int getItemCount() {
        return uploads == null ? 0 : uploads.size();
    }

    public void setUploads(@NonNull List<T> data) {
        if(uploads == null)
        uploads = new ArrayList<>(data);

        uploads.clear();
        uploads.addAll(data);
      notifyDataSetChanged();
    }

    public void addItem(T newUpload){
        if(uploads == null)
            uploads = new ArrayList<>();
        uploads.add(newUpload);
        notifyItemChanged(uploads.size() - 1);
    }

    public  void clearData(){
        if(uploads!=null)
            uploads.clear();
    }

    @NonNull
    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        mOnItemSelectedListener = onItemSelectedListener;
    }

    public void setOnSeekBarListner(OnMusicSeekBarListener listener){
        mOnSeekBarListener = listener;
    }

    public void setPlayerServiceConnection(AppMainActivity.MusicPlayerServiceConnection playerServiceConnection) {
        mPlayerServiceConnection = playerServiceConnection;
    }

    public interface OnItemSelectedListener {
        void onItemSelected(String uri);
    }

    public interface OnMusicSeekBarListener {
        void onSeekBarChanged(int position);
        void onStartHandle(View view, View view2);
    }

}
