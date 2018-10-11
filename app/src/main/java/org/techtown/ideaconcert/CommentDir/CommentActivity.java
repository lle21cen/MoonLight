package org.techtown.ideaconcert.CommentDir;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.techtown.ideaconcert.ActivityCodes;
import org.techtown.ideaconcert.R;

public class CommentActivity extends AppCompatActivity implements View.OnClickListener {

    ListView commentList;
    Button bestButton, allButton;
    CommentListViewAdapter adapter;
    private final String getCommentURL = "http://lle21cen.cafe24.com/GetComment.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        bestButton = findViewById(R.id.comment_best_btn);
        allButton = findViewById(R.id.comment_all_btn);

        Intent intent = getIntent();
        int item_pk = intent.getIntExtra("item_pk", 0);
        int contents_num = intent.getIntExtra("contents_num", 0);

        String item_title = intent.getStringExtra("item_title");
        TextView workNameView = findViewById(R.id.comment_work_name_txt);
        workNameView.setText(item_title + " " + contents_num + "화");
        workNameView.setOnClickListener(this);

        commentList = findViewById(R.id.comment_listview);
        adapter = new CommentListViewAdapter();

        CommentDBRequest commentDBRequest = new CommentDBRequest(getCommentURL, commentListener, item_pk);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(commentDBRequest);
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
                    int num_category_contents_data = jsonResponse.getInt("num_result");
                    for (int i = 0; i < num_category_contents_data; i++) {
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
                    commentList.setAdapter(adapter);
                } else {
                    Log.e("No Data", "데이터가 없습니다.");
                }
            } catch (Exception e) {
                Log.e("Comment Listener", e.getMessage());
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.comment_work_name_txt:
                setResult(ActivityCodes.COMMENT_SUCCESS);
                finish();
        }
    }
}
