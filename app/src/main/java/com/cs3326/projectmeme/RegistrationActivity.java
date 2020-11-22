package com.cs3326.projectmeme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {
    EditText displayNameEditText, emailEditText, passwordEditText, passwordConfirmEditText;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();

        displayNameEditText = (EditText) findViewById(R.id.editTextDisplayName);
        emailEditText = (EditText) findViewById(R.id.editTextEmail);
        passwordEditText = (EditText) findViewById(R.id.editTextPassword);
        passwordConfirmEditText = (EditText) findViewById(R.id.editTextPasswordConfirm);
    }

    public void onRegisterClick(View view) {
        final String email, displayName, password, passwordConfirm;
        email = emailEditText.getText().toString();
        displayName = displayNameEditText.getText().toString();
        password = passwordEditText.getText().toString();
        passwordConfirm = passwordConfirmEditText.getText().toString();

        // Email Regex
        Pattern emailRegex = Pattern.compile("^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+(?:[a-zA-Z]{2}|aero|asia|biz|cat|com|coop|edu|gov|info|int|jobs|mil|mobi|museum|name|net|org|pro|tel|travel)$");
        Matcher mEmail = emailRegex.matcher(email);

        // Password Regex
        /*
        Password Regex tester. Rules:
            At least one upper case English letter, (?=.*?[A-Z])
            At least one lower case English letter, (?=.*?[a-z])
            At least one digit, (?=.*?[0-9])
            At least one special character, (?=.*?[#?!@$%^&*-])
            Minimum eight in length .{8,} (with the anchors)
         */
        Pattern passwordRegex = Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$");
        Matcher mPassword = passwordRegex.matcher(password);

        // DisplayName Regex
        // 3-16 char | alphanumeric _ -
        Pattern displayNameRegex = Pattern.compile("^[a-z0-9_-]{3,16}$");
        Matcher mDisplayName = displayNameRegex.matcher(displayName);

        // if displayName is invalid
        if (mDisplayName.matches() == false) {
            Toast.makeText(RegistrationActivity.this, "Invalid display name", Toast.LENGTH_SHORT).show();
            return;
        }

        // if email is invalid
        if (mEmail.matches() == false) {
            Toast.makeText(RegistrationActivity.this, "Invalid email", Toast.LENGTH_SHORT).show();
            return;
        }

        // if password is invalid
        if (mPassword.matches() == false) {
            Toast.makeText(RegistrationActivity.this, "Invalid password", Toast.LENGTH_SHORT).show();
            return;
        }

        // if passwords are not equal
        if (password.equals(passwordConfirm) == false) {
            Toast.makeText(RegistrationActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("RegistrationActivity", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(displayName)
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(RegistrationActivity.this, "Registered", Toast.LENGTH_LONG).show();

                                            if (task.isSuccessful()) {
                                                Log.d("RegistrationActivity", "User profile updated.");
                                            }

                                            Intent intent = new Intent(RegistrationActivity.this, AuthenticatedRouteActivity.class);
                                            startActivity(intent);
                                            return;
                                        }
                                    });

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("RegistrationActivity", "createUserWithEmail:failure", task.getException());

                            Toast.makeText(RegistrationActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

        //Toast.makeText(RegistrationActivity.this, "Valid!", Toast.LENGTH_SHORT).show();
    }
}