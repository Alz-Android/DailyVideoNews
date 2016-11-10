package com.alz.dailyvideonews;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import android.database.DatabaseUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.facebook.stetho.Stetho;

import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor>{

    private ListView videoResults;

    private String mKeywords="business news";
    private String mOrder="date";

    private static VideoCursorAdapter mVideoAdapter;
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int LOADER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        setContentView(R.layout.activity_main);

        // Use One onClick function to handle all 6 buttons
        findViewById(R.id.btnBusiness).setOnClickListener(this);
        findViewById(R.id.btnPoltics).setOnClickListener(this);
        findViewById(R.id.btnTech).setOnClickListener(this);
        findViewById(R.id.btnRecency).setOnClickListener(this);
        findViewById(R.id.btnRating).setOnClickListener(this);
        findViewById(R.id.btnViews).setOnClickListener(this);

        videoResults = (ListView)findViewById(R.id.video_results);
        addClickListener();

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);

        mVideoAdapter = new VideoCursorAdapter(this, null , 0);
        videoResults.setAdapter(mVideoAdapter);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.i("MainActivity", " onCreateLoader");

        return new CursorLoader(
                this,
                VideosTable.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        cursor.moveToFirst();
        mVideoAdapter = new VideoCursorAdapter(this, cursor , 0);
        videoResults.setAdapter(mVideoAdapter);
  //      DatabaseUtils.dumpCursor(cursor);
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mVideoAdapter.changeCursor(null);
    }

    private boolean isOnline() {
        ConnectivityManager mngr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = mngr.getActiveNetworkInfo();
        return !(info == null || (info.getState() != NetworkInfo.State.CONNECTED));
    }


    private void addClickListener(){
        videoResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int position,
                                    long id) {
                Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
                Cursor cursor = (Cursor) mVideoAdapter.getItem(position);
                intent.putExtra("VIDEO_ID", cursor.getString(cursor.getColumnIndex("videoId")));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBusiness:
                mKeywords = "business news";
//                findViewById(R.id.btnBusiness).setBackgroundColor(Color.RED);
                break;

            case R.id.btnPoltics:
                mKeywords = "politics news";
                break;

            case R.id.btnTech:
                mKeywords = "technology news";
                break;

            case R.id.btnRecency:
                mOrder = "date";
                break;

            case R.id.btnRating:
                mOrder = "rating";
                break;

            case R.id.btnViews:
                mOrder = "viewCount";
                break;
            default:
                break;
        }
        GetVideoTask getVideo = new GetVideoTask(MainActivity.this);
        getVideo.execute(mKeywords, mOrder);
    }
}

 //   private void getNews(final String keywords, final String order){




   //     new Thread(){
   //         public void run(){
     //           YouTubeNews youtube = new YouTubeNews(MainActivity.this);
      //          youtube.search(keywords, order);
      //      }
    //    }.start();
 //   }



//    private void updateVideos(){
//
//        mVideoAdapter = new VideoCursorAdapter(this, null , 0);
//        Log.i(LOG_TAG, Integer.toString(mVideoAdapter.getCount()));
//
//
//        videoResults.setAdapter(mVideoAdapter);

//        ArrayAdapter<VideoItem> adapter = new ArrayAdapter<VideoItem>(getApplicationContext(), R.layout.video_item, searchResults){
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//                if(convertView == null){
//                    convertView = getLayoutInflater().inflate(R.layout.video_item, parent, false);
//                }
//                ImageView thumbnail = (ImageView)convertView.findViewById(R.id.video_thumbnail);
//                TextView title = (TextView)convertView.findViewById(R.id.video_title);
//
//                VideoItem searchResult = searchResults.get(position);
//                Picasso.with(getApplicationContext()).load(searchResult.getThumbnailURL()).into(thumbnail);
//                title.setText(searchResult.getTitle());
//                return convertView;
//            }
//        };
//        videoResults.setAdapter(adapter);
//    }

