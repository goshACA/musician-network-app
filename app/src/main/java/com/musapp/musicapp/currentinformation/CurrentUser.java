package com.musapp.musicapp.currentinformation;

import com.google.firebase.auth.FirebaseUser;
import com.musapp.musicapp.model.User;

public final  class CurrentUser {
    private CurrentUser(){}

    private static FirebaseUser currentFirebaseUser;

    private static User currentUser = new User();

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
       currentUser = user;
    }

    public static FirebaseUser getCurrentFirebaseUser() {
        return currentFirebaseUser;
    }

    public static void setCurrentFirebaseUser(FirebaseUser currentFirebaseUser) {
        CurrentUser.currentFirebaseUser = currentFirebaseUser;
    }
}
