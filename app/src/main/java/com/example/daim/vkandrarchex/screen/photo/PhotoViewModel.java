package com.example.daim.vkandrarchex.screen.photo;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.widget.ImageView;

import com.example.daim.vkandrarchex.content.Photo;
import com.example.daim.vkandrarchex.repository.RepositoryProvider;
import com.example.daim.vkandrarchex.utils.PhotoImageDownloader;

import java.util.List;

public class PhotoViewModel extends ViewModel{
    private LiveData<List<Photo>> mPhotos;
    private MediatorLiveData<Boolean> mIsLoadLiveData;
    private PhotoImageDownloader<ImageView> mPhotoImageDownloader;
    private int mPhotoQuantity;

    public void init(){
        mPhotos = RepositoryProvider.provideVKRepository().getPhotos();
        mPhotoQuantity = RepositoryProvider.provideVKRepository().getPhotosQuantity();
        mIsLoadLiveData = new MediatorLiveData<>();
        mIsLoadLiveData.addSource(mPhotos,(val)->{
            mIsLoadLiveData.setValue(false);
        });
        if (mPhotoImageDownloader == null){
            mPhotoImageDownloader = new PhotoImageDownloader<>();
            mPhotoImageDownloader.start();
            mPhotoImageDownloader.getLooper();
        }
        else mPhotoImageDownloader.clearQueue();
    }

    @Override
    protected void onCleared() {
        mPhotoImageDownloader.quit();
    }

    public LiveData<List<Photo>> getPhotos(){
        return mPhotos;
    }

    public LiveData<Boolean> getIsLoading(){ return mIsLoadLiveData; }

    public int getPhotoQuantity() { return mPhotoQuantity; }

    public void loadPhotos(){
        if(RepositoryProvider.provideVKRepository().loadPhoto())
            mIsLoadLiveData.setValue(true);
    }

    public void loadPhotoImage(ImageView view, String url){
        mPhotoImageDownloader.loadPhotoImage(view, url);
    }


}
