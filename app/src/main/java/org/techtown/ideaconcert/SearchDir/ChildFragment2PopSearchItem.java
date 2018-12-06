package org.techtown.ideaconcert.SearchDir;

public class ChildFragment2PopSearchItem {
    private int contents_pk;
    private String keyword;

    public ChildFragment2PopSearchItem(int contents_pk, String keyword) {
        this.contents_pk = contents_pk;
        this.keyword = keyword;
    }

    public int getContents_pk() {
        return contents_pk;
    }

    public void setContents_pk(int contents_pk) {
        this.contents_pk = contents_pk;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
