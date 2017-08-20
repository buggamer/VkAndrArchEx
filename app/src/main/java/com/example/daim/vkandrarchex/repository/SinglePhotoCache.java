package com.example.daim.vkandrarchex.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import com.example.daim.vkandrarchex.content.Photo;

import java.util.ArrayList;
import java.util.List;

public class SinglePhotoCache {

    private static MutableLiveData<List<Photo>> mPhotosCache;

    private static int mPhotoCountity;

    private SinglePhotoCache(){}

    @MainThread
    public static void init() {
        mPhotosCache = new MutableLiveData<>();
        mPhotosCache.setValue(new ArrayList());
    }

    public static void setPhotos(@NonNull List<Photo> photos) {
        List<Photo> cacheList = mPhotosCache.getValue();
        cacheList.addAll(photos);
        mPhotosCache.setValue(cacheList);
    }


    public static LiveData<List<Photo>> getPhotos() { return mPhotosCache; }

    public static void setPhotoQuantity(int photoCountity) { mPhotoCountity = photoCountity; }

    public static int getPhotoQuantity() { return mPhotoCountity; }

}
