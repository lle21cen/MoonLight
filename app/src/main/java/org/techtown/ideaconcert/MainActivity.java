package org.techtown.ideaconcert;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    final int LOGIN_SUCCESS = 100, LOGIN_FAIL=99;

    UserInformation info;
    final int LOGIN_REQ_CODE = 1001;
    Button login_logout_btn;
    boolean isLoginTurn = true;

    private SharedPreferences loginData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        info = (UserInformation) getApplication();
        login_logout_btn = findViewById(R.id.to_login_activity);
        login_logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoginTurn) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivityForResult(intent, LOGIN_REQ_CODE);
                }
                else {
                    isLoginTurn = true;
                    info.logoutSession();
                    login_logout_btn.setText("로그인");
                }
            }
        });

        // 이미 저장된 사용자 로그인 정보가 있는지 확인
        loginData = getSharedPreferences("loginData", MODE_PRIVATE);
        loadPrevInfo();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQ_CODE) {
            testInfo();
            if (resultCode == LOGIN_SUCCESS) {
                isLoginTurn = false;
                login_logout_btn.setText("로그아웃");
            }
            else if (resultCode == LOGIN_FAIL) {
                Toast.makeText(this, "Login Fail", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void loadPrevInfo() {
        // method, id, name, email
        String method = loginData.getString("loginMethod", null);
        String id = loginData.getString("userID", null);
        String name = loginData.getString("userName", null);
        String email = loginData.getString("userEmail", null);
        if (method != null) {
            info.setUserInformation(method, id, name, email, false);
            testInfo();
            isLoginTurn = false;
            login_logout_btn.setText("로그아웃");
        }
    }

    public void testInfo()
    {
        Toast.makeText(this, "Method = " + info.getLogin_method(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "ID = " + info.getUser_id(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Name = " + info.getUser_name(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Email = " + info.getUserEmail(), Toast.LENGTH_SHORT).show();
    }
}
