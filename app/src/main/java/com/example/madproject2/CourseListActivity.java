package com.example.madproject2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.madproject2.Admin.DatabaseHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;



public class CourseListActivity extends AppCompatActivity {

    private ListView listView;
    private DatabaseHelper databaseHelper;

//Fucntion of the back button
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
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_course_list);
        //action bar settings
        setTitle("Courses");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.listView);
        databaseHelper = new DatabaseHelper(this);

        // Fetch courses from the database
        final List<Course> courses = databaseHelper.getAllCourses();

        // Check if there are courses in the database
        if (courses.isEmpty()) {
            // Handle the case when there are no courses in the database
            // For example, display a message to the user
            // You can also redirect the user to add courses activity
            // or any other appropriate action
            // For now, let's just show a toast message
            Toast.makeText(this, "No courses found in the database", Toast.LENGTH_SHORT).show();
        } else {
            // Populate the ListView with the fetched courses
            ArrayAdapter<Course> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, courses);
            listView.setAdapter(adapter);

            // Set click listener for list items
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // Get the selected course
                    final Course selectedCourse = courses.get(position);

                    // Check if the user is logged in
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        // If the user is logged in, proceed to CourseDetailsActivity
                        Intent intent = new Intent(CourseListActivity.this, CourseDetailsActivity.class);
                        intent.putExtra("course", selectedCourse);
                        startActivity(intent);
                    } else {
                        // If the user is not logged in, redirect to the registration activity
                        Intent intent = new Intent(CourseListActivity.this, CourseDetailsActivity.class);
                        intent.putExtra("course", selectedCourse);
                        startActivity(intent);
                    }
                }

            });
        }
    }


    private FirebaseAuth mAuth;
}
