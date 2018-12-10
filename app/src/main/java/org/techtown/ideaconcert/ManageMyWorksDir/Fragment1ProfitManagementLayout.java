package org.techtown.ideaconcert.ManageMyWorksDir;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.techtown.ideaconcert.R;
import org.techtown.ideaconcert.UserInformation;

import java.util.Calendar;

public class Fragment1ProfitManagementLayout extends Fragment implements View.OnClickListener {

    View view;
    int currentYear, currentMonth, endDayOfMonth;
    private LinearLayout profitLayoutContainer;
    private TextView dateFromText, dateToText;
    private int selectedFromYear, selectedFromMonth, selectedToYear, selectedToMonth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.myworks_fragment1_profit_layout, container, false);
        Button oneMonthButton = view.findViewById(R.id.manage_one_month);
        Button threeMonthButton = view.findViewById(R.id.manage_three_month);
        Button sixMonthButton = view.findViewById(R.id.manage_six_month);
        Button oneYearButton = view.findViewById(R.id.manage_one_year);
        Button inquiryButton = view.findViewById(R.id.manage_inquiry_button);
        oneMonthButton.setOnClickListener(this);
        threeMonthButton.setOnClickListener(this);
        sixMonthButton.setOnClickListener(this);
        oneYearButton.setOnClickListener(this);
        inquiryButton.setOnClickListener(this);

        dateFromText = view.findViewById(R.id.manage_period_from_text);
        dateToText = view.findViewById(R.id.manage_period_to_text);
        dateFromText.setOnClickListener(this);
        dateFromText.setOnClickListener(this);

        profitLayoutContainer = view.findViewById(R.id.manage_profit_layout_container);
        setCurrentYearAndMonth(); // 현재의 년도와 월을 설정하고
        setLastDayOfMonth(); // 그에 해당하는 월의 최종 일을 구한다.
        setDateTextToCurrentDate(currentYear, currentMonth); // 그렇게 구한 날짜들로 기간조회설정의 날짜 기간을 현재 날짜 기준으로 초기화 한다.

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.manage_one_month:
                setDateTextToCurrentDate(currentYear, currentMonth);
                break;
            case R.id.manage_three_month:
                setDateTextToCurrentDate(currentYear, currentMonth - 2);
                break;
            case R.id.manage_six_month:
                setDateTextToCurrentDate(currentYear, currentMonth - 5);
                break;
            case R.id.manage_one_year:
                setDateTextToCurrentDate(currentYear, currentMonth - 11);
                break;
            case R.id.manage_inquiry_button:
                parsePeriodTextView(); // 기간설정에서 설정된 날짜 텍스트에서 년월 정보를 추출해 오는 함수
                addProfitLayoutToContainer(selectedFromYear, selectedFromMonth, selectedToYear, selectedToMonth);
                break;
            case R.id.manage_period_from_text:
                openMonthPickerDialog(dateFromText, false);
                break;
            case R.id.manage_period_to_text:
                openMonthPickerDialog(dateToText, true);
                break;
        }
    }

    private void addProfitLayoutToContainer(int fromYear, int fromMonth, int toYear, int toMonth) {
        profitLayoutContainer.removeAllViews();
        UserInformation userInformation = (UserInformation) getActivity().getApplication();
        int user_pk = userInformation.getUser_pk();

        if (fromYear > toYear) {
            Toast.makeText(getActivity(), "기간 설정 오류 (시작 연도가 종료 연도보다 큽니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        Fragment1ProfitLayout profitLayout;
        int yearGap = toYear - fromYear;
        int monthGap = toMonth - fromMonth;
        int howMany = yearGap * 12 + monthGap;
        for (int i = 0; i <= howMany; i++) {
            Log.e("년월", fromYear + "년" + fromMonth + "월");
            profitLayout = new Fragment1ProfitLayout(getActivity(), fromYear, fromMonth++, user_pk);
            if (fromMonth > 12) {
                fromMonth -= 12;
                fromYear++;
            }
            profitLayoutContainer.addView(profitLayout);
        }
    }

    private void setCurrentYearAndMonth() {
        currentYear = Calendar.getInstance().get(Calendar.YEAR);
        currentMonth = Calendar.getInstance().get(Calendar.MONTH); // 월 : 0(1월) ~ 11(12월)
        if (currentMonth == 0) {
            // 현재 월이 1월이면 이전 년도 12월 정보를 가져옴
            currentYear--;
            currentMonth = 12;
        }
    }

    protected void openMonthPickerDialog(TextView dateTextView, boolean isFromToText) {
        MonthPickerDialog dialog = new MonthPickerDialog(getContext(), false, null, dateTextView, isFromToText);
        dialog.show();
    }


    private void setDateTextToCurrentDate(int year, int month) {
        if (month < 1) {
            month += 12;
            year--;
        }
        dateFromText.setText(year + "/" + month + "/1");
        dateToText.setText(currentYear + "/" + currentMonth + "/" + endDayOfMonth);
    }

    private void parsePeriodTextView() {
        // 조회버튼을 눌렀을 때 설정된 기간들의 텍스트에서 년월 정보를 파싱하여 조회하는데 사용
        String fromText = dateFromText.getText().toString();
        String toText = dateToText.getText().toString();

        String[] arr = fromText.split("/");
        selectedFromYear = Integer.parseInt(arr[0]);
        selectedFromMonth = Integer.parseInt(arr[1]);

        arr = toText.split("/");
        selectedToYear = Integer.parseInt(arr[0]);
        selectedToMonth = Integer.parseInt(arr[1]);
    }

    private void setLastDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(currentYear, currentMonth, 1);
        endDayOfMonth = calendar.getActualMaximum(Calendar.DATE);
    }

}
