package com.cs3326.projectmeme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.cs3326.projectmeme.app.profile.ProfileFragment;
import com.cs3326.projectmeme.app.timeline.TimelineFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AppActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        setTitle("");

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // Check user logged in
        if (currentUser == null){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.actionbar, menu);
        // On init change to timeline frag
        FrameLayout contentView = (FrameLayout) findViewById(R.id.app_fragment_container);
        getSupportFragmentManager().beginTransaction()
                .replace(contentView.getId(), new TimelineFragment())
                .commit();
        return true;
    }

    //Timeline Functions
        private void changeToTimelineFragment() {
            changeToFragment(new TimelineFragment());
        }

        public void updateTimelineUI() {
            // Enable profile icon
            menu.findItem(R.id.miProfile).setVisible(true);
        }

        // Buttons
        public void onToTimelineClicked(View view) {
            changeToTimelineFragment();
        }

    //Profile Functions
        private void changeToProfileFragment() {
            changeToFragment(new ProfileFragment());
        }

        public void updateProfileUI() {
            menu.findItem(R.id.miProfile).setVisible(false);
            ProfileFragment pf = (ProfileFragment) getSupportFragmentManager().findFragmentById(R.id.app_fragment_container);
            pf.displayNameTextView.setText(currentUser.getDisplayName());
            pf.emailTextView.setText(currentUser.getEmail());
        }

        // Buttons
        public void onToProfileClicked(MenuItem mi) {
            changeToProfileFragment();
        }

        public void onSignOutClick(View view){
            mAuth.signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

    //Fragment Functions
        private void changeToFragment(Fragment fragment){
            FrameLayout contentView = (FrameLayout) findViewById(R.id.app_fragment_container);
            getSupportFragmentManager().beginTransaction()
                        .replace(contentView.getId(), fragment)
                        .addToBackStack(null)
                        .commit();
        }
}