package com.example.medswap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.medswap.Fragments.HomeFragment;
import com.example.medswap.Fragments.ScanFragment;
import com.example.medswap.Fragments.SearchFragment;
import com.example.medswap.Fragments.UserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_home_screen, new HomeFragment()).commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int itemId = item.getItemId();
            if (itemId == R.id.action_home) {
                selectedFragment = new HomeFragment();
            } else if (itemId == R.id.action_search) {
                selectedFragment = new SearchFragment();
            } else if (itemId == R.id.action_scan) {
                selectedFragment = new ScanFragment();
            } else if (itemId == R.id.action_user) {
                selectedFragment = new UserFragment();
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_home_screen, selectedFragment).commit();
            return true;
        });

    }
}