package com.example.madproject2.Admin;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.madproject2.R;
import com.google.android.material.textfield.TextInputEditText;

public class AddCourse extends AppCompatActivity {

    private TextInputEditText courseNameEditText;
    private TextInputEditText courseFeeEditText;
    private TextInputEditText durationEditText;
    private TextInputEditText startingDateEditText;
    private TextInputEditText publishedDateEditText;
    private TextInputEditText closingDateEditText;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        databaseHelper = new DatabaseHelper(this);

        // Initialize TextInputEditText fields
        courseNameEditText = findViewById(R.id.courseNameEditText);
        courseFeeEditText = findViewById(R.id.courseFeeEditText);
        durationEditText = findViewById(R.id.durationEditText);
        startingDateEditText = findViewById(R.id.startingDateEditText);
        publishedDateEditText = findViewById(R.id.publishedDateEditText);
        closingDateEditText = findViewById(R.id.closingDateEditText);
    }

    public void addCourse(View view) {
        // Get data from TextInputEditText fields
        String courseName = courseNameEditText.getText().toString().trim();
        String courseFee = courseFeeEditText.getText().toString().trim();
        String duration = durationEditText.getText().toString().trim();
        String startingDate = startingDateEditText.getText().toString().trim();
        String publishedDate = publishedDateEditText.getText().toString().trim();
        String closingDate = closingDateEditText.getText().toString().trim();

        // Check if any field is empty and display warnings for each empty field
        if (courseName.isEmpty()) {
            courseNameEditText.setError("Course name is required");
            return;
        }
        if (courseFee.isEmpty()) {
            courseFeeEditText.setError("Course fee is required");
            return;
        }
        if (duration.isEmpty()) {
            durationEditText.setError("Duration is required");
            return;
        }
        if (startingDate.isEmpty()) {
            startingDateEditText.setError("Starting date is required");
            return;
        }
        if (publishedDate.isEmpty()) {
            publishedDateEditText.setError("Published date is required");
            return;
        }
        if (closingDate.isEmpty()) {
            closingDateEditText.setError("Closing date is required");
            return;
        }

        // Proceed to insert data into the database if all fields are filled
        long courseId = databaseHelper.insertCourse(courseName, courseFee, duration, startingDate, publishedDate, closingDate);

        if (courseId != -1) {
            // Course insertion successful
            Toast.makeText(this, "Course added successfully", Toast.LENGTH_SHORT).show();
            // Optionally, clear the TextInputEditText fields after successful insertion
            clearEditTextFields();
        } else {
            Toast.makeText(this, "Failed to add course", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearEditTextFields() {
        courseNameEditText.setText("");
        courseFeeEditText.setText("");
        durationEditText.setText("");
        startingDateEditText.setText("");
        publishedDateEditText.setText("");
        closingDateEditText.setText("");
    }
}
