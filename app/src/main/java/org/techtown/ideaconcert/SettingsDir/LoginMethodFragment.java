package org.techtown.ideaconcert.SettingsDir;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.ideaconcert.ActivityCodes;
import org.techtown.ideaconcert.DatabaseRequest;
import org.techtown.ideaconcert.LoginDir.LoginActivity;
import org.techtown.ideaconcert.R;
import org.techtown.ideaconcert.UserInformation;

import java.util.Arrays;

public class LoginMethodFragment extends Fragment implements View.OnClickListener {

    private final String EmailDupCheckURL = "http://lle21cen.cafe24.com/EmailDupCheck.php";
    private final String SignInBySnsURL = "http://lle21cen.cafe24.com/SignInBySns.php";
    private final String SignUpBySnsURL = "http://lle21cen.cafe24.com/SignUpBySns.php";

    View view;
    private int user_pk;
    private String email, name, login_method; // user's email and name

    // For google
    private SignInButton google_login_btn;
    private GoogleApiClient googleApiClient;
    private FirebaseAuth auth;
    final int GOOGLE_SIGN_IN = 1001;

    // For Facebook
    CallbackManager callbackManager;
    LoginButton facebook_login;
    String TAG = "myTag";
    ProfileTracker mProfileTracker;

    UserInformation userInformation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login_method_fragment, container, false);

        Button moonlight_login_btn = view.findViewById(R.id.login_method_moonlight);
        moonlight_login_btn.setOnClickListener(this);
        Button sign_out_btn = view.findViewById(R.id.login_method_sign_out);
        sign_out_btn.setOnClickListener(this);
        userInformation = (UserInformation) getActivity().getApplication();

        // 페이스북과 구글 로그인 API 연동
        // -------------------------------------------------------------------------- //
        //                               FACEBOOK LOGIN                               //
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        AppEventsLogger.activateApp(getActivity().getApplication());

        callbackManager = CallbackManager.Factory.create();

        facebook_login = (LoginButton) view.findViewById(R.id.login_method_facebook);
        facebook_login.setReadPermissions("email");
        // If using in a fragment
        facebook_login.setFragment(this);

        // Callback registration
        facebook_login.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                AccessToken accessToken = loginResult.getAccessToken();
                setUserInformationByFacebook(accessToken);
            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(getActivity(), "취소했습니다.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(getActivity(), "에러가 발생했습니다.\n" + exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        // -------------------------------------------------------------------------- //
        //                               GOOGLE LOGIN                               //
        auth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(new FragmentActivity(), new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(getActivity(), "Google Connection Failed", Toast.LENGTH_SHORT).show();
                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        google_login_btn = view.findViewById(R.id.login_method_google);
        google_login_btn.setOnClickListener(this);
        // -------------------------------------------------------------------------- //

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_method_moonlight:
                userInformation.logoutSession();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.login_method_google:
                userInformation.logoutSession();
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
                break;
            case R.id.login_method_sign_out:
                userInformation.logoutSession();
                replaceFragment(new SettingsPreferenceFragment());
        }
    }

    // Facebook login
    public void setUserInformationByFacebook(AccessToken accessToken) {
        Profile profile = Profile.getCurrentProfile();
        if (profile == null) {
            mProfileTracker = new ProfileTracker() {
                @Override
                protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                    mProfileTracker.stopTracking();
                }
            };
            mProfileTracker.startTracking();
        }
        requestUserProfile(accessToken);
    }

    public void requestUserProfile(AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            email = response.getJSONObject().getString("email");
                            name = response.getJSONObject().getString("name");
                            login_method = "Facebook";

                            snsLoginProcess();
                            userInformation.setUserInformation(login_method, user_pk, name, email, true);
                            replaceFragment(new SettingsPreferenceFragment());
                        } catch (JSONException e) {
                            Log.e("facebook error", e.getMessage());
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private Response.Listener<String> emailDupCheckListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean exist = jsonObject.getBoolean("exist");
                if (exist) {
                    // 일치하는 이메일이 존재하는 경우 SignInListener에서 user_pk 값 설정
                    DatabaseRequest databaseRequest = new DatabaseRequest(signInBySnsListener, SignInBySnsURL, email);
                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                    requestQueue.add(databaseRequest);
                } else {
                    // 일치하는 이메일이 존재하지 않는 경우 데이터베이스에 이메일, 이름, 로그인 방법 저장
                    DatabaseRequest databaseRequest = new DatabaseRequest(null, SignUpBySnsURL, email, name, login_method);
                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                    requestQueue.add(databaseRequest);
                }
            } catch (Exception e) {
                Log.e("dup check error", "" + e.getMessage());
            }
        }
    };

    private Response.Listener<String> signUpBySnsListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean success = jsonObject.getBoolean("success");
                if (!success) {
                    Toast.makeText(getActivity(), ""+jsonObject.getString("errmsg"), Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Log.e("sns sign in error", e.getMessage());
            }
        }
    };

    private Response.Listener<String> signInBySnsListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean exist = jsonObject.getBoolean("exist");
                if (exist) {
                    user_pk = jsonObject.getInt("user_pk");
                } else {
                    Log.e("sign in sns error", "" + jsonObject.getString("errmsg"));
                }

            } catch (Exception e) {
                Log.e("sns sign in error", e.getMessage());
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();

                if (account == null) {
                    Toast.makeText(getActivity(), "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    replaceFragment(new SettingsPreferenceFragment());
                }
                Log.e(TAG, "이름 = " + account.getDisplayName());
                Log.e(TAG, "이메일 = " + account.getEmail());
                Log.e(TAG, "getAccount() = " + account.getAccount());
                Log.e(TAG, "getIdToken() = " + account.getIdToken());

                email = account.getEmail();
                name = account.getDisplayName();
                login_method = "Google";

                snsLoginProcess();

                // 사용자 정보 입력하고 액티비티 종료, 이름 형식 정리 필요
                userInformation.setUserInformation(login_method, user_pk, account.getDisplayName(), account.getEmail(), true);
                replaceFragment(new SettingsPreferenceFragment());
            } else {
                Log.e("google fail", "" + result.getStatus().getStatusMessage());
            }
        } else if (resultCode == ActivityCodes.LOGIN_SUCCESS) {
            replaceFragment(new SettingsPreferenceFragment());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            userInformation.setUserInformation("Google", 1, currentUser.getDisplayName(), currentUser.getEmail(), true); // 유저 pk 변경할 필요 있음
            replaceFragment(new SettingsPreferenceFragment());
        }
        testInfo();
    }

    protected void replaceFragment(Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.settings_layout, fragment).commit();
    }

    protected void snsLoginProcess() {
        // 1. SNS 로그인으로 얻어온 email이 데이터베이스에 존재하는지 확인한다.
        // 2. 존재한다면 user_pk를 얻어온다.
        // 3. 존재하지 않는다면 SNS 로그인으로 얻어온 이메일, 이름 정보를 이용하여 데이터베이스에 저장한다.
        DatabaseRequest databaseRequest = new DatabaseRequest(emailDupCheckListener, EmailDupCheckURL, email);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(databaseRequest);
    }

    public void testInfo() {
        Toast.makeText(getActivity(), "Method = " + userInformation.getLogin_method(), Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), "User PK = " + userInformation.getUser_pk(), Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), "Name = " + userInformation.getUser_name(), Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), "Email = " + userInformation.getUserEmail(), Toast.LENGTH_SHORT).show();
    }

}
