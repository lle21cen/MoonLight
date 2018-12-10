package org.techtown.ideaconcert.SettingsDir;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.techtown.ideaconcert.ActivityCodes;
import org.techtown.ideaconcert.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().replace(R.id.settings_layout, new SettingsPreferenceFragment()).commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ActivityCodes.LOGIN_SUCCESS) {
            Log.i("onActivityResult", "login success");
        }
    }
}