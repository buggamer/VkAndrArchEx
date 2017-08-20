package com.example.daim.vkandrarchex;


import android.app.Application;
import android.support.annotation.NonNull;

import com.example.daim.vkandrarchex.repository.RepositoryProvider;
import com.example.daim.vkandrarchex.repository.SinglePhotoCache;

public class AppDelegate extends Application {

    private static AppDelegate sInstance;

    @Override
    public void onCreate(){
        super.onCreate();
        sInstance = this;

        RepositoryProvider.init();
    }

    @NonNull
    public static AppDelegate getAppContext() { return sInstance; }

}
