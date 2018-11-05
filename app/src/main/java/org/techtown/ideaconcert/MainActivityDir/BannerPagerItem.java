package org.techtown.ideaconcert.MainActivityDir;

public class BannerPagerItem {
    String url;
    int contents_pk;

    public BannerPagerItem(String url, int contents_pk) {
        this.url = url;
        this.contents_pk = contents_pk;
    }

    public BannerPagerItem(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getContents_pk() {
        return contents_pk;
    }

    public void setContents_pk(int contents_pk) {
        this.contents_pk = contents_pk;
    }
}
