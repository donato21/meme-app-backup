package com.cs3326.projectmeme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class AppActivity extends AppCompatActivity {
    Menu menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Explore");
        setContentView(R.layout.activity_app);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.timeline_actionbar, menu);
        return true;
    }

    public void changeToProfileFragment(MenuItem mi) {
        //TODO: Change fragment code here
        System.out.println("Menu Profile Icon Clicked! ");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile_actionbar, menu);
        setTitle("Profile");
    }

    public void changeToTimelineFragment() {
        //TODO: Change fragment code here
        // Inflate the menu; this adds items to the action bar if it is present.
        setTitle("Explore");
        getMenuInflater().inflate(R.menu.timeline_actionbar, menu);
    }
}