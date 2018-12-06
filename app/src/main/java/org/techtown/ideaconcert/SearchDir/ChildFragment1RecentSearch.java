package org.techtown.ideaconcert.SearchDir;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.techtown.ideaconcert.R;
import org.techtown.ideaconcert.SQLiteDir.DBHelper;
import org.techtown.ideaconcert.SQLiteDir.DBNames;
import org.techtown.ideaconcert.SQLiteDir.RecentSearchData;

import java.util.ArrayList;

public class ChildFragment1RecentSearch extends Fragment {

    View view;
    RecyclerView recyclerView;
    ChildFragment1RecyclerAdapter adapter;
    TextView searchText;
    Button searchButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search_child_fragment_recycler_view, container, false);
        recyclerView = view.findViewById(R.id.search_recent_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        DBHelper dbHelper = new DBHelper(getContext(), DBNames.CONTENTS_DB, null, 1);
        ArrayList<RecentSearchData> data = dbHelper.selectAllRecentSearchData();

        adapter = new ChildFragment1RecyclerAdapter(data, cancelHandler, autoSearchHandler);
        recyclerView.setAdapter(adapter);

        try {
            searchText = getParentFragment().getActivity().findViewById(R.id.search_keyword);
            searchButton = getParentFragment().getActivity().findViewById(R.id.search_search_btn);
        } catch (NullPointerException ne) {
            Log.e("최근검색 버튼 초기화 오류", ne.getMessage());
        }
        return view;
    }

    private Handler cancelHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            recyclerView.swapAdapter(adapter, true);
            return true;
        }
    });

    private Handler autoSearchHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            String keyword = message.getData().getString("keyword");
            searchText.setText(keyword);
            searchButton.performClick();
            return true;
        }
    });
}
