package com.musapp.musicapp.fragments.sign_in_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

/*import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;*/
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.musapp.musicapp.R;
import com.musapp.musicapp.currentinformation.CurrentUser;
import com.musapp.musicapp.dialogs.ForgotPassDialog;
import com.musapp.musicapp.firebase.DBAccess;
import com.musapp.musicapp.firebase.DBAsyncTask;
import com.musapp.musicapp.firebase.DBAsyncTaskResponse;
import com.musapp.musicapp.firebase_repository.FirebaseAuthRepository;
import com.musapp.musicapp.firebase_repository.FirebaseRepository;
import com.musapp.musicapp.fragments.registration_fragments.registration_fragment_transaction.RegistrationTransactionWrapper;
import com.musapp.musicapp.model.User;
import com.musapp.musicapp.preferences.AppPreferences;
import com.musapp.musicapp.preferences.RegisterPreferences;
import com.musapp.musicapp.preferences.RememberPreferences;
import com.musapp.musicapp.utils.CheckUtils;
import com.musapp.musicapp.utils.ContextUtils;
import com.musapp.musicapp.utils.ErrorShowUtils;
import com.musapp.musicapp.utils.HashUtils;

import java.util.Arrays;

import javax.xml.datatype.Duration;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignInFragment extends Fragment {

    @BindView(R.id.text_fragment_sign_in_email)
    EditText email;
    @BindView(R.id.text_fragment_sign_in_password)
    EditText password;
    @BindView(R.id.action_fragment_sign_in_sign_in)
    Button signIn;
    @BindView(R.id.action_fragment_sign_in_sign_up)
    Button signUp;
    @BindView(R.id.action_fragment_sign_in_forgot_pass)
    Button forgotPass;
    @BindView(R.id.check_fragment_sign_in_remember)
    CheckBox remembered;
    @BindView(R.id.action_fragment_sign_in_facebook)
    Button facebookSign;
    @BindView(R.id.action_fragment_sign_in_twitter)
    Button twitterSign;
    @BindView(R.id.action_fragment_sign_in_google_plus)
    Button googleSign;
   // @BindView(R.id.login_button)
    //LoginButton facebookLogin;

    private boolean isClicked;
    private FirebaseAuth mAuth;
   // CallbackManager mCallbackManager;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    private View.OnClickListener onSignInClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (checkEnteredInformation()) {


                   signInFirebaseAuth();
                 //else {

               // }

            }

        }
    };

    private View.OnClickListener onSignUpClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RegistrationTransactionWrapper.registerForNextFragment((int) signUp.getTag());
            RegisterPreferences.saveState(getActivity().getBaseContext(), false);
        }
    };

    private View.OnClickListener onForgotPassClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new ForgotPassDialog().show(getFragmentManager(), "dialog");
        }
    };

    private View.OnClickListener onSocialButtonsClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO get information from api
        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppPreferences.clearGenreState(getContext());
    }

    @Override
    public void onPause() {
        super.onPause();
        email.clearComposingText();
        password.clearComposingText();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        signIn.setTag(R.integer.sign_in_fragment_main_page);

        signIn.setOnClickListener(onSignInClickListener);
        signUp.setTag(R.integer.sign_in_fragment_registration);
        signUp.setOnClickListener(onSignUpClickListener);
        forgotPass.setTag(R.integer.sign_in_fragment_forgot_pass);
        forgotPass.setOnClickListener(onForgotPassClickListener);
        facebookSign.setOnClickListener(onSocialButtonsClickListener);
        twitterSign.setOnClickListener(onSocialButtonsClickListener);
        googleSign.setOnClickListener(onSocialButtonsClickListener);

        mAuth = FirebaseAuth.getInstance();

      /*  mCallbackManager = CallbackManager.Factory.create();
        facebookLogin.setReadPermissions(Arrays.asList("email","public_profile"));

        facebookLogin.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
            }
        });


        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //goMainScreen();
                    RegistrationTransactionWrapper.registerForNextFragment((int) signUp.getTag());
                    RegisterPreferences.saveState(getActivity().getBaseContext(), false);
                }
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
   //     mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

/*    private void handleFacebookAccessToken(AccessToken token) {

        final AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        FirebaseAuthRepository.signInWithCredential(credential);
                        FirebaseUser user = mAuth.getCurrentUser();


                    }
                });*/

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private boolean checkEditTextField() {
        if (CheckUtils.checkEditTextEmpty(email)) {
            ErrorShowUtils.showEditTextError(email, ContextUtils.getResourceString(this, R.string.empty_error_message));
            return false;
        }
        if (!CheckUtils.checkIsMail(email)) {
            ErrorShowUtils.showEditTextError(email, ContextUtils.getResourceString(this, R.string.wrong_email_message));
            return false;
        }
        if (CheckUtils.checkEditTextEmpty(password)) {
            ErrorShowUtils.showEditTextError(password, ContextUtils.getResourceString(this, R.string.empty_error_message));
            return false;
        }

        return true;
    }


    private boolean checkEnteredInformation() {
        return checkEditTextField();
    }


    private void signInFirebaseAuth(){
        FirebaseAuthRepository.signInWithEmailAndPassword(email.getText().toString(), HashUtils.hash(password.getText().toString()), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    CurrentUser.setCurrentFirebaseUser(DBAccess.getFirebaseAuth().getCurrentUser());
                    getUserByEmailPassword(email.getText().toString(), HashUtils.hash(password.getText().toString()));
                }
                else{
                    Toast.makeText(getActivity(), "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getUserByEmailPassword(final String email, final String password) {
        FirebaseRepository.getUser(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean isFound = false;
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    if (email.equals(childSnapshot.child("email").getValue()) && password.equals(childSnapshot.child("password").getValue())) {
                        CurrentUser.setCurrentUser(childSnapshot.getValue(User.class));
                        CurrentUser.getCurrentUser().setToken(FirebaseInstanceId.getInstance().getToken());
                        FirebaseRepository.updateCurrentUserToken(CurrentUser.getCurrentUser().getToken());
                  //       Toast.makeText(getActivity().getBaseContext(), CurrentUser.getCurrentUser().toString(), Toast.LENGTH_LONG).showToolBar();
                        isFound = true;
                        break;
                    }
                }
                if (isFound){
                    Log.d("emm", "yee");
                    if(getActivity() != null && getActivity().getBaseContext() != null){
                    RememberPreferences.saveState(getActivity().getBaseContext(), !remembered.isChecked());
                    RememberPreferences.saveUser(getActivity().getBaseContext(), CurrentUser.getCurrentUser().getPrimaryKey());
                    RegistrationTransactionWrapper.registerForNextFragment((int) signIn.getTag());}
                }

                else {
                    Log.d("emm", "emmm");
                    Toast.makeText(getActivity().getBaseContext(), "Email or password is wrong", Toast.LENGTH_LONG).show();



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }
}
