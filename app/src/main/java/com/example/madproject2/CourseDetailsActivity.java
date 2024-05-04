package com.example.madproject2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.madproject2.Admin.DatabaseHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class CourseDetailsActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_LOCATION = 1001;
    private DatabaseHelper databaseHelper;
    private FirebaseAuth mAuth;
    private Course course;

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
        setContentView(R.layout.activity_course_details);

        setTitle("Course Details");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        databaseHelper = new DatabaseHelper(this);
        mAuth = FirebaseAuth.getInstance();

        // Get the course details from the intent
        course = getIntent().getParcelableExtra("course");

        // Set the course details to the corresponding TextViews
        TextView courseNameTextView = findViewById(R.id.course_name);
        TextView courseFeeTextView = findViewById(R.id.course_fee);
        TextView durationTextView = findViewById(R.id.duration);
        TextView startingDateTextView = findViewById(R.id.starting_date);
        TextView publishedDateTextView = findViewById(R.id.published_date);
        TextView closingDateTextView = findViewById(R.id.closing_date);

        if (course != null) {
            courseNameTextView.setText(course.getCourseName());
            courseFeeTextView.setText(course.getCourseFee());
            durationTextView.setText(course.getDuration());
            startingDateTextView.setText(course.getStartingDate());
            publishedDateTextView.setText(course.getPublishedDate());
            closingDateTextView.setText(course.getClosingDate());
        }

        // Fetch the course ID from the database based on the course name
        long courseId = databaseHelper.getCourseIdByName(course.getCourseName());

        // Initialize and populate the spinner with the fetched course ID
        initializeSpinner(courseId);
    }

    // Method to initialize and populate the spinner
    private void initializeSpinner(long courseId) {
        // Get a reference to the Spinner
        Spinner branchesSpinner = findViewById(R.id.branches_spinner);

        // Get branches related to the selected course from the database
        List<String> branchNames = databaseHelper.getBranchesForCourse(courseId);

        // Create an ArrayAdapter using the branch names list and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, branchNames);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        branchesSpinner.setAdapter(adapter);
    }

    // Method to handle discount code entry
    public void applyDiscount(View view) {
        EditText discountCodeEditText = findViewById(R.id.discount_code);
        String discountCode = discountCodeEditText.getText().toString();

        // Calculate and apply the discount
        calculateDiscount(discountCode);
    }

    // Method to calculate and display the discount value
    private void calculateDiscount(String code) {
        TextView courseFeeTextView = findViewById(R.id.course_fee);
        String courseFeeStr = courseFeeTextView.getText().toString();
        double courseFee = Double.parseDouble(courseFeeStr);

        double discount = 0;
        switch (code) {
            case "M563432":
                discount = 0.25; // 25% discount
                break;
            case "S663435":
                discount = 0.40; // 40% discount
                break;
            case "L763434":
                discount = 0.60; // 60% discount
                break;
            default:
                // No discount for invalid discount code
                break;
        }

        // Calculate the discounted fee
        double discountedFee = courseFee - (courseFee * discount);

        // Update the displayed fee
        TextView totalFeeTextView = findViewById(R.id.total_fee);
        totalFeeTextView.setText("Total Fee: " + discountedFee);
    }


    // Method to handle registration button click
    // Method to handle registration button click
    public void regBtn(View view) {
        // Get the currently logged-in user ID
        FirebaseUser user = mAuth.getCurrentUser();
        String userId = ""; // Default value if user is not logged in

        if (user != null) {
            userId = user.getUid();
        } else {
            // Handle the case where the user is not logged in
            Toast.makeText(this, "User is not logged in", Toast.LENGTH_SHORT).show();

            // Redirect the user to the registration activity
            startActivity(new Intent(this, RegisterActivity.class));

            return; // Exit the method early
        }

        // Get the selected branch ID and course ID
        Spinner branchesSpinner = findViewById(R.id.branches_spinner);
        long branchId = branchesSpinner.getSelectedItemId();
        long courseId = databaseHelper.getCourseIdByName(course.getCourseName());

        // Get the entered promotion code
        EditText promotionCodeEditText = findViewById(R.id.discount_code);
        String promotionCode = promotionCodeEditText.getText().toString();

        // Insert the registration details into the database
        long registeredCourseId = databaseHelper.insertRegisteredCourse(courseId, branchId, userId, promotionCode);
        if (registeredCourseId != -1) {
            Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Registration failed.", Toast.LENGTH_SHORT).show();
        }
    }



    // Method to handle view map button click
    public void viewMap(View view) {
        // Check if the app has location permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request location permissions if not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);
            return;
        }

        // Get the user's current location
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            // Use the user's current location to display on the map
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();

                            // Now you have latitude and longitude, you can pass this data to your map activity
                            Intent intent = new Intent(CourseDetailsActivity.this, MapActivity.class);
                            intent.putExtra("latitude", latitude);
                            intent.putExtra("longitude", longitude);
                            startActivity(intent);
                        } else {
                            // Handle the case where location is null
                            Toast.makeText(CourseDetailsActivity.this, "Unable to retrieve current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Handle the result of the location permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, now reload the map
                viewMap(null);
            } else {
                // Permission denied, inform the user
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
