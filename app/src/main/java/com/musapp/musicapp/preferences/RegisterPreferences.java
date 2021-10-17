package com.musapp.musicapp.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class RegisterPreferences{
    private static final String PREF_NAME = "RegisterPreferences";
    private static final String IS_REGISTERED = "registered";


    private RegisterPreferences() {

    }

    public static void saveState(Context context, boolean registered) {
        getSharedPreferences(context)
                .edit()
                .putBoolean(IS_REGISTERED, registered)
                .apply();
    }

    public static boolean getState(Context context){
        return getSharedPreferences(context)
                .getBoolean(IS_REGISTERED, false);
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
}
