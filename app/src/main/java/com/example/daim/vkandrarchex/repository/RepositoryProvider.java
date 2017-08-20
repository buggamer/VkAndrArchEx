package com.example.daim.vkandrarchex.repository;


import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

public class RepositoryProvider {
    private static VKRepository sVKRepository;

    private RepositoryProvider(){}

    @NonNull
    public static VKRepository provideVKRepository(){
        if(sVKRepository == null){
            sVKRepository = new DefaultVKRepository();
        }
        return sVKRepository;
    }

    @MainThread
    public static void init() { sVKRepository = new DefaultVKRepository(); }

}
