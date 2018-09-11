package org.techtown.ideaconcert;

import android.app.Application;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    final static private String dupCheckURL = "http://lle21cen.cafe24.com/DupCheck.php"; // 아이디 중복체크 URL
    final static private String emailVerifyAndRegisterURL = "http://lle21cen.cafe24.com/EmailVerifyAndRegister.php"; // 이메일 중복체크와 디비에 회원 등록하는 URL
    final static private String verificationCodeCheckURL = "http://lle21cen.cafe24.com/VerificationCodeCheck.php"; // 사용자가 입력한 인증코드를 비교하는 URL

    EditText idText, passwordText, passwordConfirm;
    TextView dupCheck, passwordCheck, passwordConfirmCheck, countdown_txt;
    String passwd1, passwd2;
    private boolean isPasswordAvailable = false, isIdAvailable = false, isEmailAvailable = false;

    // For email verification.
    TextView emailVerifyBtn;
    EditText emailCode;
    TextView emailCodeSubmitBtn;
    LinearLayout emailLayout;

    AlertDialog.Builder builder;
    int seconds = 300, countdownInterval = 1000;

    boolean isJoinBtnClicked=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        idText = (EditText) findViewById(R.id.regist_id);
        passwordText = (EditText) findViewById(R.id.regist_pwd);
        final EditText nameText = (EditText) findViewById(R.id.regist_name);
        final EditText emailText = (EditText) findViewById(R.id.regist_email);
        passwordConfirm = (EditText) findViewById(R.id.regist_pwd_confirm);

        dupCheck = (TextView) findViewById(R.id.dupCheckButton);
        passwordCheck = (TextView) findViewById(R.id.passwordCheck);
        passwordConfirmCheck = (TextView) findViewById(R.id.passwordConfirmCheck);
        countdown_txt = (TextView) findViewById(R.id.timer_view);

        final CountDownTimer countDownTimer = new CountDownTimer(5 * countdownInterval, countdownInterval) {
            @Override
            public void onTick(long l) {
                seconds--;
                countdown_txt.setText(getString(R.string.timer_format, seconds / 60, seconds % 60));
            }

            @Override
            public void onFinish() {
                Toast.makeText(RegisterActivity.this, "finish", Toast.LENGTH_SHORT).show();
            }
        };

        builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setCancelable(false).setPositiveButton("확인", null);
        Button registerButton = (Button) findViewById(R.id.btn_join);
        formAvailabilityTest();
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmailAvailable && isIdAvailable && isPasswordAvailable) {
                    isJoinBtnClicked = true;
                    countDownTimer.cancel();
                    setResult(ActivityCodes.REGISTER_SUCCESS);
                    finish();
                }
                else {
                    // 회원가입 버튼을 누르기 전에 뭔가를 바꾼 경우. 다시 입력하도록 하도로 할 필요
                }
            }
        });
        idText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isIdAvailable = false;
            }
        });

        // For email verification.
        emailLayout = findViewById(R.id.linear5);
        emailCode = findViewById(R.id.email_verify_code);
        emailCodeSubmitBtn = findViewById(R.id.email_code_submit_btn);
        emailVerifyBtn = findViewById(R.id.email_verify_btn);
        emailVerifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = idText.getText().toString();
                String userPassword = passwordText.getText().toString();
                String userName = nameText.getText().toString();
                String userEmail = emailText.getText().toString();
                // Send verification code to user's email
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean exist = jsonResponse.getBoolean("exist");
                            if (exist) {
                                builder.setMessage("This e-mail is already used. Please use another one.").create().show();
                            } else {
                                builder.setMessage("We send a verification number to your e-mail.\nPlease submit the code in 5 minute").setCancelable(false).create().show();

                                countdown_txt.setVisibility(View.VISIBLE);
                                countDownTimer.start();
                                emailLayout.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e) {
                            Log.e("Email check error", e.getMessage());
                        }
                    }
                };
                if (!isIdAvailable) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage("아이디를 입력하고 중복검사를 눌러주세요.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    idText.requestFocus();
                                    return;
                                }
                            })
                            .create().show();
                } else if (!isPasswordAvailable) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage("비밀번호가 올바르지 않거나 일치하지 않습니다.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    passwordText.requestFocus();
                                    return;
                                }
                            })
                            .create().show();
                } else if (userId.isEmpty()) {
                    idText.requestFocus();
                    return;
                } else if (userPassword.isEmpty()) {
                    passwordText.requestFocus();
                    return;
                } else if (userName.isEmpty()) {
                    nameText.requestFocus();
                    Toast.makeText(RegisterActivity.this, "What is your name?", Toast.LENGTH_SHORT).show();
                    return;
                } else if (userEmail.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Write your e-mail", Toast.LENGTH_SHORT).show();
                    emailText.requestFocus();
                    return;
                } else if (!checkEmail(userEmail)) {
                    builder.setMessage("Email format is invalid").create().show();
                    emailText.requestFocus();
                } else {
                    IdCheckAndRegister idCheckAndRegister = new IdCheckAndRegister(Request.Method.POST, emailVerifyAndRegisterURL, responseListener, null);
//              userEmail = emailText.getText().toString();
                    userEmail = "lle21cen@naver.com"; // 테스트용
                    idCheckAndRegister.doRegister(userId, userPassword, userName, userEmail);
                    RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
                    requestQueue.add(idCheckAndRegister);
                }
            }
        });
        emailCodeSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                emailCodeSubmitBtn.setText(getString(R.string.verified));
                                emailCodeSubmitBtn.setTextColor(Color.parseColor("#32cd32"));
                                isEmailAvailable = true;
                            } else {
                                Toast.makeText(RegisterActivity.this, "not same", Toast.LENGTH_SHORT).show();
                                Toast.makeText(RegisterActivity.this, "기회 몇 번?", Toast.LENGTH_SHORT).show();
                                emailCode.requestFocus();
                            }
                        } catch (Exception e) {
                            Log.d("code check error", e.getMessage());
                        }
                    }
                };
                IdCheckAndRegister idCheckAndRegister = new IdCheckAndRegister(Request.Method.POST, verificationCodeCheckURL, responseListener, null);
                String userCode = emailCode.getText().toString();
//                String userEmail = emailText.getText().toString();
                String userEmail = "lle21cen@naver.com"; // 테스트용
                idCheckAndRegister.sendUserCode(userEmail, userCode);
                RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
                requestQueue.add(idCheckAndRegister);
            }
        });
    }

    public void dupCheck(View v) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean exist = jsonResponse.getBoolean("exist");
                    if (exist) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        builder.setMessage("이 아이디는 이미 사용중입니다. ").setCancelable(false)
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        idText.setText("");
                                    }
                                }).create().show();
                    } else {
                        Toast.makeText(RegisterActivity.this, "아이디를 사용하실 수 있습니다.", Toast.LENGTH_SHORT).show();
                        passwordText.requestFocus();
                        isIdAvailable = true;
                    }
                } catch (Exception e) {
                    Log.d("duplication check error", e.getMessage());
                }
            }
        };
        IdCheckAndRegister idCheckAndRegister = new IdCheckAndRegister(Request.Method.POST, dupCheckURL, responseListener, null);
        String userID = idText.getText().toString();
        idCheckAndRegister.checkUserID(userID);
        RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
        requestQueue.add(idCheckAndRegister);
    }

    public void formAvailabilityTest() {

        passwordText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                passwd1 = passwordText.getText().toString();
                passwd2 = passwordConfirm.getText().toString();
                if (!hasFocus) {
                    if (passwd1.isEmpty()) {
                        Toast.makeText(RegisterActivity.this, "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                        isPasswordAvailable = false;
                    } else {
//                        if (비밀번호가 적합할 때) {
//                            ...
//                            passwordCheck.setVisibility(View.VISIBLE);
//                            passwordCheck.setText("사용불가");
//                            passwordCheck.setTextColor(Color.RED);
//                        }
                    }
                }
            }
        });
        passwordText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                passwd1 = passwordText.getText().toString();
                if (passwd1.equals(passwd2)) {
                    passwordConfirmCheck.setTextColor(Color.parseColor("#32cd32"));
                    isPasswordAvailable = true;
                    passwordConfirmCheck.setText("일치");
                } else {
                    passwordConfirmCheck.setTextColor(Color.RED);
                    isPasswordAvailable = false;
                    passwordConfirmCheck.setText("불일치");
                }
            }
        });


        passwd1 = passwordText.getText().toString();
        passwordConfirm.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                passwd2 = passwordConfirm.getText().toString();
                if (passwd1.equals(passwd2)) {
                    passwordConfirmCheck.setTextColor(Color.parseColor("#32cd32"));
                    isPasswordAvailable = true;
                    passwordConfirmCheck.setText("일치");
                } else {
                    passwordConfirmCheck.setTextColor(Color.RED);
                    isPasswordAvailable = false;
                    passwordConfirmCheck.setText("불일치");
                }
            }
        });
    }

    public static boolean checkEmail(String email) {
        // 이메일 형식검사.
        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        boolean isNormal = m.matches();
        return isNormal;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Destroy", Toast.LENGTH_SHORT).show();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    String errmsg = jsonResponse.getString("errmsg");
                    if (success) {

                    } else {
                        Log.d("delete data", errmsg);
                    }
                } catch (Exception e) {
                    Log.d("duplication check error", e.getMessage());
                }
            }
        };

        if (!isJoinBtnClicked) {
            DeleteRequest deleteRequest = new DeleteRequest(responseListener, idText.getText().toString());
            RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
            requestQueue.add(deleteRequest);
        }
    }
    
}
