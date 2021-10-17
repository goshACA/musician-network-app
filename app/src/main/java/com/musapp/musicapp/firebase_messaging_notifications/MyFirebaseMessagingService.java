package com.musapp.musicapp.firebase_messaging_notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.musapp.musicapp.R;
import com.musapp.musicapp.activities.AppMainActivity;
import com.musapp.musicapp.currentinformation.CurrentUser;
import com.musapp.musicapp.firebase.DBAccess;
import com.musapp.musicapp.firebase_repository.FirebaseRepository;
import com.musapp.musicapp.fragments.main_fragments.ConversationFragment;
import com.musapp.musicapp.fragments.main_fragments.HomePageFragment;
import com.musapp.musicapp.fragments.main_fragments.NotificationFragment;
import com.musapp.musicapp.fragments.main_fragments.PostDetailsFragment;
import com.musapp.musicapp.model.Conversation;
import com.musapp.musicapp.model.User;
import com.musapp.musicapp.preferences.RememberPreferences;

import com.musapp.musicapp.service.BoundService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
/*
    @Override
    public void onNewToken(String s) {
        DBAccess.setToken(s);
        CurrentUser.getCurrentUser().setToken(s);
        FirebaseRepository.updateCurrentUserToken(s);
        Log.d("Nottttt","Token ["+s+"]");

        sendRegistrationToServer(s);
    }

*/

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if(remoteMessage.getData().get("type").equals("comment")) {
            if (!remoteMessage.getData().get("tag").equals(RememberPreferences.getUser(this))) {

            }
            else if(BoundService.isFragmentBind() &&
                    BoundService.getFragmentInformation().getString("fragmentName").equals(PostDetailsFragment.class.getSimpleName()) &&
                    BoundService.getFragmentInformation().getString("id").equals(remoteMessage.getData().get("postId"))){return;}
                    loadNotificationToFirebase(remoteMessage.getData().get("tag"), remoteMessage);
                    FirebaseRepository.adddChildListenerOnNotifications(remoteMessage.getData().get("tag"), /*new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            Log.i("SNAPME", dataSnapshot.toString());
                            if (remoteMessage.getData().get("tag").equals(RememberPreferences.getUser(MyFirebaseMessagingService.this)) &&
                                    dataSnapshot.getValue(com.musapp.musicapp.model.Notification.class).getCommentatorId().equals(remoteMessage.getData().get("commenterId"))
                                    && dataSnapshot.getValue(com.musapp.musicapp.model.Notification.class).getNotificationBody().equals(remoteMessage.getData().get("body"))
                                    && dataSnapshot.getValue(com.musapp.musicapp.model.Notification.class).getPostId().equals(remoteMessage.getData().get("postId"))
                                    && String.valueOf(dataSnapshot.getValue(com.musapp.musicapp.model.Notification.class).getDate()).equals(remoteMessage.getData().get("data"))) {
                                createNotification(remoteMessage, "PostDetailsFragment");}
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            Log.i("SNAPHIM", dataSnapshot.toString());
                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    }*/
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    com.musapp.musicapp.model.Notification notification = null ;
                                    Iterator<DataSnapshot> iter = dataSnapshot.getChildren().iterator();
                                    while(iter.hasNext())
                                        notification = iter.next().getValue(com.musapp.musicapp.model.Notification.class);
                                    Log.i("SNAPME", notification.getNotificationBody());
                                    if (remoteMessage.getData().get("tag").equals(RememberPreferences.getUser(MyFirebaseMessagingService.this)) &&
                                           notification != null &&notification.getCommentatorId()!= null &&
                                           notification.getCommentatorId().equals(remoteMessage.getData().get("commenterId"))
                                            &&notification.getNotificationBody().equals(remoteMessage.getData().get("body"))
                                            &&notification.getPostId().equals(remoteMessage.getData().get("postId"))
                                           // && String.valueOf(notification.getDate()).equals(remoteMessage.getData().get("data"))
                                    ) {
                                        Log.i("SNAPME", notification.toString());
                                        createNotification(remoteMessage, "NotificationFragment");
                                    }
                                    FirebaseRepository.removeListenerNotification(remoteMessage.getData().get("tag"), this);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
        }
        else if(BoundService.isFragmentBind() &&
                BoundService.getFragmentInformation().getString("fragmentName").equals(ConversationFragment.class.getSimpleName()) &&
                BoundService.getFragmentInformation().getString("id").equals(remoteMessage.getData().get("userId"))) {
            return;
        }
        else
         createNotification(remoteMessage, "ConversationFragment");

        }


    private void loadNotificationToFirebase(final String userPrimaryKey, final RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        final com.musapp.musicapp.model.Notification pushedNotification;
        pushedNotification = new com.musapp.musicapp.model.Notification(data.get("commenterId"), data.get("postId"),
                data.get("body"), Long.parseLong(data.get("date")), data.get("commenterImageUrl"), data.get("title"));


        FirebaseRepository.getUserByPrimaryKey(userPrimaryKey, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<com.musapp.musicapp.model.Notification> notifications = new ArrayList<>();
                for(DataSnapshot infoSnapshot: dataSnapshot.child("notifications").getChildren())
                        notifications.add(infoSnapshot.getValue(com.musapp.musicapp.model.Notification.class));
                if(notifications.contains(pushedNotification))
                    return;

                   FirebaseRepository.updateUserNotificationListById(userPrimaryKey, dataSnapshot.child("notifications").getChildrenCount() + 1 , pushedNotification);

                  //  notifications.addAll((ArrayList<com.musapp.musicapp.model.Notification>) dataSnapshot.child("notifications").getValue());
               /* if(notifications.contains(pushedNotification))
                    return;
                notifications.add(pushedNotification);
                FirebaseRepository.updateUserNotificationListById(userPrimaryKey, notifications, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (remoteMessage.getData().get("tag").equals(RememberPreferences.getUser(MyFirebaseMessagingService.this))) {
                            createNotification(remoteMessage, "goTo");
                        }

                    }
                });*/

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void createNotification(RemoteMessage remoteMessage, String goTo) {


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        NotificationCompat.Builder notificationBuilder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
          createNotificationWithChannel(remoteMessage, goTo,  defaultSoundUri);
          return;
        }

        notificationBuilder = new NotificationCompat.Builder(this, "technoWeb")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(remoteMessage.getData().get("title") + " has commented your post")
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background))
                .setContentText(remoteMessage.getData().get("body"))
                .setAutoCancel(true)
                .setColor(0xffff7700)
                .setVibrate(new long[]{100, 100, 100, 100})
                .setPriority(Notification.PRIORITY_MAX)
                .setSound(defaultSoundUri);
        Intent resultIntent = new Intent(this, AppMainActivity.class);
        resultIntent.putExtra("goto", goTo);
        if(goTo.equals("ConversationFragment")){
            resultIntent.putExtra("CHAT_ID", remoteMessage.getData().get("chatId"));
        }



        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(AppMainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_ONE_SHOT
                );


        notificationBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        mNotificationManager.notify(new Random().nextInt(1000), notificationBuilder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationWithChannel(RemoteMessage remoteMessage, String goTo, Uri defaultSoundUri){
        String chanel_id = "3000";
        CharSequence name = "Carambola";
        String description = "MusicianAppCommentChannel";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = new NotificationChannel(chanel_id, name, importance);
        mChannel.setDescription(description);
        mChannel.enableLights(true);
        mChannel.setLightColor(Color.BLUE);
        mChannel.setImportance(NotificationManager.IMPORTANCE_HIGH);

        Intent intent = new Intent(this, AppMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("goto", goTo);
        if(goTo.equals("ConversationFragment")){
            intent.putExtra("CHAT_ID", remoteMessage.getData().get("chatId"));
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, chanel_id)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(remoteMessage.getData().get("title") + " has commented your post")
                .setContentText(remoteMessage.getData().get("body"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setVibrate(new long[]{100, 100, 100, 100})
                .setSound(defaultSoundUri);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        NotificationManager notificationManager1 = getSystemService(NotificationManager.class);
        notificationManager1.createNotificationChannel(mChannel);
        notificationManager1.notify(new Random().nextInt(1000), builder.build());
      //  notificationManager.notify(new Random().nextInt(1000), builder.build());

    }




}
