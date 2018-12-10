package org.techtown.ideaconcert.ContentsMainDir;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.techtown.ideaconcert.ActivityCodes;
import org.techtown.ideaconcert.R;
import org.techtown.ideaconcert.WebtoonDir.WebtoonActivity;

import java.util.ArrayList;
import java.util.Collections;

public class ContentsMainActivity extends AppCompatActivity implements View.OnClickListener {

    // 미완성 구현 사항 :
    // 이미지만 따로 동적으로 설정하도록 바꾸어서 컨텐츠 로딩에 걸리는 시간을 단축할 필요가 있음
    // 변수명 통일 시키기 ... 귀찮

    public static ArrayList<WorksListViewItem> itemList; // WebtonActivity에서도 사용하기 위해 public static으로 선언
//    private final String getContentsItemURL = "http://lle21cen.cafe24.com/GetContentsItem.php";
    private final String getContentsItemURL = ActivityCodes.DATABASE_IP + "/platform/GetContentsItem";
    private int selected_contents_pk;
    private ListView listView;
    private WorksListViewAdapter adapter;
    private Spinner sortSpinner;
    private TextView totalListText;

    private Fragment1Webtoon fragment;
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
                    JSONArray result = jsonResponse.getJSONArray("result");
                    int num_result = jsonResponse.getInt("num_result");

                    for (int i = 0; i < num_result; i++) {
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
                            Log.e("컨텐츠아이템어뎁터설정에러", e.getMessage());
                        }
                    }
                    if (sortSpinner.getSelectedItemPosition() != 0) {
                        // '첫편부터'가 선택되어 있을 경우 리스트의 순서를 역으로 정렬
                        Collections.reverse(adapter.getWorksListViewItems());
                    }
                    listView.setAdapter(adapter);
                    fragment.setAdapter(adapter);
                    // 전체 작품 수와 열람 작품 수, 캐시 보유량 textview를 설정.
                    totalListText.setText("전체 (" + num_result + "화)");
                } else {
                    Log.e("컨텐츠리스트", "데이터가 없습니다.");
                }
            } catch (Exception e) {
                Log.e("컨텐츠아이템리스너", e.getMessage());
            }
        }
    };

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
        totalListText = findViewById(R.id.contents_main_list_total_txt);

        listView = findViewById(R.id.contents_main_list_works_list);
        adapter = new WorksListViewAdapter(this, this);

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
        fragment = new Fragment1Webtoon();
        getSupportFragmentManager().beginTransaction().replace(R.id.contents_main_container, fragment, "webtoon").commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }

    public void putExtraData(Intent intent, int position) {
        itemList = adapter.getWorksListViewItems();
        intent.putExtra("position", position);
        intent.putExtra("contents_pk", selected_contents_pk);
    }
}
