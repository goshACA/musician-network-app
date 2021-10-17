package com.musapp.musicapp.utils;

import android.widget.EditText;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final  class CheckUtils {
  private CheckUtils(){}
  private static final String[] mails = {"@gmail.com","@inbox.ru","@list.ru","@bk.ru", "mail.ru"};
  private static final List<String> extension = new ArrayList<>(
          Arrays.asList("jpg", "png", "gif", "jpeg", "tiff", "esp", "ai", "raw", "psd", "pdf", "indd", "tif", "bmp",
                  "ppm","pgm", "pbm", "pnm", "heif", "bat", "bpg", "svg"));

  public static boolean checkImageExtension(String ex){
    return extension.contains(ex);
  }

  public static boolean checkEditTextEmpty(EditText editText){
    String checkableString =  editText.getText().toString();
    return checkableString.isEmpty() || !(checkableString.trim().length() > 0);
  }
  public static boolean checkIsntOneWord(EditText editText){
    int count = 0;
    for(String str: editText.getText().toString().split(" ")){
      if(str.trim().length() > 0)
        ++count;
      if(count > 2)
        return false;
    }
    return count == 2;
  }
  public static boolean checkIsMail(EditText editText){
    String checkableString = editText.getText().toString();
    boolean contains  = false;
    for(String str:mails){
      if(checkableString.endsWith(str)) {
        contains = true;
        break;
      }
    }
    //TODO check if contains wrong characters
    return contains && StringUtils.countMatches(checkableString, '@') == 1;

  }
  public static boolean checkEqual(EditText editText1, EditText editText2){
    return editText1.getText().toString().equals(editText2.getText().toString());
  }

  public static boolean isValidPassword(final String password) {

    Pattern pattern;
    Matcher matcher;
    final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
    pattern = Pattern.compile(PASSWORD_PATTERN);
    matcher = pattern.matcher(password);

    return matcher.matches();

  }
}