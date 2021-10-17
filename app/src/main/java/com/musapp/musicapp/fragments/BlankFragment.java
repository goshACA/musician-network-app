package com.musapp.musicapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import com.musapp.musicapp.R;
import com.musapp.musicapp.firebase.DBAccess;

public class BlankFragment extends Fragment {

    VideoView mVideoView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view =  inflater.inflate(R.layout.fragment_blank, container, false);
       mVideoView = view.findViewById(R.id.video_example);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        load();

    }

    void load(){

        String url = "https://firebasestorage.googleapis.com/v0/b/musiciannetwork.appspot.com/o/video%2F1552952356455.mp4?alt=media&token=a7adb36c-8193-4712-ad2c-f037726696f3";
        mVideoView.setVideoURI(Uri.parse(url));
        MediaController mMediaController = new MediaController(getContext());
        mVideoView.setMediaController(mMediaController);
        mMediaController.setAnchorView(mVideoView);
    }
}
