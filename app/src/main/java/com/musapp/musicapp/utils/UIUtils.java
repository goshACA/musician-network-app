package com.musapp.musicapp.utils;


import android.view.View;
import android.widget.Button;

public final class UIUtils {
    private UIUtils(){}

    public static Button getButtonFromView(View view, int buttonId){
        return view.findViewById(buttonId);
    }


}
