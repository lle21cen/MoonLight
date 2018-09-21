package org.techtown.ideaconcert.ContentsMainDir;

import android.graphics.drawable.Drawable;

public class WorksListViewItem {
    private Drawable worksDrawable;
    private String worksTitle;
    private String watchNum;
    private String rating;
    private String comments;

    public Drawable getWorksDrawable() {
        return worksDrawable;
    }

    public void setWorksDrawable(Drawable worksDrawable) {
        this.worksDrawable = worksDrawable;
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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
