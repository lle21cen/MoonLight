package org.techtown.ideaconcert.CommentDir;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import org.techtown.ideaconcert.UserInformation;

import java.util.ArrayList;

public class CommentListViewAdapter extends BaseAdapter {

    final private String getCommentReplyURL = "http://lle21cen.cafe24.com/GetCommentReply.php";
    final private String insertCommentLikeDataURL = "http://lle21cen.cafe24.com/InsertCommentLikeData.php";

    ArrayList<CommentListViewItem> items = new ArrayList<>();
    private Context context;
    private int itemPosition;
    ListView commentListView;
    private int user_pk;

    private int fragment_from; // 1이면 BestCommentsFragment에서 호출 2이면 AllCommentFragment에서 호출
    static final int FROM_BESTFRAGMENT = 1, FROM_ALLFRAGMENT = 2;

    public CommentListViewAdapter(ListView commentListView, int user_pk) {
        this.commentListView = commentListView;
        this.user_pk = user_pk;
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
        ImageView likeButton;

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
            likeButton = convertView.findViewById(R.id.comment_like_btn);

            ImageView bestImage = convertView.findViewById(R.id.comment_best_img);

            if (fragment_from == 2) {
                bestImage.setVisibility(View.GONE);
            }
            replyView.setText("답글 " + listViewItem.getReply_num());
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
        } else {
            // 답글 아이템 layout에 있는 view들
            emailView = convertView.findViewById(R.id.reply_item_email);
            dateView = convertView.findViewById(R.id.reply_item_date);
            commentView = convertView.findViewById(R.id.reply_item_comment);
            likeNumView = convertView.findViewById(R.id.reply_item_like_num);
            accusationView = convertView.findViewById(R.id.reply_accusation);
            likeButton = convertView.findViewById(R.id.reply_like_btn);
        }

        emailView.setText(listViewItem.getEmail());
        dateView.setText(listViewItem.getDate());
        commentView.setText("" + listViewItem.getComment());
        likeNumView.setText("" + listViewItem.getLike_num());
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 좋아요 버튼 눌렀을 경우
                CommentInsertLikeDataRequest request = new CommentInsertLikeDataRequest(insertCommentLikeDataURL, commentLikeDataInsertListener, listViewItem.getComment_pk(), user_pk);
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

    private Response.Listener<String> commentLikeDataInsertListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                boolean exist = jsonResponse.getBoolean("exist");
                if (exist) {
                    Toast.makeText(context, "이미 좋아요을 누르셨습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        Toast.makeText(context, "좋아요를 눌렀습니다.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(context, "에러 : " + jsonResponse.getString("errmsg"), Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                Log.e("답글리스너", e.getMessage());
            }
        }
    };

    public void setFragment_from(int fragment_from) {
        this.fragment_from = fragment_from;
    }
}