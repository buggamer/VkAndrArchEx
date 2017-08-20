package com.example.daim.vkandrarchex.utils;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import com.example.daim.vkandrarchex.AppDelegate;
import com.example.daim.vkandrarchex.api.VkPhotoFetchr;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class PhotoImageDownloader<T> extends HandlerThread {
    private static final String LOG_TAG = "PhotoImageDownloader";
    private static final int MESSAGE_DOWNLOAD = 0;


    PhotoImageCache cache;
    private Handler requestHandler;
    private ConcurrentMap<T, String> requestMap = new ConcurrentHashMap<>();
    private Handler responseHandler;

    public PhotoImageDownloader() {
        super(LOG_TAG);
        responseHandler = new Handler(Looper.getMainLooper());
        cache = PhotoImageCache.getPhotoCache();
    }

    @Override
    protected void onLooperPrepared() {
        requestHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == MESSAGE_DOWNLOAD){
                    T target = (T) msg.obj;
                    handleRequest(target);
                }
            }
        };
    }

    public void loadPhotoImage(T target, String url){
        if(url == null){
            requestMap.remove(target);
        }else{
            Bitmap bitmap = cache.getPhoto(url);
            if(bitmap != null) {
                ImageView targetView = (ImageView) target;
                Drawable drawable = new BitmapDrawable(AppDelegate.getAppContext().getResources(), bitmap);
                targetView.setImageDrawable(drawable);
                return;
            }
            requestMap.put(target, url);
            requestHandler.obtainMessage(MESSAGE_DOWNLOAD, target)
                    .sendToTarget();
        }
    }

    public void clearQueue(){
        requestHandler.removeMessages(MESSAGE_DOWNLOAD);
    }

    private void handleRequest(final T target) {
        final String url = requestMap.get(target);
        ImageView targetView = (ImageView)target;
        if (url == null) {
            return;
        }
        final Bitmap bitmap = getBitmap(url);

        responseHandler.post(new Runnable(){
            public void run(){
                if(requestMap.get(target) != url){
                    return;
                }
                requestMap.remove(target);
                Drawable drawable = new BitmapDrawable(AppDelegate.getAppContext().getResources(), bitmap);
                targetView.setImageDrawable(drawable);
            }
        });
    }

    private Bitmap getBitmap(String url){
        try{
/*
            if(cache.getPhoto(url) == null){
            }
*/
            byte[] bitmapBytes = VkPhotoFetchr.getUrlBytes(url);
            final Bitmap bitmap = BitmapFactory
                    .decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
            cache.putPhoto(url,bitmap);

        }catch (IOException ioe){
            Log.e(LOG_TAG, "Error downloading image", ioe);
        }
        return cache.getPhoto(url);
    }
}
