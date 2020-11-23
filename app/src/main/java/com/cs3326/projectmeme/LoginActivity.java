package com.cs3326.projectmeme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    EditText emailEditText;
    EditText passwordEditText;
    Button loginButton;
    Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // init components
        emailEditText = (EditText) findViewById(R.id.editTextEmailAddress);
        passwordEditText = (EditText) findViewById(R.id.editTextPassword);
        loginButton = (Button) findViewById(R.id.buttonRegister);
        registerButton = (Button) findViewById(R.id.buttonRegister);

        // Disable input while checking auth state
        setLoginEditable(false);

        // init firebase auth
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // check authentication state
        if(mAuth.getCurrentUser() == null){
            setLoginEditable(true);
            Log.d("LoginActivity", "No user is signed in");

            // TODO: Remove optional toast
            Toast.makeText(getApplicationContext(), "No session found", Toast.LENGTH_LONG).show();
        }
        else
        {
            Log.d("LoginActivity", "User is signed in: " + currentUser.getEmail().toString());
            Toast.makeText(LoginActivity.this, "Authentication successful.", Toast.LENGTH_SHORT).show();

            // TODO: Redirect to timeline
            Intent intent = new Intent(LoginActivity.this, AppActivity.class);
            startActivity(intent);
            return;

//            // TODO: Remove Temporary enable
//            setLoginEditable(true);
        }
    }

    public void onLoginClick(View view){
        setLoginEditable(false);

        // Collect Text
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        // Email regex tester
        Pattern emailRegex = Pattern.compile("^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+(?:[a-zA-Z]{2}|aero|asia|biz|cat|com|coop|edu|gov|info|int|jobs|mil|mobi|museum|name|net|org|pro|tel|travel)$");
        Matcher mEmail = emailRegex.matcher(email);

        // Password regex testing is extra, but can save on api calls. Let the client deal with the extra computation requirements :^)
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

        Log.d("LoginActivity", "Email Pattern " + mEmail.matches());
        Log.d("LoginActivity", "Password Pattern " + mPassword.matches());

        // Checks pattern matching
        if (mEmail.matches() && mPassword.matches()){
            setLoginEditable(false);

            //patterns match, attempt to login to firebase
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d("LoginActivity", "signInWithEmail:success");
                                Toast.makeText(LoginActivity.this, "Authentication successful.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, AppActivity.class);
                                startActivity(intent);
                                return;

                            } else {
                                Log.w("LoginActivity", "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                passwordEditText.setText("");
                                setLoginEditable(true);
                            }
                        }
                    });
        }
        else{
            Toast.makeText(getApplicationContext(), "Invalid email or password", Toast.LENGTH_SHORT).show();
            passwordEditText.setText("");
            setLoginEditable(true);
        }
    }

    // TODO: Create visual confirmation of component being disabled
    private void setLoginEditable(boolean state){

        if(state){
            emailEditText.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS);
            passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            registerButton.setEnabled(true);
            loginButton.setEnabled(true);
        }
        else
        {
            emailEditText.setInputType(InputType.TYPE_NULL);
            passwordEditText.setInputType(InputType.TYPE_NULL);
            registerButton.setEnabled(false);
            loginButton.setEnabled(false);
        }
    }

    public void onRegisterClick(View view){
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }
}
