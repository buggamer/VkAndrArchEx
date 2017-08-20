package com.example.daim.vkandrarchex.screen.gallery;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.daim.vkandrarchex.AppDelegate;
import com.example.daim.vkandrarchex.content.Photo;
import com.example.daim.vkandrarchex.widget.BaseAdapter;


public class GalleryAdapter extends BaseAdapter<PhotoHolder, Photo> {

    private final String LOG_TAG = "GalleryAdapter";

    private final int mImageWidth;
    private final int mImageHeight;
    private final int mPaginationStep = 20;

    private GalleryAdapterListener mGalleryAdapterListener;

    public GalleryAdapter(int imageHeight, int imageWidth){
        mImageWidth = imageWidth;
        mImageHeight = imageHeight;
    }

    @Override
    public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return PhotoHolder.create(AppDelegate.getAppContext(), mImageHeight, mImageWidth);
    }

    @Override
    public void onBindViewHolder(PhotoHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        Log.d(LOG_TAG, "onBindViewHolder(): " + position);
        Photo photo = getItem(position);
        String url = holder.getPhotoUrl(photo);
        mGalleryAdapterListener.photoImageRequest(holder.mImageView, url);
        holder.bind(photo, position);
        if((getItemCount() - position) == mPaginationStep) mGalleryAdapterListener.paginationRequest();
    }

    public void setOnPaginationRequest(GalleryAdapterListener listener){
        mGalleryAdapterListener = listener;
    }

    public interface GalleryAdapterListener {
        public void paginationRequest();
        public void photoImageRequest(@NonNull ImageView imageView, @NonNull String url);
    }

}
