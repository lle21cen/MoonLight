package org.techtown.ideaconcert.ManageMyWorksDir;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.techtown.ideaconcert.R;

import java.util.Calendar;


public class Fragment1ProfitLayout extends RelativeLayout {
    int currentYear, whatMonth, endDayOfMonth;

    public Fragment1ProfitLayout(Context context, int currentYear, int whatMonth) {
        super(context);
        this.whatMonth = whatMonth;
        this.currentYear = currentYear;
        init(context);
    }

    public Fragment1ProfitLayout(Context context, AttributeSet attrs, int currentYear, int whatMonth) {
        super(context, attrs);
        this.whatMonth = whatMonth;
        this.currentYear = currentYear;
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.manage_fragment1_profit_layout, this, true);

        TextView monthText = view.findViewById(R.id.manage_month_text);
        monthText.setText(whatMonth + "월");

        TextView monthPeriodText = view.findViewById(R.id.manage_month_period_text);
        Calendar calendar = Calendar.getInstance();
        calendar.set(currentYear, whatMonth, 1);
        endDayOfMonth = calendar.getActualMaximum(Calendar.DATE);
        String periodText = currentYear + "/" + whatMonth + "/01 ~ " + currentYear + "/" + whatMonth + "/" + endDayOfMonth;
        monthPeriodText.setText(periodText);

        LinearLayout profitLayout = view.findViewById(R.id.manage_profit_layout);
    }
}
