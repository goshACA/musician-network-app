package com.musapp.musicapp.utils;

import android.widget.EditText;

public final class ErrorShowUtils {
    private ErrorShowUtils(){};

    public static void showEditTextError(EditText editText, String msg){
        editText.setError(msg);
    }
}