package org.techtown.ideaconcert;

import android.app.Application;
import android.content.SharedPreferences;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

public class UserInformation extends Application {
    private String login_method; /* Normal, Facebook, Google */
    private String user_id;
    private String user_name;
    private String userEmail;
//    private String user_first_name; // Only Facebook
//    private String user_middle_name; // Only Facebook
//    private String user_last_name; // Only Facebook

    public String getLogin_method() {
        return login_method;
    }

    public void setLogin_method(String login_method) {
        this.login_method = login_method;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

//    public String getUser_first_name() {
//        return user_first_name;
//    }
//
//    public void setUser_first_name(String user_first_name) {
//        this.user_first_name = user_first_name;
//    }
//
//    public String getUser_middle_name() {
//        return user_middle_name;
//    }
//
//    public void setUser_middle_name(String user_middle_name) {
//        this.user_middle_name = user_middle_name;
//    }
//
//    public String getUser_last_name() {
//        return user_last_name;
//    }
//
//    public void setUser_last_name(String user_last_name) {
//        this.user_last_name = user_last_name;
//    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void logoutSession() {
        if (login_method.equals("Facebook")) {
            LoginManager loginManager = LoginManager.getInstance();
            loginManager.logOut();
        }

        if (login_method.equals("Google")) {
            FirebaseAuth.getInstance().signOut();
        }

        login_method = null;
        user_id = null;
        user_name = null;
        userEmail = null;
        SharedPreferences loginData = getSharedPreferences("loginData", MODE_PRIVATE);
        SharedPreferences.Editor editor = loginData.edit();
        editor.clear();
        editor.apply();
    }
    public void setUserInformation(String method, String id, String name, String email, boolean autoLogin) {
        login_method = method;
        user_id = id;
        user_name = name;
        userEmail = email;
        if (autoLogin) {
            SharedPreferences loginData = getSharedPreferences("loginData", MODE_PRIVATE);
            SharedPreferences.Editor editor = loginData.edit();
            editor.putString("loginMethod", method);
            editor.putString("userID", id);
            editor.putString("userName", name);
            editor.putString("userEmail", email);
            editor.apply();
        }
        // Family name, Given Name need?
    }

}
