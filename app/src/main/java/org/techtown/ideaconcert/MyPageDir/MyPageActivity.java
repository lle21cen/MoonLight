package org.techtown.ideaconcert.MyPageDir;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
import org.techtown.ideaconcert.ActivityCodes;
import org.techtown.ideaconcert.DatabaseRequest;
import org.techtown.ideaconcert.ManageMyWorksDir.ManageMyWorksActivity;
import org.techtown.ideaconcert.MyCashDir.MyCashActivity;
import org.techtown.ideaconcert.R;
import org.techtown.ideaconcert.SettingsDir.SettingsActivity;
import org.techtown.ideaconcert.UserInformation;

public class MyPageActivity extends AppCompatActivity implements View.OnClickListener {

    private final String snsLoginProcessURL = ActivityCodes.DATABASE_IP + "/platform/SnsLoginProcess";

    private TabLayout tabLayout;
    private ViewPager viewPager;
    UserInformation userInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        userInformation = (UserInformation) getApplication();
//        Toast.makeText(this, "role = " + userInformation.getRole(), Toast.LENGTH_SHORT).show();

        Button back_btn = findViewById(R.id.my_page_back);
        back_btn.setOnClickListener(this);

        // 내 캐시 Fragment로 이동하는 TextView와 ImageView의 클릭 리스너 설정
        TextView my_cash = findViewById(R.id.my_page_my_cash);
        TextView user_name = findViewById(R.id.my_page_id);
        SharedPreferences preferences = getSharedPreferences("loginData", MODE_PRIVATE);
        String name = preferences.getString("userName", null);
        if (name != null)
            user_name.setText(name);

        ImageView my_cash_arrow = findViewById(R.id.my_page_my_cash_img);
        my_cash.setOnClickListener(this);
        my_cash_arrow.setOnClickListener(this);

        tabLayout = findViewById(R.id.my_page_tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("최근 본 작품"));
        tabLayout.addTab(tabLayout.newTab().setText("관심 작품"));
        tabLayout.addTab(tabLayout.newTab().setText("관심 작가"));
        tabLayout.addTab(tabLayout.newTab().setText("알림 내역"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);

        viewPager = findViewById(R.id.my_page_pager);
        TabPagerAdapter pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        final Button settings_btn = findViewById(R.id.my_page_title_setting_btn);
        settings_btn.setOnClickListener(this);

        // 현재 테스트를 위해 기본으로 작가 사용자라고 가정
//        setAuthorMode();
        if (userInformation.getRole() == ActivityCodes.ROLE_AUTHOR) {
            // 사용자가 작가일 경우 내 작품 관리 버튼 VISIBLE
            setAuthorMode();
        }
    }

    protected void setAuthorMode() {
        Button managementButton = findViewById(R.id.my_page_management_btn);
        managementButton.setVisibility(View.VISIBLE);
        managementButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.my_page_title_setting_btn:
                intent = new Intent(this, SettingsActivity.class);
                startActivityForResult(intent, ActivityCodes.SETTINGS_REQUEST);
                break;

            case R.id.my_page_back:
                finish();
                break;

            case R.id.my_page_my_cash:
            case R.id.my_page_my_cash_img:
                intent = new Intent(this, MyCashActivity.class);
                startActivityForResult(intent, ActivityCodes.MYCASH_REQUEST);
                break;

            case R.id.my_page_management_btn :
                intent = new Intent(this, ManageMyWorksActivity.class);
                startActivityForResult(intent, ActivityCodes.MANAGE_MY_WORKS_REQUEST);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        snsLoginProcess();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityCodes.SETTINGS_REQUEST) {
            if (userInformation.getUser_pk() == 0) {
                finish();
            }
        }
    }

    protected void snsLoginProcess() {
        // 1. SNS 로그인으로 얻어온 email이 데이터베이스에 존재하는지 확인한다.
        // 2. 존재한다면 user_pk를 얻어온다.
        // 3. 존재하지 않는다면 SNS 로그인으로 얻어온 이메일, 이름 정보를 이용하여 데이터베이스에 저장한다. 그 후에 user_pk를 가져온다.
        String email = userInformation.getUserEmail();
        String name = userInformation.getUser_name();
        String login_method = userInformation.getLogin_method();
        if (login_method != null && !login_method.equals("Normal") && userInformation.getUser_pk() == 0) {
            DatabaseRequest databaseRequest = new DatabaseRequest(snsLoginProcessListener, snsLoginProcessURL, email, name, login_method);
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(databaseRequest);
        }
    }

    private Response.Listener<String> snsLoginProcessListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                int user_pk = jsonObject.getInt("user_pk");
                int role = jsonObject.getInt("user_type_number");
                userInformation.setUser_pk(user_pk);
                userInformation.setRole(role);

                ////////////////////////////// FOR DEBUGGING ////////////////////////////////////////
                String email = userInformation.getUserEmail();
                String name = userInformation.getUser_name();
                int pk = userInformation.getUser_pk();
//                Toast.makeText(getActivity(), "email= " + email + "name= " + name + "user_pk= " + pk + "role = " + role, Toast.LENGTH_SHORT).show();
                /////////////////////////////////////////////////////////////////////////////////////
            } catch (Exception e) {
                Log.e("sns sign in error", "" + e.getMessage());
            }
        }
    };
}
