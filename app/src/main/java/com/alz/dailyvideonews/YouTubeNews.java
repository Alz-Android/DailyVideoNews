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
import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class YouTubeNews {
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private com.google.api.services.youtube.YouTube mYoutube;
    private com.google.api.services.youtube.YouTube.Search.List mQuery;
    private Context mContext;

    public static final String API_KEY = "AIzaSyDyabUM--6OixvzaaxUk4iwnNVSSuCdjU0";

    public YouTubeNews(Context context) {
        mContext = context;
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

    public List<VideoItem> search(String keywords, String order){

        Date yesterday = new Date(System.currentTimeMillis()-24*60*60*1000);
        String  rfc3339 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(yesterday);
        DateTime dt = DateTime.parseRfc3339(rfc3339);
        mQuery.setPublishedAfter(dt);
        mQuery.setQ(keywords);
        mQuery.setOrder(order);

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
            // content provider setup
            String[] args = new String[]{"false"};
            mContext.getContentResolver().delete(VideosTable.CONTENT_URI, "favorite=?", args);
            VideoDBTable videoRow = new VideoDBTable(
                    // Data to nbe inserted

//                "false",
//                movie.id.toString(),
//                movie.posterPath,
//                movie.releaseDate.toString(),
//                isPopular
            );



            return items;
        }catch(IOException e){
            Log.d(LOG_TAG, "Search failed: "+e);
            return null;
        }
    }
}

