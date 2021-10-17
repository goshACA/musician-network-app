package com.musapp.musicapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.musapp.musicapp.R;
import com.musapp.musicapp.adapters.viewholders.MessageViewHolder;
import com.musapp.musicapp.adapters.viewholders.NotificationViewHolder;
import com.musapp.musicapp.model.Conversation;
import com.musapp.musicapp.utils.GlideUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

public class MessageDashboardRecyclerAdapter extends RecyclerView.Adapter<MessageViewHolder> {
   private List<Conversation> mConversations = new ArrayList<>();

   private Comparator<Conversation> mComparator = new Comparator<Conversation>() {
       @Override
       public int compare(Conversation o1, Conversation o2) {
           return -o1.getLastDate().compareTo(o2.getLastDate());
       }
   };

    public void setConversations(List<Conversation> conversations){
        mConversations.addAll(conversations);
        Collections.sort(mConversations, mComparator);
        notifyDataSetChanged();
    }



    public void addConversation(Conversation c){
        mConversations.add(c);
        Collections.sort(mConversations, mComparator);
        notifyDataSetChanged();
    }

    public int conversationIndex(Conversation conversation){
        return mConversations.indexOf(conversation);
    }

    public void changeItem(Conversation conversation,int index){
        mConversations.set(index, conversation);
        Collections.sort(mConversations, mComparator);
        notifyItemChanged(index);
    }
    private OnDashboardItemClick mOnDashboardItemClick;

    private MessageViewHolder.OnItemClick mOnItemClick = new MessageViewHolder.OnItemClick() {
        @Override
        public void onItemClick(int position) {
            mOnDashboardItemClick.onDashboardItemClick(mConversations.get(position));
        }
    };


    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_messages_recycler_view_item, viewGroup, false);
        return new MessageViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder messageViewHolder, int i) {
        Conversation conversation = mConversations.get(i);
        messageViewHolder.setLastDate(conversation.getLastDate());
        messageViewHolder.setLastMessage(conversation.getLastMessage());
        messageViewHolder.setUserName(conversation.getUserName());
        messageViewHolder.setOnItemClick(mOnItemClick);
        GlideUtil.setImageGlide(conversation.getUserProfile(), messageViewHolder.getUserProfile());
    }

    @Override
    public int getItemCount() {
        return mConversations.size();
    }

    public void setOnDashboardItemClick(OnDashboardItemClick onDashboardItemClick) {
        mOnDashboardItemClick = onDashboardItemClick;
    }

    public interface OnDashboardItemClick{
        void onDashboardItemClick(Conversation conversation);
    }
}
