package com.alz.dailyvideonews;

import ckm.simple.sql_provider.annotation.SimpleSQLColumn;
import ckm.simple.sql_provider.annotation.SimpleSQLTable;

import static android.R.attr.id;

/**
 * Created by Al on 2016-11-06.
 */

@SimpleSQLTable(table = "Videos", provider = "VideoProvider")
public class VideoDBTable {
    public VideoDBTable(){}

    public VideoDBTable(String favorite, String id) {
        this.mFavorite = favorite;
        this.mId = id;
    }

    @SimpleSQLColumn(value = "_id", primary = true)
    public int _id;

    @SimpleSQLColumn("favorite")
    public String mFavorite;

    @SimpleSQLColumn("id")
    public String mId;

}
