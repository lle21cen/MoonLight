package org.techtown.ideaconcert.SearchDir;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.techtown.ideaconcert.R;
import org.techtown.ideaconcert.SQLiteDir.DBHelper;
import org.techtown.ideaconcert.SQLiteDir.DBNames;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    EditText searchText;
    Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchText = findViewById(R.id.search_keyword);
        searchText.requestFocus();
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchButton.performClick();
                    return true;
                }
                return false;
            }
        });

        searchButton = findViewById(R.id.search_search_btn);
        Button backButton = findViewById(R.id.search_back_btn);
        searchButton.setOnClickListener(this);
        backButton.setOnClickListener(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.search_fragment_container, new ParentFragment1Search()).commit();
    }

    @Override
    public void onClick(View view) {
        FragmentManager fm = getSupportFragmentManager();
        switch (view.getId()) {
            case R.id.search_search_btn:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);

                DBHelper dbHelper = new DBHelper(SearchActivity.this, DBNames.CONTENTS_DB, null, 1);
                Date today = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd");
                dbHelper.insertRecentSearchData(searchText.getText().toString().trim(), dateFormat.format(today));

                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                }
//                getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.search_fragment_container, new ParentFragment2SearchResult()).commit();
                fm.beginTransaction().replace(R.id.search_fragment_container, new ParentFragment2SearchResult(), "result_fragment").addToBackStack(null).commit();
                break;
            case R.id.search_back_btn:
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
                if (fm.getBackStackEntryCount() == 0) {
                    finish();
                } else {
                    getSupportFragmentManager().popBackStack();
                }
                break;
        }
    }
}
