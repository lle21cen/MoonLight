package org.techtown.ideaconcert.ContentsMainDir;

import android.graphics.Bitmap;

import java.io.Serializable;

public class WorksListViewItem implements Serializable {
    private int contentsItemPk, contentsNum, commentCount;
//    private URL imageURL;
    private Bitmap bitmp; // 오타 - 수정귀찮
    private String worksTitle;
    private String watchNum;
    private String rating;

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

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
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
