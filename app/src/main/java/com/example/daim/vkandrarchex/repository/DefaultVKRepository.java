package com.example.daim.vkandrarchex.repository;


import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.daim.vkandrarchex.api.VkPhotoFetchr;
import com.example.daim.vkandrarchex.content.Photo;

import java.util.List;

public class DefaultVKRepository implements VKRepository {

    private static final String LOG_TAG = "DefaultVKRepository";

    private int mItemCount;
    private int mItemStep = 50;
    private boolean mIsAll = false;



    public DefaultVKRepository(){
        SinglePhotoCache.init();
    }

    @Override
    public LiveData<List<Photo>> getPhotos() {
        return SinglePhotoCache.getPhotos();
    }

    @Override
    public boolean loadPhoto() {
        if(mIsAll) return !mIsAll;
        new FethcPhotosTask().execute(mItemCount, mItemStep);
        return !mIsAll;
    }

    @Override
    public int getPhotosQuantity() {
        return SinglePhotoCache.getPhotoQuantity();
    }

    private class FethcPhotosTask extends AsyncTask<Integer, Void, List<Photo>> {

        @Override
        protected List<Photo> doInBackground(Integer... params) {
            return VkPhotoFetchr.fetchPhotos(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(List<Photo> photos) {
            if(photos.size() > 0){
                if(photos.size() < mItemStep) mIsAll = true;
                mItemCount += photos.size();
                SinglePhotoCache.setPhotos(photos);
            }
        }
    }
}
