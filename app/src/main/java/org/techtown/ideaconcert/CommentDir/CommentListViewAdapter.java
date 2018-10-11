package org.techtown.ideaconcert.CommentDir;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.techtown.ideaconcert.R;

import java.util.ArrayList;

public class CommentListViewAdapter extends BaseAdapter {

    ArrayList<CommentListViewItem> items = new ArrayList<>();

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.comment_list_item, parent, false);
        }

        TextView emailView = convertView.findViewById(R.id.comment_item_email);
        TextView dateView = convertView.findViewById(R.id.comment_item_date);
        TextView commentView = convertView.findViewById(R.id.comment_item_comment);
        TextView replyView = convertView.findViewById(R.id.comment_reply);
        TextView likeNumView = convertView.findViewById(R.id.comment_item_like_num);
        TextView accusationView = convertView.findViewById(R.id.comment_accusation);

        final CommentListViewItem listViewItem = items.get(position);

        emailView.setText(listViewItem.getEmail());
        dateView.setText(listViewItem.getDate());
        commentView.setText("" + listViewItem.getComment());
        replyView.setText("답글 " + listViewItem.getReply_num());
        likeNumView.setText("" + listViewItem.getLike_num());

        replyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "답글보기!, position=" + position, Toast.LENGTH_SHORT).show();
            }
        });
        accusationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "신고하기!, position=" + position, Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }

    public ArrayList<CommentListViewItem> getItems() {
        return items;
    }

    public void addItem(String email, String date, String comment, int reply_num, int like_num) {
        CommentListViewItem item = new CommentListViewItem();
        item.setEmail(email);
        item.setDate(date);
        item.setComment(comment);
        item.setReply_num(reply_num);
        item.setLike_num(like_num);

        items.add(item);
    }
}