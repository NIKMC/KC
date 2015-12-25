package com.nikmc.kc.logic;

import android.text.TextUtils;

/**
 * Created by NIKMC-I on 24.12.2015.
 */
public class CheckValid {

    public static boolean isEmptyValid (String login) {
        return TextUtils.isEmpty(login);
    }

    public static boolean isPhoneValid (String password) {
        return password.length() == 12;
    }

    public static boolean isEmailValid (String email) {
        return email.contains("@") && email.length()>8 ;
    }

    public static boolean isEmptyEmailValid (String email) {
        return TextUtils.isEmpty(email);
    }

}
