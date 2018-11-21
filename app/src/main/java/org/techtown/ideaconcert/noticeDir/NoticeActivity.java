package org.techtown.ideaconcert.noticeDir;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.ideaconcert.ActivityCodes;
import org.techtown.ideaconcert.R;

public class NoticeActivity extends AppCompatActivity implements View.OnClickListener{

    RecyclerView noticeRecyclerView;
    NoticeRecyclerViewAdapter adapter;
    static final String GetBoardURL = ActivityCodes.DATABASE_IP + "/platform/GetBoard";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        adapter = new NoticeRecyclerViewAdapter();

        noticeRecyclerView = findViewById(R.id.notice_recycler_view);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        noticeRecyclerView.setLayoutManager(manager);

        NoticeDataRequest request = new NoticeDataRequest(GetBoardURL, noticeListener, 1);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

        Button backButton = findViewById(R.id.notice_back_btn);
        TextView backText = findViewById(R.id.notice_back_txt);
        backButton.setOnClickListener(this);
        backText.setOnClickListener(this);
    }

    private Response.Listener noticeListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String  response) {
            try {
                JSONObject jsonObject = new JSONObject(response);

                boolean exist = jsonObject.getBoolean("exist");
                if (exist) {
                    JSONArray result = jsonObject.getJSONArray("result");
                    int num_result = jsonObject.getInt("num_result");

                    for (int i = 0; i < num_result; i++) {
                        try {
                            JSONObject temp = result.getJSONObject(i);
                            int board_pk = temp.getInt("board_pk");
                            String title = temp.getString("title");
                            String date = temp.getString("date");
                            String content = temp.getString("content");
                            adapter.addItem(board_pk, title, date, content, null);
                        } catch (Exception e) {
                            Log.e("set item error", e.getMessage());
                        }
                    }
                    noticeRecyclerView.setAdapter(adapter);
                }
            } catch (JSONException e) {
                Log.e("공지사항리스너", e.getMessage());
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.notice_back_btn :
            case R.id.notice_back_txt :
                finish();
        }
    }
}
