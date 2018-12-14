package org.techtown.ideaconcert.ManageMyWorksDir;

public class Fragment3MyContentsItem {
    String thumbnail_url, contents_name, author_name;
    int movie;
    double star_rating;

    public Fragment3MyContentsItem(String thumbnail_url, String contents_name, String author_name, int movie, double star_rating) {
        this.thumbnail_url = thumbnail_url;
        this.contents_name = contents_name;
        this.author_name = author_name;
        this.movie = movie;
        this.star_rating = star_rating;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }

    public String getContents_name() {
        return contents_name;
    }

    public void setContents_name(String contents_name) {
        this.contents_name = contents_name;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public int getMovie() {
        return movie;
    }

    public void setMovie(int movie) {
        this.movie = movie;
    }

    public double getStar_rating() {
        return star_rating;
    }

    public void setStar_rating(double star_rating) {
        this.star_rating = star_rating;
    }
}
