package com.musapp.musicapp.fragments.registration_fragments;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.musapp.musicapp.R;
import com.musapp.musicapp.currentinformation.CurrentUser;
import com.musapp.musicapp.firebase_repository.FirebaseRepository;
import com.musapp.musicapp.fragments.registration_fragments.registration_fragment_transaction.RegistrationTransactionWrapper;
import com.musapp.musicapp.image_generator.RandomGradientGenerator;
import com.musapp.musicapp.model.Info;
import com.musapp.musicapp.model.Profession;
import com.musapp.musicapp.model.User;
import com.musapp.musicapp.utils.UIUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;


public class ProfessionAndBioFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private ImageView profileImage;
    private Spinner professionSpinner;
    private TextView userInfoTextView;
    private Button nextButton;
    private Info userInfo;
    private Profession profession;
    private User user = CurrentUser.getCurrentUser();
    private final int REQUEST_CAMERA = 1;
    private final int SELECT_FILE = 0;

    private final int STORAGE_CAMERA_PERMISSION_CODE = 100;
    public boolean isPermissionAccepted = false;
    private boolean isCameraPermissionAccepted = false;
    private boolean isStoragePermissionAccepted = false;
    private Uri path;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        userInfo = new Info();
        View rootView = inflater.inflate(R.layout.profession_bio_fragment, container, false);
        profileImage = rootView.findViewById(R.id.circular_view_profession_fragment_profile_image);
        professionSpinner = rootView.findViewById(R.id.spinner_profession_fragment_profession_drop_down);
        userInfoTextView = rootView.findViewById(R.id.text_profession_fragment_user_info);
        nextButton = UIUtils.getButtonFromView(getActivity().findViewById(R.id.layout_registration_genre_info), R.id.action_fragment_grid_and_profession_next);
        professionSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> spinnerDataAdapter = ArrayAdapter.createFromResource(getContext(), R.array.professions, android.R.layout.simple_spinner_dropdown_item);
        spinnerDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        professionSpinner.setAdapter(spinnerDataAdapter);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPermissionAccepted) {
                    selectImage();
                } else {
                    requestPermission();
                    //requestCameraPermission();
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userInfo.setAdditionalInfo(userInfoTextView.getText().toString());
                RegistrationTransactionWrapper.registerForNextFragment((int) nextButton.getTag());
                setImage();
                submitInformation();
                //CurrentUser.setCurrentUser(user);
            }
        });
        return rootView;
    }
    private void setImage(){
        if(userInfo.getImageUri() == null ){
            //TODO get uri of generated drawable

            GradientDrawable generateImage = RandomGradientGenerator.getRandomGradient();
            Bitmap bitmap = RandomGradientGenerator.convertToBitmap(generateImage);
            profileImage.setImageBitmap(bitmap);
            putBitmapImageToStorage(bitmap);
            //profileImage.setImageDrawable(generateImage);
            //Bitmap bitmap = ((BitmapDrawable)generateImage).getBitmap();
            //putImageToStorage(generateImage.get);
        }
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
        Toast.makeText(getContext(), "yess", Toast.LENGTH_SHORT).show();

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nextButton.setTag(R.integer.registration_fragment_professions_4);
    }

    private void selectImage() {
        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Image");

        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Camera")) {
                    if (isCameraPermissionAccepted) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, path);
                        startActivityForResult(intent, REQUEST_CAMERA);
                    }
                } else if (items[i].equals("Gallery")) {
                    if (isStoragePermissionAccepted) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        //startActivityForResult(intent.createChooser(intent, "Select File"), SELECT_FILE);
                        startActivityForResult(intent, SELECT_FILE);
                    }
                } else if (items[i].equals("Cancel")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == REQUEST_CAMERA) {

                Bundle bundle = data.getExtras();
                Log.d("camera", "onActivityResult: data url is: " + data.getData());
                final Bitmap bmp = (Bitmap) bundle.get("data");
                profileImage.setImageBitmap(bmp);
                putBitmapImageToStorage(bmp);

            } else if (requestCode == SELECT_FILE) {

                final Uri selectedImageUri = data.getData();
                profileImage.setImageURI(selectedImageUri);
                putImageToStorage(selectedImageUri);
//                final StorageReference fileReference = FirebaseRepository.createImageStorageChild(System.currentTimeMillis() + "." + getFileExtension(selectedImageUri));
//                FirebaseRepository.putFileInStorage(fileReference, selectedImageUri, new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        FirebaseRepository.getDownloadUrl(fileReference, new OnSuccessListener<Uri>() {
//                            @Override
//                            public void onSuccess(Uri uri) {
//                                userInfo.setImageUri(uri.toString());
//                            }
//                        });
//                    }
//                });
//                final StorageReference fileReference = DBAccess.creatStorageChild("image/", System.currentTimeMillis() + "." + getFileExtension(selectedImageUri));
//                fileReference.putFile(selectedImageUri).addOnSuccessListener(
//                        new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                            @Override
//                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                    @Override
//                                    public void onSuccess(Uri uri) {
//                                        userInfo.setImageUri(uri.toString());
//
//                                    }
//                                });
//                                //userInfo.setImageUri(taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
//                            }
//                        });
                //userInfo.setImageUri(selectedImageUri.);
            }

        }
    }

    private void putImageToStorage(Uri selectedImageUri){
        final StorageReference fileReference = FirebaseRepository.createImageStorageChild(System.currentTimeMillis() + "." + getFileExtension(selectedImageUri));
        FirebaseRepository.putFileInStorage(fileReference, selectedImageUri, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                FirebaseRepository.getDownloadUrl(fileReference, new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        userInfo.setImageUri(uri.toString());
                    }
                });
            }
        });
    }

    private void putBitmapImageToStorage(Bitmap btm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        btm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        final StorageReference reference = FirebaseRepository.createImageStorageChild(System.currentTimeMillis() + "." + "jpg");
        FirebaseRepository.putBytesToFirebaseStorage(reference, data, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                FirebaseRepository.getDownloadUrl(reference, new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        userInfo.setImageUri(uri.toString());
                    }
                });
            }
        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    public ProfessionAndBioFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profession = new Profession();
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            isPermissionAccepted = true;
            isStoragePermissionAccepted = true;

        }
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            isCameraPermissionAccepted = true;
            isPermissionAccepted = true;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        profession.setName(item);
        Toast.makeText(getContext(), item, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void submitInformation() {
        user.setProfession(profession);
        user.setUserInfo(userInfo);

    }
}


