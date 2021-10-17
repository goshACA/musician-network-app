package com.musapp.musicapp.adapters.viewholders;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.musapp.musicapp.R;
import com.musapp.musicapp.activities.AppMainActivity;
import com.musapp.musicapp.adapters.inner_post_adapter.BaseUploadsAdapter;
import com.musapp.musicapp.adapters.inner_post_adapter.MusicUploadAdapter;
import com.musapp.musicapp.adapters.viewholders.post_viewholder.BasePostViewHolder;
import com.musapp.musicapp.currentinformation.CurrentUser;
import com.musapp.musicapp.enums.PostUploadType;
import com.musapp.musicapp.model.Post;
import com.musapp.musicapp.pattern.UploadTypeFactory;
import com.musapp.musicapp.pattern.UploadsAdapterFactory;
import com.musapp.musicapp.uploads.BaseUpload;
import com.musapp.musicapp.utils.GlideUtil;
import com.musapp.musicapp.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;


public class FeedViewHolder extends RecyclerView.ViewHolder {

    private ImageView mFeedProfileImage;
    private TextView mUserName;
    private TextView mPostTime;
    private ImageView mPostSetting;
    private TextView mPostText;
    private RecyclerView mRecyclerView;

    private ImageView mCommentIcon;
    private TextView mCommentCount;

    private BaseUploadsAdapter<BaseUpload, BasePostViewHolder> mUploadsAdapter;
    private OnUserProfileImageListener userProfileImageListener;
    private OnPostSettingsClickListener postSettingsClickListener;
    private BaseUploadsAdapter.OnItemSelectedListener mInnerItemClickListener;
    private BaseUploadsAdapter.OnMusicSeekBarListener mMusicSeekBarListener;
    private AppMainActivity.MusicPlayerServiceConnection mPlayerServiceConnection;

    public void setPlayerServiceConnection(AppMainActivity.MusicPlayerServiceConnection playerServiceConnection) {
        mPlayerServiceConnection = playerServiceConnection;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PopupMenu popupMenu = new PopupMenu(mPostSetting.getContext(), mPostSetting);
            popupMenu.inflate(R.menu.menu_pop_up_post_item);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.favorite_pop_up_menu_item:
                            postSettingsClickListener.onFavouriteClickListener(getAdapterPosition());
                            return true;
                    }
                    return false;
                }
            });
            popupMenu.show();
        }
    };

    private View.OnClickListener onImageViewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            userProfileImageListener.onUserImageClickListener(getAdapterPosition());
        }
    };

    public FeedViewHolder(@NonNull View itemView) {
        super(itemView);

        mFeedProfileImage = itemView.findViewById(R.id.image_profile_image_post);
        mFeedProfileImage.setOnClickListener(onImageViewClickListener);
        mUserName = itemView.findViewById(R.id.text_post_item_user_name);
        mPostTime = itemView.findViewById(R.id.text_post_item_published_time);
        mPostSetting = itemView.findViewById(R.id.image_post_item_setting);
        mPostText = itemView.findViewById(R.id.text_post_item_post_text);
        mCommentIcon = itemView.findViewById(R.id.image_comment_icon);
        mCommentCount = itemView.findViewById(R.id.text_post_item_comment_count);

        mRecyclerView = itemView.findViewById(R.id.inner_recycler_view_post_item_container);
        mPostSetting.setOnClickListener(onClickListener);
    }

    public void setFeedProfileImage(String src) {
        GlideUtil.setImageGlide(src, mFeedProfileImage);
    }

    public void setUserName(String mUserName) {
        this.mUserName.setText(mUserName);
    }

    public void setPostTime(long mPostTime) {
        this.mPostTime.setText(StringUtils.millisecondsToDateString(mPostTime));
    }

    public void setPostSetting(ImageView mPostSetting) {
        this.mPostSetting = mPostSetting;
    }

    public void setPostText(String mPostText) {
        this.mPostText.setText(mPostText);
    }

    public void setPostImage(String src) {
        //GlideUtil.setImageGlide(src, this.mPostImage);
    }

    public void setCommentIcon(ImageView mCommentIcon) {
        this.mCommentIcon = mCommentIcon;
    }

    public void setCommentCount(String mCommentCount) {
        this.mCommentCount.setText(mCommentCount);
    }

    public ImageView getPostSettingView() {
        return mPostSetting;
    }

    public void setUserProfileImageListener(OnUserProfileImageListener userProfileImageListener) {
        this.userProfileImageListener = userProfileImageListener;
    }

    public void setInnerItemClickListener(BaseUploadsAdapter.OnItemSelectedListener listener){
        mInnerItemClickListener = listener;
    }

    public void setOnSeekBarListener(BaseUploadsAdapter.OnMusicSeekBarListener listener){
        mMusicSeekBarListener = listener;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    public void initializeRecyclerView(Post post, Context context) {
        List<BaseUpload> uploads = new ArrayList<>();
        if (post.getType() == PostUploadType.NONE) {
            if (mUploadsAdapter != null) {
                mUploadsAdapter.clearData();
                mRecyclerView.setAdapter(mUploadsAdapter);
            }
            return;
        }

        mUploadsAdapter = UploadsAdapterFactory.setAdapterTypeByInputType(post.getType());
        for (String url : post.getAttachment().getFilesUrls()) {
            uploads.add(UploadTypeFactory.setUploadByType(post.getType(), url));
        }
        mUploadsAdapter.setUploads(uploads);
        //
        mUploadsAdapter.setOnItemSelectedListener(mInnerItemClickListener);
        mUploadsAdapter.setOnSeekBarListner(mMusicSeekBarListener);
        mUploadsAdapter.setPlayerServiceConnection(mPlayerServiceConnection);
        //mRecyclerView.setLayoutManager(new GridLayoutManager(context, post.getType() == PostUploadType.IMAGE ? 2 : 1));
        setRecyclerViewLayoutManager(post, context);
        mRecyclerView.setAdapter(mUploadsAdapter);
    }

    private void setRecyclerViewLayoutManager(Post post, Context context) {
        if (post.getType() == PostUploadType.IMAGE) {
            if (post.getAttachment().getFilesUrls().size() > 2) {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, 2));
                return;
            }
        }
        mRecyclerView.setLayoutManager(new GridLayoutManager(context, 1));
    }
    public void setPostSettingsClickListener(OnPostSettingsClickListener postSettingsClickListener) {
        this.postSettingsClickListener = postSettingsClickListener;
    }

    public interface OnUserProfileImageListener {
        void onUserImageClickListener(int position);
    }
    public  interface  OnPostSettingsClickListener{
        void  onFavouriteClickListener(int position);
    }
}
