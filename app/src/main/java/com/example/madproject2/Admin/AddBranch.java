package com.example.madproject2.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.madproject2.Course;
import com.example.madproject2.R;

import java.util.ArrayList;
import java.util.List;

public class AddBranch extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    private Spinner courseSpinner;
    private EditText branchNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_branch);

        databaseHelper = new DatabaseHelper(this);

        // Initialize views
        courseSpinner = findViewById(R.id.course_spinner);
        branchNameEditText = findViewById(R.id.branch_name_edit_text);

        // Populate course spinner with available courses
        populateCourseSpinner();
    }

    // Method to populate the course spinner with available courses
    private void populateCourseSpinner() {
        List<String> courseNames = new ArrayList<>();
        List<Course> courses = databaseHelper.getAllCourses();

        for (Course course : courses) {
            courseNames.add(course.getCourseName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courseNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSpinner.setAdapter(adapter);
    }

    // Method to handle add branch button click
    public void addBranch(View view) {
        String selectedCourseName = courseSpinner.getSelectedItem().toString();
        String branchName = branchNameEditText.getText().toString().trim();

        // Check if branch name is empty
        if (branchName.isEmpty()) {
            Toast.makeText(this, "Please enter branch name", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the course ID based on the selected course name
        long courseId = databaseHelper.getCourseIdByName(selectedCourseName);

        // Insert branch into the database for the selected course
        long result = databaseHelper.insertCourseBranch(courseId, branchName);

        if (result != -1) {
            Toast.makeText(this, "Branch added successfully", Toast.LENGTH_SHORT).show();
            branchNameEditText.setText("");
        } else {
            Toast.makeText(this, "Failed to add branch", Toast.LENGTH_SHORT).show();
        }
    }
}
