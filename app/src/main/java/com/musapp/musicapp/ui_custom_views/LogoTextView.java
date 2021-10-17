package com.musapp.musicapp.ui_custom_views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class LogoTextView extends android.support.v7.widget.AppCompatTextView {
    public LogoTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LogoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LogoTextView(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Denne_Freakshow.ttf");
        setTypeface(tf);

    }
    
}
