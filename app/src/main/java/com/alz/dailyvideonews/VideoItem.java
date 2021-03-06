package com.alz.dailyvideonews;

/**
 * Created by Al on 2016-10-31.
 */

public class VideoItem {
    private String title;
    private String description;
    private String thumbnailURL;
    private String id;

    public VideoItem(){}

    public VideoItem(String title, String id){
        this.title = title;
        this.id = id;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }
    public void setThumbnailURL(String thumbnail) {
        this.thumbnailURL = thumbnail;
    }

}
