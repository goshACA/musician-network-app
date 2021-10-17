package com.musapp.musicapp.fragments.registration_fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.musapp.musicapp.R;
import com.musapp.musicapp.currentinformation.CurrentUser;
import com.musapp.musicapp.fragments.registration_fragments.registration_fragment_transaction.RegistrationTransactionWrapper;
import com.musapp.musicapp.model.User;
import com.musapp.musicapp.utils.CheckUtils;
import com.musapp.musicapp.utils.ContextUtils;
import com.musapp.musicapp.utils.ErrorShowUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegistrationFragment1 extends Fragment {
  @BindView(R.id.text_fragment_registration_1_fullname) EditText fullname;
  @BindView(R.id.text_fragment_registration_1_nickname) EditText nickname;
  @BindView(R.id.action_fragment_registration_1_next) Button nextButton;
  final String TAG = RegistrationFragment1.class.getSimpleName();
  private User user = CurrentUser.getCurrentUser();

  private View.OnClickListener nextClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      if(submitInformation()){
        RegistrationTransactionWrapper.registerForNextFragment((int)nextButton.getTag());
        CurrentUser.setCurrentUser(user);}
    }
  };

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view  = inflater.inflate(R.layout.fragment_registration_1, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    nextButton.setTag(R.integer.registration_fragment_1);
    nextButton.setOnClickListener(nextClickListener);
  }

  private boolean checkEditTextField(){
    if(CheckUtils.checkEditTextEmpty(fullname)){
      ErrorShowUtils.showEditTextError(fullname, ContextUtils.getResourceString(this, R.string.empty_error_message));
      return false;
    }
    else if(!CheckUtils.checkIsntOneWord(fullname)){
      ErrorShowUtils.showEditTextError(fullname, ContextUtils.getResourceString(this, R.string.fullname_one_word_error_message));
      return false;
    }
    if(CheckUtils.checkEditTextEmpty(nickname)){
      ErrorShowUtils.showEditTextError(nickname, ContextUtils.getResourceString(this, R.string.empty_error_message));
      return false;
    }
    return true;
  }

  private boolean submitInformation(){

    if(checkEditTextField()){
      user.setFullName(fullname.getText().toString());
      user.setNickName(nickname.getText().toString());
      return true;
    }
    return false;
  }


}