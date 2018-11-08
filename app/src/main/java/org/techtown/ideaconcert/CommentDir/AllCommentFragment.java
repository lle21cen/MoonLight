package org.techtown.ideaconcert.CommentDir;

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
import org.techtown.ideaconcert.UserInformation;

public class AllCommentFragment extends Fragment {

    private final String getCommentURL = "http://lle21cen.cafe24.com/GetComment.php";

    View view;
    CommentListViewAdapter adapter;
    ListView allCommentListView;
    Button bestButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.comment_fragment, container, false);
        allCommentListView = view.findViewById(R.id.comment_listview);

        UserInformation userInformation = (UserInformation) getActivity().getApplication();
        int user_pk = userInformation.getUser_pk();
        adapter = new CommentListViewAdapter(allCommentListView, user_pk);
        adapter.setFragment_from(CommentListViewAdapter.FROM_ALLFRAGMENT);
        bestButton = getActivity().findViewById(R.id.comment_best_btn);

        Intent intent = getActivity().getIntent();
        int item_pk = intent.getIntExtra("item_pk", 0);

        try {
            CommentDBRequest commentDBRequest = new CommentDBRequest(getCommentURL, commentListener, item_pk, 2);
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(commentDBRequest);
        } catch (Exception e) {
            Log.e("comment error", ""+e.getMessage());
        }
        return view;
    }

    private Response.Listener<String> commentListener = new Response.Listener<String>() {
        private String email, date, comment;
        private int reply_num, like_num, comment_pk;

        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                JSONArray result = jsonResponse.getJSONArray("result");
                boolean exist = jsonResponse.getBoolean("exist");
                if (exist) {
                    int num_result = jsonResponse.getInt("num_result");
                    for (int i = 0; i < num_result; i++) {
                        try {
                            JSONObject temp = result.getJSONObject(i);
                            comment_pk = temp.getInt("comment_pk");
                            email = temp.getString("email");
                            date = temp.getString("date");
                            comment = temp.getString("comment");
                            reply_num = temp.getInt("reply_num");
                            like_num = temp.getInt("like_num");
                            adapter.addItem(comment_pk, email, date, comment, reply_num, like_num, 1);
                        } catch (Exception e) {
                            Log.e("댓글에러", e.getMessage());
                        }
                    }
                    allCommentListView.setAdapter(adapter);
                    bestButton.setText("BEST("+num_result+")");
                } else {
                    Log.e("댓글없음", "댓글이 없습니다.");
                }
            } catch (Exception e) {
                Log.e("댓글리스너에러", e.getMessage());
            }
        }
    };
}
