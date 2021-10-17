package com.musapp.musicapp.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public final class AppPreferences {
    private static final String PREF_NAME = "AppPreferences";
    private static final String GENRES = "quited";
    private static final String PROFESSION_ADDITIONAL_INFO = "additional_info";

    private AppPreferences() {

    }

    public static void clearGenreState(Context context){
        getSharedPreferences(context).edit().remove(GENRES).apply();
    }

    public static void saveGenreState(Context context, String genre) {
        getSharedPreferences(context)
                .edit()
                .putString(GENRES, genre)
                .apply();
    }

    public static String getGenreState(Context context){
        return getSharedPreferences(context)
                .getString(GENRES, null);
    }

    public static void saveProfessionState(Context context, String profession) {
        getSharedPreferences(context)
                .edit()
                .putString(PROFESSION_ADDITIONAL_INFO, profession)
                .apply();
    }

    public static String getProfessionState(Context context){
        return getSharedPreferences(context)
                .getString(PROFESSION_ADDITIONAL_INFO, null);
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
}
