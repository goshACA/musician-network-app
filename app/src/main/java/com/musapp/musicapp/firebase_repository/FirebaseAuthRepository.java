package com.musapp.musicapp.firebase_repository;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.musapp.musicapp.firebase.DBAccess;

public final class FirebaseAuthRepository {
    private FirebaseAuthRepository(){}

    public static void createUserWithEmailAndPassword(String email, String password, OnCompleteListener<AuthResult> onCompleteListener){
        DBAccess.getFirebaseAuth().createUserWithEmailAndPassword(email, password).addOnCompleteListener(onCompleteListener);
    }

    public static void signInWithEmailAndPassword(String email, String password, OnCompleteListener<AuthResult> onCompleteListener){
        DBAccess.getFirebaseAuth().signInWithEmailAndPassword(email, password).addOnCompleteListener(onCompleteListener);
    }

    public static void signInWithCredential(AuthCredential credential, OnCompleteListener<AuthResult> onCompleteListener){
        DBAccess.getFirebaseAuth().signInWithCredential(credential).addOnCompleteListener(onCompleteListener);
    }
    public static void signInWithCredential(AuthCredential credential){
        DBAccess.getFirebaseAuth().signInWithCredential(credential);
    }
}
