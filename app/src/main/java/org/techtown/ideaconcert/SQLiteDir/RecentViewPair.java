package org.techtown.ideaconcert.SQLiteDir;

public class RecentViewPair {
    private String contents_name;
    private String date;
    private String contents_num;

    public RecentViewPair(String contents_name, String date, String contents_num) {
        this.date = date;
        this.contents_name = contents_name;
        this.contents_num = contents_num;
    }

    public String getContents_name() {
        return contents_name;
    }

    public String getDate() {
        return date;
    }

    public String getContents_num() {
        return contents_num;
    }
}
