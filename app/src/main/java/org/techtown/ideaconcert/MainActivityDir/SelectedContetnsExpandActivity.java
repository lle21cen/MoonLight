package org.techtown.ideaconcert.MainActivityDir;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.techtown.ideaconcert.ActivityCodes;
import org.techtown.ideaconcert.R;


// sc = selected contents
public class SelectedContetnsExpandActivity extends AppCompatActivity implements View.OnClickListener {

    final private String selectedContentsURL = ActivityCodes.DATABASE_IP + "/platform/GetSelectedContents";
    GridLayout gridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_contents_expand);

        Button backBtn = findViewById(R.id.sc_expand_back);
        TextView backText = findViewById(R.id.sc_expand_txt);
        backBtn.setOnClickListener(this);
        backText.setOnClickListener(this);

        Intent intent = getIntent();
        int tag = intent.getIntExtra("tag", 2); // tag 1 :신작, 2 :베스트작, 3 :추천작
        TextView titleView = findViewById(R.id.sc_selected_name);
        if (tag == 1) {
            titleView.setText("이달의 신작보기");
        } else if (tag == 2) {
            titleView.setText("주말특가 50%할인 작품");
        } else if (tag == 3) {
            titleView.setText("이달의 베스트 9");
        } else if (tag == 4) {
            titleView.setText("추천 작품");
        }

        gridLayout = findViewById(R.id.sc_grid_layout);

        ContentsDBRequest request = new ContentsDBRequest(selectedContentsListener, selectedContentsURL, tag);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sc_expand_back:
            case R.id.sc_expand_txt:
                finish();
                break;
        }
    }

    private Response.Listener<String> selectedContentsListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                JSONArray result = jsonResponse.getJSONArray("result");
                boolean exist = jsonResponse.getBoolean("exist");
                int tag = jsonResponse.getInt("tag");
                NewArrivalRecyclerAdapter adapter;

                // tag 1 : 신작 2 : 할인작 3 : 베스트작 4:추천작

                if (exist) {
                    int num_result = jsonResponse.getInt("num_result");
                    for (int i = 0; i < num_result; i++) {
                        try {
                            JSONObject temp = result.getJSONObject(i);
                            int contents_pk = temp.getInt("contents_pk");
                            String url = temp.getString("url");
                            String contents_name = temp.getString("contents_name");
                            String painter_name = temp.getString("painter_name");
//                            String writer_name = temp.getString("writer_name"); // 글 작가 이름 추가?
                            int view_count = temp.getInt("view_count");
                            int movie = temp.getInt("movie");
                            ExpandedContentsItemLayout itemLayout = new ExpandedContentsItemLayout(SelectedContetnsExpandActivity.this, url, contents_name, painter_name, view_count, contents_pk, movie);
                            gridLayout.addView(itemLayout);
                            gridLayout.setUseDefaultMargins(true);
                            gridLayout.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
                        } catch (Exception e) {
                            Log.e("selected contents error", e.getMessage());
                        }
                    }
                } else {
                    Log.e("No Arrival", "표시 할 신작이 없습니다.");
                }
            } catch (Exception e) {
                Log.e("selectedListener", e.getMessage());
            }
        }
    };
}
