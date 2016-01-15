package com.nikmc.kc.logic;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by NIKMC-I on 24.12.2015.
 */
public class CheckValid {

    public static boolean isEmptyValid (String login) {
        return TextUtils.isEmpty(login);
    }

    public static boolean isPhoneValid (String phone) {
        return phone.length() == 10;
    }
    public static boolean isPhoneValidProfile (String phone) {
        return phone.length() == 10 || TextUtils.isEmpty(phone);
    }

    public static boolean isValid(String fio) {
        Pattern p = Pattern.compile("\\p{Punct}");
        Matcher m = p.matcher(fio.trim());
            return m.find() || TextUtils.isEmpty(fio.trim());
    }

    public static boolean isFioValid(String fio) {
        if(fio.trim().contains(" "))
            return fio.trim().indexOf(" ") != fio.trim().lastIndexOf(" ");
        else
        return false;
    }
    public static String isEmptyFIOValid(String fio) {
        if(TextUtils.isEmpty(fio.trim()))
            return "Поле не может быть пустым";
        else {
            Pattern p = Pattern.compile("\\p{Punct}");

            Matcher m = p.matcher(fio.trim());
            if (m.find())
                return "Неверно введено ФИО";
        }
        return "Неверный ввод";
    }
    public static boolean isFIO_valid(String fio) {
        return fio.trim().indexOf(" ") != fio.trim().lastIndexOf(" ");
    }
    public static boolean isFI_valid(String fio) {
        return fio.trim().indexOf(" ") == fio.trim().lastIndexOf(" ");
    }
    public static boolean isEmailValid (String email) {
        return email.contains("@") && email.length()>8 ;
    }

    public static boolean isEmptyEmailValid (String email) {
        return TextUtils.isEmpty(email);
    }

}
