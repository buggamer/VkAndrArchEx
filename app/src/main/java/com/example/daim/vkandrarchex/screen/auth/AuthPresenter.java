package com.example.daim.vkandrarchex.screen.auth;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.example.daim.vkandrarchex.utils.AuthorizationUtils;
import com.example.daim.vkandrarchex.utils.PreferenceUtils;


/**
 * Created by DAIM on 28.07.2017.
 */

public class AuthPresenter {

    private final String LOG_TAG = "AuthPresenter";

    private AuthView mAuthView;

    public AuthPresenter(@NonNull AuthView authView){ mAuthView = authView; }

    public void init() {
        String token = PreferenceUtils.getToken();
        Long expiresIn = PreferenceUtils.getExpiresIn();
        Log.d(LOG_TAG, "token: " + token);
        Log.d(LOG_TAG, "cur_time: " + System.currentTimeMillis() + ", expires_time: " + expiresIn);
        mAuthView.loadAuthWebView(AuthorizationUtils.getAuthUrl());
        if (!TextUtils.isEmpty(token) && (System.currentTimeMillis() < PreferenceUtils.getExpiresIn())) {
            Log.d(LOG_TAG, "true");
            mAuthView.openGalleryScreen();
        }else{
            Log.d(LOG_TAG, "false");
            mAuthView.loadAuthWebView(AuthorizationUtils.getAuthUrl());
        }
    }

    public void saveParseToken(String url){
       Log.d(LOG_TAG, "url: " + url);
       boolean gotToken =  AuthorizationUtils.parseAndSaveToken(url);
       if(gotToken) init();
    }
}
