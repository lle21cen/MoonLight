package org.techtown.ideaconcert.ManageMyWorksDir;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.techtown.ideaconcert.R;
import org.techtown.ideaconcert.UserInformation;

import java.util.Calendar;

public class Fragment2CalculateAccountManagement extends Fragment implements View.OnClickListener {

    int currentMonth, currentYear, endDayOfMonth;
    LinearLayout calculateContainer;
    View view;
    private String dateFromTextStr = "/"+currentMonth+"/1";

    private TextView dateFromText, dateToText;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.myworks_fragment2_calculate_layout, container, false);
        calculateContainer = view.findViewById(R.id.calculate_container);
        dateFromText = view.findViewById(R.id.calculate_period_from_text);
        dateToText = view.findViewById(R.id.calculate_period_to_text);
        Button oneMonthButton = view.findViewById(R.id.calculate_one_month);
        oneMonthButton.setOnClickListener(this);
        Button inquiryButton = view.findViewById(R.id.calculate_inquiry_button);
        inquiryButton.setOnClickListener(this);

        setCurrentYearAndMonth();
        setLastDayOfMonth();
        dateFromText.setText( dateFromTextStr);
        dateToText.setText(currentYear+"/"+currentMonth+"/"+endDayOfMonth);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.calculate_one_month :
                setDateText(currentYear, currentMonth-1);
                break;

            case R.id.calculate_inquiry_button :

        }
    }


    private void addProfitLayoutToContainer(int howMany) {
        calculateContainer.removeAllViews();
        UserInformation userInformation = (UserInformation) getActivity().getApplication();
        int user_pk = userInformation.getUser_pk();
        for (int i = 0; i < howMany; i++) {
            Fragment1ProfitLayout profitLayout;
            if (currentMonth - i > 0) {
                profitLayout = new Fragment1ProfitLayout(getActivity(), currentYear, currentMonth - i, user_pk);
            } else {
                profitLayout = new Fragment1ProfitLayout(getActivity(), currentYear - 1, currentMonth + 12 - i, user_pk); // 월이 0월 이하이면 전년도 12월부터 다시 줄여나감
            }
            calculateContainer.addView(profitLayout);

        }
    }

    private void setDateText(int year, int month) {
        if (month < 1) {

        }
        dateFromText.setText(year+"/"+month+"/1");
        dateToText.setText(currentYear+"/"+currentMonth+"/1");
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
