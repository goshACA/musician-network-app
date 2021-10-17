package com.musapp.musicapp.model;

import java.util.ArrayList;
import java.util.List;

public class Chat {
    private String primaryKey;
    private String firstUserId;
    private String secondUserId;
    private String firstUserLastSeen;
    private String secondUseLastSeen;
    private List<Message>  messages;
    public Chat(){
        messages = new ArrayList<>();
    }

    public void addMessage(Message message){
        messages.add(message);
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getFirstUserId() {
        return firstUserId;
    }

    public void setFirstUserId(String firstUserId) {
        this.firstUserId = firstUserId;
    }

    public String getSecondUserId() {
        return secondUserId;
    }

    public void setSecondUserId(String secondUserId) {
        this.secondUserId = secondUserId;
    }

    public List<Message> getMessages() {
        return messages;
    }

    @Override
    public boolean equals( Object obj) {
       if(obj instanceof  Chat)
           return ((Chat) obj).firstUserId.equals(firstUserId) && ((Chat) obj).secondUserId.equals(secondUserId)
           || ((Chat) obj).firstUserId.equals(secondUserId) && ((Chat) obj).secondUserId.equals(firstUserId);
       return false;
    }


    public String getFirstUserLastSeen() {
        return firstUserLastSeen;
    }

    public void setFirstUserLastSeen(String firstUserLastSeen) {
        this.firstUserLastSeen = firstUserLastSeen;
    }

    public String getSecondUseLastSeen() {
        return secondUseLastSeen;
    }

    public void setSecondUseLastSeen(String secondUseLastSeen) {
        this.secondUseLastSeen = secondUseLastSeen;
    }
}
