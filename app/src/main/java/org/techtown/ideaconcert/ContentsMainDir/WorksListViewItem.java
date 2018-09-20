package org.techtown.ideaconcert.ContentsMainDir;

import android.graphics.drawable.Drawable;

public class WorksListViewItem {
    private Drawable worksDrawable;
    private String worksTitle;
    private String tempStr;

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

    public String getTempStr() {
        return tempStr;
    }

    public void setTempStr(String tempStr) {
        this.tempStr = tempStr;
    }
}
