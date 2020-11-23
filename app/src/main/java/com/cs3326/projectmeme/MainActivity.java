package com.cs3326.projectmeme;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (SplashActivity.canShowSplash()) {
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
        else {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
        }
    }

    public void changeToSplashActivity() {
        Intent i = new Intent(this, SplashActivity.class);
        startActivity(i);
        SplashActivity.showedSplash();
    }
}