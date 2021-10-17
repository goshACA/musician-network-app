package com.musapp.musicapp.service;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class BoundService extends Service{
    //mMust work, because it's garanted that at the moment only one fragment is using service
    private static boolean isBind = false;
    private static Bundle fragmentInformation = null;

    private NotificationBinder mNotificationBinder;

    public static boolean isFragmentBind() {
        return isBind;
    }

    public static Bundle getFragmentInformation() {
        return fragmentInformation;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
       if(mNotificationBinder == null){
           mNotificationBinder = new NotificationBinder();
       }
       return mNotificationBinder;
    }


    @Override
    public boolean onUnbind(Intent intent) {
        isBind = false;
        fragmentInformation = null;
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        isBind = true;
        super.onRebind(intent);
    }

    public class NotificationBinder extends Binder{
        public void setBindState(boolean bind){
            isBind = bind;
        }
        public void setBindFragmentBundle(Bundle bundle){
            fragmentInformation = bundle;
        }
    }


}
