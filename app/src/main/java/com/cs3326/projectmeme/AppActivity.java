package com.cs3326.projectmeme;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cs3326.projectmeme.app.makepost.MakePostFragment;
import com.cs3326.projectmeme.app.profile.ProfileFragment;
import com.cs3326.projectmeme.app.timeline.TimelineFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class AppActivity extends AppCompatActivity {

    public FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    public Menu menu;

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
            setTitle("Explore");
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
            setTitle("Profile");
            menu.findItem(R.id.miProfile).setVisible(false);
            ProfileFragment pf = (ProfileFragment) getSupportFragmentManager().findFragmentById(R.id.app_fragment_container);
            pf.profileImageView.setImageURI(currentUser.getPhotoUrl());
            Glide.with(pf.profileImageView.getContext())
                    .load(currentUser.getPhotoUrl())
                    .into(pf.profileImageView);
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

        public void onAddProfileImageClicked(View v) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Image"), ProfileFragment.SELECT_IMAGE_CODE);
        }

    //Fragment Functions
        private void changeToFragment(Fragment fragment){
            FrameLayout contentView = (FrameLayout) findViewById(R.id.app_fragment_container);
            getSupportFragmentManager().beginTransaction()
                        .replace(contentView.getId(), fragment)
                        .addToBackStack(null)
                        .commit();
        }

    public void onFabCreatePostClick(View view) {
        FrameLayout contentView = (FrameLayout) findViewById(R.id.app_fragment_container);

        /*getSupportFragmentManager()
                .beginTransaction()
                .add(contentView.getId(), new MakePostFragment(), "MakePostFragment")
                .commit();*/

        changeToFragment(new MakePostFragment());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ProfileFragment.SELECT_IMAGE_CODE) {
            List<Fragment> fragmentList = getSupportFragmentManager().getFragments();


            for(Fragment fragment : fragmentList) {
                if(fragment instanceof ProfileFragment) {
                    ProfileFragment profileFragment = (ProfileFragment) fragment;
                    if(data != null) {
                        Toast.makeText(this, "Image Selected!", Toast.LENGTH_SHORT).show();
                        profileFragment.processSelectedImage(data.getData());
                    }
                    else {
                        Log.e("MakePostFragment", "Intent data is null");
                    }
                    break;
                }
            }
        }

        if(requestCode == MakePostFragment.SELECT_IMAGE_CODE) {
            /*MakePostFragment makePostFragment = (MakePostFragment) getSupportFragmentManager().findFragmentByTag("MakePostFragment");

            if(makePostFragment != null && data != null) {
                makePostFragment.processSelectedImage(data.getData());
            }
            else {
                Log.e("MakePostFragment", "makePostFragment or Intent data is null");
            }*/

            List<Fragment> fragmentList = getSupportFragmentManager().getFragments();

            for(Fragment fragment : fragmentList) {
                if(fragment instanceof MakePostFragment) {
                    MakePostFragment makePostFragment = (MakePostFragment) fragment;
                    if(data != null) {
                        Toast.makeText(this, "Image Selected!", Toast.LENGTH_SHORT).show();
                        makePostFragment.processSelectedImage(data.getData());
                    }
                    else {
                        Log.e("MakePostFragment", "Intent data is null");
                    }
                    break;
                }
            }
        }
    }
}