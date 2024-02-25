package com.example.medswap.Fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.medswap.R;
import com.example.medswap.REPO.Combo;
import com.example.medswap.REPO.Medication;
import com.example.medswap.REPO.MedicationAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    EditText searchEditText;
    RecyclerView recyclerView;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        searchEditText = view.findViewById(R.id.searchEditText);
        recyclerView = view.findViewById(R.id.searchBarRecylerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        getData(new FirebaseCallback() {
            @Override
            public void onCallback(List<Medication> data) {
                // Check if the fragment is still attached to its activity
                if (isAdded()) {
                    MedicationAdapter medicationAdapter = new MedicationAdapter(data, requireContext());
                    recyclerView.setAdapter(medicationAdapter);

                    searchEditText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            medicationAdapter.filter(charSequence.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                        }
                    });
                } else {
                    // Fragment is not attached, handle it accordingly (e.g., log or show a message)
                    Log.e("SearchFragment", "Fragment not attached when trying to set up UI.");
                }
            }
        });


        searchEditText.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                int drawableEndBoundary = v.getRight() - ((EditText) v).getCompoundPaddingEnd();
                if (event.getRawX() >= drawableEndBoundary) {
                    onDrawableEndClick();
                    return true;
                }
            }
            return false;
        });

        return view;
    }

    private void onDrawableEndClick() {
        hideKeyboard();
        Toast.makeText(requireContext(), "Search Button Clicked", Toast.LENGTH_SHORT).show();
    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
        }
    }

    private interface FirebaseCallback {
        void onCallback(List<Medication> data);
    }

    private void getData(FirebaseCallback callback) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("medicines");
        List<Medication> data = new ArrayList<>();

        if (isAdded()) {
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Medication medication = snapshot.getValue(Medication.class);

                        if (medication != null) {
                            data.add(medication);
                            Combo combo = medication.combo; // Assuming Medication class has a getter for combo
                            if (combo != null) {
                                Log.d("FirebaseData", "Combo Name: " + combo.name);
                            }
                        }
                    }
                    callback.onCallback(data);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("FirebaseData", "Error fetching data", error.toException());
                }
            });
        }
        else{
            SearchFragment yourFragment = new SearchFragment();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_home_screen, yourFragment)
                    .commit();
        }
    }
}
