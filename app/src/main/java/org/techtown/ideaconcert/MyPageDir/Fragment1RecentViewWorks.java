package org.techtown.ideaconcert.MyPageDir;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.techtown.ideaconcert.R;
import org.techtown.ideaconcert.SQLiteDir.DBHelper;
import org.techtown.ideaconcert.SQLiteDir.DBNames;
import org.techtown.ideaconcert.SQLiteDir.RecentViewPair;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

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
        DBHelper dbHelper = new DBHelper(getActivity(), DBNames.RECENT_VIEW_DB, null, 1);
        ArrayList<RecentViewPair> datas = dbHelper.getAllRecentViewData();

        for (int i=0; i < datas.size(); i++) {
            adapter.addItem(null, datas.get(i).getDate(), datas.get(i).getContents_name(), datas.get(i).getContents_num());
        }
        recyclerView.setAdapter(adapter);
        return view;
    }
}
