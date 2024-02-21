package com.example.medswap.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.medswap.R;
import java.util.ArrayList;

public class SearchFragment extends Fragment {
    EditText searchEditText;
    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance(String param1, String param2) {
        return new SearchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        searchEditText = view.findViewById(R.id.searchEditText);

        searchEditText.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                // Calculate the drawable's right boundary
                int drawableEndBoundary = v.getRight() - ((EditText) v).getCompoundPaddingEnd();

                // Check if the touch event occurred within the drawable's bounds
                if (event.getRawX() >= drawableEndBoundary) {
                    // Handle the click on the drawable end
                    onDrawableEndClick();
                    return true;
                }
            }
            return false;
        });

        return view;
    }

    private void onDrawableEndClick() {
        // Add your logic for when the drawable end is clicked
        // For example, open a search dialog or perform a search
        searchEditText.setFocusable(false);
        searchEditText.setFocusableInTouchMode(false);
        Toast.makeText(requireContext(), "Search Button Clicked", Toast.LENGTH_SHORT).show();
    }
}
