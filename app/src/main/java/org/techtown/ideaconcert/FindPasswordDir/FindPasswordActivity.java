package org.techtown.ideaconcert.FindPasswordDir;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
import org.techtown.ideaconcert.ActivityCodes;
import org.techtown.ideaconcert.LoginDir.LoginActivity;
import org.techtown.ideaconcert.LoginDir.LoginRequest;
import org.techtown.ideaconcert.R;

public class FindPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private final String IssueTempPasswordURL = ActivityCodes.DATABASE_IP + "/platform/IssueTempPassword";
    EditText emailView, nameView;
    TextView infoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);

        infoTextView = findViewById(R.id.find_pw_info_text);

        emailView = findViewById(R.id.find_pw_email);
        nameView = findViewById(R.id.find_pw_name);

        final Button confirmButton = findViewById(R.id.find_pw_confirm_btn);
        final Button cancelButton = findViewById(R.id.find_pw_cancel_btn);
        final Button backButton = findViewById(R.id.find_pw_back_btn);
        confirmButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.find_pw_confirm_btn:
                if (emailView.getText().toString() != null && nameView.getText().toString() != null) {
                    IssueTempPasswordRequest request = new IssueTempPasswordRequest(IssueTempPasswordURL, IssueTempPasswordListener, emailView.getText().toString(), nameView.getText().toString());
                    RequestQueue requestQueue = Volley.newRequestQueue(this);
                    requestQueue.add(request);
                }
                break;
            case R.id.find_pw_cancel_btn:
            case R.id.find_pw_back_btn :
                setResult(ActivityCodes.FIND_FAIL);
                finish();
                break;
        }
    }

    private Response.Listener IssueTempPasswordListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean exist = jsonObject.getBoolean("exist");
                if (!exist) {
                    infoTextView.setText("존재하지 않는 회원정보입니다");
                } else {
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        CustomFindPwDialog dialog = new CustomFindPwDialog(FindPasswordActivity.this);
                        dialog.show();
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                setResult(ActivityCodes.FIND_SUCCESS);
                                finish();
                            }
                        });
                    }
                    else {
                        Toast.makeText(FindPasswordActivity.this, "임시비번발급실패 " + jsonObject.getString("errmsg"), Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                Log.e("임시비번발급에러", e.getMessage());
            }
        }
    };

    public class CustomFindPwDialog extends Dialog {

        public CustomFindPwDialog(@NonNull Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 바 삭제
            setContentView(R.layout.send_temp_pw_dialog_layout);
        }

    }
}
