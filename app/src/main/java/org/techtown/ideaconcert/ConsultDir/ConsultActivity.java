package org.techtown.ideaconcert.ConsultDir;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
import org.techtown.ideaconcert.ActivityCodes;
import org.techtown.ideaconcert.R;

public class ConsultActivity extends AppCompatActivity {

    //    static final String SendConsultMailURL = ActivityCodes.DATABASE_IP + "GetContentsItem";
    static final String SendConsultMailURL = ActivityCodes.DATABASE_IP + "/platform/SendConsultMail";

    Spinner consultCategorySpinner;
    EditText titleView, contentView, emailView;
    private Response.Listener sendConsultMailListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean success = jsonObject.getBoolean("success");
                if (success) {
                    Toast.makeText(ConsultActivity.this, "문의가 접수되었습니다.", Toast.LENGTH_SHORT).show();
                    setResult(ActivityCodes.CONSULT_SUCCESS);
                    finish();
                } else {
                    String errmsg = jsonObject.getString("errmsg");
                    Log.e("문의메일전송실패", errmsg);
                }
            } catch (Exception e) {
                Log.e("상담메일리스너", e.getMessage());
            }
        }
    };
    private View.OnClickListener receiptListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String consult_category = consultCategorySpinner.getSelectedItem().toString();
//            int category_num = consultCategorySpinner.getSelectedItemPosition();
            String title = titleView.getText().toString();
            String content = contentView.getText().toString();
            String email = emailView.getText().toString();

            if (!title.isEmpty() && !content.isEmpty() && !email.isEmpty()) {
                SendConsultMailRequest request = new SendConsultMailRequest(SendConsultMailURL, sendConsultMailListener, consult_category, title, content, email);
                RequestQueue queue = Volley.newRequestQueue(ConsultActivity.this);
                queue.add(request);
            } else {
                Toast.makeText(ConsultActivity.this, "빈칸 없이 입력해주세요", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult);

        consultCategorySpinner = findViewById(R.id.consult_spinner);
        titleView = findViewById(R.id.consult_title);
        contentView = findViewById(R.id.consult_content);
        emailView = findViewById(R.id.consult_email);

        final Button receiptButton = findViewById(R.id.consult_receipt_btn);
        receiptButton.setOnClickListener(receiptListener);
        receiptButton.setClickable(false);

        final CheckBox agreementCheckBoxView = findViewById(R.id.consult_checkbox);
        agreementCheckBoxView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                receiptButton.setClickable(isChecked);
            }
        });

        final TextView backText = findViewById(R.id.consult_txt);
        backText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(ActivityCodes.CONSULT_FAIL);
                finish();
            }
        });
    }
}
