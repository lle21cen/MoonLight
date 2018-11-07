package org.techtown.ideaconcert.CommentDir;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.techtown.ideaconcert.DatabaseRequest;
import org.techtown.ideaconcert.R;

import java.util.ArrayList;

public class CommentListViewAdapter extends BaseAdapter {

    final private String getCommentReplyURL = "http://lle21cen.cafe24.com/GetCommentReply.php";

    ArrayList<CommentListViewItem> items = new ArrayList<>();
    private Context context;
    private int itemPosition;
    ListView commentListView;

    public CommentListViewAdapter(ListView commentListView) {
        this.commentListView = commentListView;
    }

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
        context = parent.getContext();
        itemPosition = position;
        final CommentListViewItem listViewItem = items.get(position);
        int tag = listViewItem.getTag();

        TextView emailView, dateView, commentView, replyView, likeNumView, accusationView;
        TextView reply_emailView, reply_dateView, reply_commentView, reply_likeNumView, reply_accusationView;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (tag == 1)
                convertView = inflater.inflate(R.layout.comment_list_item, parent, false);
            else if (tag == 2)
                convertView = inflater.inflate(R.layout.comment_reply_item, parent, false);
        }

        if (tag == 1) {
            // 댓글 아이템 layout에 있는 view들
            emailView = convertView.findViewById(R.id.comment_item_email);
            dateView = convertView.findViewById(R.id.comment_item_date);
            commentView = convertView.findViewById(R.id.comment_item_comment);
            replyView = convertView.findViewById(R.id.comment_reply);
            likeNumView = convertView.findViewById(R.id.comment_item_like_num);
            accusationView = convertView.findViewById(R.id.comment_accusation);

            emailView.setText(listViewItem.getEmail());
            dateView.setText(listViewItem.getDate());
            commentView.setText("" + listViewItem.getComment());
            replyView.setText("답글 " + listViewItem.getReply_num());
            likeNumView.setText("" + listViewItem.getLike_num());

            replyView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "답글보기!, position=" + position, Toast.LENGTH_SHORT).show();
                    DatabaseRequest request = new DatabaseRequest(getReplyCommentListener, getCommentReplyURL);
                    request.setComment_pk(listViewItem.getComment_pk());
                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    requestQueue.add(request);
                }
            });

            accusationView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "신고하기!, position=" + position, Toast.LENGTH_SHORT).show();
                }
            });
        } else if (tag == 2) {
            // 답글 아이템 layout에 있는 view들
            reply_emailView = convertView.findViewById(R.id.reply_item_email);
            reply_dateView = convertView.findViewById(R.id.reply_item_date);
            reply_commentView = convertView.findViewById(R.id.reply_item_comment);
            reply_likeNumView = convertView.findViewById(R.id.reply_item_like_num);
            reply_accusationView = convertView.findViewById(R.id.reply_accusation);

            reply_emailView.setText(listViewItem.getEmail());
            reply_dateView.setText(listViewItem.getDate());
            reply_commentView.setText("" + listViewItem.getComment());
            reply_likeNumView.setText("" + listViewItem.getLike_num());

            reply_accusationView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "신고하기!, position=" + position, Toast.LENGTH_SHORT).show();
                }
            });
        }
        return convertView;
    }

    public ArrayList<CommentListViewItem> getItems() {
        return items;
    }

    public void addItem(int comment_pk, String email, String date, String comment, int reply_num, int like_num, int tag) {
        CommentListViewItem item = new CommentListViewItem();
        item.setComment_pk(comment_pk);
        item.setEmail(email);
        item.setDate(date);
        item.setComment(comment);
        item.setReply_num(reply_num);
        item.setLike_num(like_num);
        item.setTag(tag);
        items.add(item);
    }

    private Response.Listener<String> getReplyCommentListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                JSONArray result = jsonResponse.getJSONArray("result");
                boolean exist = jsonResponse.getBoolean("exist");
                if (exist) {
                    int num_result = jsonResponse.getInt("num_result");
                    for (int i = 0; i < num_result; i++) {
                        JSONObject temp = result.getJSONObject(i);
                        String email = temp.getString("email");
                        String date = temp.getString("date");
                        String reply = temp.getString("reply");
                        int like_num = temp.getInt("like_num");

                        CommentListViewItem item = new CommentListViewItem();
                        item.setEmail(email);
                        item.setDate(date);
                        item.setComment(reply);
                        item.setLike_num(like_num);
                        item.setTag(2);
                        items.add(itemPosition, item);
                    }
                    commentListView.setAdapter(CommentListViewAdapter.this);
                } else {
                    Log.e("답글없음", "답글이 없습니다.");
                }
            } catch (Exception e) {
                Log.e("답글리스너", e.getMessage());
            }
        }
    };

}