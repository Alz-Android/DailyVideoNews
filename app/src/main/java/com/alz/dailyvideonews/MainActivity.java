package com.alz.dailyvideonews;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ListView videoResults;
    private Handler handler;
    private List<VideoItem> searchResults;
    private String mKeywords="business news";
    private String mOrder="date";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Use One onClick function to handle all 6 buttons
        findViewById(R.id.btnBusiness).setOnClickListener(this);
        findViewById(R.id.btnPoltics).setOnClickListener(this);
        findViewById(R.id.btnTech).setOnClickListener(this);
        findViewById(R.id.btnRecency).setOnClickListener(this);
        findViewById(R.id.btnRating).setOnClickListener(this);
        findViewById(R.id.btnViews).setOnClickListener(this);

        videoResults = (ListView)findViewById(R.id.video_results);
        handler = new Handler();
        addClickListener();
    }

    private void addClickListener(){
        videoResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int pos,
                                    long id) {
                Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
                intent.putExtra("VIDEO_ID", searchResults.get(pos).getId());
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
        getNews(mKeywords, mOrder);
    }

    private void getNews(final String keywords, final String order){
        new Thread(){
            public void run(){
                YouTubeNews youtube = new YouTubeNews(MainActivity.this);
                searchResults = youtube.search(keywords, order);
                handler.post(new Runnable(){
                    public void run(){
                        updateVideos();
                    }
                });
            }
        }.start();
    }

    private void updateVideos(){
        ArrayAdapter<VideoItem> adapter = new ArrayAdapter<VideoItem>(getApplicationContext(), R.layout.video_item, searchResults){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null){
                    convertView = getLayoutInflater().inflate(R.layout.video_item, parent, false);
                }
                ImageView thumbnail = (ImageView)convertView.findViewById(R.id.video_thumbnail);
                TextView title = (TextView)convertView.findViewById(R.id.video_title);

                VideoItem searchResult = searchResults.get(position);
                Picasso.with(getApplicationContext()).load(searchResult.getThumbnailURL()).into(thumbnail);
                title.setText(searchResult.getTitle());
                return convertView;
            }
        };
        videoResults.setAdapter(adapter);
    }
}
