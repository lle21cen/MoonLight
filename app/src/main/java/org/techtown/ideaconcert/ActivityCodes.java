package org.techtown.ideaconcert;

import android.app.Application;
import android.content.SharedPreferences;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

public class ActivityCodes extends Application {
    static final int LOGIN_SUCCESS = 100, LOGIN_FAIL = 99, REGISTER_REQUEST = 98; // from LoginActivty
    static final int REGISTER_SUCCESS = 97, REGISTER_FAIL = 96; // from RegisterActivity
}
