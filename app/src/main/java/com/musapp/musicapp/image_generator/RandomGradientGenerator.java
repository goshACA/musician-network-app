package com.musapp.musicapp.image_generator;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.GradientDrawable;

import java.util.Random;

public class RandomGradientGenerator {
    private RandomGradientGenerator(){}
    private static Random random = new Random();

    public static GradientDrawable getRandomGradient(){
        return  new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] {random.nextInt(),random.nextInt(),random.nextInt() });
    }

    public static Bitmap convertToBitmap(GradientDrawable drawable){
        Bitmap result = Bitmap.createBitmap(200,200, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        drawable.setBounds(0,0,200,200);
        drawable.draw(canvas);

        return result;
    }

}
