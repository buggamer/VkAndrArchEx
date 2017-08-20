package com.example.daim.vkandrarchex.utils;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.daim.vkandrarchex.BuildConfig;


/**
 * Created by DAIM on 22.07.2017.
 */

public class AuthorizationUtils {

    private static final String LOG_TAG = "AuthorizationUtils";

    private static final String REDIRECT_URI = "https://oauth.vk.com/blank.html";
    private static final String REDIRECT_URI_REGEX = "oauth.vk.com/blank.html";
    //private static final String REDIRECT_URI = "http://api.vkontakte.ru/blank.html";
    //private static final String REDIRECT_URI_REGEX = "api.vkontakte.ru/blank.html";
    private static final String SCOPE = "photos";
    private static final String DISPLAY = "mobile";
    private static final String RESPONSE_TYPE = "token";
    private static final String VERSION_API = "5.67";

    private static final String TOKEN_KEY = "access_token";
    private static final String USER_ID = "user_id";
    private static final String EXPIRES_IN = "expires_in";

    private AuthorizationUtils(){}

    @NonNull
    public static String getAuthUrl(){
        String url = Uri.parse(BuildConfig.AUTHOR_ENDPOINT)
                .buildUpon()
                .appendQueryParameter("client_id", BuildConfig.APP_ID)
                .appendQueryParameter("scope", SCOPE)
                .appendQueryParameter("redirect_uri", REDIRECT_URI)
                .appendQueryParameter("display", DISPLAY)
                .appendQueryParameter("response_type", RESPONSE_TYPE)
                .appendQueryParameter("v", VERSION_API)
                .appendQueryParameter("revoke", "1")
                .build().toString();
      return url;
    }

    public static boolean parseAndSaveToken(String url){
    //    Log.d(LOG_TAG,"parse_url: " + url);
        if(url.contains(REDIRECT_URI_REGEX) && url.contains(TOKEN_KEY) && !url.contains("error")){
            Log.d(LOG_TAG, url);
            String[] query = url.split("#");
            String[] params = query[1].split("&");
            for (String s : params){
                String[] value = s.split("=");
                if(value[0].equals(TOKEN_KEY))
                    PreferenceUtils.saveToken(value[1]);
                else if(value[0].equals(USER_ID))
                    PreferenceUtils.saveUserId(value[1]);
                else if(value[0].equals(EXPIRES_IN)){
                    long expiresTime = System.currentTimeMillis() + (Long.parseLong(value[1])*1000);
                    PreferenceUtils.saveExpiresIn(expiresTime);
                }
            }
            return true;
        }
        return false;
    }


}
