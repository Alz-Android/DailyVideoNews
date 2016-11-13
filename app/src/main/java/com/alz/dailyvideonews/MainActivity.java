package com.alz.dailyvideonews;

import android.app.ActivityOptions;
import android.content.Intent;
import android.database.Cursor;

import android.os.Build;
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
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

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

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        Tracker mTracker  = application.getDefaultTracker();

        MobileAds.initialize(this, "ca-app-pub-5707519959799753~1251811626");
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

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

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Share")
                .build());
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

    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mVideoAdapter.changeCursor(null);
    }

    private void addClickListener(){
        videoResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int position,
                                    long id) {
                Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
                Cursor cursor = (Cursor) mVideoAdapter.getItem(position);
                intent.putExtra("VIDEO_ID", cursor.getString(cursor.getColumnIndex("videoId")));

                if (Build.VERSION.SDK_INT >= 21) {
                    Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, findViewById(R.id.video_results), "video_results").toBundle();
                    startActivity(intent, bundle);
                }
                else {

                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBusiness:
                mKeywords = getString(R.string.key_Business);
                break;

            case R.id.btnPoltics:
                mKeywords = getString(R.string.key_Politics);
                break;

            case R.id.btnTech:
                mKeywords = getString(R.string.key_Tech);
                break;

            case R.id.btnRecency:
                mOrder = getString(R.string.ord_Recency);
                break;

            case R.id.btnRating:
                mOrder = getString(R.string.ord_Rating);
                break;

            case R.id.btnViews:
                mOrder = getString(R.string.ord_Views);
                break;
            default:
                break;
        }
        GetVideoTask getVideo = new GetVideoTask(MainActivity.this);
        getVideo.execute(mKeywords, mOrder);
    }
}
