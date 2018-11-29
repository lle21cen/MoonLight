package org.techtown.ideaconcert.MyPageDir;

import android.graphics.Bitmap;

public class Fragment1RecyclerItem {
    private String thumbnail_url;
    private String date, contents_name;
    private int contents_pk, contents_num;

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContents_name() {
        return contents_name;
    }

    public void setContents_name(String contents_name) {
        this.contents_name = contents_name;
    }

    public int getContents_num() {
        return contents_num;
    }

    public void setContents_num(int contents_num) {
        this.contents_num = contents_num;
    }

    public int getContents_pk() {
        return contents_pk;
    }

    public void setContents_pk(int contents_pk) {
        this.contents_pk = contents_pk;
    }
}
