package org.techtown.ideaconcert;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    final int LOGIN_SUCCESS = 100, LOGIN_FAIL=99;

    UserInformation info;
    final int LOGIN_REQ_CODE = 1001;
    ImageButton mypage_btn; // 타이틀바의 사람 모양 버튼
    boolean isLoginTurn = true;

    ViewPager vp; // TEST

    private SharedPreferences loginData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        info = (UserInformation) getApplication();

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

        // 광고 배너를 위한 viewpager 선언
        vp = findViewById(R.id.main_pager);
        vp.setAdapter(new pagerAdapter(getSupportFragmentManager()));
        vp.setCurrentItem(0);

        // 광고 배너에 넣을 이미지가 몇개인지 개수를 알아와서 그 개수로 초기화
        BannerFragment.totalPageNum= 3;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQ_CODE) {
            testInfo();
            if (resultCode == LOGIN_SUCCESS) {
                isLoginTurn = false;
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
        }
    }

    public void testInfo()
    {
        Toast.makeText(this, "Method = " + info.getLogin_method(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "ID = " + info.getUser_id(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Name = " + info.getUser_name(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Email = " + info.getUserEmail(), Toast.LENGTH_SHORT).show();
    }
    private class pagerAdapter extends FragmentStatePagerAdapter
    {
        public pagerAdapter(FragmentManager fm)
        {
            super(fm);
        }
        @Override
        public Fragment getItem(int position)
        {
            BannerFragment bannerFragment = new BannerFragment();
            bannerFragment.setPageNumber(position);
            return bannerFragment;
        }
        @Override
        public int getCount()
        {
            // 데이터베이스에서 배너 광고 개수 가져와서 리턴하도록 수정해야 함
            return 3;
        }
    }

}
