package org.techtown.ideaconcert.CommentDir;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
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

import org.json.JSONArray;
import org.json.JSONObject;
import org.techtown.ideaconcert.ActivityCodes;
import org.techtown.ideaconcert.DatabaseRequest;
import org.techtown.ideaconcert.R;

import java.util.ArrayList;

public class CommentRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    static final int FROM_BESTFRAGMENT = 1, FROM_ALLFRAGMENT = 2;
    final private String getCommentReplyURL = ActivityCodes.DATABASE_IP + "/platform/GetCommentReply";
    final private String insertCommentLikeDataURL = ActivityCodes.DATABASE_IP + "/platform/InsertCommentLikeData";
    final private String blankForBestComment = "                "; // 건드리지 말것!!
    private ArrayList<CommentListViewItem> items = new ArrayList<>();
    private int user_pk;
    private int fragment_from; // 1이면 BestCommentsFragment에서 호출 2이면 AllCommentFragment에서 호출

    public CommentRecyclerViewAdapter(int user_pk) {
        this.user_pk = user_pk;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_list_item, parent, false);
        return new CommentRecyclerViewAdapter.CommentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final CommentRecyclerViewAdapter.CommentViewHolder commentViewHolder = (CommentRecyclerViewAdapter.CommentViewHolder) holder;
        final CommentListViewItem item = items.get(position);

        if (item.getReplyAdapter() != null) {
            commentViewHolder.replyRecyclerView.setVisibility(View.VISIBLE);
            commentViewHolder.replyRecyclerView.swapAdapter(item.getReplyAdapter(), true);
        } else {
            commentViewHolder.replyRecyclerView.setVisibility(View.GONE);
        }

        commentViewHolder.emailView.setText(item.getEmail());
        commentViewHolder.dateView.setText(item.getDate());
        commentViewHolder.commentView.setText(item.getComment());
        commentViewHolder.likeNumView.setText("" + item.getLike_num());
        commentViewHolder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertCommentLikeDataListener listener = new InsertCommentLikeDataListener(view.getContext(), position, commentViewHolder.likeNumView);
                CommentInsertLikeDataRequest request = new CommentInsertLikeDataRequest(insertCommentLikeDataURL, listener, items.get(position).getComment_pk(), user_pk, 1); // tag=1 : 댓글 테이블에 정보 저장
                RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
                requestQueue.add(request);
            }
        });

        commentViewHolder.replyView.setText("답글 " + item.getReply_num());
        commentViewHolder.replyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Context context = view.getContext();

                if (items.get(position).getReplyAdapter() == null) {
                    GetReplyCommentListener replyCommentListener = new GetReplyCommentListener(position, context, commentViewHolder.replyRecyclerView);
                    DatabaseRequest request = new DatabaseRequest(replyCommentListener, getCommentReplyURL);

                    request.setComment_pk(item.getComment_pk());
                    RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
                    requestQueue.add(request);
                } else {
                    items.get(position).getReplyAdapter().clearItem();
                    items.get(position).setReplyAdapter(null);
                    commentViewHolder.replyRecyclerView.removeAllViews();
                }
            }
        });

        if (fragment_from == FROM_BESTFRAGMENT)
            commentViewHolder.commentView.setText(blankForBestComment + item.getComment());
        else {
            commentViewHolder.commentView.setText("" + item.getComment());
            commentViewHolder.bestImage.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
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

    void clearItem() {
        items.clear();
    }

    public void setFragment_from(int fragment_from) {
        this.fragment_from = fragment_from;
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {

        final TextView likeNumView;
        TextView emailView, dateView, commentView, replyView, accusationView;
        ImageView likeButton, bestImage;
        private RecyclerView replyRecyclerView;

        CommentViewHolder(View view) {
            super(view);
            emailView = view.findViewById(R.id.comment_item_email);
            dateView = view.findViewById(R.id.comment_item_date);
            commentView = view.findViewById(R.id.comment_item_comment);
            replyView = view.findViewById(R.id.comment_reply);
            likeNumView = view.findViewById(R.id.comment_item_like_num);
            accusationView = view.findViewById(R.id.comment_accusation);
            likeButton = view.findViewById(R.id.comment_like_btn);
            bestImage = view.findViewById(R.id.comment_best_img);

            replyRecyclerView = view.findViewById(R.id.comment_item_recycler_view);
            replyRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        }
    }

    class GetReplyCommentListener implements Response.Listener<String> {
        RecyclerView replyRecyclerView;
        private int position;
        private Context context;

        public GetReplyCommentListener(final int position, Context context, final RecyclerView replyRecyclerView) {
            this.position = position;
            this.context = context;
            this.replyRecyclerView = replyRecyclerView;
        }

        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                boolean exist = jsonResponse.getBoolean("exist");
                if (exist) {
                    ReplyRecyclerViewAdapter replyAdapter = new ReplyRecyclerViewAdapter(user_pk);
                    JSONArray result = jsonResponse.getJSONArray("result");
                    int num_result = jsonResponse.getInt("num_result");
                    for (int i = 0; i < num_result; i++) {
                        JSONObject temp = result.getJSONObject(i);
                        int reply_pk = temp.getInt("reply_pk");
                        String email = temp.getString("email");
                        String date = temp.getString("date");
                        String reply = temp.getString("reply");
                        int like_num = temp.getInt("like_num");

                        replyAdapter.addItem(reply_pk, email, date, reply, like_num);
                    }
                    items.get(position).setReplyAdapter(replyAdapter);
                    replyRecyclerView.setVisibility(View.VISIBLE);
                    replyRecyclerView.setAdapter(replyAdapter);
//                    Toast.makeText(context, "setAdapter " + position, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(context, "이 댓글을 좋아합니다.", Toast.LENGTH_SHORT).show();
                        items.get(position).setLike_num(items.get(position).getLike_num() + 1);
                        likeNumView.setText("" + items.get(position).getLike_num());
                    } else {
                        Toast.makeText(context, "에러 : " + jsonResponse.getString("errmsg"), Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                Log.e("댓글좋아요리스너", e.getMessage());
            }
        }
    }

}