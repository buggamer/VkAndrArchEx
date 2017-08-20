package com.example.daim.vkandrarchex.screen.photo;


import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.example.daim.vkandrarchex.content.Photo;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;
import java.util.List;

public class PhotoPagerAdapter extends PagerAdapter {

    public final static String LOG_TAG = "PhotoPagerAdapter";
    private final int mPaginationStep = 20;

    private final List<Photo> mPhotoList;
    private PhotoPagerAdapterListener mListener;

    public PhotoPagerAdapter(List<Photo> photos){ mPhotoList = photos; }

    public void attachClickViewListener(PhotoPagerAdapterListener listener){
        mListener = listener;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(container.getContext());
        mListener.loadPhoto(photoView, mPhotoList.get(position).getBigPhotoUrl());
        photoView.setOnClickListener((View v)->{mListener.onClickView();});
        Log.d(LOG_TAG, "loading url: " + mPhotoList.get(position).getBigPhotoUrl() + "\n"
                + "position: " + position + "; list.size: " + mPhotoList.size());
        container.addView(photoView);
        if((getCount() - position) == mPaginationStep) mListener.paginationRequest();
        return photoView;
    }

    @Override
    public int getCount() {
        return mPhotoList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public interface PhotoPagerAdapterListener {
        public void onClickView();
        public void loadPhoto(@NonNull ImageView target,@NonNull String url);
        public void paginationRequest();
    }
}
