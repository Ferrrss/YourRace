package com.vimoda.yourrace.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vimoda.yourrace.R;

import java.util.ArrayList;
import java.util.Map;

public class MainFragment extends Fragment {

    private TextView txNom;
    private BarChart barChart;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txNom = view.findViewById(R.id.txNom);
        barChart = view.findViewById(R.id.barChart);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Obtener el nombre del usuario desde Firebase
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            db.collection("usuarios").document(userId).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot != null && documentSnapshot.exists()) {
                                Map<String, Object> perfil = documentSnapshot.get("perfil", Map.class);
                                if (perfil != null && perfil.containsKey("nombre")) {
                                    String nombre = (String) perfil.get("nombre");
                                    txNom.setText(nombre != null ? nombre : "Usuario");
                                } else {
                                    txNom.setText("Usuario");
                                    Log.d("MainFragment", "No se encontró el campo Nombre en Perfil");
                                }
                            } else {
                                txNom.setText("Usuario");
                                Log.d("MainFragment", "No se encontró el documento del usuario");
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("MainFragment", "Error getting documents: ", e);
                            txNom.setText("Usuario");
                        }
                    });
        } else {
            txNom.setText("Usuario"+"!");
        }

        // Crear un gráfico de barras ficticio
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1f, 4f));
        entries.add(new BarEntry(2f, 6f));
        entries.add(new BarEntry(3f, 8f));
        entries.add(new BarEntry(4f, 2f));
        entries.add(new BarEntry(5f, 5f));

        BarDataSet dataSet = new BarDataSet(entries, "Datos de ejemplo");
        BarData data = new BarData(dataSet);

        barChart.setData(data);
        barChart.getDescription().setEnabled(false);
        barChart.animateY(1000);
        barChart.invalidate();
    }
}