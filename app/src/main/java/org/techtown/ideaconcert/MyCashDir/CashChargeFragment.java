package org.techtown.ideaconcert.MyCashDir;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.techtown.ideaconcert.R;

public class CashChargeFragment extends Fragment implements View.OnClickListener {
    View view;
    TextView cash_sum, amount_payment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.cash_charge_fragment, container, false);

        RelativeLayout cash_btn1 = view.findViewById(R.id.cash_1_layout);
        RelativeLayout cash_btn2 = view.findViewById(R.id.cash_50_layout);
        RelativeLayout cash_btn3 = view.findViewById(R.id.cash_200_layout);
        RelativeLayout cash_btn4 = view.findViewById(R.id.cash_1000_layout);
        RelativeLayout cash_btn5 = view.findViewById(R.id.cash_10_layout);
        RelativeLayout cash_btn6 = view.findViewById(R.id.cash_100_layout);
        RelativeLayout cash_btn7 = view.findViewById(R.id.cash_500_layout);
        RelativeLayout reset = view.findViewById(R.id.cash_reset);

        cash_sum = view.findViewById(R.id.cash_sum);
        amount_payment = view.findViewById(R.id.cash_amount_payment);

        cash_btn1.setOnClickListener(this);
        cash_btn2.setOnClickListener(this);
        cash_btn3.setOnClickListener(this);
        cash_btn4.setOnClickListener(this);
        cash_btn5.setOnClickListener(this);
        cash_btn6.setOnClickListener(this);
        cash_btn7.setOnClickListener(this);
        reset.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        int sumOfCash = Integer.valueOf(cash_sum.getText().toString());
        switch (view.getId()) {
            case R.id.cash_1_layout:
                sumOfCash += 1;
                cash_sum.setText(String.valueOf(sumOfCash));
                amount_payment.setText(String.valueOf(sumOfCash * 100));
                break;
            case R.id.cash_50_layout:
                sumOfCash += 50;
                cash_sum.setText(String.valueOf(sumOfCash));
                amount_payment.setText(String.valueOf(sumOfCash * 100));
                break;
            case R.id.cash_200_layout:
                sumOfCash += 200;
                cash_sum.setText(String.valueOf(sumOfCash));
                amount_payment.setText(String.valueOf(sumOfCash * 100));
                break;
            case R.id.cash_1000_layout:
                sumOfCash += 1000;
                cash_sum.setText(String.valueOf(sumOfCash));
                amount_payment.setText(String.valueOf(sumOfCash * 100));
                break;
            case R.id.cash_10_layout:
                sumOfCash += 10;
                cash_sum.setText(String.valueOf(sumOfCash));
                amount_payment.setText(String.valueOf(sumOfCash * 100));
                break;
            case R.id.cash_100_layout:
                sumOfCash += 100;
                cash_sum.setText(String.valueOf(sumOfCash));
                amount_payment.setText(String.valueOf(sumOfCash * 100));
                break;
            case R.id.cash_500_layout:
                sumOfCash += 500;
                cash_sum.setText(String.valueOf(sumOfCash));
                amount_payment.setText(String.valueOf(sumOfCash * 100));
                break;
            case R.id.cash_reset:
                sumOfCash = 0;
                cash_sum.setText(String.valueOf(sumOfCash));
                amount_payment.setText(String.valueOf(sumOfCash * 100));
                break;
        }
    }
}