package com.musapp.musicapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.messaging.MessagingAnalytics;
import com.musapp.musicapp.R;
import com.musapp.musicapp.adapters.viewholders.ChatMessageViewHolder;
import com.musapp.musicapp.currentinformation.CurrentUser;
import com.musapp.musicapp.model.Message;
import com.musapp.musicapp.utils.ContextUtils;
import com.musapp.musicapp.utils.GlideUtil;

import java.util.ArrayList;
import java.util.List;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageViewHolder> {
    private List<Message> mMessages = new ArrayList<>();

    public void setData(List<Message> messages){
        mMessages = messages;
        notifyDataSetChanged();
    }

    public void addData(List<Message> messages){
        mMessages.addAll(messages);
        notifyDataSetChanged();
    }

    public void addMessage(Message message, int index){
        mMessages.add(index, message);
        notifyItemInserted(index);
    }

    public void addMessage(Message message){
        mMessages.add(message);
        notifyItemInserted(mMessages.size() - 1);
    }

    @NonNull
    @Override
    public ChatMessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_conversation_message, viewGroup, false);
        return  new ChatMessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatMessageViewHolder viewHolder, int i) {
        Message message = mMessages.get(i);
        viewHolder.setMessageDate(message.getCreationDate());
        viewHolder.setMessageText(message.getMessageText());
        if (message.getCreatorId().equals(CurrentUser.getCurrentUser().getPrimaryKey())){
            viewHolder.setImageVisibility(View.GONE);
            viewHolder.setGravityOfChatBox(Gravity.END);
            viewHolder.setBubbleBackground(R.drawable.chat_bubble003_right);
        }else{
            viewHolder.setImageVisibility(View.VISIBLE);
            viewHolder.setGravityOfChatBox(Gravity.START);
        }
        GlideUtil.setImageGlide(message.getCreatorPic(), viewHolder.getUserProfile());
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }
}
