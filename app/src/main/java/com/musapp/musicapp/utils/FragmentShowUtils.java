package com.musapp.musicapp.utils;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public final class FragmentShowUtils {
    private FragmentShowUtils(){}

    private static Fragment previousFragment;
    private static Fragment currentFragment;



    public static Fragment getPreviousFragment() {
        return previousFragment;
    }

    public static void setPreviousFragment(Fragment previousFragment) {
        FragmentShowUtils.previousFragment = previousFragment;
    }

    public static void setCurrentFragment(Fragment currentFragment) {
        FragmentShowUtils.currentFragment = currentFragment;
    }

    public static Fragment getCurrentFragment() {
        return currentFragment;
    }
}
