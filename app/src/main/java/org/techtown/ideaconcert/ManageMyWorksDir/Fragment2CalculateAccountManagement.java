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

public class Fragment2CalculateAccountManagement extends Fragment implements View.OnClickListener {

    int currentMonth, currentYear, endDayOfMonth;
    LinearLayout calculateContainer;
    View view;

    private TextView dateFromText, dateToText, summaryPeriodView, totalAmountView;
    private int selectedFromYear, selectedFromMonth, selectedToYear, selectedToMonth;

    protected static int totalAmount;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.myworks_fragment2_calculate_layout, container, false);
        calculateContainer = view.findViewById(R.id.calculate_container);
        dateFromText = view.findViewById(R.id.calculate_period_from_text);
        dateFromText.setOnClickListener(this);
        dateToText = view.findViewById(R.id.calculate_period_to_text);
        dateToText.setOnClickListener(this);
        summaryPeriodView = view.findViewById(R.id.calculate_summary_period);
        totalAmountView = view.findViewById(R.id.calculate_total_amount);

        Button oneMonthButton = view.findViewById(R.id.calculate_one_month);
        oneMonthButton.setOnClickListener(this);
        Button threeMonthButton = view.findViewById(R.id.calculate_three_month);
        threeMonthButton.setOnClickListener(this);
        Button sixMonthButton = view.findViewById(R.id.calculate_six_month);
        sixMonthButton.setOnClickListener(this);
        Button oneYearButton = view.findViewById(R.id.calculate_one_year);
        oneYearButton.setOnClickListener(this);
        Button inquiryButton = view.findViewById(R.id.calculate_inquiry_button);
        inquiryButton.setOnClickListener(this);

        setCurrentYearAndMonth(); // 현재의 년도와 월을 설정하고
        setLastDayOfMonth(); // 그에 해당하는 월의 최종 일을 구한다.
        setDateTextToCurrentDate(currentYear, currentMonth); // 그렇게 구한 날짜들로 기간조회설정의 날짜 기간을 현재 날짜 기준으로 초기화 한다.
        setTextSummaryPeriodView(); // 기간조회설정의 기간을 이용하여 요약사항의 기간을 설정한다.

        TextView summaryTitle = view.findViewById(R.id.calculate_title);
        summaryTitle.setText((currentMonth+1) + "월 정산금액");
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.calculate_one_month:
                setDateTextToCurrentDate(currentYear, currentMonth);
                break;

            case R.id.calculate_three_month:
                setDateTextToCurrentDate(currentYear, currentMonth - 2);
                break;

            case R.id.calculate_six_month:
                setDateTextToCurrentDate(currentYear, currentMonth - 5);
                break;

            case R.id.calculate_one_year:
                setDateTextToCurrentDate(currentYear, currentMonth - 11);
                break;
            case R.id.calculate_inquiry_button:
                totalAmount = 0; // 총정산금액 초기화
                parsePeriodTextView(); // 기간설정에서 설정된 날짜 텍스트에서 년월 정보를 추출해 오는 함수
                addMonthDataLayoutToContainer(selectedFromYear, selectedFromMonth, selectedToYear, selectedToMonth);
                break;
            case R.id.calculate_period_from_text :
                openMonthPickerDialog(dateFromText, false);
                break;
            case R.id.calculate_period_to_text :
                openMonthPickerDialog(dateToText, true);
                break;
        }
    }

    protected void openMonthPickerDialog(TextView dateTextView, boolean isFromToText) {
        MonthPickerDialog dialog = new MonthPickerDialog(getContext(), false, null, dateTextView, isFromToText);
        dialog.show();
    }

    private void addMonthDataLayoutToContainer(int fromYear, int fromMonth, int toYear, int toMonth) {
        calculateContainer.removeAllViews();
        UserInformation userInformation = (UserInformation) getActivity().getApplication();
        int user_pk = userInformation.getUser_pk();
        if (fromYear > toYear) {
            Toast.makeText(getActivity(), "기간 설정 오류 (시작 연도가 종료 연도보다 큽니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        Fragment2MonthDataLayout monthDataLayout;
        int yearGap = toYear - fromYear;
        int monthGap = toMonth - fromMonth;
        int howMany = yearGap * 12 + monthGap;
        for (int i = 0; i <= howMany; i++) {
            Log.e("년월", fromYear + "년" + fromMonth + "월");
            monthDataLayout = new Fragment2MonthDataLayout(getActivity(), fromYear, fromMonth++, user_pk, totalAmountView);
            if (fromMonth > 12) {
                fromMonth -= 12;
                fromYear++;
            }
            calculateContainer.addView(monthDataLayout);
        }

        setTextSummaryPeriodView(); // 기간조회설정의 기간을 이용하여 요약사항의 기간을 설정한다.
    }

    private void setDateTextToCurrentDate(int year, int month) {
        if (month < 1) {
            month += 12;
            year--;
        }
        dateFromText.setText(year + "/" + month + "/1");
        dateToText.setText(currentYear + "/" + currentMonth + "/" + endDayOfMonth);
    }

    private void setTextSummaryPeriodView() {
        summaryPeriodView.setText("정정대상기간:" + dateFromText.getText().toString() + " ~ " + dateToText.getText().toString()); // 기간조회설정의 기간을 이용하여 요약사항의 기간을 설정한다.
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

    private void setCurrentYearAndMonth() {
        currentYear = Calendar.getInstance().get(Calendar.YEAR);
        currentMonth = Calendar.getInstance().get(Calendar.MONTH); // 월 : 0(1월) ~ 11(12월)
        if (currentMonth == 0) {
            // 현재 월이 1월이면 이전 년도 12월 정보를 가져옴
            currentYear--;
            currentMonth = 12;
        }
    }

    private void setLastDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(currentYear, currentMonth, 1);
        endDayOfMonth = calendar.getActualMaximum(Calendar.DATE);
    }
}
