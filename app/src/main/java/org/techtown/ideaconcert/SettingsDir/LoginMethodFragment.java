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
import org.techtown.ideaconcert.LoginDir.LoginActivity;
import org.techtown.ideaconcert.R;
import org.techtown.ideaconcert.UserInformation;

public class LoginMethodFragment extends Fragment implements View.OnClickListener {

    View view;
    private String email, name, login_method; // user's email and name

    // For google
    private SignInButton google_login_btn;
    private GoogleApiClient googleApiClient;
    private FirebaseAuth auth;
    final int GOOGLE_SIGN_IN = 2001;

    // For Facebook
    CallbackManager callbackManager;
    LoginButton facebook_login;
    String TAG = "myTag";
    ProfileTracker mProfileTracker;

    UserInformation userInformation;
    RequestQueue mRequestQueue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login_method_fragment, container, false);
        mRequestQueue = Volley.newRequestQueue(getActivity());
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
                startActivityForResult(intent, ActivityCodes.LOGIN_REQUEST);
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
                            userInformation.setUserInformation(login_method, 0, name, email, true);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result != null && result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                if (account == null) {
                    Toast.makeText(getActivity(), "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    replaceFragment(new SettingsPreferenceFragment());
                }
//                Log.e(TAG, "이름 = " + account.getDisplayName());
//                Log.e(TAG, "이메일 = " + account.getEmail());
//                Log.e(TAG, "getAccount() = " + account.getAccount());
//                Log.e(TAG, "getIdToken() = " + account.getIdToken());

                email = account.getEmail();
                name = account.getDisplayName();
                login_method = "Google";

                userInformation.setUserInformation(login_method, 0, name, email, true);
                // 사용자 정보 입력하고 액티비티 종료, 이름 형식 정리 필요
                replaceFragment(new SettingsPreferenceFragment());
            } else {
                Log.e("google fail", "" + result.getStatus().getStatusMessage());
            }
        } else if (resultCode == ActivityCodes.LOGIN_SUCCESS) {
            replaceFragment(new SettingsPreferenceFragment());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            userInformation.setUserInformation("Google", 0, currentUser.getDisplayName(), currentUser.getEmail(), true);
            replaceFragment(new SettingsPreferenceFragment());
        }
//        testInfo();
    }

    protected void replaceFragment(Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.settings_layout, fragment).commit();
    }

    public void testInfo() {
        Toast.makeText(getActivity(), "Method = " + userInformation.getLogin_method(), Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), "User PK = " + userInformation.getUser_pk(), Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), "Name = " + userInformation.getUser_name(), Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), "Email = " + userInformation.getUserEmail(), Toast.LENGTH_SHORT).show();
    }
}