package com.musapp.musicapp.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.musapp.musicapp.R;
import com.musapp.musicapp.utils.CheckUtils;
import com.musapp.musicapp.utils.ErrorShowUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForgotPassDialog extends DialogFragment {
  @BindView(R.id.text_dialog_forgot_pass_email)
  EditText email;
  @BindView(R.id.text_dialog_forgot_pass_code) EditText code;
  @BindView(R.id.action_dialog_forgot_pass_confirm)
  Button confirmCode;
  @BindView(R.id.action_dialog_forgot_pass_sent_again) Button sentAgain;





  private View.OnClickListener confirmClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      if(checkFields()){
        //TODO go to settings' password field and type new one

        dismiss();

      }
    }
  };

  private View.OnClickListener sentAgainClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      //TODO sent new code to user
    }
  };

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_fogrot_pass, new LinearLayout(getActivity()), false);

    // Build dialog
    Dialog builder = new Dialog(getActivity());
    builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
    builder.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.lightBlue)));
    builder.setContentView(view);
    ButterKnife.bind(this, view);
    confirmCode.setOnClickListener(confirmClickListener);
    sentAgain.setOnClickListener(sentAgainClickListener);
    return builder;

  }

  private boolean checkFields(){

    if(CheckUtils.checkEditTextEmpty(email)){
      ErrorShowUtils.showEditTextError(email, getString(R.string.empty_error_message));
      return false;
    }
    else if(!CheckUtils.checkIsMail(email)){
      ErrorShowUtils.showEditTextError(email, getString(R.string.wrong_email_message) );
      return false;
    }
    else if(CheckUtils.checkEditTextEmpty(code)){
      ErrorShowUtils.showEditTextError(email, getString(R.string.empty_error_message));
      return false;
    }
    else{
      //TODO check if email matches with db emails and code matches with send one
    }

    return true;
  }

}