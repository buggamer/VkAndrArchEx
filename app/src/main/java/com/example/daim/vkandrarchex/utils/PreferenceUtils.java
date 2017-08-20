package com.example.daim.vkandrarchex.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.example.daim.vkandrarchex.AppDelegate;

public class PreferenceUtils {


    private static final String AUTH_PREFERENCE = "auth_preference";
    private static final String TOKEN_KEY = "access_token";
    private static final String USER_ID = "user_id";
    private static final String EXPIRES_IN = "expires_in";

    private PreferenceUtils(){}

    private static SharedPreferences.Editor getSharedPrefEditor(){
       return AppDelegate.getAppContext().getSharedPreferences(AUTH_PREFERENCE,Context.MODE_PRIVATE)
                .edit();
    }

    private static SharedPreferences getSharedPrefernce(){
        return AppDelegate.getAppContext().getSharedPreferences(AUTH_PREFERENCE, Context.MODE_PRIVATE);
    }

    public static void saveToken(@NonNull String token){
        getSharedPrefEditor().putString(TOKEN_KEY, token).apply();
    }

    public static void saveUserId(@NonNull String userId){
        getSharedPrefEditor().putString(USER_ID, userId).apply();
    }

    public static void saveExpiresIn(@NonNull long expiresIn){
        getSharedPrefEditor().putLong(EXPIRES_IN, expiresIn).apply();
    }

    public static String getToken(){
        return getSharedPrefernce().getString(TOKEN_KEY, "");
    }

    public static String getUserId(){
        return getSharedPrefernce().getString(USER_ID, "");
    }

    public static long getExpiresIn(){
        return getSharedPrefernce().getLong(EXPIRES_IN, 0);
    }

}
