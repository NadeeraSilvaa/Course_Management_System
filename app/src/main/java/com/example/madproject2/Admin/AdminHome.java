package com.example.madproject2.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.madproject2.MainActivity;
import com.example.madproject2.R;
import com.google.firebase.auth.FirebaseAuth;

public class AdminHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
    }

    public void addCourse(View view) {
        Intent intent = new Intent(this, AddCourse.class);
        startActivity(intent);
    }

    public void addBranch(View view) {
        Intent intent = new Intent(this, AddBranch.class);
        startActivity(intent);
    }

    public void logout(View view) {
        // Sign out the current user
        FirebaseAuth.getInstance().signOut();

        // Navigate back to the login screen (MainActivity)
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish(); // Finish the current activity to prevent going back to AdminHome
    }
}