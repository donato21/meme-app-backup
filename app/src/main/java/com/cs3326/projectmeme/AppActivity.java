package com.cs3326.projectmeme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.cs3326.projectmeme.adapter.PostAdapter;
import com.cs3326.projectmeme.app.profile.ProfileFragment;
import com.cs3326.projectmeme.app.timeline.TimelineFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.Query;

public class AppActivity extends AppCompatActivity {

    // TODO: Implement adapter
    private RecyclerView mPostsRecycler;
    private Query mQuery;
    private PostAdapter mAdapter;
    private ViewGroup mEmptyView;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    TextView emailTextView, displayNameTextView;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        setTitle("");

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

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

        public void onToTimelineClicked(View view) {
            changeToTimelineFragment();
        }

        public void updateTimelineUI() {
            menu.findItem(R.id.miProfile).setVisible(true);
        }

    //Profile Functions
        private void changeToProfileFragment() {
            changeToFragment(new ProfileFragment());
        }

        public void onToProfileClicked(MenuItem mi) {
            changeToProfileFragment();
        }

        public void updateProfileUI() {
            menu.findItem(R.id.miProfile).setVisible(false);
            ProfileFragment pf = (ProfileFragment) getSupportFragmentManager().findFragmentById(R.id.app_fragment_container);
            pf.displayNameTextView.setText(currentUser.getDisplayName());
            pf.emailTextView.setText(currentUser.getEmail());
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

    //Firestore Functions
        // TODO: initRecyclerView
        private void initRecyclerView() {
            if( mQuery == null ) {
                //Error
            }

            mAdapter = new PostAdapter(mQuery, (PostAdapter.OnPostSelecterListener) this) {
                @Override
                protected void onDataChanged() {

                }
            };

            // TODO: set layout manager & set adapter
            mPostsRecycler.setLayoutManager(new LinearLayoutManager(this));
            mPostsRecycler.setAdapter(mAdapter);
        }
}