package com.example.medswap.Fragments;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.example.medswap.R;
import com.example.medswap.REPO.SharedViewModel;

import java.io.File;

public class BlankFragment extends Fragment {
    private SharedViewModel sharedViewModel;
    private CardView goBack, data;
    private ImageView imageView;
    private ProgressBar progressBar;
    public BlankFragment() {
        // Required empty public constructor
    }
    public static BlankFragment newInstance(String param1, String param2) {
        BlankFragment fragment = new BlankFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        goBack = view.findViewById(R.id.goBackBtn);
        imageView = view.findViewById(R.id.imageToShow);
        data = view.findViewById(R.id.data);
        progressBar = view.findViewById(R.id.mma);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
                sharedViewModel.getImageFile().observe(requireActivity(), file -> {
                    // Handle the received image file here
                    if (file != null) {
                        // Do something with the file
                        loadImageIntoImageView(file);
                    }
                });
                imageView.setVisibility(View.VISIBLE);
                data.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        }, 5000); // 3000 milliseconds = 3 seconds

        goBack.setOnClickListener(view1 -> {
            ScanFragment previewFragment = new ScanFragment();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container_home_screen, previewFragment)
                    .addToBackStack(null)
                    .commit();
        });
        return view;
    }

    private void loadImageIntoImageView(File file) {
        // Use your preferred method to load the image into the ImageView
        // For example, you can use a library like Glide or Picasso
        Glide.with(requireContext())
                .load(file)
                .into(imageView);
    }
}