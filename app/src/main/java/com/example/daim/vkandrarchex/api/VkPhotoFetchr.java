package com.example.daim.vkandrarchex.api;


import android.net.Uri;
import android.util.Log;

import com.example.daim.vkandrarchex.BuildConfig;
import com.example.daim.vkandrarchex.content.Likes;
import com.example.daim.vkandrarchex.content.Photo;
import com.example.daim.vkandrarchex.content.PhotoUrl;
import com.example.daim.vkandrarchex.content.Reposts;
import com.example.daim.vkandrarchex.repository.SinglePhotoCache;
import com.example.daim.vkandrarchex.utils.PreferenceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class VkPhotoFetchr {
    private final static String LOG_TAG = "VkPhotoFetchr";
    private static int totalByteSize;

    public static byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            byte[] resultArray = out.toByteArray();
            int byteLength = resultArray.length;
            totalByteSize += byteLength;
            Log.d(LOG_TAG, "photo size: " + byteLength + "\n totalByteSize: " + totalByteSize);
            return resultArray;
        } finally {
            connection.disconnect();
        }
    }

    private static String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public static List<Photo> fetchPhotos(int  offset, int count){
        try{
            String url = Uri.parse(BuildConfig.API_ENDPOINT)
                    .buildUpon()
                    .appendPath("/method/photos.getAll")
                    .appendQueryParameter("v", "5.67")
                    .appendQueryParameter("skip_hidden", "1")
                    .appendQueryParameter("photo_sizes", "1")
                    .appendQueryParameter("extended", "1")
                    .appendQueryParameter("access_token", PreferenceUtils.getToken())
                    .appendQueryParameter("owner_id", PreferenceUtils.getUserId())
                    .appendQueryParameter("offset", Integer.toString(offset))
                    .appendQueryParameter("count", Integer.toString(count))
                    .build().toString();
            String jsonString = getUrlString(url);
            Log.d(LOG_TAG, "Fire URL: " + url);
            Log.d(LOG_TAG, "Received JSON: " + jsonString);
            JSONObject jsonBody = new JSONObject(jsonString);
            List<Photo> photos = parseItems(jsonBody);
            Log.d(LOG_TAG, "List<photo>.size(): " + photos.size());
            return photos;
        }catch (JSONException je){
            Log.e(LOG_TAG, "Failed to parse JSON", je);
        }catch (IOException ioe){
            Log.e(LOG_TAG, "Failed to fetch photos", ioe);
        }
        return null;
    }

    private static List<Photo> parseItems( JSONObject jsonBody)
        throws IOException, JSONException {
        List<Photo> photoList = new ArrayList();
        JSONObject photosJsonObject = jsonBody.getJSONObject("response");
        SinglePhotoCache.setPhotoQuantity(photosJsonObject.getInt("count"));
        JSONArray photosJsonArray = photosJsonObject.getJSONArray("items");

        for(int i = 0; i < photosJsonArray.length(); i++){
            JSONObject photoJson = photosJsonArray.getJSONObject(i);

            Photo item = new Photo();
            item.setId(photoJson.getInt("id"));
            item.setText(photoJson.getString("text"));

            List<PhotoUrl> photoUrlList = new ArrayList();
            JSONArray photoUrlsArray = photoJson.getJSONArray("sizes");
            for(int j = 0; j < photoUrlsArray.length(); j++){
                JSONObject photoUrlJson = photoUrlsArray.getJSONObject(j);
                PhotoUrl photoUrl = new PhotoUrl();
                photoUrl.setHeight(photoUrlJson.getInt("height"));
                photoUrl.setWidth(photoUrlJson.getInt("width"));
                photoUrl.setType(photoUrlJson.getString("type"));
                photoUrl.setUrl(photoUrlJson.getString("src"));
                photoUrlList.add(photoUrl);
            }
            item.setPhotoUrls(photoUrlList);

            JSONObject likesJson = photoJson.getJSONObject("likes");
            Likes likes = new Likes();
            likes.setCount(likesJson.getInt("count"));
            likes.setUserLikes(likesJson.getInt("user_likes"));
            item.setLikes(likes);

            JSONObject repostsJson = photoJson.getJSONObject("reposts");
            Reposts reposts = new Reposts();
            reposts.setCount(repostsJson.getInt("count"));
            item.setReposts(reposts);

            photoList.add(item);
        }
        return photoList;
    }

}
