package org.techtown.ideaconcert.MainActivityDir;

import android.graphics.Bitmap;

public class CategoryContentsItem {
    Bitmap bitmap;
    String work_name, author_name;
    int view_count, contents_pk;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getWork_name() {
        return work_name;
    }

    public void setWork_name(String work_name) {
        this.work_name = work_name;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public int getView_count() {
        return view_count;
    }

    public void setView_count(int view_count) {
        this.view_count = view_count;
    }

    public int getContents_pk() {
        return contents_pk;
    }

    public void setContents_pk(int contents_pk) {
        this.contents_pk = contents_pk;
    }
}
