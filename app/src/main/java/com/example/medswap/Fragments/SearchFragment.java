package com.example.medswap.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import com.example.medswap.R;
import java.util.ArrayList;

public class SearchFragment extends Fragment {
    ArrayList<String> user = new ArrayList<>();
    SearchView searchView;
    ListView listView;

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
        ini();
        searchView = view.findViewById(R.id.searchView);
        listView = view.findViewById(R.id.listView); // Add this line to initialize listView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, user);
        listView.setAdapter(adapter); // Set the adapter to the listView

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (user.contains(s)) {
                    // Handle item found
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s); // Update the adapter with the filtered list
                return false;
            }
        });

        return view;
    }

    private void ini() {
        user.add("Mohit");
        user.add("Rai");
        // Add more items as needed
    }
}
