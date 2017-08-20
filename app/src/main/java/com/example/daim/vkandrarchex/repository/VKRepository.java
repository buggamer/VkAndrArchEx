package com.example.daim.vkandrarchex.repository;


import android.arch.lifecycle.LiveData;

import com.example.daim.vkandrarchex.content.Photo;

import java.util.List;



public interface VKRepository {

    public LiveData<List<Photo>>  getPhotos();

    /**
     * @return false if photoList already in cache, true if not
     */
    public boolean loadPhoto();

    public int getPhotosQuantity();

}
