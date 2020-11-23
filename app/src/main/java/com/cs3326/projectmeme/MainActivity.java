package com.cs3326.projectmeme;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cs3326.projectmeme.adapter.PostAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {

    // TODO: Implement adapter
    private RecyclerView mPostsRecycler;
    private Query mQuery;
    private PostAdapter mAdapter;
    private ViewGroup mEmptyView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        Intent i = new Intent(this, SplashActivity.class);
        startActivity(i);
    }

    // TODO: initRecyclerView
    private void initRecylclerView() {
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