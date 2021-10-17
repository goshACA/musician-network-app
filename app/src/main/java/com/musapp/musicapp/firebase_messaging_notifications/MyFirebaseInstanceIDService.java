package com.musapp.musicapp.firebase_messaging_notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.musapp.musicapp.R;
import com.musapp.musicapp.activities.AppMainActivity;
import com.musapp.musicapp.currentinformation.CurrentUser;
import com.musapp.musicapp.firebase_repository.FirebaseRepository;
import com.musapp.musicapp.preferences.RememberPreferences;

import static android.support.constraint.Constraints.TAG;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {



    @Override
    public void onTokenRefresh() {


        String tkn = FirebaseInstanceId.getInstance().getToken();
        if(!RememberPreferences.getUser(getApplicationContext()).equals("none")){
        CurrentUser.getCurrentUser().setToken(tkn);
        FirebaseRepository.updateCurrentUserToken(tkn);
        Log.d("Nottttt","Token ["+tkn+"]");

        sendRegistrationToServer(tkn);}



    }
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.

        Intent intent = new Intent(this, AppMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1410 ,/* Request code */intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("FCM Message")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1410
            ,notificationBuilder.build());
    }
}