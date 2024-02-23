package com.example.medswap.REPO;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Objects;

public class UserDataSingleton {

    private static final UserDataSingleton instance = new UserDataSingleton();

    private String username;
    private String emailAddress;

    private UserDataSingleton() {
        // private constructor to prevent instantiation
    }

    public static UserDataSingleton getInstance() {
        return instance;
    }

    public void loadUserData() {
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    username = dataSnapshot.child("fullname").getValue(String.class);
                    emailAddress = dataSnapshot.child("email").getValue(String.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle onCancelled
            }
        });
    }

    public String getUsername() {
        return username;
    }

    public String getEmailAddress() {
        return emailAddress;
    }
}
