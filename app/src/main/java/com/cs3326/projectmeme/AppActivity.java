package com.cs3326.projectmeme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

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
        getMenuInflater().inflate(R.menu.actionbar, menu);
        return true;
    }

    public void changeToProfileFragment(MenuItem mi) {
        //TODO: Change fragment code here
        System.out.println("Changed to profile! ");
        if (menu.findItem(R.id.miProfile).isVisible()) {
            menu.findItem(R.id.miProfile).setVisible(false);
        }
        setTitle("Profile");
        TextView tv = (TextView) findViewById(R.id.textView_1);
        tv.setText("Profile");
    }

    public void changeToTimelineFragment(View view) {
        //TODO: Change fragment code here
        System.out.println("Changed to timeline!");
        if (!menu.findItem(R.id.miProfile).isVisible()) {
            menu.findItem(R.id.miProfile).setVisible(true);
        }
        TextView tv = (TextView) findViewById(R.id.textView_1);
        tv.setText("Timeline");
        setTitle("Explore");
    }
}