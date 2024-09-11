package com.vimoda.yourrace.tiendaVirtual.fragments.pedidos_compras;

import android.os.Bundle;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.vimoda.yourrace.R;
import com.vimoda.yourrace.UserPreferences;
import com.vimoda.yourrace.adapter.PedidosAdapter;
import com.vimoda.yourrace.modelos.Pedido;

import java.util.ArrayList;
import java.util.List;

public class FragmentOrdersTv extends Fragment {

    private RecyclerView rcvPedidos;
    private ImageView imageView;
    private TextView textView;
    private FirebaseFirestore db;
    private String userId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        userId = UserPreferences.getUserId(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders_tv, container, false);

        rcvPedidos = view.findViewById(R.id.rcvPedidos);
        imageView = view.findViewById(R.id.imageView4);
        textView = view.findViewById(R.id.textView21);

        // Configurar el RecyclerView y su adaptador
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rcvPedidos.setLayoutManager(layoutManager);

        // Obtener los pedidos desde Firestore
        obtenerPedidosDesdeFirestore();

        return view;
    }

    private void obtenerPedidosDesdeFirestore() {
        if (userId != null && !userId.isEmpty()) {
            // Obtener referencia a la colección "compras" del usuario
            CollectionReference comprasRef = db.collection("usuarios").document(userId
            ).collection("compras");

            comprasRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    List<Pedido> listaPedidos = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Pedido pedido = documentSnapshot.toObject(Pedido.class);
                        listaPedidos.add(pedido);
                    }

                    // Mostrar u ocultar elementos según la existencia de pedidos
                    if (listaPedidos.isEmpty()) {
                        rcvPedidos.setVisibility(View.INVISIBLE);
                        imageView.setVisibility(View.VISIBLE);
                        textView.setVisibility(View.VISIBLE);
                    } else {
                        rcvPedidos.setVisibility(View.VISIBLE);
                        imageView.setVisibility(View.INVISIBLE);
                        textView.setVisibility(View.INVISIBLE);

                        // Crear y configurar el adaptador
                        PedidosAdapter adapter = new PedidosAdapter(listaPedidos);
                        rcvPedidos.setAdapter(adapter);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("Firestore", "Error al obtener pedidos: " + e.getMessage());
                    // Manejar el error según sea necesario
                }
            });
        } else {
            Log.e("Firestore", "Error: userId está vacío");
        }
    }
}