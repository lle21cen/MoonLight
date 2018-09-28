package org.techtown.ideaconcert.ContentsMainDir;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.techtown.ideaconcert.ActivityCodes;
import org.techtown.ideaconcert.R;
import org.techtown.ideaconcert.WebtoonDir.WebtoonActivity;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

public class ContentsMainActivity extends AppCompatActivity implements View.OnClickListener{

    static final String getContentsItemsImgURL = "http://lle21cen.cafe24.com/GetContentsItem.php";
    private boolean isFromNewest;
    Button firstEpiBtn;

    static TextView totalText, readingText, cashText;
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

        final ListView listView = findViewById(R.id.contents_main_list_works_list);
        adapter = new WorksListViewAdapter();

        WorksListener worksListener = new WorksListener(adapter, listView);
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

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.contents_main_first_episode :
                ArrayList<WorksListViewItem> items = adapter.getWorksListViewItems();
                if (isFromNewest) {
//                    String url = items.get(items.size()-1).getURL(); // 첫 화의 url
                    Intent intent = new Intent(ContentsMainActivity.this, WebtoonActivity.class);
//                    intent.putExtra("weburl", url);
                    startActivityForResult(intent, ActivityCodes.WEBTTON_REQUEST);
                }
                else {
//                    String url = items.get(0).getURL(); // 첫 화의 url
                    Intent intent = new Intent(ContentsMainActivity.this, WebtoonActivity.class);
//                    intent.putExtra("weburl", url);
                    startActivityForResult(intent, ActivityCodes.WEBTTON_REQUEST);
                }
        }
    }
}
