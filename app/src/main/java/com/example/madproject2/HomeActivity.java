package com.example.madproject2;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.madproject2.Fragments.CoursesFragment;
import com.example.madproject2.Fragments.HomeFragment;
import com.example.madproject2.Fragments.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Get the user ID from intent
        String userId = getIntent().getStringExtra("userId");

        // Set listener for navigation item selection
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigation_home) {
                    // Pass user ID to HomeFragment
                    Bundle bundle = new Bundle();
                    bundle.putString("userId", userId);
                    HomeFragment homeFragment = new HomeFragment();
                    homeFragment.setArguments(bundle);
                    replaceFragment(homeFragment);
                    return true;
                } else if (item.getItemId() == R.id.navigation_courses) {
                    replaceFragment(new CoursesFragment());
                    return true;
                } else if (item.getItemId() == R.id.navigation_settings) {
                    // Pass user ID to SettingsFragment
                    Bundle bundle = new Bundle();
                    bundle.putString("userId", userId);
                    SettingsFragment settingsFragment = new SettingsFragment();
                    settingsFragment.setArguments(bundle);
                    replaceFragment(settingsFragment);
                    return true;
                }
                return false;
            }
        });
    }

    // Function to replace fragment based on navigation selection (optional, adjust as needed)
    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
