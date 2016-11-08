package com.alz.dailyvideonews;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import static android.R.attr.thumbnail;

/**
 * Created by Al on 2016-11-07.
 */

public class VideoCursorAdapter extends CursorAdapter {

    private final String LOG_TAG = VideoCursorAdapter.class.getSimpleName();
    public VideoCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_item, parent, false);

        return view;
    }

    /*
        This is where we fill-in the views with the contents of the cursor.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView title = (TextView)view.findViewById(R.id.video_title);
        title.setText(cursor.getString(cursor.getColumnIndex("title")));

        ImageView thumbnail = (ImageView)view.findViewById(R.id.video_thumbnail);
        Picasso.with(context)
                .load(cursor.getString(cursor.getColumnIndex("thumbnailURL")))
                .into(thumbnail);

        Log.i(LOG_TAG, "cursor adapter end");
    }
}