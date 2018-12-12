package org.techtown.ideaconcert.RegisterActivityDir;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
import org.techtown.ideaconcert.ActivityCodes;
import org.techtown.ideaconcert.LoginDir.LoginActivity;
import org.techtown.ideaconcert.MainActivityDir.MainActivity;
import org.techtown.ideaconcert.R;
import org.techtown.ideaconcert.SettingsDir.TermsAndConditionActivity;
import org.techtown.ideaconcert.ShowProgressDialog;
import org.w3c.dom.Text;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    /*
    사용자 회원가입 화면
    플로우

    */

    final static private String emailVerifyAndRegisterURL = ActivityCodes.DATABASE_IP + "/platform/EmailVerifyAndRegister"; // 이메일 중복체크와 디비에 회원 등록하는 URL

    AlertDialog.Builder builder;
    EditText nameText, emailText, pwdText, pwdConfirmText;
    TextView infoText;
    private CheckBox checkBox;
    private Response.Listener<String> registerListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                ShowProgressDialog.dismissProgressDialog();
                JSONObject jsonResponse = new JSONObject(response);
                boolean exist = jsonResponse.getBoolean("exist");
                if (exist) {
                    builder.setMessage("이메일이 이미 사용중입니다.").create().show();
                } else {
                    CustomRegisterDialog dialog = new CustomRegisterDialog(RegisterActivity.this);
                    dialog.show();
                }
            } catch (Exception e) {
                Log.e("Email check error", e.getMessage());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailText = findViewById(R.id.register_email);
        pwdText = findViewById(R.id.register_pw);
        pwdConfirmText = findViewById(R.id.register_pw_confirm);
        nameText = findViewById(R.id.register_name);

        infoText = findViewById(R.id.register_info_text);

        builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setCancelable(false).setPositiveButton("확인", null);

        checkBox = findViewById(R.id.register_checkbox);
        TextView tacView = findViewById(R.id.register_terms_and_condition); // tac == terms and condition
        tacView.setOnClickListener(this);

        final Button cancelBtn = findViewById(R.id.register_cancel_btn);
        cancelBtn.setOnClickListener(this);

        final Button registerBtn = findViewById(R.id.register_join_btn);
        registerBtn.setOnClickListener(this);

        final Button backBtn = findViewById(R.id.register_back_btn);
        final TextView backText = findViewById(R.id.register_back_txt);
        backBtn.setOnClickListener(this);
        backText.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_terms_and_condition:
                Intent intent = new Intent(RegisterActivity.this, TermsAndConditionActivity.class);
                startActivity(intent);
                break;
            case R.id.register_cancel_btn:
                Toast.makeText(RegisterActivity.this, "회원가입이 취소 되었습니다.", Toast.LENGTH_SHORT).show();
                setResult(ActivityCodes.LOGIN_FAIL);
                finish();
                break;
            case R.id.register_join_btn:
                String email = emailText.getText().toString();
                String pw = pwdText.getText().toString();
                String pw_confirm = pwdConfirmText.getText().toString();
                String name = nameText.getText().toString();

                boolean isTwoPasswordAccord = pw.equals(pw_confirm); // 비밀번호와 비밀번호 확인이 일치하는지 검사
                boolean isEmailAvailable = ValidatePwdEmail.validateEmail(email); // 이메일 형식 검사
                boolean isPasswordAvailable = ValidatePwdEmail.validatePwd(pw); // 비밀번호 형식 검사

                if (checkBox.isChecked() && isTwoPasswordAccord && isPasswordAvailable && isEmailAvailable) {
                    EmailCheckAndRegister emailCheckAndRegister = new EmailCheckAndRegister(Request.Method.POST, emailVerifyAndRegisterURL, registerListener, null);
                    emailCheckAndRegister.doRegister(email, pw, name);
                    RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
                    requestQueue.add(emailCheckAndRegister);
                    ShowProgressDialog.showProgressDialog(RegisterActivity.this);
                } else if (!isTwoPasswordAccord) {
                    infoText.setText(getString(R.string.is_password_same)); // getString 을 꼭 쓸 필요가 있나?
                    pwdText.requestFocus();
                } else if (!isPasswordAvailable) {
                    infoText.setText(R.string.password_policy);
                    pwdText.requestFocus();
                } else if (!checkBox.isChecked()) {
                    infoText.setText("약관 및 이용안내에 동의해 주십시오");
                } else {
                    infoText.setText("이메일 형식이 잘못 되었습니다");
                    emailText.requestFocus();
                }
                break;
            case R.id.register_back_btn:
            case R.id.register_back_txt:
                setResult(ActivityCodes.REGISTER_FAIL);
                finish();
        }
    }

    public class CustomRegisterDialog extends Dialog {

        public CustomRegisterDialog(@NonNull Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 바 삭제
            setContentView(R.layout.sign_up_dialog_layout);

            Button toMainButton = findViewById(R.id.sign_up_dialog_to_main);
            Button toLoginButton = findViewById(R.id.sign_up_dialog_to_login);

            toMainButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });

            toLoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(intent, ActivityCodes.LOGIN_REQUEST);
                }
            });
        }
    }
}
