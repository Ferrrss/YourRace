package com.vimoda.yourrace.Fragments.Pedidos;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.vimoda.yourrace.R;
import com.vimoda.yourrace.adapter.PedidosAdapter;
import com.vimoda.yourrace.modelos.Pedido;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/*
public class FragmentOrders extends Fragment {
    private FirebaseFirestore db;
    private List<PedidosP> listaPedidos;
    private PedidosAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        db = FirebaseFirestore.getInstance();
        listaPedidos = new ArrayList<>();
        adapter = new OrdersAdapter(listaPedidos);

        RecyclerView rvPedidos = view.findViewById(R.id.rcvLista);
        rvPedidos.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPedidos.setAdapter(adapter);

        obtenerPedidos();

        return view;
    }

    private void obtenerPedidos() {
        db.collection("usuarios")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String userId = document.getId();
                            obtenerPerfilYCompras(userId);
                        }
                    } else {
                        Log.e("FragmentOrders", "Error obteniendo usuarios", task.getException());
                    }
                });
    }

    private void obtenerPerfilYCompras(String userId) {
        DocumentReference perfilRef = db.collection("usuarios").document(userId);
        perfilRef.get().addOnSuccessListener(perfilSnapshot -> {
            if (perfilSnapshot.exists()) {
                Map<String, Object> perfil = perfilSnapshot.getData();
                String nombre = (String) perfil.get("nombre");
                String apellido = (String) perfil.get("apellido");
                String telefono = (String) perfil.get("telefono");
                String dni = (String) perfil.get("dni");

                obtenerCompras(userId, nombre, apellido, telefono, dni);
            }
        }).addOnFailureListener(e -> Log.e("FragmentOrders", "Error obteniendo perfil", e));
    }

    private void obtenerCompras(String userId, String nombre, String apellido, String telefono, String dni) {
        db.collection("usuarios").document(userId).collection("compras")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> compra = document.getData();
                            double precioTotal = (double) compra.get("precioTotal");
                            String estado = (String) compra.get("estado");
                            // Aquí puedes construir la lista de productos si es necesario

                            PedidosP pedido = new PedidosP(nombre, apellido, telefono, dni, precioTotal, estado); // Ajusta aquí para incluir productos
                            listaPedidos.add(pedido);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e("FragmentOrders", "Error obteniendo compras", task.getException());
                    }
                });
    }
}*/
