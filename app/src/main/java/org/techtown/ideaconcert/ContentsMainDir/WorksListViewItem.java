package org.techtown.ideaconcert.ContentsMainDir;

import android.graphics.Bitmap;

public class WorksListViewItem {
    private int contentsItemPk, contentsNum, commentCount;
    private Bitmap bitmp; // 오타 - 수정귀찮
    private String worksTitle;
    private String watchNum;
    private double star_rating;


    public Bitmap getBitmp() {
        return bitmp;
    }

    public void setBitmp(Bitmap bitmp) {
        this.bitmp = bitmp;
    }

    public int getContentsNum() {
        return contentsNum;
    }

    public void setContentsNum(int contentsNum) {
        this.contentsNum = contentsNum;
    }

    public String getWorksTitle() {
        return worksTitle;
    }

    public void setWorksTitle(String worksTitle) {
        this.worksTitle = worksTitle;
    }

    public String getWatchNum() {
        return watchNum;
    }

    public void setWatchNum(String watchNum) {
        this.watchNum = watchNum;
    }

    public double getStar_rating() {
        return star_rating;
    }

    public void setStar_rating(double star_rating) {
        this.star_rating = star_rating;
    }

    public int getCommentCount() {
        return commentCount;
    }
    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
    public int getContentsItemPk() {
        return contentsItemPk;
    }
    public void setContentsItemPk(int contentsItemPk) {
        this.contentsItemPk = contentsItemPk;
    }
}
