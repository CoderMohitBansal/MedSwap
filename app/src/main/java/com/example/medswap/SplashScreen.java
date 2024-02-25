package com.example.medswap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.medswap.REPO.Combo;
import com.example.medswap.REPO.Medication;
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

        final View imageView = findViewById(R.id.app_logo);
        final View textView = findViewById(R.id.app_name);

//        uploadDataToFirebase();

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

    private void uploadDataToFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("medicines");

        // Existing data
        /*
        Medication aceclofenac = new Medication("aceclofenac","Reduce pain and swelling,", 20, "100mg", "https://pharmeasy.in/online-medicine-order/afenak-100mg-tablet-111699", new Combo("paracetamol", "325mg"));
        Medication paracetamol = new Medication("paracetamol","Reduce pain and help in fever", 7, "500mg", "https://pharmeasy.in/online-medicine-order/paracetamol-500mg-tab-15-s-torque--165262", null);
        Medication cetirizine = new Medication("cetirizine","skin rashes, Runny nose", 24, "5mg", "https://pharmeasy.in/online-medicine-order/levocetirizine-5mg-tablet-111694", new Combo("Dihydrochloride", "5mg"));

        // New data
        Medication folicAcid = new Medication("folicAcid","Anemia, Brain development", 25, "5mg", "https://pharmeasy.in/online-medicine-order/u-fit-tab-137014", null);
        Medication aspirin = new Medication("aspirin","Pain relief, Anticoagulation", 4, "150mg", "https://pharmeasy.in/online-medicine-order/44952", null);
        Medication pantoprazole = new Medication("pantoprazole","Gastritis, Acid reflux", 45, "40mg", "https://pharmeasy.in/online-medicine-order/pentopan-dsr-strip-of-10-capsules-3066624", null);

        // Push data to Firebase
        myRef.child("aceclofenac").setValue(aceclofenac);
        myRef.child("paracetamol").setValue(paracetamol);
        myRef.child("cetirizine").setValue(cetirizine);
        myRef.child("folicAcid").setValue(folicAcid);
        myRef.child("aspirin").setValue(aspirin);
        myRef.child("pantoprazole").setValue(pantoprazole);*/
    }

}