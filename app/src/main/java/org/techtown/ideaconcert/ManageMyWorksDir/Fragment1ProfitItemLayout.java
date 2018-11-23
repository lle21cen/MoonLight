package org.techtown.ideaconcert.ManageMyWorksDir;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.ideaconcert.R;

public class Fragment1ProfitItemLayout extends LinearLayout {

    String contents_name;
    int view_count, cash, profit;

    public Fragment1ProfitItemLayout(Context context, String contents_name, int view_count, int cash, int profit) {
        super(context);
        this.contents_name = contents_name;
        this.view_count = view_count;
        this.cash = cash;
        this.profit = profit;
        init(context);
    }

    public Fragment1ProfitItemLayout(Context context, @Nullable AttributeSet attrs, String contents_name, int view_count, int cash, int profit) {
        super(context, attrs);
        this.contents_name = contents_name;
        this.view_count = view_count;
        this.cash = cash;
        this.profit = profit;
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.manage_fragment1_profit_layout_item, this, true);

        TextView titleView = view.findViewById(R.id.manage_profit_title);
        TextView view_count_view = view.findViewById(R.id.manage_profit_view_count);
        TextView cash_view = view.findViewById(R.id.manage_profit_selling_cash);
        TextView profit_view = view.findViewById(R.id.manage_profit_selling_profit);



        titleView.setText(contents_name);
        view_count_view.setText(""+view_count);
        cash_view.setText(""+cash);
        profit_view.setText(""+profit);
    }

    private Response.Listener<String > getProfitDataListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonObject = new JSONObject(response);

            } catch (JSONException je) {
                Log.e("수익관리정보불러오기에러", je.getMessage());
            } catch (Exception e) {
                Log.e("수익관리정보불러오기에러", e.getMessage());
            }
        }
    };
}
