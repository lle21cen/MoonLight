package org.techtown.ideaconcert.MyPageDir;

import android.graphics.Bitmap;

public class Fragment2RecyclerItem {
    private Bitmap thumbnail;
    private String date, contents_name, url;
    private int contents_pk;
    private int alarm;

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
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

    public int getContents_pk() {
        return contents_pk;
    }

    public void setContents_pk(int contents_pk) {
        this.contents_pk = contents_pk;
    }

    public int isAlarm() {
        return alarm;
    }

    public void setAlarm(int alarm) {
        this.alarm = alarm;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
