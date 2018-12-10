package org.techtown.ideaconcert.MainActivityDir;

public class DiscountContentsItem {
    String thumbnailUrl, title, writer, painter, summary;
    int contents_pk, view_count;
    double star_rating;

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getPainter() {
        return painter;
    }

    public void setPainter(String painter) {
        this.painter = painter;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getContents_pk() {
        return contents_pk;
    }

    public void setContents_pk(int contents_pk) {
        this.contents_pk = contents_pk;
    }

    public int getView_count() {
        return view_count;
    }

    public void setView_count(int view_count) {
        this.view_count = view_count;
    }

    public double getStar_rating() {
        return star_rating;
    }

    public void setStar_rating(double star_rating) {
        this.star_rating = star_rating;
    }
}
