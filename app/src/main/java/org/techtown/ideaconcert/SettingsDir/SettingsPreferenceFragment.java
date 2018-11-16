package org.techtown.ideaconcert.SettingsDir;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
import org.techtown.ideaconcert.ActivityCodes;
import org.techtown.ideaconcert.ConsultDir.ConsultActivity;
import org.techtown.ideaconcert.DatabaseRequest;
import org.techtown.ideaconcert.FAQDir.FAQActivity;
import org.techtown.ideaconcert.FindPasswordDir.SetNewPasswordActivity;
import org.techtown.ideaconcert.R;
import org.techtown.ideaconcert.UserInformation;
import org.techtown.ideaconcert.noticeDir.NoticeActivity;

public class SettingsPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {
    private final String snsLoginProcessURL = ActivityCodes.DATABASE_IP + "/platform/SnsLoginProcess";
    UserInformation userInformation;
    SharedPreferences prefs;
    LoginMethodFragment loginMethodFragment;

    ListPreference auto_movie_play;

    public SettingsPreferenceFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings_preference);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefs.registerOnSharedPreferenceChangeListener(prefListener);

        Preference title_pref = findPreference("settings_title_bar_pref");
        Preference login_pref = findPreference("settings_login_info_pref");
        Preference change_pw_pref = findPreference("settings_change_pw");
        Preference consult_pref = findPreference("settings_consult_pref");
        Preference notice_pref = findPreference("settings_notice_pref");
        Preference faq_pref = findPreference("settings_faq_pref");
        Preference version_pref = findPreference("settings_version_pref");
        Preference clause_pref = findPreference("settings_clause_pref");

        SharedPreferences login_data = getActivity().getSharedPreferences("loginData", getActivity().MODE_PRIVATE);
        String user_email = login_data.getString("userEmail", null);
        String login_method = login_data.getString("loginMethod", null);
        if (login_method != null) {
            login_pref.setSummary(user_email);
            if (login_method.equals("Facebook")) {
                login_pref.setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.facebook_small_icon));
            } else if (login_method.equals("Google")) {
                login_pref.setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.google_small_icon));
            } else if (login_method.equals("Normal")) {
                login_pref.setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.moonlight_small_icon));
            }
        } else {
            login_pref.setSummary("로그인이 필요합니다.");
        }

        title_pref.setOnPreferenceClickListener(this);
        login_pref.setOnPreferenceClickListener(this);
        change_pw_pref.setOnPreferenceClickListener(this);
        consult_pref.setOnPreferenceClickListener(this);
        notice_pref.setOnPreferenceClickListener(this);
        faq_pref.setOnPreferenceClickListener(this);
        version_pref.setOnPreferenceClickListener(this);
        clause_pref.setOnPreferenceClickListener(this);

        // 프래그먼트 초기화
        loginMethodFragment = new LoginMethodFragment();

        auto_movie_play = (ListPreference) findPreference("auto_movie_play");
        auto_movie_play.setSummary(auto_movie_play.getValue());
    }

    private SharedPreferences.OnSharedPreferenceChangeListener prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals("auto_rotate")) {
                if (prefs.getBoolean("auto_rotate", false)) {
                    Log.i("prefs", "자동회전이 선택되었습니다.");
                } else {
                    Log.i("prefs", "자동회전이 해제되었습니다.");
                }
            }
            if (key.equals("double_tab")) {
                if (prefs.getBoolean("double_tab", false)) {
                    Log.i("prefs", "더블탭이 선택되었습니다.");
                } else {
                    Log.i("prefs", "더블탭이 해제되었습니다.");
                }
            }
            if (key.equals("use_mobile_network_notice")) {
                if (prefs.getBoolean("use_mobile_network_notice", false)) {
                    Log.i("prefs", "이동통신망알람이 선택되었습니다.");
                } else {
                    Log.i("prefs", "이동통신망알람이 해제되었습니다.");
                }
            }
            if (key.equals("push_notice")) {
                if (prefs.getBoolean("push_notice", false)) {
                    Log.i("prefs", "푸시알림이 선택되었습니다.");
                } else {
                    Log.i("prefs", "푸시알림이 해제되었습니다.");
                }
            }

            if (key.equals("auto_movie_play")) {
                Log.i("prefs", "동영상 자동 재생 설정이 변경되었습니다..");
                auto_movie_play.setSummary(prefs.getString("auto_movie_play", "사용안함"));
            }
        }
    };

    @Override
    public boolean onPreferenceClick(Preference preference) {

        Intent intent;
        switch (preference.getKey()) {
            case "settings_title_bar_pref":
                getActivity().finish();
                break;
            case "settings_login_info_pref":
                getFragmentManager().beginTransaction().replace(R.id.settings_layout, new LoginMethodFragment()).addToBackStack(null).commit();
                break;
            case "settings_consult_pref":
                intent = new Intent(getActivity(), ConsultActivity.class);
                startActivityForResult(intent, ActivityCodes.CONSULT_REQUEST);
                break;
            case "settings_notice_pref":
                intent = new Intent(getActivity(), NoticeActivity.class);
                startActivity(intent);
                break;
            case "settings_faq_pref":
                intent = new Intent(getActivity(), FAQActivity.class);
                startActivity(intent);
                break;
            case "settings_version_pref":
                Toast.makeText(getActivity(), preference.getTitle(), Toast.LENGTH_SHORT).show();
                break;
            case "settings_clause_pref":
                intent = new Intent(getActivity(), TermsAndConditionActivity.class);
                startActivity(intent);
                break;
            case "settings_change_pw" :
                UserInformation userInformation = (UserInformation) getActivity().getApplication();
                String email = userInformation.getUserEmail();
                String login_method = userInformation.getLogin_method();

                if (login_method.equals("Normal")) {
                    intent = new Intent(getActivity(), SetNewPasswordActivity.class);
                    intent.putExtra("email", email);
                    startActivityForResult(intent, ActivityCodes.SET_NEW_PW_REQUEST);
                } else {
                    Toast.makeText(getActivity(), "SNS 로그인 시 비밀번호를 변경할 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        snsLoginProcess();
    }

    protected void snsLoginProcess() {
        // 1. SNS 로그인으로 얻어온 email이 데이터베이스에 존재하는지 확인한다.
        // 2. 존재한다면 user_pk를 얻어온다.
        // 3. 존재하지 않는다면 SNS 로그인으로 얻어온 이메일, 이름 정보를 이용하여 데이터베이스에 저장한다. 그 후에 user_pk를 가져온다.
        userInformation = (UserInformation) getActivity().getApplication();
        String email = userInformation.getUserEmail();
        String name = userInformation.getUser_name();
        String login_method = userInformation.getLogin_method();
        if (login_method != null && !login_method.equals("Normal") && userInformation.getUser_pk() == 0) {
            DatabaseRequest databaseRequest = new DatabaseRequest(snsLoginProcessListener, snsLoginProcessURL, email, name, login_method);
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
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
                Toast.makeText(getActivity(), "email= " + email + "name= " + name + "user_pk= " + pk + "role = " + role, Toast.LENGTH_SHORT).show();
                /////////////////////////////////////////////////////////////////////////////////////
            } catch (Exception e) {
                Log.e("sns sign in error", "" + e.getMessage());
            }
        }
    };
}
