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

import org.techtown.ideaconcert.R;

import java.util.Calendar;

public class Fragment1ProfitManagement extends Fragment implements View.OnClickListener {

    View view;
    int currentYear, currentMonth;
    Button oneMonthButton, threeMonthButton, sixMonthButton, oneYearButton;
    private LinearLayout profitLayoutContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.manage_fragment1_profit_management, container, false);
        oneMonthButton = view.findViewById(R.id.manage_one_month);
        threeMonthButton = view.findViewById(R.id.manage_three_month);
        sixMonthButton = view.findViewById(R.id.manage_six_month);
        oneYearButton = view.findViewById(R.id.manage_one_year);
        oneMonthButton.setOnClickListener(this);
        threeMonthButton.setOnClickListener(this);
        sixMonthButton.setOnClickListener(this);
        oneYearButton.setOnClickListener(this);

        profitLayoutContainer = view.findViewById(R.id.manage_profit_layout_container);
        setCurrentYearAndMonth();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.manage_one_month:
                addProfitLayoutToContainer(1);
                break;
            case R.id.manage_three_month:
                addProfitLayoutToContainer(3);
                break;
            case R.id.manage_six_month:
                addProfitLayoutToContainer(6);
                break;
            case R.id.manage_one_year:
                addProfitLayoutToContainer(12);
                break;
        }
    }

    private void addProfitLayoutToContainer(int howMany) {
        profitLayoutContainer.removeAllViews();
        for (int i = 0; i < howMany; i++) {
            Fragment1ProfitLayout profitLayout;
            if (currentMonth - i > 0) {
                profitLayout = new Fragment1ProfitLayout(getActivity(), currentYear, currentMonth - i);
            } else {
                profitLayout = new Fragment1ProfitLayout(getActivity(), currentYear - 1, currentMonth + 12 - i); // 월이 0월 이하이면 전년도 12월부터 다시 줄여나감
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
}
