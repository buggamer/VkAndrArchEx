package com.example.daim.vkandrarchex.content;


public class PhotoUrl {

    private String mUrl;
    private int mWidth;
    private int mHeight;
    private String mType;

    public void setUrl(String url) { mUrl = url; }
    public void setWidth(int width) { mWidth = width; }
    public void setHeight(int height) { mHeight = height; }
    public void setType(String type) { mType = type; }

    public String getUrl() {
        return mUrl;
    }
    public int getWidth() {
        return mWidth;
    }
    public int getHeight() {
        return mHeight;
    }
    public String getType() {
        return mType;
    }
}
