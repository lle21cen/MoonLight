package org.techtown.ideaconcert.SearchDir;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.techtown.ideaconcert.R;
import org.techtown.ideaconcert.SQLiteDir.DBHelper;
import org.techtown.ideaconcert.SQLiteDir.DBNames;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener{

    EditText searchText;
    Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchText = findViewById(R.id.search_keyword);
        searchText.requestFocus();

        searchButton = findViewById(R.id.search_search_btn);
        Button backButton = findViewById(R.id.search_back_btn);
        searchButton.setOnClickListener(this);
        backButton.setOnClickListener(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.search_fragment_container, new ParentFragment1Search()).commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_search_btn :
                DBHelper dbHelper = new DBHelper(SearchActivity.this, DBNames.CONTENTS_DB, null, 1);
                Date today = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd");
                dbHelper.insertRecentSearchData(searchText.getText().toString(), dateFormat.format(today));

//                getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.search_fragment_container, new ParentFragment2SearchResult()).commit();
                getSupportFragmentManager().beginTransaction().replace(R.id.search_fragment_container, new ParentFragment2SearchResult()).addToBackStack(null).commit();
                break;
            case R.id.search_back_btn :
                if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                    finish();
                } else {
                    getSupportFragmentManager().popBackStack();
                }
        }
    }
}
