package com.example.madproject2.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.madproject2.Admin.DatabaseHelper;
import com.example.madproject2.Course;
import com.example.madproject2.R;
import com.example.madproject2.RegisteredCourseActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class HomeFragment extends Fragment {

    private DatabaseHelper databaseHelper;
    private FirebaseAuth mAuth;
    private TextView registeredCoursesTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(requireContext());
        mAuth = FirebaseAuth.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        registeredCoursesTextView = view.findViewById(R.id.registered_courses_text_view);

        // Set click listener for course names
        registeredCoursesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Fetch the courseId from the TextView tag
                long courseId = (long) registeredCoursesTextView.getTag();

                // Open the CourseDetailsActivity and pass the courseId
                Intent intent = new Intent(requireContext(), RegisteredCourseActivity.class);
                intent.putExtra("courseId", courseId);
                startActivity(intent);
            }
        });

        // Display registered courses for the current user
        displayRegisteredCourses();

        return view;
    }

    // Method to fetch and display registered courses for the current user
    // Display registered courses for the current user
    // Display registered courses for the current user
    private void displayRegisteredCourses() {
        // Retrieve user ID passed from HomeActivity
        Bundle args = getArguments();
        if (args != null) {
            String userId = args.getString("userId");
            Log.d("HomeFragment", "Received userID: " + userId);
            if (userId != null) {
                // Fetch registered courses from the database for the current user
                List<Course> registeredCourses = databaseHelper.getRegisteredCourses(userId);

                // Display the registered courses in the TextView
                if (!registeredCourses.isEmpty()) {
                    StringBuilder coursesStringBuilder = new StringBuilder();
                    for (Course course : registeredCourses) {
                        // Append course names to the StringBuilder
                        coursesStringBuilder.append(course.getCourseName()).append("\n\n");
                        // Get the course ID by name from the database
                        long courseId = databaseHelper.getCourseIdByName(course.getCourseName());
                        // Set course ID as tag for the TextView
                        registeredCoursesTextView.setTag(courseId);
                    }
                    // Set the StringBuilder text to the TextView
                    registeredCoursesTextView.setText(coursesStringBuilder.toString());
                } else {
                    registeredCoursesTextView.setText("No registered courses found.");
                }
            }
        }
    }



}
