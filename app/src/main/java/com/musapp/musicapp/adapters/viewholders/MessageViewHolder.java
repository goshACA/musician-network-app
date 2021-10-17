package com.musapp.musicapp.adapters.viewholders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.musapp.musicapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.image_message_image_chat_profile)
    ImageView userProfile;
    @BindView(R.id.text_message_item_user_name)
    TextView userName;
    @BindView(R.id.text_message_item_last_message)
    TextView lastMessage;
    @BindView(R.id.text_message_item_last_date)
    TextView lastDate;
    private OnItemClick mOnItemClick;
    public MessageViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClick.onItemClick(getAdapterPosition());
            }
        });
    }

    public ImageView getUserProfile(){
        return userProfile;
    }
    public void setUserName(String name){
        userName.setText(name);
    }
    public void setLastMessage(String message){
        lastMessage.setText(message);
    }
    public void setLastDate(String date){
        lastDate.setText(date);
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        mOnItemClick = onItemClick;
    }

    public interface OnItemClick{
        void onItemClick(int position);
    }
}
