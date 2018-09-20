package org.techtown.ideaconcert.ContentsMainDir;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.techtown.ideaconcert.R;

public class ContentsMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents_main);

        ListView listView = findViewById(R.id.contents_main_list_works_list);
        WorksListViewAdapter adapter = new WorksListViewAdapter();

        Spinner sortSpinner = findViewById(R.id.contents_main_list_sort);

        adapter.addItem(getResources().getDrawable(R.drawable.ic_brush_black_24dp), "dummy", "13125");
        adapter.addItem(getResources().getDrawable(R.drawable.ic_toon_icon), "dummy2", "12334");
        adapter.addItem(getResources().getDrawable(R.drawable.ic_movie_icon), "dummy3", "1312523");

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                WorksListViewItem item = (WorksListViewItem) parent.getItemAtPosition(position);

                String titleStr = item.getWorksTitle();
                String descStr = item.getTempStr();
                Drawable iconDrawable = item.getWorksDrawable();
            }
        });

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(ContentsMainActivity.this, "id= "+id + " position = " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
