package com.musapp.musicapp.activities;

import android.app.Application;

//import com.facebook.FacebookSdk;
//import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.musapp.musicapp.firebase.DBAccess;
import com.musapp.musicapp.firebase.DBAsyncTask;
import com.musapp.musicapp.utils.ContextUtils;
import com.musapp.musicapp.utils.GlideUtil;



public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final FirebaseStorage storage = FirebaseStorage.getInstance();
        DatabaseReference ref = database.getReference();
        StorageReference storageReference = storage.getReference();
        GlideUtil.setContext(this);
        ContextUtils.setContext(this);
        DBAccess.setFirebaseAuth(FirebaseAuth.getInstance());
        DBAccess.setDatabaseReference(ref);

        DBAccess.setStorageReference(storageReference);

//
//        DBAsyncTask.setDatabaseReference(ref);
//
//
//        DBAccess.setStorageReference(storageReference);
//
//
//        DatabaseReference childRef = ref.child("genres");
//
//        Map<String, Genre> genreHashMap = new HashMap<>();
//       for(Genre genre: listOfGenres){
//           genreHashMap.put(genre.getName(), genre);
//       }
//
//       childRef.setValue(genreHashMap);
//
//       childRef = ref.child("user");
//
//       childRef.addListenerForSingleValueEvent(new ValueEventListener() {
//           @Override
//           public void onDataChange(DataSnapshot dataSnapshot) {
//               ArrayList<User> userList = new ArrayList();
//               for (DataSnapshot child :
//                       dataSnapshot.getChildren()) {
//                   User user = child.getValue(User.class);
//                   userList.add(user );
//               }
//               //Here you have all user you can pass ```userList``` to your method furthur
//           }
//           @Override
//           public void onCancelled(DatabaseError databaseError) {
//           }
//       });
        DBAsyncTask.setDatabaseReference(ref);
       // FacebookSdk.sdkInitialize(getApplicationContext());
       // AppEventsLogger.activateApp(this);

    }


}
