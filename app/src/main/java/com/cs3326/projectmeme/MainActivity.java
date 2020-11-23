package com.cs3326.projectmeme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    SharedPreferences mPrefs;
    final String splashScreenPref= "SplashScreenShown";
    Boolean splashScreenShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        splashScreenShown = mPrefs.getBoolean(splashScreenPref, false);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // Magic Here
                changeToSplashActivity();
            }
        }, 1000); // Millisecond 1000 = 1 sec
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // Magic Here
                finish();
            }
        }, 1000); // Millisecond 1000 = 1 sec
    }

    public void changeToSplashActivity() {
        if (!splashScreenShown) {
            Intent i = new Intent(this, SplashActivity.class);
            startActivity(i);
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putBoolean(splashScreenPref, true);
            editor.commit();
        }
        else {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }
    }
}