package org.techtown.ideaconcert.ManageMyWorksDir;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.ideaconcert.ActivityCodes;
import org.techtown.ideaconcert.R;

import java.util.Calendar;


public class Fragment1ProfitLayout extends RelativeLayout {

    private final String GetProfitDataURL = ActivityCodes.DATABASE_IP + "/platform/GetProfitData";

    int whatYear, whatMonth, endDayOfMonth, user_pk;
    LinearLayout profitLayout;
    TextView cashSumTextView, profitSumTextView;
    private Response.Listener<String> getProfitDataListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean exist = jsonObject.getBoolean("exist");
                if (exist) {
                    int num_result = jsonObject.getInt("num_result");
                    JSONArray result = jsonObject.getJSONArray("result");
                    int cashSum = 0, profitSum = 0;
                    for (int i = 0; i < num_result; i++) {
                        JSONObject temp = result.getJSONObject(i);

                        String contents_name = temp.getString("contents_name");
                        int view_count = temp.getInt("view_count");
                        int cash = temp.getInt("cash_sum");
                        int profit = temp.getInt("profit");

                        cashSum += cash;
                        profitSum += profit;

                        Fragment1ProfitLayoutItem profitItemLayout = new Fragment1ProfitLayoutItem(getContext(), contents_name, view_count, cash, profit);
                        profitLayout.addView(profitItemLayout);
                    }
                    cashSumTextView.setText(cashSum + "캐시");
                    profitSumTextView.setText(profitSum + "원");
                } else {
//                    int user_pk = jsonObject.getInt("user_pk");
//                    int month = jsonObject.getInt("month");
//                    Toast.makeText(getContext(), "user_pk = " + user_pk + " month = " + month, Toast.LENGTH_SHORT).show();
                    Fragment1ProfitLayoutItem profitItemLayout = new Fragment1ProfitLayoutItem(getContext(), "수익이 없습니다.", 0, 0, 0);
                    profitLayout.addView(profitItemLayout);
                }
            } catch (JSONException je) {
                Log.e("수익관리정보불러오기에러", je.getMessage());
            } catch (Exception e) {
                Log.e("수익관리정보불러오기에러", e.getMessage());
            }
        }
    };

    public Fragment1ProfitLayout(Context context, int whatYear, int whatMonth, int user_pk) {
        super(context);
        this.whatMonth = whatMonth;
        this.whatYear = whatYear;
        this.user_pk = user_pk;
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.myworks_fragment1_month_data_layout, this, true);

        cashSumTextView = view.findViewById(R.id.manage_cash_sum);
        profitSumTextView = view.findViewById(R.id.manage_profit_sum);

        TextView monthText = view.findViewById(R.id.manage_month_text);
        monthText.setText(whatYear + "년 " + whatMonth + "월");

        TextView monthPeriodText = view.findViewById(R.id.manage_month_period_text);
        Calendar calendar = Calendar.getInstance();
        calendar.set(whatYear, whatMonth, 1);
        endDayOfMonth = calendar.getActualMaximum(Calendar.DATE);
        String periodText = whatYear + "/" + whatMonth + "/01 ~ " + whatYear + "/" + whatMonth + "/" + endDayOfMonth;
        monthPeriodText.setText(periodText);

        profitLayout = view.findViewById(R.id.manage_profit_layout);
        GetFragmentDataRequest request = new GetFragmentDataRequest(GetProfitDataURL, getProfitDataListener, user_pk, whatYear, whatMonth);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }
}
