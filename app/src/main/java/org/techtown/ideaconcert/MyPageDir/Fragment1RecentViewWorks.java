package org.techtown.ideaconcert.MyPageDir;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.techtown.ideaconcert.R;
import org.techtown.ideaconcert.SQLiteDir.DBHelper;
import org.techtown.ideaconcert.SQLiteDir.DBNames;
import org.techtown.ideaconcert.SQLiteDir.RecentViewData;

import java.util.ArrayList;

public class Fragment1RecentViewWorks extends Fragment {

    View view;
    RecyclerView recyclerView;
    Fragment1RecyclerAdapter adapter;
    LinearLayoutManager recyclerViewManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_page1_recent_view_works, container, false);
        recyclerView = view.findViewById(R.id.my_page_fragment1_recycler);
        recyclerViewManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(recyclerViewManager);

        adapter = new Fragment1RecyclerAdapter();
        DBHelper dbHelper = new DBHelper(getActivity(), DBNames.CONTENTS_DB, null, 1);
        ArrayList<RecentViewData> data = dbHelper.getAllRecentViewData();

        for (int i = 0; i < data.size(); i++) {
            adapter.addItem(data.get(i).getUrl(), data.get(i).getContents_pk(), data.get(i).getDate(), data.get(i).getContents_name(), data.get(i).getContents_num());
            Log.e("마이페이지 thumbnail url", data.get(i).getUrl());
        }

        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                if (e.getAction() == MotionEvent.ACTION_UP) {

                }
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
        return view;
    }
}
