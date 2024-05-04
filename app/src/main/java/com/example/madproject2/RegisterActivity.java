package com.example.madproject2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private EditText nameEditText, emailEditText, passwordEditText, confirmPasswordEditText,
            addressEditText, cityEditText, nicEditText, mobileEditText;
    private RadioGroup genderRadioGroup;
    private Button registerButton;

    private FirebaseAuth mAuth;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Navigate back to the previous activity
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setTitle("User Register");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        addressEditText = findViewById(R.id.addressEditText);
        cityEditText = findViewById(R.id.cityEditText);
        nicEditText = findViewById(R.id.nicEditText);
        mobileEditText = findViewById(R.id.mobileEditText);
        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        registerButton = findViewById(R.id.registerButton);

        // Set click listener for register button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform registration
                register();
            }
        });
    }

    private void register() {
        // Retrieve values from EditText fields
        final String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String address = addressEditText.getText().toString();
        String city = cityEditText.getText().toString();
        String nic = nicEditText.getText().toString();
        String mobile = mobileEditText.getText().toString();
        String gender = "";

        // Validate fields
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || address.isEmpty() ||
                city.isEmpty() || nic.isEmpty() || mobile.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get selected gender
        int selectedRadioButtonId = genderRadioGroup.getCheckedRadioButtonId();
        if (selectedRadioButtonId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
            gender = selectedRadioButton.getText().toString();
        }

        // Create a new user with email and password using Firebase Auth
        String finalGender = gender;
        // Create a new user with email and password using Firebase Auth
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // Save user data to database
                                saveUserDataToDatabase(user.getUid(), name, email, address, city, nic, mobile, finalGender);
                                // Registration successful, show a toast
                                Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();

                                // Navigate to MainActivity
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish(); // Finish the current activity
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegisterActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    // Method to save user data to the database
    private void saveUserDataToDatabase(String userId, String name, String email, String address, String city,
                                        String nic, String mobile, String gender) {
        // Save data to database using your existing method or modify as needed
        UserDbHelper dbHelper = new UserDbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserContract.UserEntry.COLUMN_NAME_USER_ID, userId);
        values.put(UserContract.UserEntry.COLUMN_NAME_NAME, name);
        values.put(UserContract.UserEntry.COLUMN_NAME_EMAIL, email);
        values.put(UserContract.UserEntry.COLUMN_NAME_ADDRESS, address);
        values.put(UserContract.UserEntry.COLUMN_NAME_CITY, city);
        values.put(UserContract.UserEntry.COLUMN_NAME_NIC, nic);
        values.put(UserContract.UserEntry.COLUMN_NAME_GENDER, gender);
        values.put(UserContract.UserEntry.COLUMN_NAME_MOBILE, mobile);

        long newRowId = db.insert(UserContract.UserEntry.TABLE_NAME, null, values);

        // Check if insertion was successful
        if (newRowId != -1) {
            Toast.makeText(this, "Data saved to database", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to save data to database", Toast.LENGTH_SHORT).show();
        }
    }
}
