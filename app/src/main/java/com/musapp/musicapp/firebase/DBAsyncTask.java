package com.musapp.musicapp.firebase;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public final class DBAsyncTask {
    private DBAsyncTask(){}
    private static DatabaseReference databaseReference;

    public static void waitResponse(final String childName, final DBAsyncTaskResponse response, Object obj){
      response.doForResponse(childName, obj);
        databaseReference.child(childName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot != null){
                            Iterator<DataSnapshot> childIterator = dataSnapshot.getChildren().iterator();
                            DataSnapshot snapshot = dataSnapshot;
                            while(childIterator.hasNext())
                             snapshot =  childIterator.next();
                        response.doOnResponse(snapshot.getKey(), childName);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }


    public static void waitSimpleResponse(final String childName, final DBAsyncTaskResponse response, String... args){
        response.doForResponse(childName, args);
        databaseReference.child(childName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot != null){
                            response.doOnResponse("Done", childName);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    public static void setDatabaseReference(DatabaseReference databaseReference) {
        DBAsyncTask.databaseReference = databaseReference;
    }
}
