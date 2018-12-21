package org.techtown.ideaconcert.ManageMyWorksDir;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.techtown.ideaconcert.R;

public class Fragment1ProfitLayoutItem extends LinearLayout {

    String contents_name;
    int view_count, cash, profit;

    public Fragment1ProfitLayoutItem(Context context, String contents_name, int view_count, int cash, int profit) {
        super(context);
        this.contents_name = contents_name;
        this.view_count = view_count;
        this.cash = cash;
        this.profit = profit;
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.myworks_fragment1_month_data_layout_item, this, true);

        TextView titleView = view.findViewById(R.id.manage_profit_title);
        TextView view_count_view = view.findViewById(R.id.manage_profit_view_count);
        TextView cash_view = view.findViewById(R.id.manage_profit_selling_cash);
        TextView profit_view = view.findViewById(R.id.manage_profit_selling_profit);

        titleView.setText(contents_name);
        view_count_view.setText("" + view_count);
        cash_view.setText(cash + " 캐시");
        profit_view.setText(profit + " 원");
    }
}
