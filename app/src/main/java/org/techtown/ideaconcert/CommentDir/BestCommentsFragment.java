package org.techtown.ideaconcert.CommentDir;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.techtown.ideaconcert.R;

public class BestCommentsFragment extends Fragment {

    private final String getCommentURL = "http://lle21cen.cafe24.com/GetComment.php";

    View view;
    CommentListViewAdapter adapter;
    ListView bestCommentListView;
    Button bestButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.comment_best_fragment, container, false);
        bestCommentListView = view.findViewById(R.id.best_comment_listview);
        adapter = new CommentListViewAdapter();
        Intent intent = getActivity().getIntent();
        int item_pk = intent.getIntExtra("item_pk", 0);
        try {
            CommentDBRequest commentDBRequest = new CommentDBRequest(getCommentURL, commentListener, item_pk);
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(commentDBRequest);
        } catch (Exception e) {
            Log.e("comment error", ""+e.getMessage());
        }
        return view;
    }

    private Response.Listener<String> commentListener = new Response.Listener<String>() {
        private String email, date, comment;
        private int reply_num, like_num;

        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                // php에서 받아온 JSON오브젝트 중에서 DB에 있던 값들의 배열을 JSON 배열로 변환
                JSONArray result = jsonResponse.getJSONArray("result");
                boolean exist = jsonResponse.getBoolean("exist");
                if (exist) {
                    int num_result = jsonResponse.getInt("num_result");
                    for (int i = 0; i < num_result; i++) {
                        // 데이터베이스에 들어있는 콘텐츠의 수만큼 for문을 돌려 layout에 image추가
                        try {
                            JSONObject temp = result.getJSONObject(i);
                            email = temp.getString("email");
                            date = temp.getString("date");
                            comment = temp.getString("comment");
                            reply_num = temp.getInt("reply_num");
                            like_num = temp.getInt("like_num");
                            adapter.addItem(email, date, comment, reply_num, like_num);
                        } catch (Exception e) {
                            Log.e("set item error", e.getMessage());
                        }
                    }
                    bestCommentListView.setAdapter(adapter);
                } else {
                    Log.e("No Data", "데이터가 없습니다.");
                }
            } catch (Exception e) {
                Log.e("Comment Listener", e.getMessage());
            }
        }
    };
}
