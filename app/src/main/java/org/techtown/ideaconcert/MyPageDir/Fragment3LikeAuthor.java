package org.techtown.ideaconcert.MyPageDir;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.techtown.ideaconcert.R;

public class Fragment3LikeAuthor extends Fragment {
    View view;
    RecyclerView recyclerView;
    Fragment3RecyclerAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_my_page3_like_author, container, false);
        recyclerView = view.findViewById(R.id.my_page_fragment3_recycler);
        linearLayoutManager = new LinearLayoutManager(view.getContext());
        adapter = new Fragment3RecyclerAdapter();
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter.addItem(null, "문정후 작가", 1);
        adapter.addItem(null, "문정후 작가", 1);

        recyclerView.setAdapter(adapter);
        return view;
    }
}
