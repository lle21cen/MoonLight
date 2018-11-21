package org.techtown.ideaconcert.CommentDir;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
import org.techtown.ideaconcert.ActivityCodes;
import org.techtown.ideaconcert.R;

import java.util.ArrayList;

public class ReplyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    final private String insertCommentLikeDataURL = ActivityCodes.DATABASE_IP + "/platform/InsertCommentLikeData";

    private ArrayList<CommentListViewItem> items = new ArrayList<>();
    private int user_pk;

    public ReplyRecyclerViewAdapter(int user_pk) {
        this.user_pk = user_pk;
    }

    public static class ReplyViewHolder extends RecyclerView.ViewHolder {

        TextView emailView, dateView, commentView, likeNumView, accusationView;
        ImageView likeButton;

        ReplyViewHolder(View view) {
            super(view);
            emailView = view.findViewById(R.id.reply_item_email);
            dateView = view.findViewById(R.id.reply_item_date);
            commentView = view.findViewById(R.id.reply_item_comment);
            likeNumView = view.findViewById(R.id.reply_item_like_num);
            accusationView = view.findViewById(R.id.reply_accusation);
            likeButton = view.findViewById(R.id.reply_like_btn);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_reply_item, parent, false);
        return new ReplyRecyclerViewAdapter.ReplyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ReplyRecyclerViewAdapter.ReplyViewHolder replyViewHolder = (ReplyRecyclerViewAdapter.ReplyViewHolder) holder;
        final CommentListViewItem item = items.get(position);

        replyViewHolder.emailView.setText(item.getEmail());
        replyViewHolder.dateView.setText(item.getDate());
        replyViewHolder.commentView.setText(item.getComment());
        replyViewHolder.likeNumView.setText(""+item.getLike_num());
        replyViewHolder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertCommentLikeDataListener listener = new InsertCommentLikeDataListener(view.getContext(), position, replyViewHolder.likeNumView);
                CommentInsertLikeDataRequest request = new CommentInsertLikeDataRequest(insertCommentLikeDataURL, listener, items.get(position).getComment_pk(), user_pk, 2); // tag=2 : 답글 테이블에 정보 저장
                RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
                requestQueue.add(request);
            }
        });
    }

    class InsertCommentLikeDataListener implements Response.Listener<String> {
        private int position;
        private TextView likeNumView;
        private Context context;

        public InsertCommentLikeDataListener(Context context, int position, TextView likeNumView) {
            this.context = context;
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
                        Toast.makeText(context, "이 답글을 좋아합니다.", Toast.LENGTH_SHORT).show();
                        items.get(position).setLike_num(items.get(position).getLike_num() + 1);
                        likeNumView.setText("" + items.get(position).getLike_num());
                    } else {
                        Toast.makeText(context, "에러 : " + jsonResponse.getString("errmsg"), Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                Log.e("답글좋아요리스너", e.getMessage());
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(int comment_pk, String email, String date, String comment, int like_num) {
        CommentListViewItem item = new CommentListViewItem();
        item.setComment_pk(comment_pk);
        item.setEmail(email);
        item.setDate(date);
        item.setComment(comment);
        item.setLike_num(like_num);
        items.add(item);
    }

    void clearItem() {
        items.clear();
    }
}