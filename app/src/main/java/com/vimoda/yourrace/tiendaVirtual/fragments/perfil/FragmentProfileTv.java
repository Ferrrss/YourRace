package com.vimoda.yourrace.tiendaVirtual.fragments.perfil;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.vimoda.yourrace.R;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FragmentProfileTv extends Fragment {
    private EditText usernameEditText, passwordEditText, nombreEditText, apellidoEditText, dniEditText,
            telefonoEditText;
    private Button updateButton;
    private FirebaseFirestore db;
    private String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_tv, container, false);

        usernameEditText = view.findViewById(R.id.edtUser3);
        passwordEditText = view.findViewById(R.id.edtContra1);
        nombreEditText = view.findViewById(R.id.edtNomb);
        apellidoEditText = view.findViewById(R.id.edtApelli);
        dniEditText = view.findViewById(R.id.editDni2);
        telefonoEditText = view.findViewById(R.id.edtTelef1);
        updateButton = view.findViewById(R.id.btnActualizar3);

        db = FirebaseFirestore.getInstance();

        // Obtener el ID del usuario guardado en SharedPreferences
        SharedPreferences prefs = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        userId = prefs.getString("userId", "");

        if (!userId.isEmpty()) {
            loadUserData();
        } else {
            Toast.makeText(getContext(), "No hay usuario logueado", Toast.LENGTH_SHORT).show();
        }

        setupUpdateButton();

        return view;
    }

    private void loadUserData() {
        db.collection("usuarios").document(userId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                usernameEditText.setText(userId);
                                passwordEditText.setText(document.getString("contrasenia"));

                                Map<String, Object> perfil = (Map<String, Object>) document.get("perfil");
                                if (perfil != null) {
                                    nombreEditText.setText((String) perfil.get("nombre"));
                                    apellidoEditText.setText((String) perfil.get("apellido"));
                                    dniEditText.setText((String) perfil.get("dni"));
                                    telefonoEditText.setText((String) perfil.get("telefono"));
                                }
                            }
                        } else {
                            Toast.makeText(getContext(), "Error al cargar datos: " +
                                    task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void setupUpdateButton() {
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = nombreEditText.getText().toString();
                String apellido = apellidoEditText.getText().toString();
                String dni = dniEditText.getText().toString();
                String telefono = telefonoEditText.getText().toString();
                String contrasenia = passwordEditText.getText().toString();

                // Validar longitud de DNI y teléfono
                if (dni.length() != 8) {
                    dniEditText.setError("El DNI debe tener 8 caracteres");
                    return;
                }

                if (telefono.length() != 9) {
                    telefonoEditText.setError("El teléfono debe tener 9 caracteres");
                    return;
                }

                // Si pasa las validaciones, proceder con la actualización
                Map<String, Object> perfilUpdates = new HashMap<>();
                perfilUpdates.put("nombre", nombre);
                perfilUpdates.put("apellido", apellido);
                perfilUpdates.put("dni", dni);
                perfilUpdates.put("telefono", telefono);

                Map<String, Object> updates = new HashMap<>();
                updates.put("contrasenia", contrasenia);
                updates.put("perfil", perfilUpdates);

                db.collection("usuarios").document(userId)
                        .update(updates)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(), "Perfil actualizado con éxito",
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Error al actualizar: " + e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

}