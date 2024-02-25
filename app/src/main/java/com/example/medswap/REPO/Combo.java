package com.example.medswap.REPO;

public class Combo {
    public String name;
    public String dosage;

    public Combo() {
        // Default constructor required for calls to DataSnapshot.getValue(Combo.class)
    }

    public Combo(String name, String dosage) {
        this.name = name;
        this.dosage = dosage;
    }
}