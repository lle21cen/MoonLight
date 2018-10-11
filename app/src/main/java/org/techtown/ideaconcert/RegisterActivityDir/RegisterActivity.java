package org.techtown.ideaconcert.RegisterActivityDir;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
import org.techtown.ideaconcert.ActivityCodes;
import org.techtown.ideaconcert.DeleteRequest;
import org.techtown.ideaconcert.ForceQuitManageService;
import org.techtown.ideaconcert.R;
import org.techtown.ideaconcert.ShowProgressDialog;

public class RegisterActivity extends AppCompatActivity {

    /*
    사용자 회원가입 화면.
    구현 사항

    */

    final static private String emailVerifyAndRegisterURL = "http://lle21cen.cafe24.com/EmailVerifyAndRegister.php"; // 이메일 중복체크와 디비에 회원 등록하는 URL

    TextView passwordCheck, passwordConfirmCheck;
    String passwd1, passwd2;
    private boolean isTwoPasswordaAccord = false;

    // For email verification.

    AlertDialog.Builder builder;

    boolean isJoinBtnClicked = false; // 회원가입 버튼을 눌렀을 시 true로 변하며 activity가 종료되었는데 true가 아닌경우 회원가입을 무효처리함.
    ForceQuitManageService forceQuitManageService; // 강제종료 시 남아있는 데이터를 제거하기 위한 서비스 변수.
    CountDownTimer countDownTimer;

    EditText nameText, emailText, pwdText, pwdConfirmText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        forceQuitManageService = new ForceQuitManageService();
        startService(new Intent(this, forceQuitManageService.getClass()));

        emailText = (EditText) findViewById(R.id.register_email);
        pwdText = (EditText) findViewById(R.id.register_pw);
        pwdConfirmText = (EditText) findViewById(R.id.register_pw_confirm);
        nameText = (EditText) findViewById(R.id.register_name);

        passwordCheck = (TextView) findViewById(R.id.register_pw_check);
        passwordConfirmCheck = (TextView) findViewById(R.id.register_pw_confirm_check);

        builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setCancelable(false).setPositiveButton("확인", null);

        Button registerButton = (Button) findViewById(R.id.register_join_btn);
        formAvailabilityTest();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailText.getText().toString();
                String pw = pwdText.getText().toString();
                String name = nameText.getText().toString();

                boolean isEmailAvailable = ValidatePwdEmail.validateEmail(email); // 이메일 형식검사
                boolean isPasswordAvailable = ValidatePwdEmail.validatePwd(pw); // 비밀번호 형식검사

                if (isTwoPasswordaAccord && isPasswordAvailable && isEmailAvailable) {

                    EmailCheckAndRegister emailCheckAndRegister = new EmailCheckAndRegister(Request.Method.POST, emailVerifyAndRegisterURL, registerListener, null);
                    emailCheckAndRegister.doRegister(email, pw, name);
                    RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
                    requestQueue.add(emailCheckAndRegister);
                    ShowProgressDialog.showProgressDialog(RegisterActivity.this);
                } else if (!isTwoPasswordaAccord) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage("비밀번호 확인과 일치하지 않습니다.").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            pwdText.requestFocus();
                        }
                    }).show();
                } else if (!isPasswordAvailable) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage("비밀번호는 8~16자리, 영문 숫자 혼용입니다.").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            pwdText.requestFocus();
                        }
                    }).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage("이메일 형식이 잘못 되었습니다.").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            emailText.requestFocus();
                        }
                    }).show();
                }
            }
        });
    }

    Response.Listener<String> registerListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                ShowProgressDialog.dismissProgressDialog();
                JSONObject jsonResponse = new JSONObject(response);
                boolean exist = jsonResponse.getBoolean("exist");
                if (exist) {
                    builder.setMessage("이메일이 이미 사용중입니다.").create().show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage("회원가입에 성공했습니다. 이메일을 확인해 주세요.").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setResult(ActivityCodes.REGISTER_SUCCESS);
                            finish();
                        }
                    });
                }
            } catch (Exception e) {
                Log.e("Email check error", e.getMessage());
            }
        }
    };

    public void formAvailabilityTest() {
        pwdText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                passwd1 = pwdText.getText().toString();
                passwd2 = pwdConfirmText.getText().toString();
                if (!hasFocus) {
                    if (passwd1.isEmpty()) {
                        Toast.makeText(RegisterActivity.this, "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                        isTwoPasswordaAccord = false;
                    }
                }
            }
        });
        pwdText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                passwd1 = pwdText.getText().toString();
                if (passwd1.equals(passwd2)) {
                    passwordConfirmCheck.setTextColor(Color.parseColor("#32cd32"));
                    isTwoPasswordaAccord = true;
                    passwordConfirmCheck.setText("일치");
                } else {
                    passwordConfirmCheck.setTextColor(Color.RED);
                    isTwoPasswordaAccord = false;
                    passwordConfirmCheck.setText("불일치");
                }
            }
        });

        passwd1 = pwdText.getText().toString();
        pwdConfirmText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                passwd2 = pwdConfirmText.getText().toString();
                if (passwd1.equals(passwd2)) {
                    passwordConfirmCheck.setTextColor(Color.parseColor("#32cd32"));
                    isTwoPasswordaAccord = true;
                    passwordConfirmCheck.setText("일치");
                } else {
                    passwordConfirmCheck.setTextColor(Color.RED);
                    isTwoPasswordaAccord = false;
                    passwordConfirmCheck.setText("불일치");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!isJoinBtnClicked) {
            // 회원가입 버튼을 누르지 않고 Activity가 종료되면 임시저장하였던 사용자 정보를 삭제
            DeleteRequest deleteRequest = new DeleteRequest(deleteResponseListener, emailText.getText().toString());
            RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
            requestQueue.add(deleteRequest);
        }
    }

    Response.Listener<String> deleteResponseListener = new Response.Listener<String>() {
        // DB에서 임시저장된 사용자 정보를 삭제하는 리스너
        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                boolean success = jsonResponse.getBoolean("success");
                String errmsg = jsonResponse.getString("errmsg");
                if (success) {

                } else {
                    Log.e("delete data", errmsg);
                }
            } catch (Exception e) {
                Log.e("db error for delete", e.getMessage());
            }
        }
    };
}
