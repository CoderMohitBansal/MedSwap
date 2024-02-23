package com.example.medswap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.medswap.REPO.UserDataSingleton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class SplashScreen extends AppCompatActivity {
    private static final int SPLASH_DURATION = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        loadUserData();
        final View imageView = findViewById(R.id.app_logo);
        final View textView = findViewById(R.id.app_name);

        Animation slideDownAnimation = AnimationUtils.loadAnimation(this, R.anim.up_to_down);
        Animation slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.down_to_up);

        imageView.startAnimation(slideDownAnimation);
        textView.startAnimation(slideUpAnimation);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(intent);
            Animatoo.INSTANCE.animateFade(this);
            finish();
        }, SPLASH_DURATION);
    }

    private void loadUserData() {
        UserDataSingleton.getInstance().loadUserData();
    }
}