package org.techtown.ideaconcert.ContentsMainDir;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.techtown.ideaconcert.R;

public class ContentsMainActivity extends AppCompatActivity {

    static final String getContentsItemsImgURL = "http://lle21cen.cafe24.com/GetContentsItem.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents_main);

        ListView listView = findViewById(R.id.contents_main_list_works_list);
        WorksListViewAdapter adapter = new WorksListViewAdapter();

        WorksListener worksListener = new WorksListener(adapter, listView);
        WorksDBRequest worksDBRequest = new WorksDBRequest(getContentsItemsImgURL, worksListener);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(worksDBRequest);

//        Spinner sortSpinner = findViewById(R.id.contents_main_list_sort);
//        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
    }
}
