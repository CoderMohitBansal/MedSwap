package com.example.medswap.REPO;

public class Medication {
    public String name;
    public String description;
    public String description_hindi;
    public int price;
    public String conc;
    public String Buy;
    public Combo combo;

    public Medication() {
        // Default constructor required for calls to DataSnapshot.getValue(Medication.class)
    }

    public Medication(String name, String description_hindi, String description, int price, String conc, String Buy, Combo combo) {
        this.name = name;
        this.description = description;
        this.description_hindi = description_hindi;
        this.price = price;
        this.conc = conc;
        this.Buy = Buy;
        this.combo = combo;
    }
}


