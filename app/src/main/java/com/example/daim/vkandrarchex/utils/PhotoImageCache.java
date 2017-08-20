package com.example.daim.vkandrarchex.utils;


import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class PhotoImageCache {
    private static final String LOG_TAG = "PhotoImageCache";

    private static final int SMALL_PHOTO_CACHE_SIZE = 30;
    private static final int BIG_PHOTO_CACHE_SIZE = 5;
    Map<String, Bitmap> mSmallPhotoCache;
    Map<String, Bitmap> mBigPhotoCache;

    private static volatile PhotoImageCache photoImageCache;

    private PhotoImageCache(){
        LinkedHashMap<String, Bitmap> smallPhotoHashMap = new LinkedHashMap<String, Bitmap>(SMALL_PHOTO_CACHE_SIZE, 0.75f, true){
            @Override
            protected boolean removeEldestEntry(Entry<String, Bitmap> eldest) {
                return size() > SMALL_PHOTO_CACHE_SIZE;
            }
        };

        LinkedHashMap<String, Bitmap> bigPhotoHashMap = new LinkedHashMap<String, Bitmap>(BIG_PHOTO_CACHE_SIZE, 0.75f, true){
            @Override
            protected boolean removeEldestEntry(Entry<String, Bitmap> eldest) {
                return size() > BIG_PHOTO_CACHE_SIZE;
            }
        };
        mSmallPhotoCache = Collections.synchronizedMap(smallPhotoHashMap);
        mBigPhotoCache = Collections.synchronizedMap(bigPhotoHashMap);
    }

    public static PhotoImageCache getPhotoCache(){
        PhotoImageCache localInstance = photoImageCache;
        if(localInstance == null){
            synchronized (PhotoImageCache.class){
                localInstance = photoImageCache;
                if(photoImageCache == null){
                    photoImageCache = localInstance = new PhotoImageCache();
                }
            }
        }
        return localInstance;
    }

    synchronized public Bitmap getPhoto(String url){
        Bitmap result = getSmallPhoto(url);
        if(result == null) result = getBigPhoto(url);
        return result;
    }

    synchronized public void putPhoto(@NonNull String url,@NonNull Bitmap bitmap){
        if(bitmap.getAllocationByteCount() > 2000000){
            putBigPhoto(url, bitmap);
        }
        else{
            putSmallPhoto(url, bitmap);;
        }
    }

    private void putSmallPhoto(String url, Bitmap bitmap){
        mSmallPhotoCache.put(url, bitmap);
    }

    private Bitmap getSmallPhoto(String url){
        Bitmap result = mSmallPhotoCache.get(url);
        return result;
    }

    private void putBigPhoto(String url, Bitmap bitmap){
        mBigPhotoCache.put(url, bitmap);
    }

    private Bitmap getBigPhoto(String url){
        Bitmap result = mBigPhotoCache.get(url);
        return result;
    }
}
