package org.techtown.ideaconcert;

import android.app.Application;
import android.content.SharedPreferences;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

public class ActivityCodes extends Application {
    public static final int LOGIN_SUCCESS = 100, LOGIN_FAIL = 99,  LOGIN_REQUEST= 1001;// from LoginActivty
    public static final int REGISTER_SUCCESS = 97, REGISTER_FAIL = 96, REGISTER_REQUEST = 98; // from RegisterActivity
    public static final int FIND_SUCCESS = 95, FIND_FAIL= 94, FIND_REQUEST = 93; // from RegisterActivity
}
