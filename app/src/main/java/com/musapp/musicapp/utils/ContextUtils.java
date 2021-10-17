package com.musapp.musicapp.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;

public final class ContextUtils {

  private static Context mContext;

  public static void setContext(Context context) {
    mContext = context;
  }

  private ContextUtils(){}

  public static String getResourceString(Fragment fragment, int id){
    return fragment.getActivity().getBaseContext().getString(id);
  }
  public static int getResourceInteger(Fragment fragment, int id){
    return fragment.getActivity().getBaseContext().getResources().getInteger(id);
  }
  public static Drawable getRsourceDrawable(int id){
    return mContext.getDrawable(id);
  }
}