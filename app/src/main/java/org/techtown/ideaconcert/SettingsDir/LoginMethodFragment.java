package org.techtown.ideaconcert.SettingsDir;

import android.app.Fragment;
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

import java.util.Arrays;

public class LoginMethodFragment extends Fragment implements View.OnClickListener {

    View view;

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

        // 페이스북과 구글 로그인 API 연동
        // --------------------------------------------------------------------------
        //                               FACEBOOK LOGIN
        userInformation = (UserInformation) getActivity().getApplication();
        FacebookSdk.sdkInitialize(FacebookSdk.getApplicationContext());
        AppEventsLogger.activateApp(getActivity());

        callbackManager = CallbackManager.Factory.create();
        facebook_login = view.findViewById(R.id.login_method_facebook);
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
                        Log.e(TAG, "onCancel");
                    }
                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Log.e(TAG, "onError");
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
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.login_method_google:
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
                break;
            case R.id.login_method_sign_out :
                userInformation.logoutSession();
                getActivity().setResult(ActivityCodes.LOGIN_SUCCESS);
                getActivity().finish();
        }
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
        Toast.makeText(getActivity(), "user id = " +accessToken.getUserId(), Toast.LENGTH_SHORT).show();
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            String email = response.getJSONObject().getString("email");
                            String name = response.getJSONObject().getString("name");
                            userInformation.setUserInformation("Facebook", 1, name, email, true); // 유저 pk 변경할 필요 있음
                            getActivity().setResult(ActivityCodes.LOGIN_SUCCESS);
                            getActivity().finish();
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
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();

                if (account == null) {
                    getActivity().setResult(ActivityCodes.LOGIN_FAIL);
                    getActivity().finish();
                }
                Log.e(TAG, "이름 = " + account.getDisplayName());
                Log.e(TAG, "이메일 = " + account.getEmail());
                Log.e(TAG, "getAccount() = " + account.getAccount());
                Log.e(TAG, "getIdToken() = " + account.getIdToken());

                // 사용자 정보 입력하고 액티비티 종료, 이름 형식 정리 필요
                userInformation.setUserInformation("Google", 1, account.getDisplayName(), account.getEmail(), true); // 유저 pk 변경할 필요 있음, 자동로그인도
                getFragmentManager().beginTransaction().replace(R.id.settings_layout, new SettingsPreferenceFragment()).commit();
            } else {
                Log.e("google fail", ""+result.getStatus().getStatusMessage());
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            userInformation.setUserInformation("Google", 1, currentUser.getDisplayName(), currentUser.getEmail(), true); // 유저 pk 변경할 필요 있음
            getActivity().setResult(ActivityCodes.LOGIN_SUCCESS);
            getActivity().finish();
        }
    }
}
