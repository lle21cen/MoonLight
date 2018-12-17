package org.techtown.ideaconcert.LoginDir;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
import org.techtown.ideaconcert.ActivityCodes;
import org.techtown.ideaconcert.FindPasswordDir.FindPasswordActivity;
import org.techtown.ideaconcert.R;
import org.techtown.ideaconcert.RegisterActivityDir.RegisterActivity;
import org.techtown.ideaconcert.UserInformation;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    // For Database login
    EditText userEmail, userPw;
//    private SharedPreferences loginData;
    Button login_btn;
    TextView find_btn, login_result_text;

    UserInformation userInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final TextView toRegisterActivity = findViewById(R.id.login_regist_btn);
        final Button backBtn = findViewById(R.id.login_back_btn);
        final TextView backText = findViewById(R.id.login_login_txt);

        toRegisterActivity.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        backText.setOnClickListener(this);

        // 인터넷 연결 상태 확인하는 코드 추가하기.

        userInformation = (UserInformation) getApplication();
        // --------------------------------------------------------------------------
        //                               find id or password
        find_btn = findViewById(R.id.login_find_btn);
        find_btn.setOnClickListener(this);

        // --------------------------------------------------------------------------
        //                               For save login data
//        loginData = getSharedPreferences("loginData", MODE_PRIVATE);

        // --------------------------------------------------------------------------
        //                               DATABASE LOGIN
        userEmail = findViewById(R.id.login_user_email);
        userPw = findViewById(R.id.login_user_pw);
        login_btn = findViewById(R.id.login_btn);
        login_btn.setOnClickListener(this);

        login_result_text = findViewById(R.id.login_result_text);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn:
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                boolean pw_correct = jsonResponse.getBoolean("pw_correct");
                                if (!pw_correct) {
                                    Toast.makeText(LoginActivity.this, "비밀번호가 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                                } else {
                                    userInformation.logoutSession();

                                    int user_pk = jsonResponse.getInt("user_pk");
                                    String email = jsonResponse.getString("email");
                                    String name = jsonResponse.getString("name");
                                    int role = jsonResponse.getInt("user_type_number");
                                    int cash = jsonResponse.getInt("cash");
                                    Toast.makeText(LoginActivity.this, "cash = " + cash, Toast.LENGTH_SHORT).show();
                                    userInformation.setUserInformation("Normal", user_pk, name, email, true, role, cash);
                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                    builder.setMessage("로그인에 성공했습니다 ").setCancelable(false)
                                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    setResult(ActivityCodes.LOGIN_SUCCESS);
                                                    finish();
                                                }
                                            }).show();
                                }
                            } else {
                                login_result_text.setVisibility(View.VISIBLE);
                                login_result_text.setText("가입정보가 없거나 로그인 정보가 잘못 되었습니다.");
                            }
                        } catch (Exception e) {
                            Log.e("로그인에러", e.getMessage());
                        }
                    }
                };
                LoginRequest loginRequest = new LoginRequest(userEmail.getText().toString(), userPw.getText().toString(), responseListener);
                RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                requestQueue.add(loginRequest);
                break;
            case R.id.login_regist_btn:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent, ActivityCodes.REGISTER_REQUEST);
                break;
            case R.id.login_find_btn:
                // id/Password찾기 버튼 리스너
                intent = new Intent(LoginActivity.this, FindPasswordActivity.class);
                startActivityForResult(intent, ActivityCodes.FIND_REQUEST);
                break;
            case R.id.login_back_btn:
            case R.id.login_login_txt:
                setResult(ActivityCodes.LOGIN_FAIL);
                finish();
        }
    }
}