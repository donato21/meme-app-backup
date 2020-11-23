package com.cs3326.projectmeme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthenticatedRouteActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    TextView emailTextView, displayNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_fragment);

        displayNameTextView = (TextView) findViewById(R.id.textViewDisplayName);
        emailTextView = (TextView) findViewById(R.id.textViewEmail);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser == null){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return;
        }

        displayNameTextView.setText(currentUser.getDisplayName());
        displayNameTextView.setText(currentUser.getDisplayName());
        emailTextView.setText(currentUser.getEmail());
    }

    public void onSignOutClick(View view){
        mAuth.signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}