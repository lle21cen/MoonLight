package org.techtown.ideaconcert.MainActivityDir;

import android.graphics.Bitmap;

public class NewArrivalItem {
    Bitmap bitmap;
    String work_name, author_name;
    double star_rating;

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

    public double getStar_rating() {
        return star_rating;
    }

    public void setStar_rating(double star_rating) {
        this.star_rating = star_rating;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
