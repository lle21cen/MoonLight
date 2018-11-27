package org.techtown.ideaconcert.ManageMyWorksDir;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import org.techtown.ideaconcert.R;

import java.util.Calendar;

public class MonthPickerDialog extends Dialog implements View.OnClickListener {

    TextView yearTextView;
    TextView dateTextView;
    boolean isFromToText; // true이면 periodToTextView를 클릭한 경우

    public MonthPickerDialog(@NonNull Context context, TextView dateTextView, boolean isFromToText) {
        super(context);
        this.dateTextView = dateTextView;
        this.isFromToText = isFromToText;
    }

    public MonthPickerDialog(@NonNull Context context, int themeResId, TextView dateTextView, boolean isFromToText) {
        super(context, themeResId);
        this.dateTextView = dateTextView;
        this.isFromToText = isFromToText;
    }

    public MonthPickerDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener, TextView dateTextView, boolean isFromToText) {
        super(context, cancelable, cancelListener);
        this.dateTextView = dateTextView;
        this.isFromToText = isFromToText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 바 삭제
        setContentView(R.layout.myworks_datepicker_dialog);

        ImageView prevYearButton = findViewById(R.id.myworks_dialog_prev_year);
        ImageView nextYearButton = findViewById(R.id.myworks_dialog_next_year);
        prevYearButton.setOnClickListener(this);
        nextYearButton.setOnClickListener(this);

        yearTextView = findViewById(R.id.myworks_dialog_year);

        int resourceId;
        for (int i = 0; i < 12; i++) {
            final int selectedMonth = i + 1;
            resourceId = getContext().getResources().getIdentifier("dialog_month_" + (i + 1), "id", getContext().getPackageName());
            findViewById(resourceId).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String selectedYear = yearTextView.getText().toString();
                    if (!isFromToText) {
                        dateTextView.setText(selectedYear + "/" + selectedMonth + "/1");
                    } else {
                        dateTextView.setText(selectedYear + "/" + selectedMonth + "/"+getLastDayOfMonth(Integer.parseInt(selectedYear), selectedMonth));
                    }
                    dismiss();
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        int curYear = Integer.parseInt(yearTextView.getText().toString());
        switch (view.getId()) {
            case R.id.myworks_dialog_prev_year:
                yearTextView.setText(String.valueOf(curYear - 1));
                break;
            case R.id.myworks_dialog_next_year:
                yearTextView.setText(String.valueOf(curYear + 1));
                break;
        }
    }

    private int getLastDayOfMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);
        return calendar.getActualMaximum(Calendar.DATE);
    }
}
