package org.techtown.ideaconcert.SearchDir;

public class ParentFragment2SearchResultItem {
    int contents_pk, movie;
    String thumbnail_url, contents_name, author_name;
    double star_rating;

    public ParentFragment2SearchResultItem(int contents_pk, String thumbnail_url, String contents_name, String author_name, double star_rating, int movie) {
        this.contents_pk = contents_pk;
        this.thumbnail_url = thumbnail_url;
        this.contents_name = contents_name;
        this.author_name = author_name;
        this.star_rating = star_rating;
        this.movie = movie;
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

    public double getStar_rating() {
        return star_rating;
    }

    public void setStar_rating(double star_rating) {
        this.star_rating = star_rating;
    }
}
