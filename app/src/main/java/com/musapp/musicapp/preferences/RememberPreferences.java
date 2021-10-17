package com.musapp.musicapp.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.musapp.musicapp.model.Comment;
import com.musapp.musicapp.model.User;

public class RememberPreferences {
    private static final String PREF_NAME = "RememberPreferences";
    private static final String IS_QUITED = "quited";
    private static final String USER_ID = "user_id";

    private RememberPreferences() {

    }

    public static void saveState(Context context, boolean quit) {
        getSharedPreferences(context)
                .edit()
                .putBoolean(IS_QUITED, quit)
                .apply();
    }

    public static void saveUser(Context context,String currentUserPrimaryKey){
        getSharedPreferences(context)
                .edit()
                .putString(USER_ID, currentUserPrimaryKey)
                .apply();
    }

    public static boolean getState(Context context){
        return getSharedPreferences(context)
                .getBoolean(IS_QUITED, true);
    }

    public static String  getUser(Context context){
        return getSharedPreferences(context)
                .getString(USER_ID, "none");
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
}
