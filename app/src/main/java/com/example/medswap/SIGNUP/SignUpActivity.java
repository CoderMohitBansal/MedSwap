/**
 * SignUpActivity.java
 * --------------------
 * This class handles the user registration process in the MedSwap application.
 * Users provide their full name, username, email, phone number, and password for registration.
 * The input is validated, and the user is registered using Firebase authentication.
 * Existing email check is performed to avoid duplicate registrations.
 * <p>
 * Author: Mohit Bansal
 * Date: 10-02-2024
 * Version: 1.0.0
 * <p>
 * Package: com.example.medswap.SIGNUP
 * <p>
 * Dependencies:
 * - Firebase authentication
 * - Animatoo library for animations
 * <p>
 * Layout Resources:
 * - R.layout.activity_sign_up: Layout resource for the SignUpActivity
 * <p>
 * Resources:
 * - R.id.fullname_signup: TextInputEditText for user's full name
 * - R.id.username_signup: TextInputEditText for user's username
 * - R.id.email_signup: TextInputEditText for user's email
 * - R.id.phoneNumber_signup: TextInputEditText for user's phone number
 * - R.id.password_signup: TextInputEditText for user's password
 * - R.id.progress_bar_signup: ProgressBar for indicating registration progress
 * - R.id.back_signup: Button to navigate back to the main activity
 * - R.id.go_button_signup: Button to initiate the user registration process
 * <p>
 * Methods:
 * - onCreate(Bundle savedInstanceState): Initializes the activity and sets up event listeners.
 * - initializeViews(): Initializes TextInputEditText and ProgressBar views.
 * - registerUser(): Initiates the user registration process and handles success or failure.
 * - validateInput(String fullname, String username, String email, String phoneNumber, String password):
 *   Validates user input for registration.
 * - navigateToMainActivity(): Navigates back to the main activity.
 * - validateName(String name): Validates the user's full name.
 * - validateUsername(String name): Validates the user's username.
 * - validateEmail(String email): Validates the user's email address and checks for duplicates.
 * - checkEmail(String email): Checks if the email already exists in the database.
 * - checkEmailExists(String email, OnEmailCheckListener listener): Asynchronously checks if the email exists.
 * - validatePhoneNumber(String phoneNumber): Validates the user's phone number.
 * - validatePassword(String password): Validates the user's password.
 * <p>
 * Inner Interfaces:
 * - OnEmailCheckListener: Interface to handle email check callback.
 * <p>
 * Note: This class relies on Firebase for user authentication and Animatoo library for animations.
 */

package com.example.medswap.SIGNUP;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.medswap.HomeActivity;
import com.example.medswap.MainActivity;
import com.example.medswap.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {
    private TextInputEditText fullNameEditText, userNameEditText, emailEditText, phoneNumberEditText, passwordEditText;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initializeViews();

        findViewById(R.id.back_signup).setOnClickListener(view -> navigateToMainActivity());

        findViewById(R.id.go_button_signup).setOnClickListener(view -> registerUser());
    }

    private void initializeViews() {
        fullNameEditText = findViewById(R.id.fullname_signup);
        userNameEditText = findViewById(R.id.username_signup);
        emailEditText = findViewById(R.id.email_signup);
        phoneNumberEditText = findViewById(R.id.phoneNumber_signup);
        passwordEditText = findViewById(R.id.password_signup);
        progressBar = findViewById(R.id.progress_bar_signup);

        int orangeColor = getResources().getColor(R.color.orange);
        progressBar.getIndeterminateDrawable().setColorFilter(orangeColor, PorterDuff.Mode.SRC_IN);
    }

    private void registerUser() {
        progressBar.setVisibility(View.VISIBLE);
        String fullname = Objects.requireNonNull(fullNameEditText.getText()).toString().trim();
        String username = Objects.requireNonNull(userNameEditText.getText()).toString().trim();
        String email = Objects.requireNonNull(emailEditText.getText()).toString().trim();
        String phoneNumber = Objects.requireNonNull(phoneNumberEditText.getText()).toString().trim();
        String password = Objects.requireNonNull(passwordEditText.getText()).toString().trim();

        if (validateInput(fullname, username, email, phoneNumber, password)) {
            FirebaseManager firebaseManager = new FirebaseManager();
            firebaseManager.registerUser(email, password, fullname, username, phoneNumber)
                    .thenAccept(success -> {
                        progressBar.setVisibility(View.GONE);
                        if (success) {
                            startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                            Animatoo.INSTANCE.animateInAndOut(SignUpActivity.this);
                        } else
                            Toast.makeText(SignUpActivity.this, "Problem with credentials\nEmail may already be registered or Password is weak!", Toast.LENGTH_SHORT).show();

                    });
        } else
            progressBar.setVisibility(View.GONE);

    }

    private boolean validateInput(String fullname, String username, String email, String phoneNumber, String password) {
        return validateName(fullname) && validateUsername(username) && validateEmail(email) &&
                validatePhoneNumber(phoneNumber) && validatePassword(password);
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(intent);
        Animatoo.INSTANCE.animateShrink(SignUpActivity.this);
        finish();
    }

    private boolean validateName(String name) {
        if (name.isEmpty()) {
            fullNameEditText.setError("Field cannot be empty");
            return false;
        } else {
            fullNameEditText.setError(null);
            return true;
        }
    }

    private boolean validateUsername(String name) {
        if (name.isEmpty()) {
            userNameEditText.setError("Field cannot be empty");
            return false;
        } else if (name.length() > 15) {
            userNameEditText.setError("Username too long");
            return false;
        } else if (name.contains(" ")) {
            userNameEditText.setError("White spaces are not allowed");
            return false;
        } else {
            userNameEditText.setError(null);
            return true;
        }
    }

    private boolean validateEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (email.isEmpty()) {
            emailEditText.setError("Field cannot be empty");
            return false;
        } else if (!email.matches(emailPattern)) {
            emailEditText.setError("Invalid email address");
            return false;
        } else {
            checkEmail(email);
            return true;
        }
    }

    private void checkEmail(String email) {
        checkEmailExists(email, isEmailExists -> {
            if (isEmailExists) {
                emailEditText.setError("Email is already registered");
            } else {
                emailEditText.setError(null);
            }
        });
    }

    private void checkEmailExists(String email, OnEmailCheckListener listener) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
        mDatabase.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listener.onEmailCheck(snapshot.exists());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "Database error: " + error.getMessage());
                listener.onEmailCheck(false);
            }
        });
    }

    // Interface to handle email check callback
    interface OnEmailCheckListener {
        void onEmailCheck(boolean isEmailExists);
    }

    private boolean validatePhoneNumber(String phoneNumber) {
        if (phoneNumber.isEmpty()) {
            phoneNumberEditText.setError("Field cannot be empty");
            return false;
        } else if (phoneNumber.length() != 10) {
            phoneNumberEditText.setError("Invalid phone number");
            return false;
        } else {
            phoneNumberEditText.setError(null);
            return true;
        }
    }

    private boolean validatePassword(String password) {
        if (password.isEmpty()) {
            passwordEditText.setError("Field cannot be empty");
            return false;
        } else {
            passwordEditText.setError(null);
            return true;
        }
    }
}
