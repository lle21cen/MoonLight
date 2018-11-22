package org.techtown.ideaconcert.ContentsMainDir;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.techtown.ideaconcert.ActivityCodes;
import org.techtown.ideaconcert.MainActivityDir.GetBitmapImageFromURL;
import org.techtown.ideaconcert.MainActivityDir.SetBitmapImageFromUrlTask;
import org.techtown.ideaconcert.R;
import org.techtown.ideaconcert.SQLiteDir.DBHelper;
import org.techtown.ideaconcert.SQLiteDir.DBNames;
import org.techtown.ideaconcert.WebtoonDir.WebtoonActivity;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

public class ContentsMainActivity extends AppCompatActivity implements View.OnClickListener {

    // 미완성 구현 사항 :
    // 이미지만 따로 동적으로 설정하도록 바꾸어서 컨텐츠 로딩에 걸리는 시간을 단축할 필요가 있음
    // 변수명 통일 시키기 ... 귀찮

//    private final String getContentsItemURL = ActivityCodes.DATABASE_IP + "/platform/GetContentsItem";
    private final String getContentsItemURL = "http://lle21cen.cafe24.com/GetContentsItem.php";

    private final String getContentsLIkeCountURL = ActivityCodes.DATABASE_IP + "/platform/GetContentsLikeCount";
    private final String insertDeleteContentsLikeDataURL = ActivityCodes.DATABASE_IP + "/platform/InsertDeleteContentsLikeData";

    private int selected_contents_pk;
    private boolean is_like_clicked = false, is_summary_opened = false;

    private Button first_episode_btn, contents_like_btn, summary_btn, follow_up_btn;
    private ListView listView;
    private TextView totalText, readingText, cashText, titlebar_title, info_title, info_writer, info_painter,
            info_view_count, contents_like_count, summary_text, list_total_txt;
    private WorksListViewAdapter adapter;
    private Spinner sortSpinner;
    private ImageView thumbnail, back_btn;

    public static ArrayList<WorksListViewItem> itemList; // WebtonActivity에서도 사용하기 위해 public static으로 선언
    private int user_pk;

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityCodes.WEBTOON_REQUEST) {
            Intent intent = new Intent(ContentsMainActivity.this, ContentsMainActivity.class);
            intent.putExtra("contents_pk", selected_contents_pk);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents_main);

        Intent intent = getIntent();
        selected_contents_pk = intent.getIntExtra("contents_pk", 0);

        // TextView 초기화
        totalText = findViewById(R.id.contents_main_status_total); // 전체 몇 편
        readingText = findViewById(R.id.contents_main_status_reading); // 그 중 몇 편을 열람
        cashText = findViewById(R.id.contents_main_status_cash); // 보유 캐시
        info_title = findViewById(R.id.contents_main_info_title);
        info_writer = findViewById(R.id.contents_main_info_writer);
        info_painter = findViewById(R.id.contents_main_info_painter);
        info_view_count = findViewById(R.id.contents_main_info_view_count);
        titlebar_title = findViewById(R.id.contents_main_title_bar_title);
        contents_like_count = findViewById(R.id.contents_main_info_like_count);
        summary_text = findViewById(R.id.contents_main_summary);
        list_total_txt = findViewById(R.id.contents_main_list_total_txt);

        titlebar_title.setOnClickListener(this);

        thumbnail = findViewById(R.id.contents_main_info_img);
        back_btn = findViewById(R.id.contents_main_back);

        back_btn.setOnClickListener(this);

        first_episode_btn = findViewById(R.id.contents_main_first_episode); // 첫화보기 버튼
        contents_like_btn = findViewById(R.id.contents_main_like_btn);
        summary_btn = findViewById(R.id.contents_main_open_summary_btn);
        follow_up_btn = findViewById(R.id.contents_main_follow_up_button);
        first_episode_btn.setOnClickListener(this);
        contents_like_btn.setOnClickListener(this);
        summary_btn.setOnClickListener(this);
        follow_up_btn.setOnClickListener(this);

        listView = findViewById(R.id.contents_main_list_works_list);
        adapter = new WorksListViewAdapter();

        WorksDBRequest worksDBRequest = new WorksDBRequest(getContentsItemURL, getContentsItemListener, selected_contents_pk);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(worksDBRequest);

        sortSpinner = findViewById(R.id.contents_main_list_sort);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, R.layout.contents_main_works_list_spinner_item, getResources().getStringArray(R.array.sort_method));
        sortSpinner.setAdapter(spinnerAdapter);

        // 스피너 : "최신편부터", "첫편부터" 두 개의 아이템을 가지고 있으며 기본은 '최신편부터' 이고 한번 누를때마다 ArrayList의 순서를 바꾸어 반대로 보여지게 된다.
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                // Spinner가 눌릴 때마다 ArrayList를 뒤집어서 보여주는 코드
                ArrayList<WorksListViewItem> items = adapter.getWorksListViewItems();
                if (items.size() != 0) {
                    if ((position == 0 && items.get(0).getContentsNum() == 1) || (position == 1 && items.get(0).getContentsNum() != 1)) {
                        Collections.reverse(items);
                        listView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(ContentsMainActivity.this, WebtoonActivity.class);
                putExtraData(intent, position);
                startActivityForResult(intent, ActivityCodes.WEBTOON_REQUEST);
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("loginData", MODE_PRIVATE);
        user_pk = sharedPreferences.getInt("userPk", 0);
        if (user_pk == 0)
            user_pk = 1;
        ContentsLikeDBRequest contentsItemLikeDBRequest = new ContentsLikeDBRequest(getContentsLIkeCountURL, getContentsLikeCountListener, selected_contents_pk, user_pk, 1);
        requestQueue.add(contentsItemLikeDBRequest);
    }

    private Response.Listener<String> getContentsLikeCountListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean exist = jsonObject.getBoolean("exist");
                if (exist) {
                    contents_like_btn.setBackgroundDrawable(ContextCompat.getDrawable(ContentsMainActivity.this, R.drawable.pick_2));
                    is_like_clicked = true;
                }
                int count = jsonObject.getInt("count");
                contents_like_count.setText("" + count);
            } catch (Exception e) {
                Log.e("like count error", e.getMessage());
            }
        }
    };

    private Response.Listener<String> getContentsItemListener = new Response.Listener<String>() {
        private String title, watch_num;
        private double star_rating;
        private int contents_item_pk, contents_num, comments_count;
        private String thumbnail_url, movie_url;

        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                // php에서 받아온 JSON오브젝트 중에서 DB에 있던 값들의 배열을 JSON 배열로 변환
                boolean exist = jsonResponse.getBoolean("exist");
                if (exist) {
                    String info_img_url = jsonResponse.getString("url");
                    String main_contents_name = jsonResponse.getString("contents_name");
                    String writer_name = jsonResponse.getString("writer_name");
                    String painter_name = jsonResponse.getString("painter_name");
                    int view_count = jsonResponse.getInt("view_count");
                    String summary = jsonResponse.getString("summary");

                    SetBitmapImageFromUrlTask task = new SetBitmapImageFromUrlTask(thumbnail, 110, 80);
                    task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, info_img_url);

                    titlebar_title.setText(main_contents_name);
                    info_title.setText(main_contents_name);
                    info_writer.setText(writer_name);
                    info_painter.setText(painter_name);
                    info_view_count.setText("" + view_count);
                    summary_text.setText(summary);

                    if (!jsonResponse.getBoolean("result_exist")) return;

                    JSONArray result = jsonResponse.getJSONArray("result");
                    int num_result = jsonResponse.getInt("num_result");

                    for (int i = 0; i < num_result; i++) {
                        // 데이터베이스에 들어있는 콘텐츠의 수만큼 for문을 돌려 layout에 image추가
                        try {
                            JSONObject temp = result.getJSONObject(i);
                            contents_item_pk = temp.getInt("contents_item_pk");
                            contents_num = temp.getInt("contents_num");
                            title = temp.getString("contents_name");
                            thumbnail_url = temp.getString("url");
                            watch_num = temp.getString("view_count");
                            star_rating = temp.getDouble("star_rating");
                            comments_count = temp.getInt("comments_count");

                            movie_url = null;
                            if (temp.has("movie_url"))
                                movie_url = temp.getString("movie_url");

                            adapter.addItem(contents_item_pk, contents_num, title, thumbnail_url, watch_num, star_rating, comments_count, movie_url);
                        } catch (Exception e) {
                            Log.e("set item error", e.getMessage());
                        }
                    }

                    if (sortSpinner.getSelectedItemPosition() != 0) {
                        // '첫편부터'가 선택되어 있을 경우 리스트의 순서를 역으로 정렬
                        Collections.reverse(adapter.getWorksListViewItems());
                    }

                    listView.setAdapter(adapter);

                    // 전체 작품 수와 열람 작품 수, 캐시 보유량 textview를 설정.
                    totalText.setText(String.valueOf(num_result));
                    list_total_txt.setText("전체 (" + num_result + "화)");
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
                    if (is_like_clicked) {
                        is_like_clicked = false;
                        Toast.makeText(ContentsMainActivity.this, "취소되었습니다.", Toast.LENGTH_SHORT).show();
                        contents_like_count.setText(String.valueOf(Integer.parseInt(contents_like_count.getText().toString())-1));
                        contents_like_btn.setBackgroundDrawable(ContextCompat.getDrawable(ContentsMainActivity.this, R.drawable.ic_favorite_border_black_24dp));
                    } else {
                        is_like_clicked = true;
                        Toast.makeText(ContentsMainActivity.this, "관심목록에 담겼습니다.", Toast.LENGTH_SHORT).show();
                        contents_like_count.setText(String.valueOf(Integer.parseInt(contents_like_count.getText().toString())+1));
                        contents_like_btn.setBackgroundDrawable(ContextCompat.getDrawable(ContentsMainActivity.this, R.drawable.pick_2));
                    }
                } else {
                    Log.e("success check errmsg", jsonObject.getString("errmsg"));
                }
            } catch (Exception e) {
                Log.e("success check error", e.getMessage());
            }
        }
    };

    @Override
    public void onClick(View view) {
        ArrayList<WorksListViewItem> items;
        int position = 0;
        Intent intent;
        switch (view.getId()) {
            case R.id.contents_main_first_episode:
                items = adapter.getWorksListViewItems();
                if (sortSpinner.getSelectedItemPosition() == 0)
                    position = items.size() - 1;
                else
                    position = 0;
                intent = new Intent(ContentsMainActivity.this, WebtoonActivity.class);
                putExtraData(intent, position);
                startActivityForResult(intent, ActivityCodes.WEBTOON_REQUEST);
                break;
            case R.id.contents_main_back:
            case R.id.contents_main_title_bar_title:
                finish();
                break;
            case R.id.contents_main_like_btn:
                // 타이틀바에 있는 좋아요 버튼을 누르면 해당 작품의 데이터베이스 테이블에 like 1 증가
                ContentsLikeDBRequest contentsItemLikeDBRequest;
                RequestQueue requestQueue = Volley.newRequestQueue(ContentsMainActivity.this);
                if (is_like_clicked) {
                    contentsItemLikeDBRequest = new ContentsLikeDBRequest(insertDeleteContentsLikeDataURL, successCheckListener, selected_contents_pk, user_pk, 2);
                } else {
                    contentsItemLikeDBRequest = new ContentsLikeDBRequest(insertDeleteContentsLikeDataURL, successCheckListener, selected_contents_pk, user_pk, 1);
                }
                requestQueue.add(contentsItemLikeDBRequest);
                break;
            case R.id.contents_main_open_summary_btn:
                if (is_summary_opened) {
                    is_summary_opened = false;
                    summary_text.setMaxLines(3);
                    summary_text.setEllipsize(TextUtils.TruncateAt.END);
                    summary_btn.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.ic_keyboard_arrow_down_black_24dp));
                } else {
                    is_summary_opened = true;
                    summary_text.setMaxLines(Integer.MAX_VALUE);
                    summary_text.setEllipsize(null);
                    summary_btn.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.ic_keyboard_arrow_up_black_24dp));
                }
                break;
            case R.id.contents_main_follow_up_button :
                int last_view_contents_num = getLastViewContentsNum();
                items = adapter.getWorksListViewItems();
                for (int i=0; i<items.size(); i++) {
                    if (items.get(i).getContentsNum() == last_view_contents_num) {
                        position = i;
                        break;
                    }
                }
                intent = new Intent(ContentsMainActivity.this, WebtoonActivity.class);
                putExtraData(intent, position);
                startActivityForResult(intent, ActivityCodes.WEBTOON_REQUEST);
                break;
        }
    }

    public void putExtraData(Intent intent, int position) {
        itemList = adapter.getWorksListViewItems();
        intent.putExtra("position", position);
        intent.putExtra("contents_pk", selected_contents_pk);
    }

    protected int getReadContentsCount() {
        DBHelper dbHelper = new DBHelper(this, DBNames.CONTENTS_DB, null, 1);
        return dbHelper.getReadContentsCount(selected_contents_pk);
    }

    protected int getLastViewContentsNum() {
        DBHelper dbHelper = new DBHelper(this, DBNames.CONTENTS_DB, null, 1);
        return dbHelper.getLastViewContentsNum(selected_contents_pk);
    }
}
