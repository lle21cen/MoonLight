package org.techtown.ideaconcert.MainActivityDir;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.techtown.ideaconcert.ActivityCodes;
import org.techtown.ideaconcert.ContentsMainDir.ContentsMainActivity;
import org.techtown.ideaconcert.LoginActivity;
import org.techtown.ideaconcert.R;
import org.techtown.ideaconcert.ShowProgressDialog;
import org.techtown.ideaconcert.UserInformation;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    final int CONTENTS_WIDTH = 200, CONTENTS_HEIGHT = 250; // 카테고리 컨텐츠와 큐레이션 컨텐츠의 가로, 세로 길이
    final private String categoryContentsURL = "http://lle21cen.cafe24.com/GetCategoryContents.php";
    final private String arrivalContentsURL = "http://lle21cen.cafe24.com/GetArrivalContents.php";

    UserInformation info; // 로그인 한 사용자의 정보를 저장. Application 수준에서 관리됨.
    Button mypage_btn, open_category_btn; // 타이틀바의 사람 모양 버튼
    boolean isLoginTurn = true; // 로그인이 된 상태인지 안된 상태인지 판단. true일 경우 로그인이 안 된 상태

    private SharedPreferences loginData; // 사용자의 로그인 정보를 파일로 저장하여 로그인 상태를 유지함

    ViewPager pager; // 배너
    private final int BANNER_FLIP_TIME = 5000; // 배너가 자동으로 넘어가는 시간 (1000 = 1초)
    private ArrayList<URL> bannerUrlArray; // 배너 데이터베이스에 있는 URL을 저장하여 BannerPagerAdapter 클래스에서 사용
    private int num_banner_data; // 배너 데이터베이스에 들어있는 데이터의 개수를 저장
    private Handler bannerHandler; // 배너가 일정 시간 경과 시 자동으로 넘어가도록 만드는 핸들러

    private TextView pop, love, edu, chivalry, action, comic;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        info = (UserInformation) getApplication();
        bannerUrlArray = new ArrayList<>();

        open_category_btn = findViewById(R.id.main_open_category);
        open_category_btn.setOnClickListener(this); // 카테고리 펼처보기 버튼 setOnClickListener 설정

        pop = findViewById(R.id.main_pop);
        love = findViewById(R.id.main_love);
        edu = findViewById(R.id.main_edu);
        chivalry = findViewById(R.id.main_chivalry);
        action = findViewById(R.id.main_action);
        comic = findViewById(R.id.main_comic);

        pop.setOnClickListener(this);
        love.setOnClickListener(this);
        edu.setOnClickListener(this);
        chivalry.setOnClickListener(this);
        action.setOnClickListener(this);
        comic.setOnClickListener(this);

        mypage_btn = findViewById(R.id.title_mypage_btn);
        mypage_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLoginTurn) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivityForResult(intent, ActivityCodes.LOGIN_REQUEST);
                } else {
                    isLoginTurn = true;
                    info.logoutSession();
                }
            }
        });

        // 이미 저장된 사용자 로그인 정보가 있는지 확인
        loginData = getSharedPreferences("loginData", MODE_PRIVATE);
        loadPrevInfo();

        // 광고 배너 페이저 초기화
        pager = findViewById(R.id.main_pager);

        // 광고 배너에 넣을 이미지가 몇개인지 개수를 알아와서 그 개수로 초기화
        BannerDBRequest bannerDBRequest = new BannerDBRequest(bannerListener);
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
//        requestQueue.add(bannerDBRequest);

        // 광고 배너 핸들러 => 자동으로 넘기는 기능을 담당
        bannerHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                int cur = pager.getCurrentItem();
                if (cur == num_banner_data - 1) {
                    pager.setCurrentItem(0);
                } else {
                    pager.setCurrentItem(cur + 1);
                }
                super.handleMessage(msg);
            }
        };

        // 카테고리별 콘텐츠 정보를 데이터베이스에서 얻어와서 scroll view에 설정
//        LinearLayout categoryContentsLayout = findViewById(R.id.main_category_layout);
        ContentsDBRequest contentsDBRequest = new ContentsDBRequest(CategoryContentsListener, categoryContentsURL);
        requestQueue.add(contentsDBRequest);

        // 신작 콘텐츠 정보를 데이터베이스에서 얻어와서 scroll view에 설정
        LinearLayout contentsLayout = findViewById(R.id.main_arrival_layout);
        ArrivalContentsListener arrivalContentsListener = new ArrivalContentsListener(contentsLayout, this);
        contentsDBRequest = new ContentsDBRequest(arrivalContentsListener, arrivalContentsURL);
        requestQueue.add(contentsDBRequest);
        ShowProgressDialog.showProgressDialog(this);
    }

    // 광고 배너가 일정 주기로 자동으로 넘어가도록 하는 Thread
    Thread bannerThread = new Thread() {
        @Override
        public void run() {
            super.run();
            while (true) {
                try {
                    // 5초에 한번 Handler에 신호를 보내 배너가 다음으로 넘어가도록 함.
                    Thread.sleep(BANNER_FLIP_TIME);
                    bannerHandler.sendEmptyMessage(0);
                } catch (Exception e) {
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
                JSONObject jsonResponse = new JSONObject(response);
                // php에서 받아온 JSON오브젝트 중에서 DB에 있던 값들의 배열을 JSON 배열로 변환
                JSONArray result = jsonResponse.getJSONArray("result");
                boolean exist = jsonResponse.getBoolean("exist");

                if (exist) {
                    num_banner_data = jsonResponse.getInt("num_result");
                    for (int i = 0; i < num_banner_data; i++) {
                        // 데이터베이스에 들어있는 배너의 수만큼 for문을 돌려 url을 배열에 저장
                        JSONObject temp = result.getJSONObject(i);
                        bannerUrlArray.add(new URL(temp.getString("url")));
                    }

                    // 광고 배너 ViewPager 관련 변수 선언, 초기화
                    BannerPagerAdapter adapter = new BannerPagerAdapter(getLayoutInflater(), num_banner_data, bannerUrlArray);
                    pager.setAdapter(adapter);
                    bannerThread.start();
                } else {
                    Log.e("No Banner", "표시 할 배너가 없습니다.");
                }
            } catch (Exception e) {
                Log.e("bannerListener", e.getMessage());
            }
        }
    };

    // 카테고리 콘텐츠 데이터베이스에서 url과 정보를 가져와 처리하는 리스너
    Response.Listener<String> CategoryContentsListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                ShowProgressDialog.dismissProgressDialog();
                JSONObject jsonResponse = new JSONObject(response);
                // php에서 받아온 JSON오브젝트 중에서 DB에 있던 값들의 배열을 JSON 배열로 변환
                JSONArray result = jsonResponse.getJSONArray("result");
                boolean exist = jsonResponse.getBoolean("exist");

                if (exist) {
                    LinearLayout contentsLayout = findViewById(R.id.main_contents_layout);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(CONTENTS_WIDTH, CONTENTS_HEIGHT);
                    lp.setMargins(10, 10, 10, 10);
                    int num_category_contents_data = jsonResponse.getInt("num_result");
                    for (int i = 0; i < num_category_contents_data; i++) {
                        // 데이터베이스에 들어있는 콘텐츠의 수만큼 for문을 돌려 layout에 image추가
                        try {
                            JSONObject temp = result.getJSONObject(i);
                            URL url = new URL(temp.getString("url"));

                            GetBitmapImageFromURL getBitmapImageFromURL = new GetBitmapImageFromURL(url);
                            getBitmapImageFromURL.start();
                            getBitmapImageFromURL.join();
                            Bitmap bitmap = getBitmapImageFromURL.getBitmap();

                            ImageView contentsImg = new ImageView(MainActivity.this);
                            contentsImg.setImageBitmap(bitmap);
//                            contentsImg.setOnClickListener(MainActivity.this); // onClick 리스너 달기

                            contentsImg.setLayoutParams(lp);
                            contentsLayout.addView(contentsImg);
//                            Log.e("size", "" + bitmap.getHeight() + ", " + bitmap.getWidth());
                        } catch (Exception e) {
                            Log.e("setBitmap error", e.getMessage());
                        }
                    }
                } else {
                    Log.e("No Banner", "표시 할 배너가 없습니다.");
                }
            } catch (Exception e) {
                Log.e("contentsListener", e.getMessage());
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityCodes.LOGIN_REQUEST) {
            testInfo();
            if (resultCode == ActivityCodes.LOGIN_SUCCESS) {
                isLoginTurn = false;
            } else if (resultCode == ActivityCodes.LOGIN_FAIL) {
                Toast.makeText(this, "Login Fail", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void loadPrevInfo() {
        // 이전에 로그인 했던 사용자의 정보를 가져와 저장.
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

    @Override
    public void onClick(View view) {

        //--------------------------------------------- 카테고리 텍스트 선택시 색상 변경 -------------------------------------------------- //
        pop.setTextColor(Color.parseColor("#000000"));
        love.setTextColor(Color.parseColor("#000000"));
        edu.setTextColor(Color.parseColor("#000000"));
        chivalry.setTextColor(Color.parseColor("#000000"));
        action.setTextColor(Color.parseColor("#000000"));
        comic.setTextColor(Color.parseColor("#000000"));

        switch (view.getId())
        {
            case R.id.main_pop :
                pop.setTextColor(Color.parseColor("#ff0000"));
                break;

            case R.id.main_love :
                love.setTextColor(Color.parseColor("#ff0000"));
                break;

            case R.id.main_edu :
                edu.setTextColor(Color.parseColor("#ff0000"));
                break;

            case R.id.main_chivalry :
                chivalry.setTextColor(Color.parseColor("#ff0000"));
                break;

            case R.id.main_action :
                action.setTextColor(Color.parseColor("#ff0000"));
                break;

            case R.id.main_comic :
                comic.setTextColor(Color.parseColor("#ff0000"));
                break;
            // ------------------------------------------------------------------------------------------------------------- //
            case R.id.main_open_category :
                startActivity(new Intent(MainActivity.this, ContentsMainActivity.class));
                break;
        }
    }
}
