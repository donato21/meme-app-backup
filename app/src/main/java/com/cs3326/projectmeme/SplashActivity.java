package com.cs3326.projectmeme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // Magic Here
                changeToLoginActivity();
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

    public void changeToLoginActivity() {
        Intent i = new Intent(this, Login.class);//Changed temp to test AppActivity
        startActivity(i);
    }
}