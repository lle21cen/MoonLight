package org.techtown.ideaconcert.WebtoonDir;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.ideaconcert.ActivityCodes;
import org.techtown.ideaconcert.CommentDir.CommentActivity;
import org.techtown.ideaconcert.ContentsMainDir.ContentsMainActivity;
import org.techtown.ideaconcert.ContentsMainDir.WorksListViewItem;
import org.techtown.ideaconcert.MainActivityDir.SetBitmapImageFromUrlTask;
import org.techtown.ideaconcert.R;
import org.techtown.ideaconcert.SQLiteDir.DBHelper;
import org.techtown.ideaconcert.SQLiteDir.DBNames;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WebtoonActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout headerLayout, footerLayout, webtoonLayout, contents_option_menu;
    TextView title_text, star_rating_text, like_count_text, comments_count_text, order_text;
    Button option_btn, comments_btn, prev_btn, next_btn, like_btn;
    private final String getContentsItemImageURL = ActivityCodes.DATABASE_IP + "/platform/GetContentsItemImage";
    private final String getContentsLIkeCountURL = ActivityCodes.DATABASE_IP + "/platform/GetContentsLikeCount";
    private final String insertDeleteContentsItemLikeDataURL = ActivityCodes.DATABASE_IP + "/platform/InsertDeleteContentsItemLikeData";
    private final String PlusViewCountURL = ActivityCodes.DATABASE_IP + "/platform/PlusViewCount";
//    private final String PlusViewCountURL = "http://lle21cen.cafe24.com/PlusViewCount.php";

    int user_pk, contents_item_pk, item_comments_count, contents_num, contents_pk;
    String item_title;
    int item_position;

    private boolean is_like_clicked;
    ListView contents_listView;
    ArrayList<WorksListViewItem> items;

    private int deviceWidth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webtoon);

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        deviceWidth = dm.widthPixels;
        if (deviceWidth > 1000) {
            deviceWidth /= 2;
        }

        LinearLayout commentLayout = findViewById(R.id.webtoon_comment_layout);
        headerLayout = findViewById(R.id.webtoon_header_layout);
        footerLayout = findViewById(R.id.webtoon_footer_layout);
        webtoonLayout = findViewById(R.id.main_webtoon_layout);
        contents_option_menu = findViewById(R.id.webtoon_option_layout);

        title_text = findViewById(R.id.webtoon_title); // 상단, 웹툰 제목
        star_rating_text = findViewById(R.id.webtoon_star_rating); // 상단, 웹툰 별점
        like_count_text = findViewById(R.id.webtoon_like_count); // 하단, 좋아요 수
        comments_count_text = findViewById(R.id.webtoon_comments_count); // 하단, 댓글 수
        order_text = findViewById(R.id.webtoon_order); // 하단, 현재 몇화

        option_btn = findViewById(R.id.webtoon_contents_option); // 상단, 옵션 확대 버튼
        comments_btn = findViewById(R.id.webtoon_comments_btn); // 하단, 댓글 버튼
        prev_btn = findViewById(R.id.webtoon_prev_btn); // 하단, 이전 화 보기 버튼
        next_btn = findViewById(R.id.webtoon_next_btn); // 하단, 다음 화 보기 버튼
        like_btn = findViewById(R.id.webtoon_like_btn); // 하단, 좋아요 버튼

        items = ContentsMainActivity.itemList;
        Intent intent = getIntent();
        item_position = intent.getIntExtra("position", 0); // 0일 경우 오류 처리 - 귀찮
        contents_item_pk = items.get(item_position).getContentsItemPk(); // 0일 경우 오류 처리 - 귀찮
        item_title = items.get(item_position).getWorksTitle();
        item_comments_count = items.get(item_position).getCommentCount();
        contents_num = items.get(item_position).getContentsNum();
        contents_pk = intent.getIntExtra("contents_pk", 0);

        title_text.setText(item_title + " " + contents_num + "화");
        comments_count_text.setText("" + item_comments_count);
        order_text.setText("" + contents_num);
        star_rating_text.setText("" + items.get(item_position).getStar_rating());

        contents_listView = findViewById(R.id.webtoon_contents_num_listview);
        final ContentsNumListViewAdapter adapter = new ContentsNumListViewAdapter();
        for (int i = 0; i < items.size(); i++) {
            adapter.addItem(items.get(i).getContentsNum());
        }
        contents_listView.setAdapter(adapter); // 숫자를 누르면 작품 회차 목록이 나오도록 하는 listview와 adapter

        ContentsItemImageRequest contentsItemImageRequest
                = new ContentsItemImageRequest(getContentsItemImageURL, getItemImageListener, contents_item_pk);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(contentsItemImageRequest);

        Button backBtn = findViewById(R.id.webtoon_back);
        backBtn.setOnClickListener(this);
        webtoonLayout.setOnClickListener(this);
        title_text.setOnClickListener(this);
        prev_btn.setOnClickListener(this);
        next_btn.setOnClickListener(this);
        option_btn.setOnClickListener(this);
        comments_btn.setOnClickListener(this);
        order_text.setOnClickListener(this);
        comments_count_text.setOnClickListener(this);
        like_btn.setOnClickListener(this);
        like_count_text.setOnClickListener(this);
        commentLayout.setOnClickListener(this);

        contents_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // 리스트뷰에서 선택된 아이템에 몇화라고 적혀있는지 읽은 다음 텍스트를 int로 바꾸고 items 배열에서 그 값이 있는 위치를 intent에 포함하여 액티비티 재시작.
                ArrayList<ContentsNumListViewItem> contentsItems = adapter.getContentsNumListViewItems();
                int select_num = contentsItems.get(position).getContents_num();
                int next_position = 1;
                for (int i = 0; i < items.size(); i++) {
                    WorksListViewItem tempItem = items.get(i);
                    if (tempItem.getContentsNum() == select_num) {
                        next_position = i;
                        break;
                    }
                }
                Intent intent = new Intent(WebtoonActivity.this, WebtoonActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("position", next_position);
                startActivity(intent);
                finish();
            }
        });

        // 컨텐츠 아이템의 좋아요 개수를 DB에서 불러와 설정
        SharedPreferences sharedPreferences = getSharedPreferences("loginData", MODE_PRIVATE);
        user_pk = sharedPreferences.getInt("userPk", 0);
        if (user_pk == 0)
            user_pk = 1;

        ContentsItemLikeDBRequest contentsItemLikeDBRequest = new ContentsItemLikeDBRequest(getContentsLIkeCountURL, getContentsItemLikeCountListener, contents_item_pk, user_pk, 2);
        requestQueue.add(contentsItemLikeDBRequest);

        addRecentViewDataToSqlite();
        insertContentsUrlToSqlite(contents_pk, items.get(item_position).getThumbnail_url());

        // 웹툰 뷰 카운트 증가 시키기
        plusViewCount();
    }

    private void plusViewCount() {
        PlusViewCountRequest request = new PlusViewCountRequest(PlusViewCountURL, plusViewCountListener, contents_item_pk);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private Response.Listener<String> plusViewCountListener = new Response.Listener<String>() {
        // 사용자가 웹튼을 보려고 할 경우 웹툰의 뷰카운트를 1증가 시키는 리스너
        // 실패시 로그에 오류 메시지를 출력하고 종료
        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean success = jsonObject.getBoolean("success");
                if (success) {

                }
                else {
                    Log.e("뷰카운트증가실패", ""+jsonObject.getString("errmsg"));
                }
            } catch (JSONException je) {
                Log.e("뷰카운트증가리스너 1", ""+je.getMessage());
            } catch (Exception e) {
                Log.e("뷰카운트증가리스너 2", ""+e.getMessage());
            }
        }
    };

    private Response.Listener<String> getContentsItemLikeCountListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                boolean exist = jsonResponse.getBoolean("exist");
                int count = jsonResponse.getInt("count");
                like_count_text.setText("" + count);
                if (exist) {
                    like_btn.setBackgroundDrawable(ContextCompat.getDrawable(WebtoonActivity.this, R.drawable.pick_2));
                    is_like_clicked = true;
                } else {
                    like_btn.setBackgroundDrawable(ContextCompat.getDrawable(WebtoonActivity.this, R.drawable.ic_favorite_border_white_24dp));
                    is_like_clicked = false;
                }
            } catch (Exception e) {
                Log.e("좋아요를", e.getMessage());
            }
        }
    };

    private Response.Listener<String> getItemImageListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                // php에서 받아온 JSON오브젝트 중에서 DB에 있던 값들의 배열을 JSON 배열로 변환
                JSONArray result = jsonResponse.getJSONArray("result");
                boolean exist = jsonResponse.getBoolean("exist");

                if (exist) {
                    int num_category_contents_data = jsonResponse.getInt("num_result");
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    lp.setMargins(10, 10, 10, 10);
                    for (int i = 0; i < num_category_contents_data; i++) {
                        // 데이터베이스에 들어있는 컨텐츠의 수만큼 for문을 돌려 layout에 image추가
                        try {
                            JSONObject temp = result.getJSONObject(i);

                            String url_str = temp.getString("image_url");

                            ImageView webtoon = new ImageView(WebtoonActivity.this);
                            webtoon.setAdjustViewBounds(true); // 이미지의 가로를 화면 전체 크기에 맞춤
                            webtoon.setLayoutParams(lp);

                            SetBitmapImageFromUrlTask task = new SetBitmapImageFromUrlTask(webtoon, deviceWidth, 0);
                            task.execute(url_str);

//                            URL url = new URL(ActivityCodes.DATABASE_IP + temp.getString("image_url"));
//                            GetBitmapImageFromURL getBitmapImageFromURL = new GetBitmapImageFromURL(url);
//                            getBitmapImageFromURL.start();
//                            getBitmapImageFromURL.join();
//                            webtoon.setImageBitmap(getBitmapImageFromURL.getBitmap());

                            webtoonLayout.addView(webtoon);
                        } catch (Exception e) {
                            Log.e("Except in webtoon", e.getMessage());
                        }
                    }

                } else {
                    Log.e("No Data", "데이터가 없습니다.");
                }
            } catch (Exception e) {
                Log.e("works listener", e.getMessage());
            }
        }
    };


    private Response.Listener<String> successCheckListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean success = jsonObject.getBoolean("success");
                if (success) {
                    int likeCount = Integer.parseInt(like_count_text.getText().toString());
                    if (!is_like_clicked) {
                        is_like_clicked = true;
                        Toast.makeText(WebtoonActivity.this, "이 작품을 좋아합니다.", Toast.LENGTH_SHORT).show();
                        like_btn.setBackgroundDrawable(ContextCompat.getDrawable(WebtoonActivity.this, R.drawable.pick_2));
                        like_count_text.setText(String.valueOf(likeCount + 1));
                    } else {
                        Toast.makeText(WebtoonActivity.this, "취소되었습니다.", Toast.LENGTH_SHORT).show();
                        is_like_clicked = false;
                        like_count_text.setText(String.valueOf(likeCount - 1));
                        like_btn.setBackgroundDrawable(ContextCompat.getDrawable(WebtoonActivity.this, R.drawable.ic_favorite_border_white_24dp));
                    }
                } else {
                    Log.e("웹툰좋아요삽입삭제실패", jsonObject.getString("errmsg"));
                }
            } catch (Exception e) {
                Log.e("웹툰좋아요삽입삭제오류", e.getMessage());
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.webtoon_back:
            case R.id.webtoon_title:
                setResult(ActivityCodes.WEBTOON_SUCCESS);
                finish();
                break;
            case R.id.webtoon_contents_option:
                if (contents_option_menu.getVisibility() == View.GONE)
                    contents_option_menu.setVisibility(View.VISIBLE);
                else
                    contents_option_menu.setVisibility(View.GONE);
                break;
            case R.id.webtoon_like_btn:
            case R.id.webtoon_like_count:
                InsertDeleteContentsLikeRequest insertDeleteContentsLikeRequest;
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                if (!is_like_clicked) {
                    insertDeleteContentsLikeRequest
                            = new InsertDeleteContentsLikeRequest(insertDeleteContentsItemLikeDataURL, successCheckListener, contents_item_pk, user_pk, 1); // 1 : INSERT
                } else {
                    insertDeleteContentsLikeRequest
                            = new InsertDeleteContentsLikeRequest(insertDeleteContentsItemLikeDataURL, successCheckListener, contents_item_pk, user_pk, 2); // 2 : DELETE
                }
                requestQueue.add(insertDeleteContentsLikeRequest);
                break;
            case R.id.webtoon_comment_layout:
            case R.id.webtoon_comments_btn:
            case R.id.webtoon_comments_count:
                Intent intent = new Intent(WebtoonActivity.this, CommentActivity.class);
                intent.putExtra("item_pk", contents_item_pk);
                intent.putExtra("item_title", item_title);
                intent.putExtra("contents_num", contents_num);
                startActivityForResult(intent, ActivityCodes.COMMENT_REQUEST);
                break;
            case R.id.webtoon_prev_btn:
                if (contents_num > 1) {
                    resumeAtNewPos(true);
                } else {
                    Toast.makeText(this, "이전 화가 없습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.webtoon_next_btn:
                if (contents_num < items.size()) {
                    resumeAtNewPos(false);
                } else {
                    Toast.makeText(this, "다음 화가 없습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.main_webtoon_layout:
                if (headerLayout.getVisibility() == View.GONE) {
                    headerLayout.setVisibility(View.VISIBLE);
                    footerLayout.setVisibility(View.VISIBLE);
                } else {
                    headerLayout.setVisibility(View.GONE);
                    footerLayout.setVisibility(View.GONE);
                }
                contents_option_menu.setVisibility(View.GONE);
                contents_listView.setVisibility(View.GONE);
                break;
            case R.id.webtoon_order:
                if (contents_listView.getVisibility() == View.GONE)
                    contents_listView.setVisibility(View.VISIBLE);
                else
                    contents_listView.setVisibility(View.GONE);
                break;

        }
    }

    void resumeAtNewPos(boolean flag) {
        Intent intent = new Intent(WebtoonActivity.this, WebtoonActivity.class);
        int new_position;
        for (new_position = item_position - 1; new_position <= new_position + 1; new_position++) {
            if (new_position < 0 || new_position >= items.size()) continue;
            if (flag && items.get(new_position).getContentsNum() == (contents_num - 1))
                break;
            else if (!flag && items.get(new_position).getContentsNum() == (contents_num + 1))
                break;
        }
        intent.putExtra("position", new_position);
        startActivity(intent);
        finish();
    }

    private void addRecentViewDataToSqlite() {
        // 마이페이지 최근 본 웹툰 목록에 웹툰 이름과 현재 날짜 저장하기
        Date today = new Date();
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd kk:mm"); // 초단위로 저장하여 가장 최근에 본 작품을 갱신.
        DBHelper dbHelper = new DBHelper(this, DBNames.CONTENTS_DB, null, 1);
        dbHelper.insertOrUpdateRecentViewData(contents_pk, item_title, date.format(today), String.valueOf(today), contents_num);
    }

    private void insertContentsUrlToSqlite(int contents_pk, String url) {
        DBHelper dbHelper = new DBHelper(this, DBNames.CONTENTS_DB, null, 1);
        dbHelper.insertContentsUrl(contents_pk, url);
    }
}