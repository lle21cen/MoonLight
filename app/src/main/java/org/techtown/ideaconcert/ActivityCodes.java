package org.techtown.ideaconcert;

import android.app.Application;
import android.content.SharedPreferences;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

public class ActivityCodes extends Application {
    public static final int LOGIN_SUCCESS = 100, LOGIN_FAIL = 99,  LOGIN_REQUEST= 98;// from LoginActivty
    public static final int REGISTER_SUCCESS = 97, REGISTER_FAIL = 96, REGISTER_REQUEST = 95; // from RegisterActivity
    public static final int FIND_SUCCESS = 94, FIND_FAIL= 93, FIND_REQUEST = 92; // from RegisterActivity
    public static final int WEBTOON_SUCCESS = 91, WEBTOON_FAIL= 90, WEBTOON_REQUEST = 89; // from ContentsMainActivity
    public static final int COMMENT_SUCCESS = 88, COMMENT_FAIL= 87, COMMENT_REQUEST = 86; // from WebtoonActivity
    public static final int SETTINGS_SUCCESS = 85, SETTINGS_FAIL= 84, SETTINGS_REQUEST = 83; // from WebtoonActivity
    public static final int MYCASH_SUCCESS = 82, MYCASH_FAIL= 81, MYCASH_REQUEST = 80; // from MyPageActivity

    public static final String DATABASE_IP = "http://192.168.1.186:8090/platform/";
}
