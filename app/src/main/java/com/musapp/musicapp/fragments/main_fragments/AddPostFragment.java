package com.musapp.musicapp.fragments.main_fragments;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.musapp.musicapp.R;
import com.musapp.musicapp.adapters.inner_post_adapter.BaseUploadsAdapter;
import com.musapp.musicapp.adapters.viewholders.post_viewholder.BasePostViewHolder;
import com.musapp.musicapp.currentinformation.CurrentUser;
import com.musapp.musicapp.enums.PostUploadType;
import com.musapp.musicapp.fragments.main_fragments.toolbar.SetToolBarAndNavigationBarState;
import com.musapp.musicapp.model.Post;
import com.musapp.musicapp.model.User;
import com.musapp.musicapp.pattern.UploadTypeFactory;
import com.musapp.musicapp.pattern.UploadsAdapterFactory;
import com.musapp.musicapp.service.UploadForegroundService;
import com.musapp.musicapp.uploads.AttachedFile;
import com.musapp.musicapp.uploads.BaseUpload;
import com.musapp.musicapp.utils.CheckUtils;

import java.io.File;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class AddPostFragment extends Fragment {

    private Post mNewPost;
    private EditText mPostText;
    private RecyclerView mPostAttachment;
    private ImageView mAddImage;
    private ImageView mAddVideo;
    private ImageView mAddMusic;
    private TextView mSelectedFilesName;
    DatabaseReference mDatabaseReference;
    private PostUploadType mType;
    private int selectedFiles = 0;
    private SetToolBarAndNavigationBarState mSetToolBarAndNavigationBarState;

    private boolean isStoragePermissionAccepted = false;
    private boolean isCameraPermissionAccepted = false;
    private final int STORAGE_CAMERA_PERMISSION_CODE = 101;
    public boolean isPermissionAccepted = false;
    private final int SELECT_IMAGE = 12;
    private final int SELECT_VIDEO = 13;
    private final int SELECT_AUDIO = 14;
    public static final String SONG_ARTIST = "Artist";
    public static final String SONG_TITLE = "Title";
    public static final String SONG_DURATION = "Duration";

    private AttachedFile mAttachedFile;
    private User user = CurrentUser.getCurrentUser();

    private Map<String, Uri> fileUri  = new HashMap<>();

    private Bundle bundle;
    private boolean typeIsChoosed = false;

    private BaseUploadsAdapter<BaseUpload, BasePostViewHolder> mAdapter;

    public AddPostFragment() {

        mNewPost = new Post();
     mNewPost.setProfileImage(CurrentUser.getCurrentUser().getUserInfo().getImageUri());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_post, container, false);
        mPostText = view.findViewById(R.id.text_fragment_add_post_text);
        mPostAttachment = view.findViewById(R.id.recycler_view_fragment_add_post_file);
        mAddImage = view.findViewById(R.id.image_fragment_add_post_attach_image);
        mAddMusic = view.findViewById(R.id.image_fragment_add_post_attach_music);
        mAddVideo = view.findViewById(R.id.image_fragment_add_post_attach_video);
        mSelectedFilesName = view.findViewById(R.id.text_add_post_fragment_selected_file_names);

        mType = PostUploadType.NONE;
        mAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isStoragePermissionAccepted();
                mType = PostUploadType.IMAGE;

                mAttachedFile = new AttachedFile();
                mAttachedFile.setFileType(PostUploadType.IMAGE);

                if (isStoragePermissionAccepted){
                    selectImage();
                }else{
                    requestPermission();
                }
            }
        });
        mAddVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStoragePermissionAccepted();
                mType = PostUploadType.VIDEO;
                mAttachedFile = new AttachedFile();
                mAttachedFile.setFileType(PostUploadType.VIDEO);
                if (isStoragePermissionAccepted){
                    selectVideo();
                }else{
                    requestPermission();
                }
            }
        });
        mAddMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStoragePermissionAccepted();
                mType = PostUploadType.MUSIC;
                mAttachedFile = new AttachedFile();
                mAttachedFile.setFileType(PostUploadType.MUSIC);
                if(isStoragePermissionAccepted){
                    selectAudio();
                }else{
                    requestPermission();
                }
            }
        });
        mSetToolBarAndNavigationBarState.setTitle(R.string.add_post_action_bar_title_menu);
        return view;
    }

    private void selectImage(){

      if (isStoragePermissionAccepted){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, SELECT_IMAGE);
      }

    }

    private void selectVideo(){
        if(isStoragePermissionAccepted){
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setDataAndType(android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI, "video/*");
            startActivityForResult(intent, SELECT_VIDEO);
        }
    }

    private void selectAudio(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, "audio/*");
        startActivityForResult(Intent.createChooser(intent,"Gallery"), SELECT_AUDIO);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("DODO", "onActivityResult: firs here");

        if(resultCode == Activity.RESULT_OK){

            initUploadsAdapter();

            if(requestCode == SELECT_IMAGE){
                mAddVideo.setEnabled(false);
                mAddMusic.setEnabled(false);

                final Uri selectedImageUri = data.getData();
                if(CheckUtils.checkImageExtension(getFileExtension(selectedImageUri))){
                    addToUploads(selectedImageUri.toString());
                    fileUri.put(System.currentTimeMillis() + "." + getFileExtension(selectedImageUri), selectedImageUri);
                    selectedFiles++;
                    mSelectedFilesName.setText(String.valueOf(selectedFiles));
                }else{
                    Toast.makeText(getContext(), "type of image does not support", Toast.LENGTH_LONG).show();
                }


            }else if(requestCode == SELECT_VIDEO){
                mAddMusic.setEnabled(false);
                mAddImage.setEnabled(false);
                final Uri selectedVideoUri = data.getData();
                addToUploads(selectedVideoUri.toString());
                fileUri.put(System.currentTimeMillis() + "." + getFileExtension(selectedVideoUri), selectedVideoUri);
                selectedFiles++;
               mSelectedFilesName.setText(String.valueOf(selectedFiles));

            }else if(requestCode == SELECT_AUDIO){
                mAddVideo.setEnabled(false);
                mAddImage.setEnabled(false);

                final Uri selectedAudioUri = data.getData();
                //String[] metadata = getSongCustomName(selectedAudioUri);
                fileUri.put(getSongCustomName(selectedAudioUri) + "." + getFileExtension(selectedAudioUri), selectedAudioUri);
                selectedFiles++;
                mSelectedFilesName.setText(String.valueOf(selectedFiles));

            }

        }
    }

    public String getSongCustomName(Uri uri){

        String[] metadata = new String[3];
        MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
        metadataRetriever.setDataSource(getContext(), uri);
        try{
            //get artist name
           metadata[0] = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
           if(metadata[0] == null || metadata[0].equals("")){
               metadata[0] = "Unknown";
           }
           //get song title
           metadata[1] = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            if(metadata[1] == null || metadata[1].equals("")){
                metadata[1] = "Unknown";
            }
           //get song duration
           metadata[2] = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            if(metadata[2] == null || metadata[2].equals("")){
                metadata[2] = "9999";
            }

        }catch (Exception ex){
            metadata[0] = "Unknown";
            metadata[1] = "Unknown";
            metadata[2] = "9999";
            ex.printStackTrace();
        }

        return metadata[0] + "$" + metadata[1] + "$" + metadata[2] + "$" ;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_post_fragment_publish_post, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_and_publish_post: {
                savePost();
            }break;
        }
        return true;
    }
    private void savePost(){
        mNewPost.setPostText(mPostText.getText().toString());
        mNewPost.setType(mType);
//        DateFormat simple = new SimpleDateFormat("dd MMM HH:mm", Locale.US);
//        Date date = new Date(System.currentTimeMillis());
        mNewPost.setPublishedTime( System.currentTimeMillis());
        mNewPost.setUserId(CurrentUser.getCurrentUser().getPrimaryKey());
        mNewPost.setUserName(CurrentUser.getCurrentUser().getNickName());
        startService();
        quitFragment();

    }

    private void startService(){
        Intent intent = new Intent(getContext(), UploadForegroundService.class);
        intent.setAction(UploadForegroundService.ADD_POST_INTENT_ACTION);
        intent.putExtra(Post.class.getSimpleName(), mNewPost);
        intent.putExtra(AttachedFile.class.getSimpleName(), mAttachedFile);
        intent.putExtra(HashMap.class.getSimpleName(), (Serializable) fileUri);
        getActivity().startService(intent);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void quitFragment() {

           getFragmentManager().beginTransaction()
                   .remove(this)
                 //  .replace(R.id.layout_activity_app_container, FragmentShowUtils.getPreviousFragment())
                   .commit();
           getFragmentManager().popBackStack();
    }

    public boolean isStoragePermissionAccepted(){
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED){
            isStoragePermissionAccepted = true;
        }else{
            isStoragePermissionAccepted = false;
        }
        return isStoragePermissionAccepted;
    }
    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, STORAGE_CAMERA_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == STORAGE_CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isPermissionAccepted = true;
                isStoragePermissionAccepted = true;
                selectImage();
            }
            if (grantResults.length > 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                isPermissionAccepted = true;
                isCameraPermissionAccepted = true;
                selectImage();
            }
        }
    }

    private BaseUploadsAdapter.OnItemSelectedListener mOnItemSelectedListener = new BaseUploadsAdapter.OnItemSelectedListener() {
        @Override
        public void onItemSelected(String uri) {

        }
    };

    List<BaseUpload> uploads = new ArrayList<>();

    private void initUploadsAdapter(){
        if (mAdapter == null)
            mAdapter = UploadsAdapterFactory.setAdapterTypeByInputType(mType);
        if(mType == PostUploadType.IMAGE){
            mAdapter.setOnItemSelectedListener(mOnItemSelectedListener);
        }
        mPostAttachment.setLayoutManager(new GridLayoutManager(getContext(), 1));
        mPostAttachment.setAdapter(mAdapter);
    }

    private void addToUploads(String url){
        if(mType == PostUploadType.NONE || mType == PostUploadType.MUSIC)
            return;

        uploads.add(UploadTypeFactory.setUploadByType(mType, url));
        mAdapter.setUploads(uploads);
    }

    public void setSetToolBarAndNavigationBarState(SetToolBarAndNavigationBarState toolBarTitle){
        mSetToolBarAndNavigationBarState = toolBarTitle;
    }
}
