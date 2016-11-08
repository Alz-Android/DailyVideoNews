package com.alz.dailyvideonews;

import android.graphics.Bitmap;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        WebView myWebView;


        getActionBar().setDisplayHomeAsUpEnabled(true);


        myWebView = (WebView) findViewById(R.id.webview);
        myWebView.setWebViewClient(new myWebClient());
        WebSettings webSettings = myWebView.getSettings();
        myWebView.loadUrl("http://www.youtube.com/watch?v="+getIntent().getStringExtra("VIDEO_ID"));
        webSettings.setJavaScriptEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


// The web client was needed to place the video within the App isntead of opening YouTube
// Also without this Client 2 back clicks were needed to return the App to the main page
    public class myWebClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event)
//    {
//        if ((keyCode == KeyEvent.KEYCODE_BACK) ) {
//            this.finish();
//            myWebView.goBack();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}
