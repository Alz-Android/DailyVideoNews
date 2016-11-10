package com.alz.dailyvideonews;

import ckm.simple.sql_provider.annotation.SimpleSQLColumn;
import ckm.simple.sql_provider.annotation.SimpleSQLTable;


/**
 * Created by Al on 2016-11-06.
 */

@SimpleSQLTable(table = "Videos", provider = "VideoProvider")
public class VideoDBTable {
    public VideoDBTable(){}

    public VideoDBTable(String videoId, String title, String description, String thumbnailURL, boolean widget) {
        this.mVideoId = videoId;
        this.mTitle = title;
        this.mDescription = description;
        this.mThumbnailURL = thumbnailURL;
        this.mWidget = widget;
    }

    @SimpleSQLColumn(value = "_id", primary = true)
    public int _id;

    @SimpleSQLColumn("videoId")
    public String mVideoId;

    @SimpleSQLColumn("title")
    public String mTitle;

    @SimpleSQLColumn("description")
    public String mDescription;

    @SimpleSQLColumn("thumbnailURL")
    public String mThumbnailURL;

    @SimpleSQLColumn("widget")
    public boolean mWidget;
}
