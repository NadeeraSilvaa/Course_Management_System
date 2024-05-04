package com.example.madproject2.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.madproject2.Course;
import com.example.madproject2.Admin.DatabaseHelper;
import com.example.madproject2.CourseDetailsActivity;
import com.example.madproject2.R;

import java.util.List;

public class CoursesFragment extends Fragment {

    private ListView courseListView;
    private DatabaseHelper databaseHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(requireContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_courses, container, false);

        courseListView = rootView.findViewById(R.id.course_list_view);

        // Fetch courses from the database
        final List<Course> courses = databaseHelper.getAllCourses();

        // Check if there are courses in the database
        if (courses.isEmpty()) {
            // Handle no courses scenario
            Toast.makeText(requireContext(), "No courses found", Toast.LENGTH_SHORT).show();
        } else {
            // Populate the ListView with courses
            ArrayAdapter<Course> adapter = new ArrayAdapter<>(requireContext(),
                    android.R.layout.simple_list_item_1, courses);
            courseListView.setAdapter(adapter);

            // Set click listener for list items to navigate to CourseDetailsActivity
            courseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // Handle list item click by starting CourseDetailsActivity with selected course details
                    Course selectedCourse = courses.get(position);
                    Intent intent = new Intent(requireContext(), CourseDetailsActivity.class);
                    intent.putExtra("course", selectedCourse);
                    startActivity(intent);
                }
            });
        }

        return rootView;
    }
}
