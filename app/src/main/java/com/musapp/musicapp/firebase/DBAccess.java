package com.musapp.musicapp.firebase;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.StorageReference;
import com.musapp.musicapp.model.User;
import com.musapp.musicapp.utils.HashUtils;

import java.util.HashMap;
import java.util.Map;

public final class DBAccess {
    private DBAccess() {
    }

    private static DatabaseReference databaseReference;
    private static StorageReference mStorageReference;
    private static FirebaseAuth sFirebaseAuth;
    private static String DEFAULT_PATH = "";
    private static String token = FirebaseInstanceId.getInstance().getToken();

    //CREATE
    public static void createChild(String childName, Object obj) {
        String key = databaseReference.child(DEFAULT_PATH + childName).push().getKey();
        databaseReference.child(DEFAULT_PATH + childName).child(key).setValue(obj);

    }

    public static void createChild(String path, String childName, Object obj) {
        String key = databaseReference.child(path + '/' + childName).push().getKey();
        databaseReference.child(path + '/' + childName).child(key).setValue(obj);

    }

    public static void createField(Object obj, String path) {
        DatabaseReference childRef = databaseReference.child(path);
        childRef.setValue(obj);
    }

    public static void createFields(HashMap<String, Object> fields, String path) {
        for (Map.Entry<String, Object> entry : fields.entrySet()) {
            //TODO find better solution
            databaseReference.child(path + entry.getKey()).setValue(entry.getValue());
        }
    }


    public static void setDatabaseReference(DatabaseReference databaseReference) {
        DBAccess.databaseReference = databaseReference;
    }

    public static boolean selectEmail(final String childName, final String email, final String password) {
        final boolean[] res = new boolean[1];
        databaseReference.child(childName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            String emailField = (String) snapshot.child("email").getValue();
                            if (email.equals(emailField)) {
                                String passField = (String) snapshot.child("password").getValue();
                                if (HashUtils.hash(password).equals(passField))
                                    res[0] = true;
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

        return res[0];
    }

    public static void setStorageReference(StorageReference ref) {
        mStorageReference = ref;
    }

    public static StorageReference creatStorageChild(String path, String childName) {
        StorageReference ref = mStorageReference.child(path + childName);

        return ref;
    }

    public static DatabaseReference getUserReference(String childPath) {
        return databaseReference.child(childPath);
    }

    public static DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    public static StorageReference getStorageReference() {
        return mStorageReference;
    }

    public static void setToken(String newToken){token = newToken;}

    public static String getToken() {
        return token;
    }


    public static FirebaseAuth getFirebaseAuth() {
        return sFirebaseAuth;
    }

    public static void setFirebaseAuth(FirebaseAuth firebaseAuth) {
        sFirebaseAuth = firebaseAuth;
    }
}
