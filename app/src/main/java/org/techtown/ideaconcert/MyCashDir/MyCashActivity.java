package org.techtown.ideaconcert.MyCashDir;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import org.techtown.ideaconcert.ActivityCodes;
import org.techtown.ideaconcert.R;

public class MyCashActivity extends AppCompatActivity implements View.OnClickListener {

    Button cash_charge_btn, cash_history_btn;
    CashChargeFragment cashChargeFragment;
    CashHistoryFragment cashHistoryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cash);

        cashHistoryFragment = new CashHistoryFragment();
        cashChargeFragment = new CashChargeFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.cash_container, cashChargeFragment).commit();

        Button back_btn = findViewById(R.id.cash_back_btn);
        back_btn.setOnClickListener(this);

        cash_charge_btn = findViewById(R.id.cash_cash_charge_btn);
        cash_history_btn = findViewById(R.id.cash_cash_history);
        cash_charge_btn.setOnClickListener(this);
        cash_history_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cash_cash_charge_btn:
                cash_charge_btn.setBackgroundColor(Color.rgb(255, 255, 255));
                cash_charge_btn.setTextColor(Color.rgb(120, 106, 170));
                cash_history_btn.setBackgroundColor(Color.rgb(120, 106, 170));
                cash_history_btn.setTextColor(Color.rgb(255, 255, 255));
                getSupportFragmentManager().beginTransaction().replace(R.id.cash_container, cashChargeFragment).commit();
                break;

            case R.id.cash_cash_history:
                cash_charge_btn.setBackgroundColor(Color.rgb(120, 106, 170));
                cash_charge_btn.setTextColor(Color.rgb(255, 255, 255));
                cash_history_btn.setBackgroundColor(Color.rgb(255, 255, 255));
                cash_history_btn.setTextColor(Color.rgb(120, 106, 170));
                getSupportFragmentManager().beginTransaction().replace(R.id.cash_container, cashHistoryFragment).commit();
                break;

            case R.id.cash_back_btn:
                setResult(ActivityCodes.MYCASH_SUCCESS);
                finish();
                break;
        }
    }
}
