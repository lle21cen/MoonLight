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
import org.w3c.dom.Text;

import java.util.ArrayList;

public class CommentListViewAdapter extends BaseAdapter {

    final private String getCommentReplyURL = "http://lle21cen.cafe24.com/GetCommentReply.php";
    final private String insertCommentLikeDataURL = "http://lle21cen.cafe24.com/InsertCommentLikeData.php";

    ArrayList<CommentListViewItem> items = new ArrayList<>();
    private Context context;

    ListView commentListView;
    private int user_pk;

    final private String blankForBestComment = "                "; // 건드리지 말것!!

    private int fragment_from; // 1이면 BestCommentsFragment에서 호출 2이면 AllCommentFragment에서 호출
    static final int FROM_BESTFRAGMENT = 1, FROM_ALLFRAGMENT = 2;

    private CommentListViewItem listViewItem;

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

        listViewItem = items.get(position);

        ImageView likeButton;
        TextView emailView, dateView, commentView, replyView, accusationView;
        final TextView likeNumView;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.comment_list_item, parent, false);
        }


        // 댓글 아이템 layout에 있는 view들
        emailView = convertView.findViewById(R.id.comment_item_email);
        dateView = convertView.findViewById(R.id.comment_item_date);
        commentView = convertView.findViewById(R.id.comment_item_comment);
        replyView = convertView.findViewById(R.id.comment_reply);
        likeNumView = convertView.findViewById(R.id.comment_item_like_num);
        accusationView = convertView.findViewById(R.id.comment_accusation);
        likeButton = convertView.findViewById(R.id.comment_like_btn);

        ImageView bestImage = convertView.findViewById(R.id.comment_best_img);

        replyView.setText("답글 " + listViewItem.getReply_num());
        replyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "답글보기!, position=" + position, Toast.LENGTH_SHORT).show();
                GetReplyCommentListener listener = new GetReplyCommentListener(position);
                DatabaseRequest request = new DatabaseRequest(listener, getCommentReplyURL);
                request.setComment_pk(listViewItem.getComment_pk());
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(request);
            }
        });

        emailView.setText(listViewItem.getEmail());
        dateView.setText(listViewItem.getDate());

        if (fragment_from == FROM_BESTFRAGMENT)
            commentView.setText(blankForBestComment + listViewItem.getComment());
        else {
            commentView.setText("" + listViewItem.getComment());
            bestImage.setVisibility(View.GONE);
        }

        likeNumView.setText("" + listViewItem.getLike_num());

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 좋아요 버튼 눌렀을 경우
                InsertCommentLikeDataListener listener = new InsertCommentLikeDataListener(position, likeNumView);
                CommentInsertLikeDataRequest request = new CommentInsertLikeDataRequest(insertCommentLikeDataURL, listener, items.get(position).getComment_pk(), user_pk);
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

    public void addItem(int comment_pk, String email, String date, String comment, int reply_num, int like_num) {
        CommentListViewItem item = new CommentListViewItem();
        item.setComment_pk(comment_pk);
        item.setEmail(email);
        item.setDate(date);
        item.setComment(comment);
        item.setReply_num(reply_num);
        item.setLike_num(like_num);
        items.add(item);
    }

    class GetReplyCommentListener implements Response.Listener<String> {
        private int position;

        public GetReplyCommentListener(int position) {
            this.position = position;
        }

        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                boolean exist = jsonResponse.getBoolean("exist");
                if (exist) {
                    JSONArray result = jsonResponse.getJSONArray("result");
                    int num_result = jsonResponse.getInt("num_result");
                    for (int i = 0; i < num_result; i++) {
                        JSONObject temp = result.getJSONObject(i);
                        String email = temp.getString("email");
                        String date = temp.getString("date");
                        String reply = temp.getString("reply");
                        int like_num = temp.getInt("like_num");


                        Toast.makeText(context, "답글정보\n" + email + " " + date + " " + " " + reply + " " + like_num, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("답글없음", "답글이 없습니다.");
                }
            } catch (Exception e) {
                Log.e("답글리스너", e.getMessage());
            }
        }
    }

    class InsertCommentLikeDataListener implements Response.Listener<String> {
        private int position;
        private TextView likeNumView;

        public InsertCommentLikeDataListener(int position, TextView likeNumView) {
            this.position = position;
            this.likeNumView = likeNumView;
        }

        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                boolean exist = jsonResponse.getBoolean("exist");
                if (exist) {
                    Toast.makeText(context, "이미 '좋아요'를 누르셨습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        Toast.makeText(context, "이 댓글을 좋아합니다.", Toast.LENGTH_SHORT).show();
                        items.get(position).setLike_num(items.get(position).getLike_num() + 1);
                        likeNumView.setText(""+items.get(position).getLike_num());
                    } else {
                        Toast.makeText(context, "에러 : " + jsonResponse.getString("errmsg"), Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                Log.e("좋아요리스너", e.getMessage());
            }
        }
    }

    public void setFragment_from(int fragment_from) {
        this.fragment_from = fragment_from;
    }
}