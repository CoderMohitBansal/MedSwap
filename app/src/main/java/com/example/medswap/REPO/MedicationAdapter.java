package com.example.medswap.REPO;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medswap.REPO.Combo;
import com.example.medswap.REPO.Medication;
import com.example.medswap.R;

import java.util.ArrayList;
import java.util.List;

public class MedicationAdapter extends RecyclerView.Adapter<MedicationAdapter.MedicationViewHolder> {

    private List<Medication> originalList;
    private List<Medication> filteredList;
    private Context context;
    public MedicationAdapter(List<Medication> medicationList, Context context) {
        this.originalList = medicationList;
        this.filteredList = new ArrayList<>(medicationList);
        this.context = context;
    }

    @NonNull
    @Override
    public MedicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_medicine_row, parent, false);
        return new MedicationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicationViewHolder holder, int position) {
        Medication medication = filteredList.get(position);

        holder.medicineName.setText(medication.name);
        holder.medicineConcentration.setText(medication.conc);
        holder.medicineDescription.setText(medication.description);
        holder.medicinePrice.setText(String.valueOf(medication.price));
        holder.comboName.setText(medication.description_hindi);
        if (medication.combo != null) {
            Combo combo = medication.combo;
        }
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public void filter(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(originalList);
        } else {
            query = query.toLowerCase();
            for (Medication medication : originalList) {
                if (medication.name.toLowerCase().contains(query)) {
                    filteredList.add(medication);
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class MedicationViewHolder extends RecyclerView.ViewHolder {
        TextView medicineName, medicineConcentration, medicineDescription, medicinePrice;
        TextView comboName;
        CardView comboLayout;

        public MedicationViewHolder(@NonNull View itemView) {
            super(itemView);

            medicineName = itemView.findViewById(R.id.medicine_name);
            medicineConcentration = itemView.findViewById(R.id.medicine_concentration);
            medicineDescription = itemView.findViewById(R.id.medicine_description);
            medicinePrice = itemView.findViewById(R.id.medicine_price);

            comboName = itemView.findViewById(R.id.combo_name);
        }
    }
}
