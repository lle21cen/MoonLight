package org.techtown.ideaconcert.SettingsDir;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.techtown.ideaconcert.R;

public class SettingsActivity extends AppCompatActivity {

    FragmentManager fm;
    FragmentTransaction transaction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_settings);
        setContentView(R.layout.settings_practice);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content, new DummyFragment()).commit();
        }
    }
}