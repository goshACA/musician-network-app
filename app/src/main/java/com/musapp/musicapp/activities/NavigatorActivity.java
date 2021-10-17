package com.musapp.musicapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.musapp.musicapp.currentinformation.CurrentUser;
import com.musapp.musicapp.firebase_repository.FirebaseRepository;
import com.musapp.musicapp.model.User;
import com.musapp.musicapp.preferences.RememberPreferences;

public class NavigatorActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Class<?> activityClass;

        try {
            SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
            activityClass = Class.forName(
                    prefs.getString("lastActivity", StartActivity.class.getName()));



            if(RememberPreferences.getUser(this).equals("none")|| RememberPreferences.getState(this))
                activityClass = StartActivity.class;


        } catch(ClassNotFoundException ex) {
            activityClass = StartActivity.class;
        }

        startActivity(new Intent(this, activityClass));
        finish();
    }

    private void setCurrentUser(){
     /*   FirebaseRepository.setCurrentUser(RememberPreferences.getUser(this), new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                CurrentUser.setCurrentUser(dataSnapshot.getValue(User.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
    }
}
