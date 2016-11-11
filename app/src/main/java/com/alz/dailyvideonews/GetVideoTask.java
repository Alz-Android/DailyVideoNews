package com.alz.dailyvideonews;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;

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

import com.alz.dailyvideonews.R;
import static android.R.attr.bitmap;


public class GetVideoTask extends AsyncTask<String, Void, Void> {
    public static final String API_KEY = "AIzaSyDyabUM--6OixvzaaxUk4iwnNVSSuCdjU0";
    private final String LOG_TAG = GetVideoTask.class.getSimpleName();
    private com.google.api.services.youtube.YouTube mYoutube;
    private com.google.api.services.youtube.YouTube.Search.List mQuery;
    private Context mContext;


    private AppWidgetManager appWidgetManager;
    private RemoteViews rv;
    private ComponentName watchWidget;

    public GetVideoTask(Context context) {

        mContext = context;
        mYoutube = new com.google.api.services.youtube.YouTube.Builder(new NetHttpTransport(),
                new JacksonFactory(),
                new HttpRequestInitializer() {
                    @Override
                    public void initialize(HttpRequest hr) throws IOException {
                    }
                }).setApplicationName(context.getResources().getString(R.string.app_name)).build();

        try {
            mQuery = mYoutube.search().list("id,snippet");
            mQuery.setKey(API_KEY);
            mQuery.setType("video");
            mQuery.setFields("items(id/videoId,snippet/title,snippet/description,snippet/thumbnails/default/url, snippet/publishedAt)");
        } catch (IOException e) {
            Log.d(LOG_TAG, "connection error: " + e);
        }
    }

    @Override
    protected Void doInBackground(String... params) {
        Date yesterday = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
        String rfc3339 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(yesterday);
        DateTime dt = DateTime.parseRfc3339(rfc3339);
        mQuery.setPublishedAfter(dt);
        mQuery.setQ(params[0]);
        mQuery.setOrder(params[1]);

        try {
            SearchListResponse response = mQuery.execute();
            List<SearchResult> results = response.getItems();
            List<VideoItem> items = new ArrayList<VideoItem>();
            Log.i(LOG_TAG, results.toString());

            // Clearing database since we only keep the last loaded data
            mContext.getContentResolver().delete(VideosTable.CONTENT_URI, null, null);

            for (SearchResult result : results) {
                VideoItem item = new VideoItem();
                item.setId(result.getId().getVideoId());
                item.setTitle(result.getSnippet().getTitle());
                item.setDescription(result.getSnippet().getDescription());
                item.setThumbnailURL(result.getSnippet().getThumbnails().getDefault().getUrl());
                items.add(item);

                VideoDBTable videoRow = new VideoDBTable(
                        item.getId(),
                        item.getTitle(),
                        item.getDescription(),
                        item.getThumbnailURL(),
                        false
                );
                mContext.getContentResolver().insert(VideosTable.CONTENT_URI, VideosTable.getContentValues(videoRow, false));
            }

        } catch (IOException e) {
            Log.d(LOG_TAG, "Search failed: " + e);
        }
        return null;
    }


//    @Override
//    protected void onPostExecute(Void aVoid) {
//        super.onPostExecute(aVoid);
//
//        if (appWidgetManager != null) {
//            String finalString = "sync @";
//            rv.setTextViewText(R.id.list_view, finalString);
//            appWidgetManager.updateAppWidget(watchWidget, rv);
//        }
//    }
}