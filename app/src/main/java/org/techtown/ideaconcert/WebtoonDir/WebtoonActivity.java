package org.techtown.ideaconcert.WebtoonDir;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.techtown.ideaconcert.ActivityCodes;
import org.techtown.ideaconcert.ContentsMainDir.WorksListViewItem;
import org.techtown.ideaconcert.MainActivityDir.GetBitmapImageFromURL;
import org.techtown.ideaconcert.R;

import java.net.URL;
import java.util.ArrayList;

public class WebtoonActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout headerLayout, footerLayout, webtoonLayout;
    TextView title_text, star_rating_text, like_count_text, comments_count_text, order_text;
    Button option_btn, comments_btn, prev_btn, next_btn;
    private final String getContentsItemImageURL = "http://lle21cen.cafe24.com/GetContentsItemImage.php";

//    int item_pk, item_comments_count, item_num;
//    String item_title;
    int item_position;


    // 시리얼 라이저블 다 삭제 !! 안됨 !!
    ArrayList<WorksListViewItem> items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webtoon);

        headerLayout = findViewById(R.id.webtoon_header_layout);
        footerLayout = findViewById(R.id.webtoon_footer_layout);
        webtoonLayout = findViewById(R.id.main_webtoon_layout);

        title_text = findViewById(R.id.webtoon_title); // 상단, 웹툰 제목
        star_rating_text = findViewById(R.id.webtoon_star_rating); // 상단, 웹툰 별점
        like_count_text = findViewById(R.id.webtoon_like_count); // 하단, 좋아요 수
        comments_count_text = findViewById(R.id.webtoon_comments_count); // 하단, 댓글 수
        order_text = findViewById(R.id.webtoon_order); // 하단, 현재 몇화

        option_btn = findViewById(R.id.webtoon_contents_option); // 상단, 옵션 확대 버튼
        comments_btn = findViewById(R.id.webtoon_comments_btn); // 하단, 댓글 버튼
        prev_btn = findViewById(R.id.webtoon_prev_btn); // 하단, 이전 화 보기 버튼
        next_btn = findViewById(R.id.webtoon_next_btn); // 하단, 다음 화 보기 버튼

        Intent intent = getIntent();
//        item_pk = intent.getIntExtra("item_pk", 0); // 0일 경우 오류 처리 - 귀찮
//        item_title = intent.getStringExtra("item_title");
//        item_comments_count = intent.getIntExtra("item_comments_count", 0);
        item_position = intent.getIntExtra("position", 0); // 0일 경우 오류 처리 - 귀찮
        items = (ArrayList<WorksListViewItem>) intent.getSerializableExtra("items");

        title_text.setText(items.get(item_position).getWorksTitle() + " " + items.get(item_position).getContentsNum() + "화");
        comments_count_text.setText(""+items.get(item_position).getCommentCount());
        order_text.setText(""+items.get(item_position).getContentsNum());

        ContentsItemImageRequest contentsItemImageRequest
                = new ContentsItemImageRequest(getContentsItemImageURL, getItemImageListener, items.get(item_position).getContentsItemPk());
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
    }

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
                    for (int i = 0; i < num_category_contents_data; i++) {
                        // 데이터베이스에 들어있는 컨텐츠의 수만큼 for문을 돌려 layout에 image추가
                        try {
                            JSONObject temp = result.getJSONObject(i);

                            URL url = new URL(temp.getString("image_urls"));
                            GetBitmapImageFromURL getBitmapImageFromURL = new GetBitmapImageFromURL(url);
                            getBitmapImageFromURL.start();
                            getBitmapImageFromURL.join();
                            Bitmap bitmap = getBitmapImageFromURL.getBitmap();

                            ImageView webtoon = new ImageView(WebtoonActivity.this);
                            webtoon.setImageBitmap(bitmap);
                            webtoon.setAdjustViewBounds(true); // 이미지의 가로를 화면 전체 크기에 맞춤.
                            webtoon.setLayoutParams(lp);

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.webtoon_back:
                setResult(ActivityCodes.WEBTOON_SUCCESS);
                finish();
                break;
            case R.id.webtoon_title:
                setResult(ActivityCodes.WEBTOON_SUCCESS);
                finish();
                break;
            case R.id.webtoon_contents_option:

                break;
            case R.id.webtoon_like_btn:
                break;
            case R.id.webtoon_comments_btn:
                break;
            case R.id.webtoon_prev_btn:
//                if (item_pk > 1) {
//                    Intent intent = new Intent(WebtoonActivity.this, WebtoonActivity.class);
//                    intent.putExtra("item_pk", items.get());
//                    intent.putExtra("item_title", item_title);
//                    intent.putExtra("item_comments_count", item_comments_count);
//                    intent.putExtra("item_num", item_num);
//                }
                break;
            case R.id.webtoon_next_btn:
                break;
            case R.id.main_webtoon_layout:
                if (headerLayout.getVisibility() == View.GONE) {
                    headerLayout.setVisibility(View.VISIBLE);
                    footerLayout.setVisibility(View.VISIBLE);
                } else {
                    headerLayout.setVisibility(View.GONE);
                    footerLayout.setVisibility(View.GONE);
                }
                break;
        }
    }
}