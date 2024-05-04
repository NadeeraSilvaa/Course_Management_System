package com.example.madproject2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.madproject2.Admin.AdminHome;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();

        // Check if the user is already signed in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is already signed in, proceed to HomeActivity
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.putExtra("userId", currentUser.getUid());
            startActivity(intent);
            finish(); // Finish MainActivity to prevent going back
        }
    }


    public void sendVerifyEmail(View view) {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(MainActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(MainActivity.this, "Authentication successful.", Toast.LENGTH_SHORT).show();

                        // Determine if the user is an admin or not
                        isAdmin(user.getEmail());
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        Exception exception = task.getException();
                        if (exception != null) {
                            // Handle specific exceptions
                            if (exception instanceof FirebaseAuthInvalidUserException) {
                                // Handle invalid user
                            } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
                                // Handle invalid credentials
                            } else {
                                // Handle other exceptions
                            }
                        }
                    }
                });
    }

    private void isAdmin(String email) {
        // Check if the email belongs to an admin
        // For demonstration purposes, let's assume admin emails are those ending with "admin.com"
        if (email != null && email.endsWith("admin.com")) {
            // Admin login
            Intent intent = new Intent(MainActivity.this, AdminHome.class);
            startActivity(intent);
            finish(); // Finish current activity to prevent user from going back to login screen
        } else {
            // Check if the email belongs to a normal user
            // Proceed with normal user login
            signInNormalUser();
        }
    }

    private void signInNormalUser() {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        intent.putExtra("userId", mAuth.getCurrentUser().getUid()); // Pass user's unique identifier
        startActivity(intent);
        finish(); // Finish current activity to prevent user from going back to login screen
    }

    public void btn_course(View view) {
        Intent intent = new Intent(MainActivity.this, CourseListActivity.class);
        startActivity(intent);
    }
}
