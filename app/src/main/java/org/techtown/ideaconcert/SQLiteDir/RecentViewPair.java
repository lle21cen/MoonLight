package org.techtown.ideaconcert.SQLiteDir;

public class RecentViewPair {
    private int contents_pk;
    private String contents_name;
    private String date;
    private String contents_num;

    public RecentViewPair(int contents_pk, String contents_name, String date, String contents_num) {
        this.contents_pk = contents_pk;
        this.contents_name = contents_name;
        this.date = date;
        this.contents_num = contents_num;
    }

    public int getContents_pk() {
        return contents_pk;
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
