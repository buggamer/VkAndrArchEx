package com.example.daim.vkandrarchex.content;

import java.util.List;

public class Response {

    private int mPhotoQuantity;
    private List<Photo> mPhotos;

    public int getPhotoQuantity() { return mPhotoQuantity; }
    public List<Photo> getPhotos() { return mPhotos; }

    public void setPhotoQuantity(int photoQuantity) { mPhotoQuantity = photoQuantity; }
    public void setPhotos(List<Photo> photos) { mPhotos = photos; }
}
