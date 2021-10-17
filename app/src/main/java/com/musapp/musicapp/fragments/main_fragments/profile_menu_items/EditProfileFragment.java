package com.musapp.musicapp.fragments.main_fragments.profile_menu_items;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.musapp.musicapp.R;
import com.musapp.musicapp.currentinformation.CurrentUser;
import com.musapp.musicapp.firebase_repository.FirebaseRepository;
import com.musapp.musicapp.fragments.main_fragments.ProfileFragment;
import com.musapp.musicapp.fragments.main_fragments.toolbar.SetToolBarAndNavigationBarState;
import com.musapp.musicapp.model.Comment;
import com.musapp.musicapp.model.Gender;
import com.musapp.musicapp.model.Info;
import com.musapp.musicapp.model.Post;
import com.musapp.musicapp.model.Profession;
import com.musapp.musicapp.model.User;
import com.musapp.musicapp.utils.CheckUtils;
import com.musapp.musicapp.utils.ContextUtils;
import com.musapp.musicapp.utils.ErrorShowUtils;
import com.musapp.musicapp.utils.GlideUtil;
import com.musapp.musicapp.utils.HashUtils;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.support.constraint.Constraints.TAG;

public class EditProfileFragment extends Fragment {

    @BindView(R.id.image_view_edit_profile_fragment_profile_image)
    CircleImageView mUserImage;
    @BindView(R.id.edit_text_fragment_edit_profile_full_name)
    EditText mFullname;
    @BindView(R.id.edit_text_fragment_edit_profile_nickname)
    EditText mNickname;
    @BindView(R.id.edit_text_fragment_edit_profile_birthday)
    EditText mBirthday;
    @BindView(R.id.action_fragment_edit_profile_male)
    RadioButton mMaleRadioButton;
    @BindView(R.id.action_fragment_edit_profile_female)
    RadioButton mFemaleRadioButton;
    @BindView(R.id.spinner_edit_profile_fragment_profession_drop_down)
    Spinner mProfessionSpinner;
    @BindView(R.id.edit_text_fragment_edit_profile_additional_info)
    EditText mAdditionalInfo;
    @BindView(R.id.action_edit_profile_fragment_save)
    Button mSaveButton;
    @BindView(R.id.text_edit_profile_fragment_email)
    TextView mEmailText;
    @BindView(R.id.edit_text_fragment_edit_profile_password)
    EditText mPassword;
    @BindView(R.id.edit_text_fragment_edit_profile_confirm_password)
    EditText mConfirmPassword;
    @BindView(R.id.edit_text_fragment_edit_profile_last_password)
    EditText mLastPassword;
    @BindView(R.id.progress_bar_edit_profile)
    ProgressBar mProgressBar;

    private User mEditedUser = new User();
    private Info mInfo = new Info();

    private Calendar myCalendar = Calendar.getInstance();
    private DatePickerDialog datePickerDialog;

    private final int REQUEST_CAMERA = 1;
    private final int SELECT_FILE = 0;
    private boolean isCameraPermissionAccepted = false;
    private boolean isStoragePermissionAccepted = false;
    private Uri path;
    private SetToolBarAndNavigationBarState mSetToolBarAndNavigationBarState;
    private boolean isImagePicked = false;
    private String mChangedImageUrl;

    public void setSetToolBarAndNavigationBarState(SetToolBarAndNavigationBarState setToolBarAndNavigationBarState) {
        mSetToolBarAndNavigationBarState = setToolBarAndNavigationBarState;
    }

    private AdapterView.OnItemSelectedListener mItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String item = parent.getItemAtPosition(position).toString();
            Profession newProfession = new Profession();
            newProfession.setName(item);
            mEditedUser.setProfession(newProfession);
            Toast.makeText(getContext(), item, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    private void updateLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        mBirthday.setText(sdf.format(myCalendar.getTime()));
    }

    private View.OnClickListener mDataPickerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            datePickerDialog = new DatePickerDialog(getActivity(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                            String date1 = dayOfMonth + "//" + (monthOfYear + 1) + "//" + year;
                            mBirthday.setText(date1);
                        }
                    }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();


        }
    };

    private View.OnClickListener mImagePickerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(isStoragePermissionAccepted || isCameraPermissionAccepted){
                selectImage();
            }else{
                Toast.makeText(getContext(),"please allow permissions in settings!", Toast.LENGTH_LONG).show();
            }
        }
    };

    private boolean check1 = false;
    private boolean check2 = false;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mProfessionSpinner.setOnItemSelectedListener(mItemSelectedListener);
        ArrayAdapter<CharSequence> spinnerDataAdapter = ArrayAdapter.createFromResource(getContext(), R.array.professions, android.R.layout.simple_spinner_dropdown_item);
        spinnerDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mProfessionSpinner.setAdapter(spinnerDataAdapter);
        User us = CurrentUser.getCurrentUser();
        mProfessionSpinner.setSelection(spinnerDataAdapter.getPosition(CurrentUser.getCurrentUser().getProfession().getName()));
        fillViews();

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isGood = saveUser();
                if(isGood){
                    if (isImagePicked){
                        mProgressBar.setVisibility(View.VISIBLE);

                        FirebaseRepository.updatePostsCreatorImage(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                                    Post post = postSnapshot.getValue(Post.class);
                                    if(post.getUserId().equals(CurrentUser.getCurrentUser().getPrimaryKey())){
                                        FirebaseRepository.updatePostUserImage(post.getPrimaryKey(), mInfo.getImageUri());
                                    }
                                }
                                //
                                check1 = true;
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        FirebaseRepository.getComments(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                                    Comment comment = postSnapshot.getValue(Comment.class);
                                    if(comment.getCreatorId().equals(CurrentUser.getCurrentUser().getPrimaryKey())){
                                        FirebaseRepository.updateCommentUserImage(comment.getPrimaryKey(), mInfo.getImageUri());
                                    }
                                }
                                check2 = true;
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                              if(check1 && check2){
                                  mProgressBar.setVisibility(View.GONE);
                                  quitFragment();
                                  return;
                              }
                              handler.postDelayed(this, 100);
                            }
                        }, 500);
                    }else{
                        quitFragment();
                    }
                }
            }
        });

        mBirthday.setOnClickListener(mDataPickerListener);
        mUserImage.setOnClickListener(mImagePickerListener);
        mSetToolBarAndNavigationBarState.setTitle(R.string.title_edit_profile);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEditedUser = CurrentUser.getCurrentUser();

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            isStoragePermissionAccepted = true;

        }
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            isCameraPermissionAccepted = true;
        }
    }

    private void fillViews(){
        User currentUser = CurrentUser.getCurrentUser();
        GlideUtil.setImageGlide(currentUser.getUserInfo().getImageUri(), mUserImage);
        mInfo.setImageUri(currentUser.getUserInfo().getImageUri());
        String email = "email: " + currentUser.getEmail();
        mEmailText.setText(email);
        mFullname.setText(currentUser.getFullName());
        mNickname.setText(currentUser.getNickName());
        mAdditionalInfo.setText(currentUser.getUserInfo().getAdditionalInfo());
        mBirthday.setText(currentUser.getBirthDay());
        if(currentUser.getGender()== Gender.MAN){
            mMaleRadioButton.setChecked(true);
        }else{
            mFemaleRadioButton.setChecked(true);
        }
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
                final Bitmap bmp = (Bitmap) bundle.get("data");
                mUserImage.setImageBitmap(bmp);
                putBitmapImageToStorage(bmp);
                isImagePicked = true;

            } else if (requestCode == SELECT_FILE) {

                final Uri selectedImageUri = data.getData();
                mUserImage.setImageURI(selectedImageUri);
                putImageToStorage(selectedImageUri);
                isImagePicked = true;

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
                        mInfo.setImageUri(uri.toString());

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
                        mInfo.setImageUri(uri.toString());
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

    private boolean saveUser(){
        mEditedUser.setFullName(mFullname.getText().toString());
        mEditedUser.setNickName(mNickname.getText().toString());
        mEditedUser.setBirthDay(mBirthday.getText().toString());
        mInfo.setAdditionalInfo(mAdditionalInfo.getText().toString());
        mEditedUser.setUserInfo(mInfo);
        mEditedUser.setGender(mMaleRadioButton.isChecked() ? Gender.MAN : Gender.WOMAN);
        if (setNewPassword()){
            //TODO save(update) user in firebase
            FirebaseRepository.updateCurrentUser(CurrentUser.getCurrentUser().getPrimaryKey(), mEditedUser);
            CurrentUser.setCurrentUser(mEditedUser);
            return true;
        }
        return false;
    }

    private void updatePasswordInFirebase(){
        final String newPass = mPassword.getText().toString();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String lastPass = mLastPassword.getText().toString();

        if ( lastPass.isEmpty()){
            Toast.makeText(getContext(), "Invalid password!", Toast.LENGTH_LONG).show();
            return;
        }

        //I don't have user's password so can't add credentials
        AuthCredential credential = EmailAuthProvider
                .getCredential(CurrentUser.getCurrentUser().getEmail(), lastPass);
//        user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()) {
//                    user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if (task.isSuccessful()) {
//                                Log.d("Update Pass", "Password updated");
//                            } else {
//                                Log.d("Update Pass", "Error password not updated");
//                            }
//                        }
//                    });
//                } else {
//                    Log.d("Update Pass", "Error auth failed");
//                }
//            }
//        });

        // Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword( HashUtils.hash(newPass)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("Update Pass", "Password updated");
                                    } else {
                                        Log.d("Update Pass", "Error password not updated");
                                    }
                                }
                            });
                        } else {
                            Log.d("Update Pass", "Error auth failed");
                            Toast.makeText(getContext(), "Invalid password!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void quitFragment(){
        ProfileFragment fragment = new ProfileFragment();
        fragment.setSetToolBarAndNavigationBarState(mSetToolBarAndNavigationBarState);
        getFragmentManager().beginTransaction()
                .replace(R.id.layout_activity_app_container, fragment)
                .commit();
        //getFragmentManager().popBackStack();
    }

    private boolean setNewPassword(){
        String newPass = mPassword.getText().toString();
        String newConfirmPass = mConfirmPassword.getText().toString();
        if((newPass == null || newPass.isEmpty()) && (newConfirmPass == null || newConfirmPass.isEmpty())){
            return true;
        }
        if (checkPassword()){
            mEditedUser.setPassword(HashUtils.hash(newPass));
            FirebaseRepository.updatePassword(CurrentUser.getCurrentUser().getPrimaryKey(), HashUtils.hash(newPass));
            updatePasswordInFirebase();
            return true;
        }
        return false;
    }
    private boolean checkPassword(){
        if (!CheckUtils.checkEqual(mPassword, mConfirmPassword)) {
            ErrorShowUtils.showEditTextError(mConfirmPassword, ContextUtils.getResourceString(this, R.string.password_match));
            return false;
        }
        return true;
    }

    public EditProfileFragment() {
    }
}
