package org.techtown.ideaconcert;

import android.app.Application;
import android.content.SharedPreferences;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

public class UserInformation extends Application {
    private String login_method; /* Normal, Facebook, Google */
    private int user_pk;
    private String userEmail; // user_email 로 수정하자 나중에
    private String user_name;
    private int cash;

//    private String user_first_name; // Only Facebook
//    private String user_middle_name; // Only Facebook
//    private String user_last_name; // Only Facebook

    public String getLogin_method() {
        return login_method;
    }

    public void setLogin_method(String login_method) {
        this.login_method = login_method;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }


    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public int getUser_pk() {
        return user_pk;
    }

    public void setUser_pk(int user_pk) {
        this.user_pk = user_pk;
    }

    public int getCash() {
        return cash;
    }

    public void setCash(int cash) {
        this.cash = cash;
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
        user_pk = 0;
        user_name = null;
        userEmail = null;
        SharedPreferences loginData = getSharedPreferences("loginData", MODE_PRIVATE);
        SharedPreferences.Editor editor = loginData.edit();
        editor.clear();
        editor.apply();
    }
    public void setUserInformation(String method, int pk, String name, String email, boolean autoLogin) {
        user_pk = pk;
        login_method = method;
        user_name = name;
        userEmail = email;
        if (autoLogin) {
            SharedPreferences loginData = getSharedPreferences("loginData", MODE_PRIVATE);
            SharedPreferences.Editor editor = loginData.edit();
            editor.putInt("userPk", pk);
            editor.putString("loginMethod", method);
            editor.putString("userName", name);
            editor.putString("userEmail", email);
            editor.apply();
        }
        // Family name, Given Name need?
    }

}
