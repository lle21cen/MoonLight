package org.techtown.ideaconcert.FAQDir;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.techtown.ideaconcert.R;

public class FAQActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView prevClickedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        TextView allButton, serviceButton, payButton, contentButton, publishButton, etcButton;
        allButton = findViewById(R.id.faq_all);
        serviceButton = findViewById(R.id.faq_service);
        payButton = findViewById(R.id.faq_pay);
        contentButton = findViewById(R.id.faq_contents);
        publishButton = findViewById(R.id.faq_publish);
        etcButton = findViewById(R.id.faq_etc);
        allButton.setOnClickListener(this);
        serviceButton.setOnClickListener(this);
        payButton.setOnClickListener(this);
        contentButton.setOnClickListener(this);
        publishButton.setOnClickListener(this);
        etcButton.setOnClickListener(this);

        setInitState(allButton);

        Button backButton = findViewById(R.id.faq_back_btn);
        TextView backText = findViewById(R.id.faq_back_txt);
        backButton.setOnClickListener(this);
        backText.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (prevClickedButton != null)
            prevClickedButton.setBackgroundColor(Color.rgb(255, 255, 255));
        TextView nowClickedButton = (TextView) view;
        nowClickedButton.setBackgroundColor(Color.rgb(192, 192, 192));
        prevClickedButton = nowClickedButton;

        switch (view.getId()) {
            case R.id.faq_all:
                getFragmentManager().beginTransaction().replace(R.id.faq_fragment_layout, new FragmentByCategory(0)).commit();
                break;
            case R.id.faq_service:
                getFragmentManager().beginTransaction().replace(R.id.faq_fragment_layout, new FragmentByCategory(2)).commit();
                break;
            case R.id.faq_pay:
                getFragmentManager().beginTransaction().replace(R.id.faq_fragment_layout, new FragmentByCategory(3)).commit();
                break;
            case R.id.faq_contents:
                getFragmentManager().beginTransaction().replace(R.id.faq_fragment_layout, new FragmentByCategory(4)).commit();
                break;
            case R.id.faq_publish:
                getFragmentManager().beginTransaction().replace(R.id.faq_fragment_layout, new FragmentByCategory(5)).commit();
                break;
            case R.id.faq_etc:
                getFragmentManager().beginTransaction().replace(R.id.faq_fragment_layout, new FragmentByCategory(6)).commit();
                break;

            case R.id.faq_back_btn:
            case R.id.faq_back_txt:
                finish();
        }
    }

    protected void setInitState(TextView nowClickedButton) {
        nowClickedButton.setBackgroundColor(Color.rgb(192, 192, 192));
        prevClickedButton = nowClickedButton;
        getFragmentManager().beginTransaction().replace(R.id.faq_fragment_layout, new FragmentByCategory(0)).commit();
    }
}
