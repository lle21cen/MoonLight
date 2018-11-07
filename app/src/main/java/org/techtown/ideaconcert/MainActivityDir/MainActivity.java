package org.techtown.ideaconcert.MainActivityDir;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.techtown.ideaconcert.ActivityCodes;
import org.techtown.ideaconcert.MyPageDir.MyPageActivity;
import org.techtown.ideaconcert.R;
import org.techtown.ideaconcert.SettingsDir.SettingsActivity;
import org.techtown.ideaconcert.UserInformation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //        final private String getBannerInfoURL = "http://lle21cen.cafe24.com/GetBannerInfo.php";
    final private String getBannerInfoURL = ActivityCodes.DATABASE_IP + "GetBannerInfo";
    final private String categoryContentsURL = ActivityCodes.DATABASE_IP + "GetCategoryContents";
//    final private String selectedContentsURL = "http://lle21cen.cafe24.com/GetSelectedContents.php";
    final private String selectedContentsURL = ActivityCodes.DATABASE_IP + "GetSelectedContents";
//    final private String discountContentsURL = "http://lle21cen.cafe24.com/GetDiscountContents.php";

    ScrollView mainScrollView; // 메인 액태비티 최상위 레이아웃 ScrollView

    UserInformation info; // 로그인 한 사용자의 정보를 저장. Application 수준에서 관리됨.
    Button mypage_btn, open_category_btn, footer_up_btn;

    boolean isLoginTurn = true; // 로그인이 된 상태인지 안된 상태인지 판단. true일 경우 로그인이 안 된 상태 -----------> 지울지 판단할 필요 생김 나중에 귀찮

    private SharedPreferences loginData; // 사용자의 로그인 정보를 파일로 저장하여 로그인 상태를 유지함

    private ViewPager bannerPager; // (???) 배너
    private final int BANNER_FLIP_TIME = 5000; // 배너가 자동으로 넘어가는 시간 (1000 = 1초)
    private int banner_data_num; // 배너 데이터베이스에 들어있는 데이터의 개수를 저장
    private Handler bannerHandler; // 배너가 일정 시간 경과 시 자동으로 넘어가도록 만드는 핸들러

    // 카테고리 메뉴에 필요한 변수들
    private CategoryContentsRecyclerAdapter popAdapter, loveAdapter, academyAdapter, chivalryAdapter, actionAdapter, comicAdapter;

    private RecyclerView categoryRecycler;
    private RecyclerView.LayoutManager categoryRecyclerLayoutManager, newArrivalLayoutManger,
            bestRecyclerManager, recommendRecyclerManager, eventRecyclerManager;

    // 신작 메뉴에 필요한 변수들
    private NewArrivalRecyclerAdapter newArrivalRecyclerAdapter;
    private RecyclerView arrivalRecycler;

    // 할인 메뉴에 필요한 변수들
    private ViewPager discountPager;
    private CircleAnimIndicator circleAnimIndicator;

    // 베스트 메뉴에 필요한 변수들
    private NewArrivalRecyclerAdapter bestRecyclerAdapter;
    private RecyclerView bestRecycler;

    // 추천 메뉴에 필요한 변수들
    private NewArrivalRecyclerAdapter recommendRecyclerAdapter;
    private RecyclerView recommendRecycler;

    // 추천 메뉴에 필요한 변수들
    private EventRecyclerAdapter eventRecyclerAdapter;
    private RecyclerView eventBannerRecycler;

    // categoryContentsListener 클래스 선언
    CategoryContentsListener categoryContentsListener;

    private int deviceWidth;
    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        deviceWidth = dm.widthPixels;

        final RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        mainScrollView = findViewById(R.id.main_scroll_view);

        info = (UserInformation) getApplication(); // 유저 정보를 저장하기 위한 Application 변수

        TextView bestContentsDateTextView = findViewById(R.id.main_best_date);
        Date today = new Date();
        SimpleDateFormat date = new SimpleDateFormat("yyyy.MM");
        bestContentsDateTextView.setText("[" + date.format(today) + "]");

        // 카테고리 TabLayout 기본 설정
        TabLayout category_tab_layout = findViewById(R.id.main_category_tab);
        category_tab_layout.addTab(category_tab_layout.newTab().setText("인기"));
        category_tab_layout.addTab(category_tab_layout.newTab().setText("연애"));
        category_tab_layout.addTab(category_tab_layout.newTab().setText("학원"));
        category_tab_layout.addTab(category_tab_layout.newTab().setText("무협"));
        category_tab_layout.addTab(category_tab_layout.newTab().setText("액션"));
        category_tab_layout.addTab(category_tab_layout.newTab().setText("코믹"));
//        category_tab_layout.addTab(category_tab_layout.newTab().setIcon(R.drawable.option));
        category_tab_layout.setTabGravity(TabLayout.GRAVITY_FILL);

        footer_up_btn = findViewById(R.id.main_footer_up_btn);
        footer_up_btn.setOnClickListener(this);

        // 카테고리 메뉴에 필요한 변수들
        categoryRecycler = findViewById(R.id.main_category_recycler);
        categoryRecyclerLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        categoryRecycler.setLayoutManager(categoryRecyclerLayoutManager);
        categoryRecycler.addItemDecoration(new RecyclerViewDecoration(10));

        popAdapter = new CategoryContentsRecyclerAdapter();
        loveAdapter = new CategoryContentsRecyclerAdapter();
        academyAdapter = new CategoryContentsRecyclerAdapter();
        chivalryAdapter = new CategoryContentsRecyclerAdapter();
        actionAdapter = new CategoryContentsRecyclerAdapter();
        comicAdapter = new CategoryContentsRecyclerAdapter();

        // 어뎁터 변수들 선언
        loveAdapter = new CategoryContentsRecyclerAdapter();
        academyAdapter = new CategoryContentsRecyclerAdapter();
        chivalryAdapter = new CategoryContentsRecyclerAdapter();
        actionAdapter = new CategoryContentsRecyclerAdapter();
        comicAdapter = new CategoryContentsRecyclerAdapter();

        // 신작 메뉴에 필요한 변수들
        arrivalRecycler = findViewById(R.id.main_arrival_recycler);
        newArrivalLayoutManger = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        arrivalRecycler.setLayoutManager(newArrivalLayoutManger);
        newArrivalRecyclerAdapter = new NewArrivalRecyclerAdapter();
        arrivalRecycler.addItemDecoration(new RecyclerViewDecoration(10));

        // 할인 메뉴에 필요한 변수들
        discountPager = findViewById(R.id.main_discount_pager);
        circleAnimIndicator = (CircleAnimIndicator) findViewById(R.id.circleAnimIndicator);
        discountPager.addOnPageChangeListener(onPageChangeListener);


        // 베스트 메뉴에 필요한 변수들
        bestRecycler = findViewById(R.id.main_best_recycler);
        bestRecyclerManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        bestRecycler.setLayoutManager(bestRecyclerManager);
        bestRecyclerAdapter = new NewArrivalRecyclerAdapter();
        bestRecycler.addItemDecoration(new RecyclerViewDecoration(10));

        // 추천 메뉴에 필요한 변수들
        recommendRecycler = findViewById(R.id.main_recommend_recycler);
        recommendRecyclerManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recommendRecycler.setLayoutManager(recommendRecyclerManager);
        recommendRecyclerAdapter = new NewArrivalRecyclerAdapter();
        recommendRecycler.addItemDecoration(new RecyclerViewDecoration(10));

        // 오늘의 이벤트에 필요한 변수들
        eventBannerRecycler = findViewById(R.id.main_event_recycler);
        eventRecyclerManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        eventBannerRecycler.setLayoutManager(eventRecyclerManager);
        eventRecyclerAdapter = new EventRecyclerAdapter(deviceWidth, (int)Math.round(deviceWidth/2.6));
        eventBannerRecycler.addItemDecoration(new RecyclerViewDecoration(10));

        // 이건 왜 여기???
        mypage_btn = findViewById(R.id.title_mypage_btn);
        mypage_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (info.getUser_pk() == 0) {
                    Intent intent;
                    intent = new Intent(getApplicationContext(), SettingsActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent;
                    intent = new Intent(getApplicationContext(), MyPageActivity.class);
                    intent.putExtra("user_pk", info.getUser_pk());
                    startActivity(intent);
                }
            }
        });

        // 이미 저장된 사용자 로그인 정보가 있는지 확인
        loginData = getSharedPreferences("loginData", MODE_PRIVATE);
        loadPrevInfo();

        // 광고 배너 페이저 초기화
        bannerPager = findViewById(R.id.main_pager);
        ConstraintLayout.LayoutParams viewPagerParams = new ConstraintLayout.LayoutParams(deviceWidth, (int)Math.round(deviceWidth / 1.7));
        viewPagerParams.topToBottom = R.id.main_toolbar;
        bannerPager.setLayoutParams(viewPagerParams);

        // 상단 광고 배너(ViewPager)에 넣을 이미지가 몇개인지 개수를 알아와서 그 개수로 초기화
        BannerDBRequest bannerDBRequest = new BannerDBRequest(bannerListener, getBannerInfoURL, 1); // 최상단 배너 정보
        requestQueue.add(bannerDBRequest); // 광고 배너 묶어 놓은 상태 -> 풀어서 사용

        // 이벤트 광고 배너(RecyclerView)에 넣을 이미지가 몇개인지 개수를 알아와서 그 개수로 초기화
        bannerDBRequest = new BannerDBRequest(eventBannerListener, getBannerInfoURL, 2); // 오늘의 이벤트 광고 배너 정보
        requestQueue.add(bannerDBRequest); // 광고 배너 묶어 놓은 상태 -> 풀어서 사용

        // 광고 배너 핸들러 => 자동으로 넘기는 기능을 담당
        bannerHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                int cur = bannerPager.getCurrentItem();
                if (cur == banner_data_num - 1) {
                    bannerPager.setCurrentItem(0);
                } else {
                    bannerPager.setCurrentItem(cur + 1);
                }
                super.handleMessage(msg);
            }
        };

        category_tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition() + 1;
                CategoryContentsRecyclerAdapter adapter = getCategoryAdapterByPosition(position);
                if (adapter.getItemCount() == 0) {
                    categoryContentsListener = new CategoryContentsListener(adapter);
                    ContentsDBRequest contentsDBRequest = new ContentsDBRequest(categoryContentsListener, categoryContentsURL, 0, position); // tag 필요 없음
                    requestQueue.add(contentsDBRequest);
                } else {
                    categoryRecycler.swapAdapter(adapter, true);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // 카테고리별 콘텐츠 정보를 데이터베이스에서 얻어와서 recycler view에 설정
        ContentsDBRequest contentsDBRequest;

        int position = category_tab_layout.getSelectedTabPosition() + 1;
        CategoryContentsRecyclerAdapter adapter = getCategoryAdapterByPosition(position);
        if (adapter.getItemCount() == 0) {
            categoryContentsListener = new CategoryContentsListener(adapter);
            contentsDBRequest = new ContentsDBRequest(categoryContentsListener, categoryContentsURL, 0, position); // tag 필요 없음
            requestQueue.add(contentsDBRequest);
        } else {
            categoryRecycler.setAdapter(adapter);
        }

        // 신작 콘텐츠 정보를 데이터베이스에서 얻어와서 recycler view에 설정
        contentsDBRequest = new ContentsDBRequest(newArrivalListener, selectedContentsURL, 1); // 1 : 신작 작품
        requestQueue.add(contentsDBRequest);

        // 할인 작품 정보를 데이터베이스에서 얻어와서 recycler view에 설정
        contentsDBRequest = new ContentsDBRequest(disCountContentsListener, selectedContentsURL, 2); // 2 : 할인 작품
        requestQueue.add(contentsDBRequest);

        // 베스트 작품 정보를 데이터베이스에서 얻어와서 recycler view에 설정
        contentsDBRequest = new ContentsDBRequest(newArrivalListener, selectedContentsURL, 3); // 3 : 베스트 작품
        requestQueue.add(contentsDBRequest);

        //  작품 정보를 데이터베이스에서 얻어와서 recycler view에 설정
        contentsDBRequest = new ContentsDBRequest(newArrivalListener, selectedContentsURL, 4); // 4 : 추천 작품
        requestQueue.add(contentsDBRequest);
    }

    // Indicator 초기화
    private void initIndicator(int count) {
        circleAnimIndicator.setItemMargin(10);
        circleAnimIndicator.setAnimDuration(300);
        circleAnimIndicator.createDotPanel(count, R.drawable.ic_circle_black_10dp, R.drawable.ic_circle_purple_10dp);
    }

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            circleAnimIndicator.selectDot(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    // 광고 배너가 일정 주기로 자동으로 넘어가도록 하는 Thread
    private Thread bannerThread = new Thread() {
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
    private Response.Listener<String> bannerListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonResponse = new JSONObject(response);

                // php에서 받아온 JSON오브젝트 중에서 DB에 있던 값들의 배열을 JSON 배열로 변환
                JSONArray result = jsonResponse.getJSONArray("result");
                boolean exist = jsonResponse.getBoolean("exist");
                if (exist) {
                    ArrayList<BannerPagerItem> items = new ArrayList<>();
                    int num_result = jsonResponse.getInt("num_result");
                    for (int i = 0; i < num_result; i++) {
                        // 데이터베이스에 들어있는 배너의 수만큼 for문을 돌려 url을 배열에 저장
                        JSONObject temp = result.getJSONObject(i);
                        String url = temp.getString("url");
                        int contents_pk = temp.getInt("contents_pk");
                        items.add(new BannerPagerItem(url));
                    }
                    // 광고 배너 ViewPager 관련 변수 선언, 초기화
                    banner_data_num = num_result;
                    BannerPagerAdapter adapter = new BannerPagerAdapter(getLayoutInflater(), num_result, items, deviceWidth, (int)Math.round(deviceWidth/1.7));
                    bannerPager.setAdapter(adapter);
                    bannerThread.start(); // 최상단 배너에 대한 adater가 설정되면 자동으로 넘어가는 Thread가 실행되도록 설정
                } else {
                    Log.e("No Banner", "표시 할 배너가 없습니다.");
                }
            } catch (Exception e) {
                Log.e("bannerListener", e.getMessage());
            }
        }
    };

    private Response.Listener<String> eventBannerListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                // php에서 받아온 JSON오브젝트 중에서 DB에 있던 값들의 배열을 JSON 배열로 변환
                JSONArray result = jsonResponse.getJSONArray("result");
                boolean exist = jsonResponse.getBoolean("exist");
                if (exist) {
                    int num_result = jsonResponse.getInt("num_result");
                    for (int i = 0; i < num_result; i++) {
                        // 데이터베이스에 들어있는 배너의 수만큼 for문을 돌려 url을 배열에 저장
                        JSONObject temp = result.getJSONObject(i);
                        String url = temp.getString("url");

                        int contents_pk = temp.getInt("contents_pk");

                        eventRecyclerAdapter.addItem(contents_pk, url);
                    }
                    eventBannerRecycler.setAdapter(eventRecyclerAdapter);
                } else {
                    Log.e("No Banner", "표시 할 배너가 없습니다.");
                }
            } catch (Exception e) {
                Log.e("bannerListener", e.getMessage());
            }
        }
    };

    // 선택된 메뉴 리스너
    private Response.Listener<String> newArrivalListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                // php에서 받아온 JSON오브젝트 중에서 DB에 있던 값들의 배열을 JSON 배열로 변환
                JSONArray result = jsonResponse.getJSONArray("result");
                boolean exist = jsonResponse.getBoolean("exist");
                int tag = jsonResponse.getInt("tag");
                NewArrivalRecyclerAdapter adapter;
                RecyclerView recyclerView;
                if (tag == 1) {
                    adapter = newArrivalRecyclerAdapter;
                    recyclerView = arrivalRecycler;
                } else if (tag == 3) {
                    adapter = bestRecyclerAdapter;
                    recyclerView = bestRecycler;
                } else if (tag == 4) {
                    adapter = recommendRecyclerAdapter;
                    recyclerView = recommendRecycler;
                } else {
                    Log.e("tag error", "tag" + tag);
                    return;
                }

                if (exist) {
                    int num_category_contents_data = jsonResponse.getInt("num_result");
                    for (int i = 0; i < num_category_contents_data; i++) {
                        // 데이터베이스에 들어있는 콘텐츠의 수만큼 for문을 돌려 layout에 image추가
                        try {
                            JSONObject temp = result.getJSONObject(i);
                            String url = temp.getString("url");
                            int contents_pk = temp.getInt("contents_pk");
                            String contents_name = temp.getString("contents_name");
                            String writer_name = temp.getString("writer_name");
                            String painter_name = temp.getString("painter_name");
                            double star_rating = temp.getDouble("star_rating");

                            adapter.addItem(contents_pk, url, contents_name, star_rating, painter_name);
                        } catch (Exception e) {
                            Log.e("selected contents error", e.getMessage());
                        }
                    }
                    recyclerView.setAdapter(adapter);
                } else {
                    Log.e("No Arrival", "표시 할 신작이 없습니다.");
                }
            } catch (Exception e) {
                Log.e("selectedListener", e.getMessage());
            }
        }
    };

    class CategoryContentsListener implements Response.Listener<String> {

        CategoryContentsRecyclerAdapter adapter;

        public CategoryContentsListener(CategoryContentsRecyclerAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                // php에서 받아온 JSON오브젝트 중에서 DB에 있던 값들의 배열을 JSON 배열로 변환
                boolean exist = jsonResponse.getBoolean("exist");
                if (exist) {
                    JSONArray result = jsonResponse.getJSONArray("result");
                    int num_category_contents_data = jsonResponse.getInt("num_result");
                    for (int i = 0; i < num_category_contents_data; i++) {
                        // 데이터베이스에 들어있는 콘텐츠의 수만큼 for문을 돌려 layout에 image추가
                        try {
                            JSONObject temp = result.getJSONObject(i);
                            int contents_pk = temp.getInt("contents_pk");
                            String url = temp.getString("url");
                            String contents_name = temp.getString("contents_name");
                            String painter_name = temp.getString("painter_name");
//                            String writer_name = temp.getString("writer_name"); // 글 작가 이름 추가?
                            int view_count = temp.getInt("view_count");
                            int movie = temp.getInt("movie");

                            adapter.addItem(url, contents_name, painter_name, view_count, contents_pk, movie);
                        } catch (Exception e) {
                            Log.e("카테고리아이템에러", e.getMessage());
                        }
                    }
                } else {
                    Log.e("No Contents", "표시 할 컨텐츠가 없습니다.");
                }
            } catch (Exception e) {
                Log.e("카테고리리스너에러", "리스너에러");
            } finally {
                categoryRecycler.swapAdapter(adapter, true);
            }
        }
    }

    // 할인메뉴 리스너
    private Response.Listener<String> disCountContentsListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                // php에서 받아온 JSON오브젝트 중에서 DB에 있던 값들의 배열을 JSON 배열로 변환
                JSONArray result = jsonResponse.getJSONArray("result");
                boolean exist = jsonResponse.getBoolean("exist");
                if (exist) {
                    int num_category_contents_data = jsonResponse.getInt("num_result");
                    ArrayList<DiscountContentsItem> items1 = new ArrayList<>();
                    ArrayList<DiscountContentsItem> items2 = new ArrayList<>();
                    ArrayList<DiscountContentsItem> items3 = new ArrayList<>();
                    ArrayList<DiscountContentsItem> items;
                    for (int i = 0; i < num_category_contents_data; i++) {
                        // 데이터베이스에 들어있는 콘텐츠의 수만큼 for문을 돌려 layout에 image추가
                        try {
                            if (i % 3 == 0) items = items1;
                            else if (i % 3 == 1) items = items2;
                            else items = items3;

                            JSONObject temp = result.getJSONObject(i);
                            String url = temp.getString("url");

                            int contents_pk = temp.getInt("contents_pk");
                            String contents_name = temp.getString("contents_name");
                            String writer_name = temp.getString("writer_name");
                            String painter_name = temp.getString("painter_name");
                            String summary = temp.getString("summary");
                            int view_count = temp.getInt("view_count");
                            double star_rating = temp.getDouble("star_rating");

                            DiscountContentsItem item = new DiscountContentsItem();
                            item.setThumbnailUrl(url);
                            item.setContents_pk(contents_pk);
                            item.setTitle(contents_name);
                            item.setWriter(writer_name);
                            item.setPainter(painter_name);
                            item.setSummary(summary);
                            item.setView_count(view_count);
                            item.setStar_rating(star_rating);
                            items.add(item);

                        } catch (Exception e) {
                            Log.e("discount contents error", e.getMessage());
                        }
                    }
                    DiscountPagerAdapter adapter = new DiscountPagerAdapter(getLayoutInflater(), items1, items2, items3);
                    discountPager.setAdapter(adapter);
                    initIndicator(items1.size());
                } else {
                    Log.e("No Arrival", "할인 목록이 없습니다..");
                }
            } catch (Exception e) {
                Log.e("할인작품리스너에러", e.getMessage());
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
        int user_pk = loginData.getInt("userPk", 0);
        String name = loginData.getString("userName", null);
        String email = loginData.getString("userEmail", null);
        int role = loginData.getInt("userRole", 0);
        if (method != null) {
            info.setUserInformation(method, user_pk, name, email, false, role);
            testInfo();
            isLoginTurn = false;
        }
    }

    public void testInfo() {
        Log.i("Main, Method", info.getLogin_method());
        Log.i("Main, User PK", "" + info.getUser_pk());
        Log.i("Main, Name", info.getUser_name());
        Log.i("Main, Email", info.getUserEmail());
        Log.i("Main, Role", "" + info.getRole());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_footer_up_btn:
                mainScrollView.fullScroll(ScrollView.FOCUS_UP);
        }
    }

    public CategoryContentsRecyclerAdapter getCategoryAdapterByPosition(int position) {
        CategoryContentsRecyclerAdapter adapter;
        if (position == 1) {
            adapter = popAdapter;
        } else if (position == 2) {
            adapter = loveAdapter;
        } else if (position == 3) {
            adapter = academyAdapter;
        } else if (position == 4) {
            adapter = chivalryAdapter;
        } else if (position == 5) {
            adapter = actionAdapter;
        } else if (position == 6) {
            adapter = comicAdapter;
        } else {
            return null;
        }
        return adapter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bannerPager.removeAllViews();
        categoryRecycler.removeAllViews();
        arrivalRecycler.removeAllViews();
        discountPager.removeAllViews();
        bestRecycler.removeAllViews();
        recommendRecycler.removeAllViews();
        eventBannerRecycler.removeAllViews();
    }
}
