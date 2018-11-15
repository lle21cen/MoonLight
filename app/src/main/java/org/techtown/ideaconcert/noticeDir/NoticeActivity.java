package org.techtown.ideaconcert.noticeDir;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import org.techtown.ideaconcert.R;

public class NoticeActivity extends AppCompatActivity {

    ListView noticeListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        noticeListView = findViewById(R.id.notice_list_view);

    }
}
