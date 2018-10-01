package org.techtown.ideaconcert.ContentsMainDir;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import org.techtown.ideaconcert.R;
import org.techtown.ideaconcert.WebtoonDir.WebtoonActivity;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

public class ContentsMainActivity extends AppCompatActivity implements View.OnClickListener {

    static final String getContentsItemsImgURL = "http://lle21cen.cafe24.com/GetContentsItem.php";
    private boolean isFromNewest = true;
    Button firstEpiBtn;
    private ListView listView;
    private TextView totalText, readingText, cashText;
    private WorksListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents_main);

        totalText = findViewById(R.id.contents_main_status_total); // 전체 몇 편
        readingText = findViewById(R.id.contents_main_status_reading); // 그 중 몇 편을 열람
        cashText = findViewById(R.id.contents_main_status_cash); // 보유 캐시

        firstEpiBtn = findViewById(R.id.contents_main_first_episode); // 첫화보기 버튼
        firstEpiBtn.setOnClickListener(this);

        listView = findViewById(R.id.contents_main_list_works_list);
        adapter = new WorksListViewAdapter();

        WorksDBRequest worksDBRequest = new WorksDBRequest(getContentsItemsImgURL, worksListener);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(worksDBRequest);

        Spinner sortSpinner = findViewById(R.id.contents_main_list_sort);
        // 스피너 : "최신편부터", "첫편부터" 두 개의 아이템을 가지고 있으며 기본은 '최신편부터' 이고 한번 누를때마다 ArrayList의 순서를 바꾸어 반대로 보여지게 된다.
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                // Spinner가 눌릴 때마다 ArrayList를 뒤집어서 보여주는 코드

                if (position == 0) isFromNewest = true;
                else isFromNewest = false;
                ArrayList<WorksListViewItem> items = adapter.getWorksListViewItems();
                Collections.reverse(items);
                listView.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(ContentsMainActivity.this, "position:" + position + " id:" + id, Toast.LENGTH_SHORT).show();

            }
        });
    }

    Response.Listener<String> worksListener = new Response.Listener<String>() {
        private String title, watch_num, rating, comments_num;
        private int contents_num;
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
                    for (int i = 0; i < num_category_contents_data; i++) {
                        // 데이터베이스에 들어있는 콘텐츠의 수만큼 for문을 돌려 layout에 image추가
                        try {
                            JSONObject temp = result.getJSONObject(i);
                            contents_num = temp.getInt("contents_num");
                            title = temp.getString("name");
                            url = new URL(temp.getString("url"));
                            watch_num = temp.getString("watch");
                            rating = temp.getString("grade");
                            comments_num = temp.getString("comments_num");

                            GetBitmapImageFromURL getBitmapImageFromURL = new GetBitmapImageFromURL(url);
                            getBitmapImageFromURL.start();
                            getBitmapImageFromURL.join();

                            Bitmap bitmap = getBitmapImageFromURL.getBitmap();
                            adapter.addItem(contents_num, title, bitmap, watch_num, rating, comments_num);
                        } catch (Exception e) {
                            Log.e("set item error", e.getMessage());
                        }
                    }

                    if (isFromNewest) {
                        // '최신편부터'가 선택되어 있을 경우 리스트의 순서를 역으로
                        if (adapter.getWorksListViewItems().get(0).getContentsNum() != 1) {
                            Collections.reverse(adapter.getWorksListViewItems());
                        }
                    }
                    listView.setAdapter(adapter);

                    // 전체 작품 수와 열람 작품 수, 캐시 보유량 textview를 설정.
                    // totalText.setText(num_category_contents_data);
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
                if (isFromNewest) {
//                    String url = items.get(items.size()-1).getURL(); // 첫 화의 url
                    Intent intent = new Intent(ContentsMainActivity.this, WebtoonActivity.class);
//                    intent.putExtra("weburl", url);
                    startActivityForResult(intent, ActivityCodes.WEBTTON_REQUEST);
                } else {
//                    String url = items.get(0).getURL(); // 첫 화의 url
                    Intent intent = new Intent(ContentsMainActivity.this, WebtoonActivity.class);
//                    intent.putExtra("weburl", url);
                    startActivityForResult(intent, ActivityCodes.WEBTTON_REQUEST);
                }
        }
    }
}
