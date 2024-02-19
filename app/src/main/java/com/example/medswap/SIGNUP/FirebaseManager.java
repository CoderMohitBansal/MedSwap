/**
 * FirebaseManager.java
 * ---------------------
 * This class manages Firebase authentication and database operations for user registration in the MedSwap application.
 * It includes methods for registering users, updating user profiles, and saving user data to the Firebase Realtime Database.
 * <p>
 * Author: [Author Name]
 * Date: [Date]
 * Version: [Version Number]
 * <p>
 * Package: com.example.medswap.SIGNUP
 * <p>
 * Dependencies:
 * - Firebase Authentication
 * - Firebase Realtime Database
 * <p>
 * Methods:
 * - FirebaseManager(): Constructor initializing FirebaseAuth and DatabaseReference.
 * - registerUser(String email, String password, String fullName, String username, String phoneNumber):
 *   Registers a new user with the provided credentials and saves user data to the Firebase Realtime Database.
 * - saveUserToDatabase(String userId, String fullName, String username, String email, String phoneNumber, CompletableFuture<Boolean> result):
 *   Saves user data to the Firebase Realtime Database.
 * <p>
 * Usage:
 * 1. Create an instance of FirebaseManager to manage authentication and database operations.
 * 2. Call registerUser() with user credentials to initiate the user registration process.
 * 3. User profile is updated using Firebase authentication, and user data is saved to the Realtime Database.
 * 4. CompletableFuture<Boolean> is returned to indicate registration success or failure.
 * <p>
 * Example:
 * FirebaseManager firebaseManager = new FirebaseManager();
 * CompletableFuture<Boolean> registrationResult = firebaseManager.registerUser(email, password, fullName, username, phoneNumber);
 * <p>
 * Note: This class is designed for use in the user registration process and relies on Firebase services.
 */
package com.example.medswap.SIGNUP;

import androidx.annotation.NonNull;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class FirebaseManager {
    private final FirebaseAuth mAuth;

    /**
     * Constructor initializing FirebaseAuth and DatabaseReference.
     */
    public FirebaseManager() {
        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * Registers a new user with the provided credentials and saves user data to the Firebase Realtime Database.
     *
     * @param email       User's email address for authentication
     * @param password    User's password for authentication
     * @param fullName    User's full name
     * @param username    User's chosen username
     * @param phoneNumber User's phone number
     * @return CompletableFuture<Boolean> indicating registration success or failure
     */
    public CompletableFuture<Boolean> registerUser(String email, String password, String fullName, String username, String phoneNumber) {
        CompletableFuture<Boolean> result = new CompletableFuture<>();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(fullName)
                                    .build();
                            user.updateProfile(profileUpdates);
                            saveUserToDatabase(user.getUid(), fullName, username, email, phoneNumber, result);
                        }
                    } else {
                        result.complete(false);
                    }
                });
        return result;
    }

    /**
     * Saves user data to the Firebase Realtime Database.
     *
     * @param userId       User's unique identifier
     * @param fullName     User's full name
     * @param username     User's chosen username
     * @param email        User's email address
     * @param phoneNumber  User's phone number
     * @param result       CompletableFuture<Boolean> indicating save operation success or failure
     */
    private void saveUserToDatabase(String userId, String fullName, String username, String email, String phoneNumber, CompletableFuture<Boolean> result) {
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("users");

        userReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Map<String, Object> userData = new HashMap<>();
                    userData.put("UserId", userId);
                    userData.put("fullname", fullName);
                    userData.put("username", username);
                    userData.put("email", email);
                    userData.put("phoneNumber", phoneNumber);

                    userReference.child(userId).setValue(userData).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            result.complete(true);
                        } else {
                            result.complete(false);
                        }
                    });
                } else {
                    result.complete(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                result.complete(false);
            }
        });
    }
}