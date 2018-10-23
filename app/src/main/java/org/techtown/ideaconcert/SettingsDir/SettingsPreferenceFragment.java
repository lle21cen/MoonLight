package org.techtown.ideaconcert.SettingsDir;

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

import org.techtown.ideaconcert.R;

public class SettingsPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {
    SharedPreferences prefs;
    LoginMethodFragment loginMethodFragment;

    ListPreference auto_movie_play;

    public SettingsPreferenceFragment() {}
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings_preference);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefs.registerOnSharedPreferenceChangeListener(prefListener);

        Preference title_pref = findPreference("settings_title_bar_pref");
        Preference login_pref = findPreference("settings_login_info_pref");
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
        }
        else {
            login_pref.setSummary("로그인이 필요합니다.");
        }

        title_pref.setOnPreferenceClickListener(this);
        login_pref.setOnPreferenceClickListener(this);
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

        switch (preference.getKey()) {
            case "settings_title_bar_pref":
                getActivity().finish();
                break;
            case "settings_login_info_pref":
                getFragmentManager().beginTransaction().replace(R.id.settings_layout, new LoginMethodFragment()).commit();
                break;
            case "settings_consult_pref":
                Toast.makeText(getActivity(), preference.getTitle(), Toast.LENGTH_SHORT).show();
                break;
            case "settings_notice_pref":
                Toast.makeText(getActivity(), preference.getTitle(), Toast.LENGTH_SHORT).show();
                break;
            case "settings_faq_pref":
                Toast.makeText(getActivity(), preference.getTitle(), Toast.LENGTH_SHORT).show();
                break;
            case "settings_version_pref":
                Toast.makeText(getActivity(), preference.getTitle(), Toast.LENGTH_SHORT).show();
                break;
            case "settings_clause_pref":
                Toast.makeText(getActivity(), preference.getTitle(), Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
}
