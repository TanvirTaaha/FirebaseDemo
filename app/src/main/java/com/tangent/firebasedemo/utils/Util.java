package com.tangent.firebasedemo.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import timber.log.Timber;

public class Util {

    //Database related items
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "contact_db";
    public static final String TABLE_NAME = "contacts";

    //Contacts table column names
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_PHONE_NUMBER = "phone_number";

    //FirebaseDatabase constants
    public static final String BRANCH_MESSAGES = "messages";
    public static final String BRANCH_USERS = "users";


    public static boolean isNotEmpty(CharSequence s) {
        return !TextUtils.isEmpty(s);
    }

    public static void toggleKeyboard(EditText editText) {
        editText.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        Timber.i("toggle keyboard called for:%s id:%d", editText.toString(), editText.getId());
    }

    public static void showKeyboard(EditText editText) {
        editText.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
        Timber.i("show keyboard called for:%s id:%d", editText.toString(), editText.getId());
    }

    public static void hideKeyboard(EditText editText) {
        editText.clearFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        Timber.i("hide keyboard called for:%s id:%d", editText.toString(), editText.getId());
    }

    /**
     * https://stackoverflow.com/q/20812922/8928251
     *
     * @param fragment the fragment to close
     */
    public static void closeFragment(Fragment fragment) {
        if (fragment.getActivity() != null) {
            fragment.getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }
}
