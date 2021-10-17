package com.musapp.musicapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.musapp.musicapp.R;
import com.musapp.musicapp.adapters.viewholders.CommentViewHolder;
import com.musapp.musicapp.model.Comment;
import com.musapp.musicapp.model.User;

import java.util.ArrayList;
import java.util.List;

public class CommentRecyclerViewAdapter extends RecyclerView.Adapter<CommentViewHolder> {

    private List<Comment> mData;
    private OnCommentItemSelectedListener mOnCommentItemSelectedListener;

    private PerformImageClick mPerformImageClick;
    CommentViewHolder.ImageOnClickListener mImageOnClickListener = new CommentViewHolder.ImageOnClickListener() {
        @Override
        public void onImagePerformClick(int position) {
            mPerformImageClick.onPerformClick(mData.get(position).getCreatorId());
        }
    };


    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_post_inner_coments_recycler_view_comments, viewGroup, false);
        final CommentViewHolder viewHolder = new CommentViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnCommentItemSelectedListener != null) {
                    mOnCommentItemSelectedListener.onItemSelected(mData.get(viewHolder.getAdapterPosition()).getCreatorId());
                    //TODO implement this interface in openedPostFragment
                }
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder commentViewHolder, int i) {
        Comment comment = mData.get(i);
        commentViewHolder.setProfileImage(comment.getUserProfileImageUrl());
        commentViewHolder.setCommentText(comment.getCommentText());
        commentViewHolder.setPublishedTime(comment.getCreationTime());
        commentViewHolder.setUserName(comment.getUserCreatorNickName());
        commentViewHolder.setImageOnClickListener(mImageOnClickListener);
    }

    @Override
    public int getItemCount() {
        return  mData == null ? 0 : mData.size();

    }

    public void setData(List<Comment> mData) {
        if(this.mData == null){
            this.mData = new ArrayList<>();
        }else{
            this.mData.clear();
        }
        if(mData != null){
            this.mData.addAll(mData);
        }
        notifyDataSetChanged();


    }

    public void addComment(Comment comment){
        if(mData == null){
            mData = new ArrayList<>();
        }
        if(!mData.contains(comment)){
            mData.add(comment);
        }
        notifyDataSetChanged();
    }

    public void setOnCommentItemSelectedListener(OnCommentItemSelectedListener onCommentItemSelectedListener) {
        mOnCommentItemSelectedListener = onCommentItemSelectedListener;
    }

    public void setPerformImageClick(PerformImageClick performImageClick) {
        mPerformImageClick = performImageClick;
    }

    public interface OnCommentItemSelectedListener{
        void onItemSelected(String id);
    }

    public interface PerformImageClick{
        void onPerformClick(String userId);
    }
}
