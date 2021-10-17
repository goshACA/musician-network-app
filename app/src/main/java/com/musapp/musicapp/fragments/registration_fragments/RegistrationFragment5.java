package com.musapp.musicapp.fragments.registration_fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.musapp.musicapp.R;
import com.musapp.musicapp.currentinformation.CurrentUser;
import com.musapp.musicapp.firebase.DBAccess;
import com.musapp.musicapp.firebase_repository.FirebaseAuthRepository;
import com.musapp.musicapp.firebase_repository.FirebaseRepository;
import com.musapp.musicapp.fragments.registration_fragments.registration_fragment_transaction.RegistrationTransactionWrapper;
import com.musapp.musicapp.model.User;
import com.musapp.musicapp.preferences.RegisterPreferences;
import com.musapp.musicapp.utils.CheckUtils;
import com.musapp.musicapp.utils.ContextUtils;
import com.musapp.musicapp.utils.ErrorShowUtils;
import com.musapp.musicapp.utils.HashUtils;

import java.util.HashMap;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegistrationFragment5 extends Fragment {
    @BindView(R.id.text_fragment_registration_5_email)
    EditText email;
    @BindView(R.id.text_fragment_registration_5_password)
    EditText password;
    @BindView(R.id.text_fragment_registration_5_confirm_pass)
    EditText confirm_password;
    @BindView(R.id.action_fragment_registration_5_next)
    Button nextButton;

    private User user = CurrentUser.getCurrentUser();
    private View.OnClickListener nextClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (submitInformation()) {
                RegistrationTransactionWrapper.registerForNextFragment((int) nextButton.getTag());
                RegisterPreferences.saveState(getActivity().getBaseContext(), true);
                CurrentUser.setCurrentUser(user);
                fireBaseAuth();
            }
        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration_5, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nextButton.setTag(R.integer.registration_fragment_5);
        nextButton.setOnClickListener(nextClickListener);
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
        if (!CheckUtils.checkEqual(password, confirm_password)) {
            ErrorShowUtils.showEditTextError(confirm_password, ContextUtils.getResourceString(this, R.string.password_match));
            return false;
        }
        if(password.getText().toString().length() < 6){
            ErrorShowUtils.showEditTextError(password, ContextUtils.getResourceString(this, R.string.short_password_error));
            return false;
        }
        if (!CheckUtils.isValidPassword(password.getText().toString())){
            ErrorShowUtils.showEditTextError(password, ContextUtils.getResourceString(this, R.string.invalid_password_error));
            return false;
        }
        return true;
    }

    private boolean submitInformation() {

        if (checkEditTextField()) {
            user.setEmail(email.getText().toString());
            //TODO does password need to be saved locally?
            user.setPassword(HashUtils.hash(password.getText().toString()));
            return true;
        }
        return false;
    }


    private void addUserToFirebase() {
        CurrentUser.getCurrentUser().setToken(DBAccess.getToken());
        FirebaseRepository.addCurrentUserToFirebase(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseRepository.setUserInnerPrimaryKey(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Iterator<DataSnapshot> lastChild = dataSnapshot.getChildren().iterator();
                        CurrentUser.getCurrentUser().setPrimaryKey(lastChild.next().getKey());
                        FirebaseRepository.setUserInnerPrimaryKeyToFirebase();
                        FirebaseRepository.setUserHashedPassword(HashUtils.hash(password.getText().toString()));
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

    }

    private void fireBaseAuth(){
        FirebaseAuthRepository.createUserWithEmailAndPassword(email.getText().toString(), HashUtils.hash(password.getText().toString()), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    addUserToFirebase();
                }
                else{
                    Toast.makeText(getActivity(), "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
