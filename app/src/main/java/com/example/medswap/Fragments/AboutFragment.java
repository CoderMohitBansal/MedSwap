package com.example.medswap.Fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.medswap.R;
import com.ramotion.paperonboarding.PaperOnboardingFragment;
import com.ramotion.paperonboarding.PaperOnboardingPage;

import java.util.ArrayList;

public class AboutFragment extends Fragment {
    public AboutFragment() {
        // Required empty public constructor
    }
    public static AboutFragment newInstance(String param1, String param2) {
        AboutFragment fragment = new AboutFragment();
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
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        final PaperOnboardingFragment paperOnboardingFragment = PaperOnboardingFragment.newInstance(getDataForOnBoarding());
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container_about_us, paperOnboardingFragment);
        fragmentTransaction.commit();

        return view;
    }

    private ArrayList<PaperOnboardingPage> getDataForOnBoarding() {
        PaperOnboardingPage src1 = new PaperOnboardingPage(
                "We navigate the maze of medication costs",
                "MedSwap guides users in navigating complex medication prices, providing transparent information for confident healthcare spending decisions and optimal choices.",
                R.color.white,
                R.drawable.doctor,
                R.drawable.doctor
        );
        PaperOnboardingPage src2 = new PaperOnboardingPage(
                "Say goodbye to High Prices, Hello to Health",
                "Making healthcare affordable, accessible, and convenient – MedSwap does it all.",
                R.color.white,
                R.drawable.rocket_image,
                R.drawable.rocket_image
        );
        PaperOnboardingPage src3 = new PaperOnboardingPage(
                "We simplify medication management,leading with expert insights",
                "MedSwap optimizes scans for health and budget, identifying cost-saving opportunities without compromising quality for improved well-being.",
                R.color.white,
                R.drawable.doctor_see_patient,
                R.drawable.doctor_see_patient
        );

        ArrayList<PaperOnboardingPage> elements = new ArrayList<>();
        elements.add(src1);
        elements.add(src2);
        elements.add(src3);

        return elements;
    }
}