package org.techtown.ideaconcert.ManageMyWorksDir;

import android.content.Context;
import android.util.AttributeSet;
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
import org.techtown.ideaconcert.R;

import java.util.Calendar;


public class Fragment2MonthDataLayout extends RelativeLayout {

    private final String GetProfitDataURL = "http://lle21cen.cafe24.com/GetCalculationData.php";

    int currentYear, whatMonth, endDayOfMonth, user_pk;
    LinearLayout calLayout;

    public Fragment2MonthDataLayout(Context context, int currentYear, int whatMonth, int user_pk) {
        super(context);
        this.whatMonth = whatMonth;
        this.currentYear = currentYear;
        this.user_pk = user_pk;
        init(context);
    }

    public Fragment2MonthDataLayout(Context context, AttributeSet attrs, int currentYear, int whatMonth, int user_pk) {
        super(context, attrs);
        this.whatMonth = whatMonth;
        this.currentYear = currentYear;
        this.user_pk = user_pk;
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.myworks_fragment2_month_data_layout, this, true);

        TextView monthText = view.findViewById(R.id.calculate_month_text);
        monthText.setText(currentYear + "년 " + whatMonth + "월");

        TextView monthPeriodText = view.findViewById(R.id.calculate_month_period_text);
        Calendar calendar = Calendar.getInstance();
        calendar.set(currentYear, whatMonth, 1);
        endDayOfMonth = calendar.getActualMaximum(Calendar.DATE);
        String periodText = currentYear + "/" + whatMonth + "/01 ~ " + currentYear + "/" + whatMonth + "/" + endDayOfMonth;
        monthPeriodText.setText(periodText);

        calLayout = view.findViewById(R.id.calculate_layout);
        GetFragmentDataRequest request = new GetFragmentDataRequest(GetProfitDataURL, getCalculationDataListener, user_pk, currentYear, whatMonth);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }

    private Response.Listener<String > getCalculationDataListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean exist = jsonObject.getBoolean("exist");
                if (exist) {
                    int num_result = jsonObject.getInt("num_result");
                    JSONArray result = jsonObject.getJSONArray("result");
                    for (int i = 0; i < num_result; i++) {
                        JSONObject temp = result.getJSONObject(i);

                        int cal_pk = temp.getInt("cal_pk");
                        int profit = temp.getInt("profit");
                        int deduction = temp.getInt("deduction");
                        String due_date = temp.getString("due_date");
                        int cal_state = temp.getInt("cal_state");

                        Fragment2MonthDataLayoutItem monthDataItemLayout = new Fragment2MonthDataLayoutItem(getContext(), profit,  deduction, due_date, cal_state, user_pk, currentYear, whatMonth);
                        //Context context, int profit, int deduction, String due_date, int cal_state, int user_pk, int year, int month
                        calLayout.addView(monthDataItemLayout);
                    }
                } else {
//                    int user_pk = jsonObject.getInt("user_pk");
//                    int month = jsonObject.getInt("month");
//                    Toast.makeText(getContext(), "user_pk = " + user_pk + " month = " + month, Toast.LENGTH_SHORT).show();
                    Fragment1ProfitLayoutItem profitItemLayout = new Fragment1ProfitLayoutItem(getContext(), "수익이 없습니다.", 0, 0, 0);
                    calLayout.addView(profitItemLayout);
                }
            } catch (JSONException je) {
                Log.e("수익관리정보불러오기에러", je.getMessage());
            } catch (Exception e) {
                Log.e("수익관리정보불러오기에러", e.getMessage());
            }
        }
    };
}
