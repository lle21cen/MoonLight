package org.techtown.ideaconcert.MyCashDir;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.techtown.ideaconcert.R;

import java.util.ArrayList;

public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<HistoryListViewItem> items = new ArrayList<>();

    public static class HistoryRecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView dateView;
        TextView cashView;
        TextView purchaseView;

        HistoryRecyclerViewHolder(View view) {
            super(view);
            dateView = view.findViewById(R.id.main_arrival_item_name);
            cashView = view.findViewById(R.id.main_arrival_item_star_rating);
            purchaseView = view.findViewById(R.id.main_arrival_item_author_name);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cash_history_fragment_item, parent, false);
        return new HistoryRecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        HistoryRecyclerViewHolder historyRecyclerViewHolder = (HistoryRecyclerViewHolder) holder;
        HistoryListViewItem item = items.get(position);

        historyRecyclerViewHolder.dateView.setText(item.getDate());
        historyRecyclerViewHolder.cashView.setText("+"+item.getCash());
        if (item.isPurchase()) {
            historyRecyclerViewHolder.purchaseView.setText("구매 취소");
            historyRecyclerViewHolder.purchaseView.setTextColor(Color.rgb(233, 0, 5));
        }
        else {
            historyRecyclerViewHolder.purchaseView.setText("구매 완료");
        }

        historyRecyclerViewHolder.purchaseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Toast.makeText(context, "position = " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(String date, String cash, boolean purchase) {
        items.add(new HistoryListViewItem(date, cash, purchase));
    }
}