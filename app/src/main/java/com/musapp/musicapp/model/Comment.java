package com.musapp.musicapp.model;

public class Comment {
    private String primaryKey;
    private String userCreatorNickName;
    private String userProfileImageUrl;
    private long creationTime;
    private String commentText;
    private String creatorId;

    public Comment() {
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getUserCreatorNickName() {
        return userCreatorNickName;
    }

    public void setUserCreatorNickName(String userCreatorNickName) {
        this.userCreatorNickName = userCreatorNickName;
    }

    public String getUserProfileImageUrl() {
        return userProfileImageUrl;
    }

    public void setUserProfileImageUrl(String userProfileImageUrl) {
        this.userProfileImageUrl = userProfileImageUrl;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }
        if(obj == null){
            return false;
        }
        Comment comment = (Comment) obj;
        return comment.getPrimaryKey().equals( this.primaryKey);

    }
}
