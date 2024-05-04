package com.example.madproject2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.madproject2.Admin.DatabaseHelper;

public class RegisteredCourseActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    private TextView courseNameTextView;
    private TextView courseFeeTextView;
    private TextView durationTextView;
    private TextView startingDateTextView;
    private TextView publishedDateTextView;
    private TextView closingDateTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_course);

        databaseHelper = new DatabaseHelper(this);

        courseNameTextView = findViewById(R.id.course_name_text_view);
        courseFeeTextView = findViewById(R.id.course_fee_text_view);
        durationTextView = findViewById(R.id.duration_text_view);
        startingDateTextView = findViewById(R.id.starting_date_text_view);
        publishedDateTextView = findViewById(R.id.published_date_text_view);
        closingDateTextView = findViewById(R.id.closing_date_text_view);

        // Get the course ID passed from HomeFragment
        long courseId = getIntent().getLongExtra("courseId", -1);
        Log.d("RegisteredCourseActivity", "Received courseId: " + courseId);

        // Fetch course details from the database based on the course ID
        Course course = databaseHelper.getCourseDetails(courseId);

        if (course != null) {
            Log.d("RegisteredCourseActivity", "Course details retrieved successfully");
            courseNameTextView.setText(course.getCourseName());
            courseFeeTextView.setText(course.getCourseFee());
            durationTextView.setText(course.getDuration());
            startingDateTextView.setText(course.getStartingDate());
            publishedDateTextView.setText(course.getPublishedDate());
            closingDateTextView.setText(course.getClosingDate());
        } else {
            Log.d("RegisteredCourseActivity", "Failed to retrieve course details");
        }
    }
}
