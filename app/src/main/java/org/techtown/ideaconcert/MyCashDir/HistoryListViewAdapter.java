package org.techtown.ideaconcert.MyCashDir;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.techtown.ideaconcert.R;

import java.util.ArrayList;

public class HistoryListViewAdapter extends BaseAdapter {

    private ArrayList<HistoryListViewItem> items = new ArrayList<>();

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final Context context = parent.getContext();

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.cash_history_fragment_item, parent, false);
        }
        TextView dateText = view.findViewById(R.id.history_item_date);
        TextView cashText = view.findViewById(R.id.history_item_cash);
        TextView purchaseText = view.findViewById(R.id.history_item_purchase);

        HistoryListViewItem item = items.get(position);
        dateText.setText(item.getDate());
        cashText.setText("+" + item.getCash());
        if (item.isPurchase()) {
            purchaseText.setText("구매 완료");
        }
        else {
            purchaseText.setText("구매 취소");
            purchaseText.setTextColor(Color.rgb(233, 0, 5));
        }
        return view;
    }

    public void addItem(String date, String cash, boolean purchase) {
        HistoryListViewItem item = new HistoryListViewItem();
        item.setDate(date);
        item.setCash(cash);
        item.setPurchase(purchase);
        items.add(item);
    }
}
