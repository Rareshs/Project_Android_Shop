package com.example.project_app;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load HomeFragment only if it's not already added
        if (savedInstanceState == null) {
            loadHomeFragment();
        }
    }

    // Method to load the HomeFragment
    private void loadHomeFragment() {
        // Find existing HomeFragment instance if it exists
        Fragment homeFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        // Add HomeFragment if it doesn't exist
        if (homeFragment == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
            Log.d("MainActivity", "HomeFragment added successfully.");
        } else {
            Log.d("MainActivity", "HomeFragment already exists.");
        }
    }
}
