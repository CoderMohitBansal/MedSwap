package com.example.medswap.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.medswap.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeFragment extends Fragment {
    LinearLayout scanNowBtn, searchBtn, manageProfile, aboutUs;
    public HomeFragment() {
        // Required empty public constructor
    }
    public static HomeFragment newInstance(String param1, String param2) {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        scanNowBtn = view.findViewById(R.id.scanNow);
        searchBtn = view.findViewById(R.id.searchBtn_home_fragment);
        manageProfile = view.findViewById(R.id.manageProfile);
        aboutUs = view.findViewById(R.id.aboutUs);

        aboutUs.setOnClickListener(view1 -> {
            AboutFragment anotherFragment = new AboutFragment();
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_home_screen, anotherFragment).commit();
        });

        scanNowBtn.setOnClickListener(view1 -> {
            ScanFragment anotherFragment = new ScanFragment();
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_home_screen, anotherFragment).commit();
            if (getActivity() != null) {
                BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
                bottomNavigationView.setSelectedItemId(R.id.action_scan);
            }
        });

        searchBtn.setOnClickListener(view12 -> {
            SearchFragment anotherFragment = new SearchFragment();
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_home_screen, anotherFragment).commit();
            if (getActivity() != null) {
                BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
                bottomNavigationView.setSelectedItemId(R.id.action_search);
            }
        });

        manageProfile.setOnClickListener(view13 -> {
            UserFragment anotherFragment = new UserFragment();
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_home_screen, anotherFragment).commit();
            if (getActivity() != null) {
                BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
                bottomNavigationView.setSelectedItemId(R.id.action_user);
            }
        });
        return view;
    }
}