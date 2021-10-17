package com.musapp.musicapp.model;

import android.util.Log;

import com.musapp.musicapp.utils.StringUtils;

public class Notification {

    private String commentatorImageUrl;
    private String commentatorId;
    private String postId;
    private String notificationBody;
    private String description;
    private long date;

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public Notification(){
        description = "Description";
        date = System.currentTimeMillis();
    }

    public Notification(String commentatorId, String postId, String notificationBody, long date, String commentatorImageUrl){
        this.commentatorId = commentatorId;
        this.postId = postId;
        this.notificationBody = notificationBody;
        this.date = date;
        this.commentatorImageUrl = commentatorImageUrl;
    }

    public Notification(String commentatorId, String postId, String notificationBody, long date, String commentatorImageUrl, String commentatorName){
        this(commentatorId, postId, notificationBody, date, commentatorImageUrl);
        description = commentatorName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public String getCommentatorId() {
        return commentatorId;
    }

    public void setCommentatorId(String commentatorId) {
        this.commentatorId = commentatorId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getNotificationBody() {
        return notificationBody;
    }

    public void setNotificationBody(String notificationBody) {
        this.notificationBody = notificationBody;
    }

    public String getCommentatorImageUrl() {
        return commentatorImageUrl;
    }

    public void setCommentatorImageUrl(String commentatorImageUrl) {
        this.commentatorImageUrl = commentatorImageUrl;
    }

    @Override
    public boolean equals(Object obj) {
      if(obj instanceof Notification){
          boolean res = ((Notification) obj).commentatorId.equals(commentatorId)
                  && ((Notification) obj).commentatorImageUrl.equals(commentatorImageUrl)
                  && String.valueOf(((Notification) obj).date).equals(String.valueOf(date))
                  && ((Notification) obj).description.equals(description)
                  && ((Notification) obj).notificationBody.equals(notificationBody)
                  && ((Notification) obj).postId.equals(postId);
          Log.d("tttt", res + "");
          return res;
      }
          return false;
    }
}
