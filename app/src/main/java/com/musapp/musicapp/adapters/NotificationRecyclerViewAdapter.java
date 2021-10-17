package com.musapp.musicapp.adapters;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.musapp.musicapp.R;
import com.musapp.musicapp.adapters.viewholders.NotificationViewHolder;
import com.musapp.musicapp.firebase.DBAccess;
import com.musapp.musicapp.firebase_repository.FirebaseRepository;
import com.musapp.musicapp.model.Notification;
import com.musapp.musicapp.utils.GlideUtil;

import java.util.ArrayList;
import java.util.List;

public class NotificationRecyclerViewAdapter extends RecyclerView.Adapter<NotificationViewHolder> {
    private List<Notification> data = new ArrayList<>();
    private OnProfileImageClickListener mOnProfileImageClickListener;
    private NotificationViewHolder.ImageOnClickListener mImageOnClickListener = new NotificationViewHolder.ImageOnClickListener() {
        @Override
        public void onImagePerformClick(int position) {
            mOnProfileImageClickListener.onProfileImageClick(data.get(position));
        }
    };

    private OnItemClickListener mOnItemClickListener;
   private NotificationViewHolder.ItemClickPerformance mItemClickPerformance = new NotificationViewHolder.ItemClickPerformance() {
       @Override
       public void onItemClickPerformance(int position) {
           mOnItemClickListener.onItemClick(data.get(position).getPostId());
       }
   };

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_user_notification_list, viewGroup, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder notificationViewHolder, int i) {
        Notification notification = data.get(i);
        notificationViewHolder.setDescriptionText(notification.getDescription());
        notificationViewHolder.setBodyText(notification.getNotificationBody());
        notificationViewHolder.setDate(notification.getDate());
        GlideUtil.setImageGlide(notification.getCommentatorImageUrl(), notificationViewHolder.getUserImage());
        notificationViewHolder.setImageOnClickListener(mImageOnClickListener);
        notificationViewHolder.setItemClickPerformance(mItemClickPerformance);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public void setData(List<Notification> notificationArrayList){

            data.clear();
            data.addAll(notificationArrayList);
        notifyDataSetChanged();
    }

    public void addItem(Notification notification, int pos){
        data.add(pos, notification);
        notifyItemInserted(pos);
    }

    public void setOnProfileImageClickListener(OnProfileImageClickListener onProfileImageClickListener) {
        mOnProfileImageClickListener = onProfileImageClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnProfileImageClickListener{
        void onProfileImageClick(Notification notification);
    }

    public interface OnItemClickListener{
        void onItemClick(String postId);
    }

}
