package com.musapp.musicapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.musapp.musicapp.currentinformation.CurrentUser;
import com.musapp.musicapp.enums.PostUploadType;
import com.musapp.musicapp.firebase_repository.FirebaseRepository;
import com.musapp.musicapp.model.Post;
import com.musapp.musicapp.uploads.AttachedFile;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UploadForegroundService extends IntentService {

    //TODO separate upload and firebase parts to different services

    public static final String ADD_POST_INTENT_ACTION = "add.post.intent.action";

    private AttachedFile attachedFile;
    private Post post;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public UploadForegroundService(String name) {
        super(name);
    }

    public UploadForegroundService(){
        super("ForegroundService");

    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
         super.onStartCommand(intent, flags, startId);
         return START_STICKY;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent == null || intent.getAction() == null || intent.getExtras() == null)
            return;
        switch (intent.getAction()){
            case ADD_POST_INTENT_ACTION:{
                post = (Post) intent.getParcelableExtra(Post.class.getSimpleName());
                attachedFile =  (AttachedFile) intent.getSerializableExtra(AttachedFile.class.getSimpleName());
                uploadFilesToStorage((HashMap<String, Uri>) intent.getSerializableExtra(HashMap.class.getSimpleName()));
            }break;
        }


    }

    private void uploadFilesToStorage(final HashMap<String, Uri> filesUri){
        if(attachedFile == null)
           sentPostToFirebase();
       else if(attachedFile.getFileType() == PostUploadType.IMAGE)
        {

      for(Map.Entry<String, Uri> entry: filesUri.entrySet()){
           final StorageReference fileReference = FirebaseRepository.createImageStorageChild(entry.getKey());

           FirebaseRepository.putFileInStorage(fileReference, entry.getValue(), new OnSuccessListener<UploadTask.TaskSnapshot>() {
               @Override
               public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                  FirebaseRepository.getDownloadUrl(fileReference, new OnSuccessListener<Uri>() {
                      @Override
                      public void onSuccess(Uri uri) {
                          attachedFile.addFile(uri.toString());
                          if(attachedFile.getFilesUrls().size() == filesUri.size()) {
                              post.setAttachment(attachedFile);
                              sentPostToFirebase();

                          }
                      }
                  });
               }
           });

       }}

       else if(attachedFile.getFileType() == PostUploadType.VIDEO){
            for(Map.Entry<String, Uri> entry: filesUri.entrySet()){
                final StorageReference fileReference = FirebaseRepository.createVideoStorageChild(entry.getKey());

                FirebaseRepository.putFileInStorage(fileReference, entry.getValue(), new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        FirebaseRepository.getDownloadUrl(fileReference, new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                attachedFile.addFile(uri.toString());
                                if(attachedFile.getFilesUrls().size() == filesUri.size()){
                                    post.setAttachment(attachedFile);
                                    sentPostToFirebase();}

                            }
                        });
                    }
                });
        }}

        else if(attachedFile.getFileType() == PostUploadType.MUSIC){
            for(final Map.Entry<String, Uri> entry: filesUri.entrySet()){
                final StorageReference fileReference = FirebaseRepository.createMusicStorageChild(entry.getKey());

                FirebaseRepository.putFileInStorage(fileReference, entry.getValue(), new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        FirebaseRepository.getDownloadUrl(fileReference, new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                //attachedFile.addFile(uri.toString());
                                attachedFile.addFile(getSongName(entry.getKey(), uri.toString()));
                                if(attachedFile.getFilesUrls().size() == filesUri.size()){
                                    post.setAttachment(attachedFile);
                                    sentPostToFirebase();}

                            }
                        });
                    }
                });
            }
        }

    }

    private String getSongName(String name, String uri){
        return name.substring(0, name.length()-4) + uri;
    }



    /*private void sentAttachmentAndPostToFirebase(){
        FirebaseRepository.createAttachment(attachedFile, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseRepository.setInnerAttachmentKey(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Iterator<DataSnapshot> lastChild = dataSnapshot.getChildren().iterator();
                        String attachmentId  = (lastChild.next().getKey());
                        attachedFile.setPrimaryKey(attachmentId);
                        FirebaseRepository.setInnerAtachmentKeyToFirebase(attachmentId);
                        post.setAttachmentId(attachmentId);
                        sentPostToFirebase();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });}*/

    private void sentPostToFirebase(){

        FirebaseRepository.createPost(post, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseRepository.setPostInnerPrimaryKey(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Iterator<DataSnapshot> lastChild = dataSnapshot.getChildren().iterator();
                        post.setPrimaryKey(lastChild.next().getKey());
                        CurrentUser.getCurrentUser().addPostId(post.getPrimaryKey());
                        FirebaseRepository.updateCurrentUserPostsInFirebase();
                        FirebaseRepository.setPostInnerPrimaryKeyToFirebase(post.getPrimaryKey());

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        // TODO start foreground ?
    }


}
