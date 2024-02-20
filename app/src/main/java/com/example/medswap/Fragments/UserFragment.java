package com.example.medswap.Fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.medswap.MainActivity;
import com.example.medswap.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class UserFragment extends Fragment {
    NumberPicker height;
    NumberPicker weight;
    TextView answerTextview;
    ProgressBar pg;

    public UserFragment() {
        // Required empty public constructor
    }

    public static UserFragment newInstance(String param1, String param2) {
        return new UserFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        height = view.findViewById(R.id.heightPicker);
        weight = view.findViewById(R.id.weightPicker);
        answerTextview = view.findViewById(R.id.answerBMI);
        pg = view.findViewById(R.id.progress_bar_logout);

        view.findViewById(R.id.logout_btn).setOnClickListener(view1 -> {
            pg.setVisibility(View.VISIBLE);
            logout();
        });

        // Initialize NumberPickers
        height.setMinValue(100);
        height.setMaxValue(250);
        weight.setMinValue(30);
        weight.setMaxValue(200);

        height.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                calculateBMI();
            }
        });

        weight.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                calculateBMI();
            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void calculateBMI() {
        double h = height.getValue() / 100.0;
        double w = weight.getValue();

        if (h == 0) {
            return;
        }

        double bmiValue = w / (h * h);

        String msgToDisplay = String.format("%.2f\nConsidered: %s", bmiValue, getHealthMessage(bmiValue));
        answerTextview.setText(msgToDisplay);
    }

    private String getHealthMessage(double bmi) {
        if (bmi < 18.5)
            return "Underweight";
        else if (bmi < 25.0)
            return "Healthy";
        else if (bmi < 30.0)
            return "Overweight";
        else
            return "Obese";
    }

    private void logout() {
        try{
            FirebaseAuth.getInstance().signOut();
        }catch (Exception ignored){}
        pg.setVisibility(View.GONE);
        Intent intent = new Intent(requireContext(), MainActivity.class);
        startActivity(intent);
        Animatoo.INSTANCE.animateShrink(requireContext());
    }
}
