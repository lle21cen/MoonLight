package org.techtown.ideaconcert.ContentsMainDir;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.techtown.ideaconcert.MainActivityDir.ArrivalContentsListener;
import org.techtown.ideaconcert.MainActivityDir.ContentsDBRequest;
import org.techtown.ideaconcert.R;
import org.techtown.ideaconcert.ShowProgressDialog;

public class ContentsMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents_main);

        ListView listView = findViewById(R.id.contents_main_list_works_list);
        WorksListViewAdapter adapter = new WorksListViewAdapter();

        Spinner sortSpinner = findViewById(R.id.contents_main_list_sort);

//        LinearLayout contentsLayout = findViewById(R.id.main_arrival_layout);
//        ArrivalContentsListener arrivalContentsListener = new ArrivalContentsListener(contentsLayout, this);
//        WorksDBRequest worksDBRequest = new ContentsDBRequest(arrivalContentsListener);
//        requestQueue.add(contentsDBRequest);
//        ShowProgressDialog.showProgressDialog(this);
//        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                WorksListViewItem item = (WorksListViewItem) parent.getItemAtPosition(position);

                String titleStr = item.getWorksTitle();
                String watchNumStr = item.getWatchNum();
                Drawable worksDrawable = item.getWorksDrawable();


            }
        });

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
