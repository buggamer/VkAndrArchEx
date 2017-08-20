package com.example.daim.vkandrarchex.content;

import java.util.List;


public class Photo {

    private Integer mId;

    private List<PhotoUrl> mPhotoUrls;

    private String mText;

    private Likes mLikes;

    private Reposts mReposts;


    public void setId(Integer mId) {
        this.mId = mId;
    }

    public void setPhotoUrls(List<PhotoUrl> mPhotoUrls) { this.mPhotoUrls = mPhotoUrls; }

    public void setText(String mText) { this.mText = mText; }

    public void setLikes(Likes mLikes) { this.mLikes = mLikes; }

    public void setReposts(Reposts mReposts) { this.mReposts = mReposts; }

    public Integer getId() {
        return mId;
    }

    public List<PhotoUrl> getPhotoUrls() {
        return mPhotoUrls;
    }

    public String getText() {
        return mText;
    }

    public Likes getLikes() {
        return mLikes;
    }

    public Reposts getReposts() {
        return mReposts;
    }

    public String getBigPhotoUrl() { return mPhotoUrls.get(mPhotoUrls.size() - 1).getUrl(); }

}
