package org.techtown.ideaconcert;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FindIDPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    final static private String verificationCodeCheckURL = "http://lle21cen.cafe24.com/VerificationCodeCheck.php"; // 사용자가 입력한 인증코드를 비교하는 URL
    Button btn_send, btn_cancel, btn_submit, btn_finish;
    EditText email_txt, code_txt, new_pwd, new_pwd_confirm;
    int submitChance = 5;

    LinearLayout hidden_layout;

    // 5분 안에 인증 코드를 입력하지 않을시 처리방법? 인증번호 재 업데이트 후 안알려주기?
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_idpassword);

        hidden_layout = findViewById(R.id.hidden_layout);

        btn_send = findViewById(R.id.btn_send);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_submit = findViewById(R.id.btn_submit);
        btn_finish = findViewById(R.id.finish);

        email_txt = findViewById(R.id.find_email_txt);
        code_txt = findViewById(R.id.find_code_text);
        new_pwd = findViewById(R.id.new_pwd);
        new_pwd_confirm = findViewById(R.id.new_pwd_confirm);

        btn_send.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        btn_finish.setOnClickListener(this);
    }

    Response.Listener<String> sendEmailListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                boolean exist = jsonResponse.getBoolean("exist");
                if (exist) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(FindIDPasswordActivity.this);
                    builder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            code_txt.setText("");
                            code_txt.setVisibility(View.VISIBLE);
                        }
                    });
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        // 이메일이 존재하는 경우 : 이메일로 보낸 인증코드와 비교할 코드를 사용자가 입력하도록 유도
                        builder.setMessage("Check your e-mail").create().show();
                    } else {
                        String errmsg = jsonResponse.getString("errmsg");
                        builder.setMessage("Error occur: " + errmsg).create().show();
                    }
                } else {
                    // 가입되어 있는 이메일이 아닌경우. 회원가입을 먼저 하도록 유도
                    AlertDialog.Builder builder = new AlertDialog.Builder(FindIDPasswordActivity.this);
                    builder.setMessage("This e-mail is not exists. Sign up first.").setCancelable(false)
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    setResult(ActivityCodes.FIND_FAIL);
                                    finish();
                                }
                            }).create().show();
                }
            } catch (Exception e) {
                Log.e("find_error", e.getMessage());
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send:
                try {
                    String userEmail = email_txt.getText().toString();
                    sendCodeRequest sendCodeRequest = new sendCodeRequest(userEmail, sendEmailListener);
                    RequestQueue requestQueue = Volley.newRequestQueue(FindIDPasswordActivity.this);
                    requestQueue.add(sendCodeRequest);
                } catch (Exception e) {
                    Log.e("onClick error", e.getMessage());
                }
                break;

            case R.id.btn_submit:
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                Toast.makeText(FindIDPasswordActivity.this, "Confirm", Toast.LENGTH_SHORT).show();
                                hidden_layout.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(FindIDPasswordActivity.this, "Chance: " + --submitChance + "/5", Toast.LENGTH_SHORT).show();
                                if (submitChance == 0) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(FindIDPasswordActivity.this);
                                    builder.setMessage("Chance over. Retry?").setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // retry 버튼이 눌렸을 경우 할 일.
                                        }
                                    }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            setResult(ActivityCodes.FIND_FAIL);
                                            finish();
                                        }
                                    }).create().show();
                                }
                                code_txt.requestFocus();
                            }
                        } catch (Exception e) {
                            Log.e("code check listener", e.getMessage());
                        }
                    }
                };
                try {
                    IdCheckAndRegister idCheckAndRegister = new IdCheckAndRegister(Request.Method.POST, verificationCodeCheckURL, responseListener, null);
                    String userCode = code_txt.getText().toString();
                    String userEmail = email_txt.getText().toString();
                    idCheckAndRegister.sendUserCode(userEmail, userCode);
                    RequestQueue requestQueue = Volley.newRequestQueue(FindIDPasswordActivity.this);
                    requestQueue.add(idCheckAndRegister);
                } catch (Exception e) {
                    Log.e("code check error", e.getMessage());
                }
                // 코드 비교하기
                break;
            case R.id.btn_cancel:
                setResult(ActivityCodes.FIND_FAIL);
                finish();
                break;
            case R.id.finish:
                String userEmail = email_txt.getText().toString();
                String userPassword = new_pwd.getText().toString();
                UpdatePasswordRequest updatePasswordRequest = new UpdatePasswordRequest(userEmail, userPassword, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                Toast.makeText(FindIDPasswordActivity.this, "Finish!!", Toast.LENGTH_SHORT).show();
                                hidden_layout.setVisibility(View.VISIBLE);
                            } else {
                                String errmsg = jsonResponse.getString("errmsg");
                                AlertDialog.Builder builder = new AlertDialog.Builder(FindIDPasswordActivity.this);
                                builder.setMessage("Error: " + errmsg).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        setResult(ActivityCodes.FIND_FAIL);
                                        finish();
                                    }
                                }).create().show();
                            }
                        } catch (Exception e) {
                            Log.e("update pwd error", e.getMessage());
                        }
                    }
                });
                RequestQueue requestQueue = Volley.newRequestQueue(FindIDPasswordActivity.this);
                requestQueue.add(updatePasswordRequest);
        }
    }

    public class UpdatePasswordRequest extends StringRequest {
        private Map<String, String> parameters;
        final static private String URL = "http://lle21cen.cafe24.com/UpdatePassword.php";

        public UpdatePasswordRequest(String userEmail, String userPassword, Response.Listener<String> listener) {
            super(Method.POST, URL, listener, null);
            parameters = new HashMap<>();
            parameters.put("userEmail", userEmail);
            parameters.put("userPassword", userPassword);
        }

        public Map<String, String> getParams() {
            return parameters;
        }
    }

    public class sendCodeRequest extends StringRequest {
        private Map<String, String> parameters;
        final static private String URL = "http://lle21cen.cafe24.com/SendMailCode.php";

        public sendCodeRequest(String userEmail, Response.Listener<String> listener) {
            super(Method.POST, URL, listener, null);
            parameters = new HashMap<>();
            parameters.put("userEmail", userEmail);
        }

        public Map<String, String> getParams() {
            return parameters;
        }
    }
}
