package org.techtown.ideaconcert.ContentsMainDir;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.techtown.ideaconcert.ActivityCodes;
import org.techtown.ideaconcert.MainActivityDir.GetBitmapImageFromURL;
import org.techtown.ideaconcert.R;
import org.techtown.ideaconcert.WebtoonDir.WebtoonActivity;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

public class ContentsMainActivity extends AppCompatActivity implements View.OnClickListener {

    // 미완성 구현 사항 :
    // 이미지만 따로 동적으로 설정하도록 바꾸어서 컨텐츠 로딩에 걸리는 시간을 단축할 필요가 있음
    // 변수명 통일 시키기 ... 귀찮

    static final String getContentsItemsImgURL = "http://lle21cen.cafe24.com/GetContentsItem.php";
    private int selected_contents_pk;

    private Button firstEpiBtn;
    private ListView listView;
    private TextView totalText, readingText, cashText, titlebar_title, info_title, info_writer, info_painter, info_view_count, contents_summary;
    private WorksListViewAdapter adapter;
    private Spinner sortSpinner;
    private ImageView thumbnail, back_btn;

    public static ArrayList<WorksListViewItem> itemList; // WebtonActivity에서도 사용하기 위해 public static으로 선언
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents_main);

        Intent intent = getIntent();
        selected_contents_pk = intent.getIntExtra("contents_pk", 0);

        totalText = findViewById(R.id.contents_main_status_total); // 전체 몇 편
        readingText = findViewById(R.id.contents_main_status_reading); // 그 중 몇 편을 열람
        cashText = findViewById(R.id.contents_main_status_cash); // 보유 캐시
        info_title = findViewById(R.id.contents_main_info_title);
        info_writer = findViewById(R.id.contents_main_info_writer);
        info_painter = findViewById(R.id.contents_main_info_painter);
        contents_summary = findViewById(R.id.contents_main_summary);
        info_view_count = findViewById(R.id.contents_main_info_view_count);
        titlebar_title = findViewById(R.id.contents_main_title_bar_title);
        titlebar_title.setOnClickListener(this);

        back_btn = findViewById(R.id.contents_main_back);
        back_btn.setOnClickListener(this);

        firstEpiBtn = findViewById(R.id.contents_main_first_episode); // 첫화보기 버튼
        firstEpiBtn.setOnClickListener(this);

        listView = findViewById(R.id.contents_main_list_works_list);
        adapter = new WorksListViewAdapter();

        WorksDBRequest worksDBRequest = new WorksDBRequest(getContentsItemsImgURL, worksListener, selected_contents_pk);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(worksDBRequest);

        sortSpinner = findViewById(R.id.contents_main_list_sort);
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
                ArrayList<WorksListViewItem> items = adapter.getWorksListViewItems();
                Intent intent = new Intent(ContentsMainActivity.this, WebtoonActivity.class);
                putExtraData(intent, position);
                startActivityForResult(intent, ActivityCodes.WEBTTON_REQUEST);
            }
        });
    }

    Response.Listener<String> worksListener = new Response.Listener<String>() {
        private String title, watch_num;
        private double star_rating;
        private int contents_item_pk, contents_num, comments_count;
        private URL url;

        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                // php에서 받아온 JSON오브젝트 중에서 DB에 있던 값들의 배열을 JSON 배열로 변환
                JSONArray result = jsonResponse.getJSONArray("result");
                boolean exist = jsonResponse.getBoolean("exist");

                if (exist) {
                    int num_category_contents_data = jsonResponse.getInt("num_result");
                    URL thumbnailURL = new URL(jsonResponse.getString("thumbnail"));
                    String main_contents_name = jsonResponse.getString("contents_name");
                    String writer_name = jsonResponse.getString("writer_name");
                    String painter_name = jsonResponse.getString("painter_name");
                    int view_count = jsonResponse.getInt("view_count");
                    String summary = jsonResponse.getString("summary");

                    // thumbnail.setImageBitmap(); 효율성을 위해 나중에 쓰래드로 구현.
                    titlebar_title.setText(main_contents_name);
                    info_title.setText(main_contents_name);
                    info_writer.setText(writer_name);
                    info_painter.setText(painter_name);
                    info_view_count.setText(""+view_count);
                    contents_summary.setText(summary);

                    for (int i = 0; i < num_category_contents_data; i++) {
                        // 데이터베이스에 들어있는 콘텐츠의 수만큼 for문을 돌려 layout에 image추가
                        try {
                            JSONObject temp = result.getJSONObject(i);
                            contents_item_pk = temp.getInt("contents_item_pk");
                            contents_num = temp.getInt("contents_num");
                            title = temp.getString("contents_name");
                            url = new URL(temp.getString("url"));
                            watch_num = temp.getString("view_count");
                            star_rating = temp.getDouble("star_rating");
                            comments_count = temp.getInt("comments_count");

                            GetBitmapImageFromURL getBitmapImageFromURL = new GetBitmapImageFromURL(url);
                            getBitmapImageFromURL.start();
                            getBitmapImageFromURL.join();
                            Bitmap bitmap = getBitmapImageFromURL.getBitmap();
                            // 이미지만 따로 동적으로 설정하도록 바꾸어서 컨텐츠 로딩에 걸리는 시간을 단축할 필요가 있음

                            adapter.addItem(contents_item_pk, contents_num, title, bitmap, watch_num, star_rating, comments_count);
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
                    totalText.setText(String.valueOf(num_category_contents_data));
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
            case R.id.contents_main_first_episode:
                ArrayList<WorksListViewItem> items = adapter.getWorksListViewItems();
                int position;
                if (sortSpinner.getSelectedItemPosition() == 0)
                    position = items.size()-1;
                else
                    position = 0;
                Intent intent = new Intent(ContentsMainActivity.this, WebtoonActivity.class);
                putExtraData(intent, position);
                startActivityForResult(intent, ActivityCodes.WEBTTON_REQUEST);
                break;
            case R.id.contents_main_back : case R.id.contents_main_title_bar_title :
                finish();
        }
    }

    public void putExtraData(Intent intent, int position)
    {
        itemList = adapter.getWorksListViewItems();
        intent.putExtra("position", position);
    }
}
