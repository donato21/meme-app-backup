package com.cs3326.projectmeme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.cs3326.projectmeme.app.profile.ProfileFragment;
import com.cs3326.projectmeme.app.timeline.TimelineFragment;

public class AppActivity extends AppCompatActivity {
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        setTitle("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.actionbar, menu);

        // On init change to timeline frag
        changeToTimelineFragment();
        return true;
    }

    private void changeToTimelineFragment() {
        menu.findItem(R.id.miProfile).setVisible(true);
        changeToFragment(new TimelineFragment());
    }

    private void changeToProfileFragment() {
        menu.findItem(R.id.miProfile).setVisible(false);
        changeToFragment(new ProfileFragment());
    }

    private void changeToFragment(Fragment fragment){
        FrameLayout contentView = (FrameLayout) findViewById(R.id.app_fragment_container);
        getSupportFragmentManager().beginTransaction()
                .replace(contentView.getId(), fragment)
                .addToBackStack(null)
                .commit();
    }

    public void onToProfileClicked(MenuItem mi) {
        changeToProfileFragment();
    }

    public void onToTimelineClicked(View view) {
        changeToTimelineFragment();
    }
}