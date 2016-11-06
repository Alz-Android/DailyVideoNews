package com.alz.dailyvideonews;

/**
 * Created by Al on 2016-10-31.
 */

import android.content.Context;
import android.util.Log;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class YouTubeNews {
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private com.google.api.services.youtube.YouTube mYoutube;
    private com.google.api.services.youtube.YouTube.Search.List mQuery;

    public static final String API_KEY = "AIzaSyDyabUM--6OixvzaaxUk4iwnNVSSuCdjU0";

    public YouTubeNews(Context context) {
        mYoutube = new com.google.api.services.youtube.YouTube.Builder(new NetHttpTransport(),
                                    new JacksonFactory(),
                                    new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest hr) throws IOException {}
        }).setApplicationName(context.getResources().getString(R.string.app_name)).build();

        try{
            mQuery = mYoutube.search().list("id,snippet");
            mQuery.setKey(API_KEY);
            mQuery.setType("video");
            mQuery.setFields("items(id/videoId,snippet/title,snippet/description,snippet/thumbnails/default/url, snippet/publishedAt)");
        }catch(IOException e){
            Log.d(LOG_TAG, "connection error: "+e);
        }
    }

    public List<VideoItem> search(String keywords){
        mQuery.setQ(keywords);
        mQuery.setOrder("rating"); //date, viewCount
        try{
            SearchListResponse response = mQuery.execute();
            List<SearchResult> results = response.getItems();

            Log.i(LOG_TAG,results.toString());

            List<VideoItem> items = new ArrayList<VideoItem>();
            for(SearchResult result:results){
                VideoItem item = new VideoItem();
                item.setTitle(result.getSnippet().getTitle());
                item.setDescription(result.getSnippet().getDescription());
                item.setThumbnailURL(result.getSnippet().getThumbnails().getDefault().getUrl());
                item.setId(result.getId().getVideoId());
                items.add(item);
            }
            return items;
        }catch(IOException e){
            Log.d("YC", "Could not search: "+e);
            return null;
        }
    }
}

