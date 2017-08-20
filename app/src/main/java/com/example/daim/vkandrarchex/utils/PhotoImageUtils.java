package com.example.daim.vkandrarchex.utils;

import android.widget.ImageView;

import com.example.daim.vkandrarchex.content.Photo;
import com.example.daim.vkandrarchex.content.PhotoUrl;

import java.util.List;

/**
 * Created by DAIM on 13.08.2017.
 */

public class PhotoImageUtils {
    private final static int XSIZE = 604;
    private final static int YSIZE = 807;

    public static String getGalleryScreenPhotoUrl(Photo photo, int width, int height) {
        int maxSize = Math.max(width, height);
        List<PhotoUrl> photoUrls = photo.getPhotoUrls();
        if(maxSize < YSIZE){
            for(int i = 0; i < photoUrls.size(); i++){
                if(photoUrls.get(i).getType().equals("x"))
                    return photoUrls.get(i).getUrl();
            }
        }else{
            for(int j = 0; j < photoUrls.size(); j++){
                if(photoUrls.get(j).getType().equals("y"))
                    return photoUrls.get(j).getUrl();
            }
        }
        return photoUrls.get(photoUrls.size() - 1).getUrl();
    }

}
