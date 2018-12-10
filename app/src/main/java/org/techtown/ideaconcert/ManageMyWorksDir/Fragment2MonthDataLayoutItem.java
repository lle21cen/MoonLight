package org.techtown.ideaconcert.ManageMyWorksDir;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.techtown.ideaconcert.R;

public class Fragment2MonthDataLayoutItem extends LinearLayout {

    String due_date; // 정산 예정일
    int profit, deduction, cal_state; // cal_state : 정산 상태(1=미정산 2=정산완료)

    public Fragment2MonthDataLayoutItem(Context context, int profit, int deduction, String due_date, int cal_state) {
        super(context);
        this.due_date = due_date;
        this.profit = profit;
        this.deduction = deduction;
        this.cal_state = cal_state;
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.myworks_fragment2_month_data_layout_item, this, true);

        TextView profitView = view.findViewById(R.id.calculation_profit);
        TextView deductionView = view.findViewById(R.id.calculation_deduction);
        TextView amountView = view.findViewById(R.id.calculation_cal_amount);
        TextView dueDateView = view.findViewById(R.id.calculation_cal_due_date);
        TextView calStateView = view.findViewById(R.id.calculation_cal_state);

        profitView.setText(profit + "원");
        deductionView.setText(deduction + "원");
        amountView.setText((profit - deduction) + "원");
        dueDateView.setText(due_date);
        if (cal_state == 1) {
            calStateView.setTextColor(Color.rgb(233, 0, 5));
            calStateView.setText("미정산");
        } else if (cal_state == 2) {
            calStateView.setTextColor(Color.rgb(120, 106, 170));
            calStateView.setText("정산완료");
        }
    }
}
