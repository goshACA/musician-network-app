package com.musapp.musicapp.model;

import android.net.Uri;

public class Conversation {
    private String userProfile;
    private String userName;
    private String lastMessage;
    private String lastDate;
    private String chatId;

    public Conversation(){}
    public Conversation(String userName, String lastMessage, String lastDate){
        this.userName = userName;
        this.lastMessage = lastMessage;
        this.lastDate = lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public String getUserName() {
        return userName;
    }

    public String getLastDate() {
        return lastDate;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    @Override
    public boolean equals(Object obj) {
       if(obj instanceof Conversation)
           return ((Conversation) obj).chatId.equals(chatId);
       return false;
    }
}
