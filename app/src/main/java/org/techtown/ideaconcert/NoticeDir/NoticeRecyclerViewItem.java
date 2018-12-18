package org.techtown.ideaconcert.NoticeDir;

import android.widget.TextView;

public class NoticeRecyclerViewItem {
    private int board_pk;
    private String title, date, content;
    private TextView contentView;

    public NoticeRecyclerViewItem(int board_pk, String title, String date, String content, TextView contentView) {
        this.board_pk = board_pk;
        this.title = title;
        this.date = date;
        this.content = content;
        this.contentView = contentView;
    }

    public int getBoard_pk() {
        return board_pk;
    }

    public void setBoard_pk(int board_pk) {
        this.board_pk = board_pk;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public TextView getContentView() {
        return contentView;
    }

    public void setContentView(TextView contentView) {
        this.contentView = contentView;
    }
}
