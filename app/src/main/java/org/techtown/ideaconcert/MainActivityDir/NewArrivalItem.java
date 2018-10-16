package org.techtown.ideaconcert.MainActivityDir;

import android.graphics.Bitmap;

public class NewArrivalItem {
    // 신작보기와 베스트 9과 추천 작품에서 사용
    int contents_pk;
    Bitmap bitmap;
    String work_name, painter_name;
    double star_rating;

    public int getContents_pk() {
        return contents_pk;
    }

    public void setContents_pk(int contents_pk) {
        this.contents_pk = contents_pk;
    }

    public String getWork_name() {
        return work_name;
    }

    public void setWork_name(String work_name) {
        this.work_name = work_name;
    }

    public String getPainter_name() {
        return painter_name;
    }

    public void setPainter_name(String painter_name) {
        this.painter_name = painter_name;
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
