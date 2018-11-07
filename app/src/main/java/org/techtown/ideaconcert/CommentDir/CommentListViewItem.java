package org.techtown.ideaconcert.CommentDir;

public class CommentListViewItem {
    private String email, date, comment;
    private int comment_pk, reply_num, like_num, tag; // tag값이 1이면 댓글 아이템, 2이면 답글 아이템

    public int getComment_pk() {
        return comment_pk;
    }

    public void setComment_pk(int comment_pk) {
        this.comment_pk = comment_pk;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getLike_num() {
        return like_num;
    }

    public void setLike_num(int like_num) {
        this.like_num = like_num;
    }

    public int getReply_num() {
        return reply_num;
    }

    public void setReply_num(int reply_num) {
        this.reply_num = reply_num;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }
}
