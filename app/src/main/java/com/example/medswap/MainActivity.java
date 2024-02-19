package com.example.medswap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.medswap.SIGNUP.SignUpActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    TextInputEditText emailEditText;
    TextInputEditText passwordEditText;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button signupBtn = findViewById(R.id.button_signup);
        Button loginBtn = findViewById(R.id.login_button);
        Button guestBtn = findViewById(R.id.guest_button);
        emailEditText = findViewById(R.id.email_login);
        passwordEditText = findViewById(R.id.password_login);
        progressBar = findViewById(R.id.progress_bar_login);

        int orangeColor = getResources().getColor(R.color.orange);
        progressBar.getIndeterminateDrawable().setColorFilter(orangeColor, PorterDuff.Mode.SRC_IN);

        signupBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(intent);
            Animatoo.INSTANCE.animateShrink(MainActivity.this);
            finish();
        });

        loginBtn.setOnClickListener(view -> {
            String email = Objects.requireNonNull(emailEditText.getText()).toString().trim();
            String password = Objects.requireNonNull(passwordEditText.getText()).toString().trim();

            if (validateEmail(email) && validatePassword(password)){
                login(email, password);
            }
        });

        guestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startHomeScreen();
            }
        });

    }

    private void login(String email, String password) {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    progressBar.setVisibility(View.GONE);
                    startHomeScreen();
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    handleLoginFailure(Objects.requireNonNull(e.getMessage()));
                });
    }


    private void startHomeScreen() {
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            Animatoo.INSTANCE.animateShrink(this);
            finish();
        }, 0);
    }

    private void handleLoginFailure(String errorMessage) {
        progressBar.setVisibility(View.GONE);
        if (errorMessage.contains("no user record")) {
            Toast.makeText(this, "Email does not exist", Toast.LENGTH_SHORT).show();
        } else if (errorMessage.contains("password is invalid")) {
            Toast.makeText(this, "Invalid Password", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Invalid Email/Password", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (email.isEmpty()) {
            emailEditText.setError("Field cannot be empty");
            progressBar.setVisibility(View.GONE);
            return false;
        } else if (!email.matches(emailPattern)) {
            emailEditText.setError("Invalid email address");
            progressBar.setVisibility(View.GONE);
            return false;
        } else {
            emailEditText.setError(null);
            return true;
        }
    }

    private boolean validatePassword(String password) {
        if (password.isEmpty()) {
            passwordEditText.setError("Field cannot be empty");
            progressBar.setVisibility(View.GONE);
            return false;
        } else {
            passwordEditText.setError(null);
            return true;
        }
    }
}