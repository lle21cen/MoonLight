package org.techtown.ideaconcert.SQLiteDir;

public class RecentSearchData {
    String contents_name, search_date;

    public RecentSearchData(String contents_name, String search_date) {
        this.contents_name = contents_name;
        this.search_date = search_date;
    }

    public String getContents_name() {
        return contents_name;
    }

    public void setContents_name(String contents_name) {
        this.contents_name = contents_name;
    }

    public String getSearch_date() {
        return search_date;
    }

    public void setSearch_date(String search_date) {
        this.search_date = search_date;
    }
}
