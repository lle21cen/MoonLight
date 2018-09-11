package org.techtown.ideaconcert;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    // For Facebook
    CallbackManager callbackManager;
    LoginButton facebook_login;
    String TAG = "myTag";
    ProfileTracker mProfileTracker;
    UserInformation userInformation;

    // For google
    private SignInButton btn_google;
    private GoogleApiClient googleApiClient;
    private FirebaseAuth auth;
    final int GOOGLE_SIGN_IN = 1001;

    // For Database login
    EditText edit_userID, edit_userPass;
    private SharedPreferences loginData;
    private CheckBox auto_login;
    Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final Button toRegistActivity = findViewById(R.id.btn_regist);
        toRegistActivity.setOnClickListener(this);

        // 인터넷 연결 상태 확인하는 코드 추가하기.

        // --------------------------------------------------------------------------
        //                               For save login data
        loginData = getSharedPreferences("loginData", MODE_PRIVATE);
        auto_login = findViewById(R.id.auto_login);

        // --------------------------------------------------------------------------
        //                               DATABASE LOGIN
        edit_userID = findViewById(R.id.login_user_id);
        edit_userPass = findViewById(R.id.login_user_pass);
        btn_login = findViewById(R.id.btn_login);

        // --------------------------------------------------------------------------
        //                               FACEBOOK LOGIN
        userInformation = (UserInformation) getApplication();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        // 페이스북 아이디로 이미 로그인이 되어 있는지 판단.
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if (isLoggedIn) {
            setUserInfomationsByFacebook(accessToken);
            setResult(ActivityCodes.LOGIN_SUCCESS);
            finish();
        }
        // Button management
        Button btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);

        callbackManager = CallbackManager.Factory.create();
        facebook_login = findViewById(R.id.btn_facebook);
        facebook_login.setReadPermissions(Arrays.asList("public_profile", "email", "user_friends"));
        facebook_login.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        setUserInfomationsByFacebook(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Log.d(TAG, "onCancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Log.d(TAG, "onError");
                    }
                });
        // --------------------------------------------------------------------------


        // -------------------------------------------------------------------------- //
        //                               GOOGLE LOGIN                               //
        auth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        btn_google = (SignInButton) findViewById(R.id.btn_google);
        btn_google.setOnClickListener(this);
        // -------------------------------------------------------------------------- //
    }

    // Facebook login
    public void setUserInfomationsByFacebook(AccessToken accessToken) {
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
                            String id = response.getJSONObject().getString("id");
                            String email = response.getJSONObject().getString("email");
                            String name = response.getJSONObject().getString("name");
                            userInformation.setUserInformation("Facebook", id, name, email, auto_login.isChecked());
                            setResult(ActivityCodes.LOGIN_SUCCESS);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();

                if (account == null) {
                    setResult(ActivityCodes.LOGIN_FAIL);
                    finish();
                }
                Log.d(TAG, "이름 = " + account.getDisplayName());
                Log.d(TAG, "이메일 = " + account.getEmail());
                Log.d(TAG, "getId() = " + account.getId());
                Log.d(TAG, "getAccount() = " + account.getAccount());
                Log.d(TAG, "getIdToken() = " + account.getIdToken());

                // 사용자 정보 입력하고 액티비티 종료, 이름 형식 정리 필요
                userInformation.setUserInformation("Google", account.getId(), account.getDisplayName(), account.getEmail(), auto_login.isChecked());
                setResult(ActivityCodes.LOGIN_SUCCESS);
                finish();
            }
        }
        else if (requestCode == ActivityCodes.REGISTER_REQUEST) {
            Toast.makeText(this, "It's from Register Act", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            userInformation.setUserInformation("Google", currentUser.getUid(), currentUser.getDisplayName(), currentUser.getEmail(), auto_login.isChecked());
            setResult(ActivityCodes.LOGIN_SUCCESS);
            finish();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {

                                String id = jsonResponse.getString("userID");
                                String name = jsonResponse.getString("userName");
                                String email = jsonResponse.getString("userEmail");
                                userInformation.setUserInformation("Normal", id, name, email, auto_login.isChecked());

                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage("로그인에 성공했습니다 ").setCancelable(false)
                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                setResult(ActivityCodes.LOGIN_SUCCESS);
                                                finish();
                                            }
                                        }).create().show();
                            } else {
                                Toast.makeText(LoginActivity.this, "로그인에 실패했습니다.", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            Log.d("dberror", e.getMessage());
                        }
                    }
                };
                LoginRequest loginRequest = new LoginRequest(edit_userID.getText().toString(), edit_userPass.getText().toString(), responseListener);
                RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                requestQueue.add(loginRequest);
                break;
            case R.id.btn_google:
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
                break;
            case R.id.btn_regist:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent, ActivityCodes.REGISTER_REQUEST);
                break;
        }
    }
}
