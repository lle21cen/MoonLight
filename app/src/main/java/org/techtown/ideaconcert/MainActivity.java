package org.techtown.ideaconcert;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    final int LOGIN_SUCCESS = 100, LOGIN_FAIL = 99;

    UserInformation info;
    final int LOGIN_REQ_CODE = 1001;
    ImageButton mypage_btn; // 타이틀바의 사람 모양 버튼
    boolean isLoginTurn = true;

    private SharedPreferences loginData;
    private ArrayList<URL> urlArray;

    private int num_banner_data;

    private Handler bannerHandler;
    ViewPager pager;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        info = (UserInformation) getApplication();
        urlArray = new ArrayList<>();

        mypage_btn = findViewById(R.id.title_mypage_btn);
        mypage_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLoginTurn) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivityForResult(intent, LOGIN_REQ_CODE);
                } else {
                    isLoginTurn = true;
                    info.logoutSession();
                }
            }
        });

        // 이미 저장된 사용자 로그인 정보가 있는지 확인
        loginData = getSharedPreferences("loginData", MODE_PRIVATE);
        loadPrevInfo();

        // 광고 배너에 넣을 이미지가 몇개인지 개수를 알아와서 그 개수로 초기화
        BannerDBRequest bannerDBRequest = new BannerDBRequest(bannerListener);
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(bannerDBRequest);
        ShowProgressDialog.showProgressDialog(this);

        // 광고 배너 페이저 초기화
        pager = findViewById(R.id.main_pager);

        // 광고 배너 핸들러 => 자동으로 넘기는 기능을 담당
         bannerHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                int cur = pager.getCurrentItem();
                if (cur == num_banner_data-1) {
                    pager.setCurrentItem(0);
                }
                else
                {
                    pager.setCurrentItem(cur+1);
                }
                super.handleMessage(msg);
            }
        };
    }

    // 광고 배너가 일정 주기로 자동으로 넘어가도록 하는 Thread
    Thread bannerThread = new Thread() {
        @Override
        public void run() {
            super.run();
            while (true) {
                try {
                    // 5초에 한번 Handler에 신호를 보내 배너가 다음으로 넘어가도록 함.
                    Thread.sleep(5000);
                    bannerHandler.sendEmptyMessage(0);
                }catch (Exception e) {
                    Log.e("thread", e.getMessage());
                }
            }
        }
    };

    // 광고 배너 데이터베이스에서 배너의 개수와 정보를 알아 와 처리하기 위한 listener
    Response.Listener<String> bannerListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                ShowProgressDialog.dismissProgressDialog();
                JSONObject jsonResponse = new JSONObject(response);
                // php에서 받아온 JSON오브젝트 중에서 DB에 있던 값들의 배열을 JSON 배열로 변환
                JSONArray result = jsonResponse.getJSONArray("result");
                boolean exist = jsonResponse.getBoolean("exist");


                if (exist) {
                    num_banner_data = jsonResponse.getInt("num_result");
                    for (int i=0; i<num_banner_data; i++) {
                        // 데이터베이스에 들어있는 배너의 수만큼 for문을 돌려 url을 배열에 저장
                        JSONObject temp = result.getJSONObject(i);
                        urlArray.add(new URL(temp.getString("url")));
                    }

                    // 광고 배너 ViewPager 관련 변수 선언, 초기화
                    BannerPagerAdapter adapter = new BannerPagerAdapter(getLayoutInflater(), num_banner_data, urlArray);
                    pager.setAdapter(adapter);
                    bannerThread.start();
                }
                else {
                    Log.e("No Banner","표시 할 배너가 없습니다.");
                }
            } catch (Exception e) {
                Log.e("bannerListener", e.getMessage());
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQ_CODE) {
            testInfo();
            if (resultCode == LOGIN_SUCCESS) {
                isLoginTurn = false;
            } else if (resultCode == LOGIN_FAIL) {
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
        }
    }

    public void testInfo() {
        Toast.makeText(this, "Method = " + info.getLogin_method(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "ID = " + info.getUser_id(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Name = " + info.getUser_name(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Email = " + info.getUserEmail(), Toast.LENGTH_SHORT).show();
    }
}
