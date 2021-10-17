package com.musapp.musicapp.model;

import android.arch.persistence.room.Ignore;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class User {
    private String fullName;
    private String nickName;
    private String password;
    private String email;
    private String birthDay;
    private Gender gender;
    private List<String> postId;
    private Profession profession;
    private Info userInfo;
    private List<String> genresId;
    private List<String> chatsId;

    private String primaryKey;

    private List<String> favouritePostId;
    private String token;

    private List<Notification> notificationList;


    public User() {
        genresId = new ArrayList<>();
        postId = new ArrayList<>();
        favouritePostId = new ArrayList<>();
        notificationList = new ArrayList<>();
        chatsId = new ArrayList<>();
    }

    public List<String> getPostId() {
        return postId;
    }

    public List<String> getGenresId() {
        return genresId;
    }

    public void setGenresId(List<String> genresId) {
        this.genresId = genresId;
    }

    public void addGenreId(String genre) {
        genresId.add(genre);
    }

    public void addFavouritePostId(String postId) {
        favouritePostId.add(postId);
    }

    public void addChatId(String chatId) {
        chatsId.add(chatId);
    }

    public void setPostId(List<String> postId) {
        this.postId = postId;
    }

    public void addNotification(Notification notification) {
        notificationList.add(notification);
    }

    public void addPostId(String id) {
        postId.add(id);
    }


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public Info getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(Info userInfo) {
        this.userInfo = userInfo;
    }

    public void setFavouritePostId(List<String> favouritePostId) {
        this.favouritePostId = favouritePostId;
    }

    public List<String> getFavouritePostId() {
        return favouritePostId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<Notification> getNotificationList() {
        return notificationList;
    }

    public List<String> getChatsId() {
        return chatsId;
    }

    public void setNotificationList(List<Notification> notificationList) {
        this.notificationList = notificationList;
    }
}
