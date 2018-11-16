package org.techtown.ideaconcert.FindPasswordDir;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.ideaconcert.ActivityCodes;
import org.techtown.ideaconcert.R;
import org.techtown.ideaconcert.RegisterActivityDir.ValidatePwdEmail;

public class SetNewPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private final String SetNewPasswordURL = ActivityCodes.DATABASE_IP + "/platform/UpdatePassword";

    EditText newPwView,newPwConfirmView;
    TextView infoTextView;

    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);

        email = getIntent().getStringExtra("email");
        if (email == null) {
            Toast.makeText(this, "이메일 전달 오류", Toast.LENGTH_SHORT).show();
            return;
        }

        final Button backBtn = findViewById(R.id.new_pw_back_btn);
        final Button confirmBtn = findViewById(R.id.new_pw_confirm_btn);
        backBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);

        newPwView = findViewById(R.id.new_pw_new_pw);
        newPwConfirmView = findViewById(R.id.new_pw_pw_confirm);

        infoTextView = findViewById(R.id.new_pw_info_text);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.new_pw_back_btn :
                setResult(ActivityCodes.SET_NEW_PW_FAIL);
                finish();
                break;
            case R.id.new_pw_confirm_btn :
                ValidatePwdEmail validatePwdEmail = new ValidatePwdEmail();
                String pw = newPwView.getText().toString();
                boolean isPwValidate = validatePwdEmail.validatePwd(pw);
                if (!isPwValidate) {
                    infoTextView.setText("비밀번호 형식이 올바르지 않습니다");
                    newPwView.requestFocus();
                } else if (!pw.equals(newPwConfirmView.getText().toString())) {
                    infoTextView.setText("비밀번호가 일치하지 않습니다");
                    newPwConfirmView.requestFocus();
                } else {
                    SetNewPasswordRequest request = new SetNewPasswordRequest(SetNewPasswordURL, setNewPwListener, email, pw);
                    RequestQueue queue = Volley.newRequestQueue(this);
                    queue.add(request);
                }
                break;
        }
    }

    private Response.Listener setNewPwListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String  response) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean success = jsonObject.getBoolean("success");
                if (success) {
                    Toast.makeText(SetNewPasswordActivity.this, "비밀번호 변경이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    setResult(ActivityCodes.SET_NEW_PW_SUCCESS);
                    finish();
                } else {
                    Toast.makeText(SetNewPasswordActivity.this, "비밀버호 변경에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    Log.e("비밀번호 변경 실패", ""+jsonObject.getString("errmsg"));
                }
            } catch (JSONException e) {
                Log.e("비밀번호 변경 리스너", e.getMessage());
                e.printStackTrace();
            }
        }
    };
}
