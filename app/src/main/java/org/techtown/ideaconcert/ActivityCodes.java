package org.techtown.ideaconcert;

import android.app.Application;

public class ActivityCodes extends Application {
    public static final int LOGIN_SUCCESS = 100, LOGIN_FAIL = 99,  LOGIN_REQUEST= 98;// from LoginActivty
    public static final int REGISTER_SUCCESS = 97, REGISTER_FAIL = 96, REGISTER_REQUEST = 95; // from RegisterActivity
    public static final int FIND_SUCCESS = 94, FIND_FAIL= 93, FIND_REQUEST = 92; // from RegisterActivity
    public static final int WEBTOON_SUCCESS = 91, WEBTOON_FAIL= 90, WEBTOON_REQUEST = 89; // from ContentsMainActivity
    public static final int COMMENT_SUCCESS = 88, COMMENT_FAIL= 87, COMMENT_REQUEST = 86; // from WebtoonActivity
    public static final int SETTINGS_SUCCESS = 85, SETTINGS_FAIL= 84, SETTINGS_REQUEST = 83; // from WebtoonActivity
    public static final int MYCASH_SUCCESS = 82, MYCASH_FAIL= 81, MYCASH_REQUEST = 80; // from MyPageActivity
    public static final int WEBTOON_MOVIE_SUCCESS = 79, WEBTOON_MOVIE_FAIL= 78, WEBTOON_MOVIE_REQUEST = 77; // from WebtoonActivity
    public static final int SET_NEW_PW_SUCCESS = 76, SET_NEW_PW_FAIL= 75, SET_NEW_PW_REQUEST = 74; // from WebtoonActivity
    public static final int CONSULT_SUCCESS = 73, CONSULT_FAIL= 72, CONSULT_REQUEST = 71; // ConsultActivity

    public static final String DATABASE_IP = "http://58.77.182.75:8090";
}
