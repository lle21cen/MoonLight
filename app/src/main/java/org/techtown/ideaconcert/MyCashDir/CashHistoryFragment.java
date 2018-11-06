package org.techtown.ideaconcert.MyCashDir;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.techtown.ideaconcert.R;

public class CashHistoryFragment extends Fragment {

    View view;
    RecyclerView historyListView;
    HistoryRecyclerViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.cash_history_fragment, container, false);
        historyListView = view.findViewById(R.id.cash_history_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        historyListView.setLayoutManager(layoutManager);

        adapter = new HistoryRecyclerViewAdapter();

        adapter.addItem("2018.11.01", "200", true);
        adapter.addItem("2018.11.01", "2200", false);

        historyListView.setAdapter(adapter);

        return view;
    }
}
