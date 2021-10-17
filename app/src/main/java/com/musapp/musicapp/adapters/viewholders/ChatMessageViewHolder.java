package com.musapp.musicapp.adapters.viewholders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.musapp.musicapp.R;
import com.musapp.musicapp.utils.ContextUtils;
import com.musapp.musicapp.utils.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatMessageViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.image_message_chat_image_conversation_profile)
    ImageView userProfile;
    @BindView(R.id.text_message_chat_item_last_message)
    TextView messageText;
    @BindView(R.id.text_message_chat_item_last_date)
    TextView messageDate;
    @BindView(R.id.linear_layout_message_box)
    LinearLayout chatBox;
    @BindView(R.id.linear_layout_linear)
    LinearLayout bubbleLayout;
    
    public ChatMessageViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

    }
    public void setImageVisibility(int val){
        userProfile.setVisibility(val);
    }
    public void setGravityOfChatBox(int gravity){
        chatBox.setGravity(gravity);
    }
    public void setBubbleBackground(int id){
        bubbleLayout.setBackground(ContextUtils.getRsourceDrawable(id));
    }
    public ImageView getUserProfile(){
        return userProfile;
    }
    public void setMessageText(String message){
        messageText.setText(message);
    }
    public void setMessageDate(long date){
        messageDate.setText(StringUtils.millisecondsToDateString(date));
    }
}
