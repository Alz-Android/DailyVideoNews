package com.alz.dailyvideonews;

import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.util.Log;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

/**
 * Created by Al on 2016-11-08.
 */

public class WidgetListViewService extends RemoteViewsService {
    public WidgetListViewService() {
        super();
    }

    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {

            private Cursor cursor = null;
            private ArrayList<VideoItem> records;

            public void onCreate() {
                Log.i("ListViewWidgetService", "onCreate");
                records = new ArrayList<VideoItem>();
            }

            public RemoteViews getViewAt(int position) {
                Log.i("ListViewWidgetServiceGV", records.get(position).getTitle());

                RemoteViews rv = new RemoteViews(getPackageName(), R.layout.widget_list_item);
                rv.setTextViewText(R.id.item, records.get(position).getTitle());
                Intent fillInIntent = new Intent();
                fillInIntent.setData(VideosTable.CONTENT_URI);
                rv.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);
                return rv;
            }

            public int getCount() {
                Log.e("size=", records.size() + "");
                return records.size();
            }

            public void onDataSetChanged() {

                final long identityToken = Binder.clearCallingIdentity();

                cursor = getContentResolver().query(
                        VideosTable.CONTENT_URI,
                        new String[]{VideosTable.FIELD_TITLE, VideosTable.FIELD_VIDEOID},
                        null,
                        null,
                        null);
                Binder.restoreCallingIdentity(identityToken);

                cursor.moveToFirst();
                Log.i("ListViewWidgetService", String.valueOf(cursor.getCount()));

                try {
                    cursor.moveToPosition(-1);
                    while (cursor.moveToNext()) {
                        records.add (new VideoItem(cursor.getString(0),cursor.getString(1)) );
                        Log.i("ListViewWidgetService", cursor.getString(0));
                    }
                } finally {
                    cursor.close();
                }
            }

            public int getViewTypeCount() {
                return 1;
            }

            public long getItemId(int position) {
                return position;
            }

            public void onDestroy() {
                records.clear();
            }

            public boolean hasStableIds() {
                return true;
            }

            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget);
            }
        };
    }
}
