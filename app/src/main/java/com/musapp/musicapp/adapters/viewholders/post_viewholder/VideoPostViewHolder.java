package com.musapp.musicapp.adapters.viewholders.post_viewholder;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.musapp.musicapp.R;

import bg.devlabs.fullscreenvideoview.FullscreenVideoView;
import bg.devlabs.fullscreenvideoview.PlaybackSpeedOptions;
import bg.devlabs.fullscreenvideoview.orientation.PortraitOrientation;

public class VideoPostViewHolder extends BasePostViewHolder {
    //private VideoView videoView;
    private FullscreenVideoView videoView;
    private MediaController mMediaController;
    private Context mContext;
    public VideoPostViewHolder(@NonNull View itemView, Context context, String url) {
        super(itemView);
        mContext = context;
        videoView = itemView.findViewById(R.id.video_post_item_upload_video);
        setVideo(url);
//        videoView.setVideoURI(Uri.parse(url));
//        mMediaController = new MediaController(context);
//        videoView.setMediaController(mMediaController);
//        mMediaController.setAnchorView(videoView);
//        videoView.requestFocus();

    }

    public FullscreenVideoView getVideoView() {
        return videoView;
    }

    public void setVideo(String url) {
       //TODO set video by url
//        videoView.setVideoURI(Uri.parse(url));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            videoView.videoUrl(url)
                    .enableAutoStart()
                    .rewindSeconds(10)
                    .fastForwardSeconds(10)
                    .addSeekBackwardButton()
                    .addSeekForwardButton()
                    .addPlaybackSpeedButton()
                    .portraitOrientation(PortraitOrientation.DEFAULT);
        }else{
            videoView.videoUrl(url)
                    .enableAutoStart()
                    .rewindSeconds(10)
                    .fastForwardSeconds(10)
                    .addSeekBackwardButton()
                    .addSeekForwardButton()
                    .portraitOrientation(PortraitOrientation.DEFAULT);
        }
    }
}
