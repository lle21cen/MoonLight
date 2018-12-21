package org.techtown.ideaconcert.CommentDir;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
import org.techtown.ideaconcert.ActivityCodes;
import org.techtown.ideaconcert.R;
import org.techtown.ideaconcert.UserInformation;

public class CommentActivity extends AppCompatActivity implements View.OnClickListener {

    static EditText commentEditText;
    static int comment_pk; // 답글달기 시 어떤 댓글이 선택되었는지 Handler를 통해 알아옴
    public static Handler setCommentTextHintHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            try {
                Bundle bundle = message.getData();
                boolean isReply = bundle.getBoolean("isReply");
                if (isReply) {
                    String commentEmail = bundle.getString("commentEmail");
                    comment_pk = bundle.getInt("comment_pk");
                    commentEditText.setHint(commentEmail + "님에게 답글쓰기");
                    commentEditText.requestFocus();
                } else {
                    commentEditText.setHint("댓글을 입력하세요 :)");
                }
                return true;
            } catch (Exception e) {
                Log.e("댓글달기 힌트 재설정 에러", e.getMessage());
                return false;
            }
        }
    });
    static int sel_position;
    private final String insertDeleteCommentURL = ActivityCodes.DATABASE_IP + "/platform/InsertDeleteComment";
    private final String insertDeleteReplyURL = ActivityCodes.DATABASE_IP + "/platform/InsertDeleteReply";
    Button bestButton, allButton;
    ViewPager commentPager;
    private Response.Listener<String> commentInsertListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                boolean success = jsonResponse.getBoolean("success");
                if (success) {
                    Toast.makeText(CommentActivity.this, "댓글이 달렸습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = getIntent();
                    intent.putExtra("isRefresh", true);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivityForResult(intent, ActivityCodes.COMMENT_REQUEST);
                    finish();
                } else {
                    Toast.makeText(CommentActivity.this, "댓글 달기에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    String errmsg = jsonResponse.getString("errmsg");
                    Log.e("댓글달기실패이유", "" + errmsg);
                }
            } catch (Exception e) {
                Log.e("댓글 삽입 삭제 리스너", e.getMessage());
            }
        }
    };
    private Response.Listener<String> ReplyInsertListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                boolean success = jsonResponse.getBoolean("success");
                if (success) {
                    Toast.makeText(CommentActivity.this, "답글이 달렸습니다.", Toast.LENGTH_SHORT).show();
                    // 답글 입력 시, 자동으로 답글 개수가 늘어나고, 그 답글이 자동으로 보이도록 하고 싶은데 망했음
                    // ExpandableListView 로 바꾸는 작업부터 다시 시작해서 싹 다 갈아엎어야함
                    Intent intent = getIntent();
                    intent.putExtra("isRefresh", false);
                    intent.putExtra("sel_comment_pk", comment_pk);
                    intent.putExtra("cur_fragment_num", commentPager.getCurrentItem());
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivityForResult(intent, ActivityCodes.COMMENT_REQUEST);
                } else {
                    Toast.makeText(CommentActivity.this, "댓글/답글 달기에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    String errmsg = jsonResponse.getString("errmsg");
                    Log.e("댓글달기실패이유", "" + errmsg);
                }
            } catch (Exception e) {
                Log.e("댓글 삽입 삭제 리스너", e.getMessage());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        commentEditText = findViewById(R.id.comment_edit_text);
        bestButton = findViewById(R.id.comment_best_btn);
        allButton = findViewById(R.id.comment_all_btn);
        bestButton.setOnClickListener(this);
        allButton.setOnClickListener(this);

        Button enterButton = findViewById(R.id.comment_enter);
        enterButton.setOnClickListener(this);

        commentPager = findViewById(R.id.comment_view_pager);
        CommentPagerAdapter pagerAdapter = new CommentPagerAdapter(getSupportFragmentManager());
        commentPager.setAdapter(pagerAdapter);
        commentPager.setCurrentItem(0);
        commentPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setButtonAndBackgroundColor(position);
                commentEditText.setHint(getString(R.string.input_comment)); // pager를 넘겼을 때 답글이 아닌 댓글 입력이 되도록 설정
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Intent intent = getIntent();
        int contents_num = intent.getIntExtra("contents_num", 0);
        boolean isRefresh = intent.getBooleanExtra("isRefresh", false);
        if (isRefresh)
            commentPager.setCurrentItem(1, true);
        else {
            int cur_fragment_num = intent.getIntExtra("cur_fragment_num", 3);
            if (cur_fragment_num < 3) {
                commentPager.setCurrentItem(cur_fragment_num, true); // 답글 입력시 있었던 Fragment로 전환
            }
        }

        String item_title = intent.getStringExtra("item_title");
        TextView workNameView = findViewById(R.id.comment_work_name_txt);
        workNameView.setText(item_title + " " + contents_num + "화");
        workNameView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.comment_work_name_txt:
                setResult(ActivityCodes.COMMENT_SUCCESS);
                finish();
                break;
            case R.id.comment_best_btn:
                setButtonAndBackgroundColor(0);
                commentPager.setCurrentItem(0);
                break;
            case R.id.comment_all_btn:
                setButtonAndBackgroundColor(1);
                commentPager.setCurrentItem(1);
                break;
            case R.id.comment_enter:
                String comment = commentEditText.getText().toString();
                if (comment.isEmpty()) {
                    Toast.makeText(this, "문자를 입력하세요", Toast.LENGTH_SHORT).show();
                    break;
                }
                UserInformation userInformation = (UserInformation) getApplication();
                String email = userInformation.getUserEmail();

                Intent intent = getIntent();
                int item_pk = intent.getIntExtra("item_pk", 0);

                if (commentEditText.getHint().toString().contains("댓글")) {
                    CommentInsertDeleteRequest request = new CommentInsertDeleteRequest(insertDeleteCommentURL, commentInsertListener, 1, email, comment, item_pk);
                    RequestQueue requestQueue = Volley.newRequestQueue(this);
                    requestQueue.add(request);
                } else if (commentEditText.getHint().toString().contains("답글") && comment_pk != 0) {
                    ReplyDBRequest request = new ReplyDBRequest(insertDeleteReplyURL, ReplyInsertListener, 1, email, comment, comment_pk); // comment == reply
                    RequestQueue requestQueue = Volley.newRequestQueue(this);
                    requestQueue.add(request);
                }
                break;
        }
    }

    private void setButtonAndBackgroundColor(int position) {
        if (position == 0) {
            bestButton.setTextColor(Color.rgb(120, 106, 170));
            bestButton.setBackgroundColor(Color.rgb(255, 255, 255));
            allButton.setTextColor(Color.rgb(255, 255, 255));
            allButton.setBackgroundColor(Color.rgb(120, 106, 170));
        } else if (position == 1) {
            allButton.setTextColor(Color.rgb(120, 106, 170));
            allButton.setBackgroundColor(Color.rgb(255, 255, 255));
            bestButton.setTextColor(Color.rgb(255, 255, 255));
            bestButton.setBackgroundColor(Color.rgb(120, 106, 170));
        }
    }
}
