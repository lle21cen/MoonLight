package org.techtown.ideaconcert.MyPageDir;

import android.graphics.Bitmap;

public class Fragment1RecyclerItem {
    private Bitmap thumbnail;
    private String date, contents_name, contents_num;
    private int contents_pk;

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

    public String getContents_num() {
        return contents_num;
    }

    public void setContents_num(String contents_num) {
        this.contents_num = contents_num;
    }

    public int getContents_pk() {
        return contents_pk;
    }

    public void setContents_pk(int contents_pk) {
        this.contents_pk = contents_pk;
    }
}
