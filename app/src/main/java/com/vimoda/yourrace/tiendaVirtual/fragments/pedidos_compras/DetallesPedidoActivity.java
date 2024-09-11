package com.vimoda.yourrace.tiendaVirtual.fragments.pedidos_compras;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.vimoda.yourrace.R;
import com.vimoda.yourrace.UserPreferences;
import com.vimoda.yourrace.adapter.DetallesPedidosAdapter;
import com.vimoda.yourrace.modelos.DetalleProducto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class DetallesPedidoActivity extends AppCompatActivity {
    private static final String TAG = "DetallesPedidoActivity";
    private RecyclerView recyclerViewDetalles;
    private DetallesPedidosAdapter adapter;
    private List<DetalleProducto> listaDetalles;
    private FirebaseFirestore db;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_pedido);

        db = FirebaseFirestore.getInstance();
        userId = UserPreferences.getUserId(this);

        recyclerViewDetalles = findViewById(R.id.rcvDetallesPedido);
        recyclerViewDetalles.setLayoutManager(new LinearLayoutManager(this));

        listaDetalles = new ArrayList<>();
        adapter = new DetallesPedidosAdapter(listaDetalles);
        recyclerViewDetalles.setAdapter(adapter);

        obtenerDetallesPedido();
    }

    private void obtenerDetallesPedido() {
        String userId = UserPreferences.getUserId(this);
        Log.d(TAG, "Obteniendo detalles de pedido para userId: " + userId);

        if (userId != null && !userId.isEmpty()) {
            CollectionReference comprasRef = db.collection("usuarios").document(userId).collection(
                    "compras");

            comprasRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    Log.d(TAG, "Número de documentos recuperados: " + queryDocumentSnapshots.size());
                    listaDetalles.clear();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Log.d(TAG, "Procesando documento: " + documentSnapshot.getId());
                        // Obtener el mapa de productos
                        Map<String, Object> productosMap = (Map<String, Object>) documentSnapshot.get("productos");
                        if (productosMap != null) {
                            Log.d(TAG, "Número de productos en el mapa: " + productosMap.size());
                            for (Map.Entry<String, Object> entry : productosMap.entrySet()) {
                                Map<String, Object> detallesProducto = (Map<String, Object>) entry.getValue();
                                try {
                                    double precio = 0.0;
                                    double subtotal = 0.0;
                                    Object precioObj = detallesProducto.get("precio");
                                    Object subtotalObj = detallesProducto.get("subtotal");

                                    if (precioObj instanceof Double) {
                                        precio = (Double) precioObj;
                                    } else if (precioObj instanceof Long) {
                                        precio = ((Long) precioObj).doubleValue();
                                    } else if (precioObj instanceof String) {
                                        precio = Double.parseDouble((String) precioObj);
                                    }

                                    if (subtotalObj instanceof Double) {
                                        subtotal = (Double) subtotalObj;
                                    } else if (subtotalObj instanceof Long) {
                                        subtotal = ((Long) subtotalObj).doubleValue();
                                    } else if (subtotalObj instanceof String) {
                                        subtotal = Double.parseDouble((String) subtotalObj);
                                    }

                                    DetalleProducto detalle = new DetalleProducto(
                                            (String) detallesProducto.get("marca"),
                                            (String) detallesProducto.get("modelo"),
                                            ((Long) detallesProducto.get("cantidad")).intValue(),
                                            (String) detallesProducto.get("talla"),
                                            precio,
                                            subtotal
                                    );
                                    listaDetalles.add(detalle);
                                    Log.d(TAG, "Producto añadido: " + detalle.toString());
                                } catch (Exception e) {
                                    Log.e(TAG, "Error al convertir datos del producto: " + e.getMessage(),
                                            e);
                                    Log.e(TAG, "Detalles del producto que causó el error: " +
                                            detallesProducto.toString());
                                }
                            }
                        } else {
                            Log.w(TAG, "No se encontraron productos en este pedido");
                        }
                    }
                    Log.d(TAG, "Total de productos añadidos a la lista: " + listaDetalles.size());
                    adapter.notifyDataSetChanged();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "Error al obtener detalles de pedido: " + e.getMessage());
                }
            });
        } else {
            Log.e(TAG, "Error: userId está vacío");
        }
    }
}
/*
private void obtenerDetallesPedido() {
        String userId = UserPreferences.getUserId(this);
        Log.d(TAG, "Obteniendo detalles de pedido para userId: " + userId);

        if (userId != null && !userId.isEmpty()) {
            CollectionReference comprasRef = db.collection("usuarios").document(userId).collection("compras");

            comprasRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    Log.d(TAG, "Número de documentos recuperados: " + queryDocumentSnapshots.size());
                    listaDetalles.clear();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Log.d(TAG, "Procesando documento: " + documentSnapshot.getId());
                        // Obtener el mapa de productos
                        Map<String, Object> productosMap = (Map<String, Object>) documentSnapshot.get("productos");
                        if (productosMap != null) {
                            Log.d(TAG, "Número de productos en el mapa: " + productosMap.size());
                            for (Map.Entry<String, Object> entry : productosMap.entrySet()) {
                                Map<String, Object> detallesProducto = (Map<String, Object>) entry.getValue();
                                try {
                                    double precio = 0.0;
                                    double subtotal = 0.0;
                                    Object precioObj = detallesProducto.get("precio");
                                    Object subtotalObj = detallesProducto.get("subtotal");


                                    DetalleProducto detalle = new DetalleProducto(
                                            (String) detallesProducto.get("marca"),
                                            (String) detallesProducto.get("modelo"),
                                            ((Long) detallesProducto.get("cantidad")).intValue(),
                                            (String) detallesProducto.get("talla"),
                                            precio,
                                            subtotal
                                    );
                                    listaDetalles.add(detalle);
                                    Log.d(TAG, "Producto añadido: " + detalle.toString());
                                } catch (Exception e) {
                                    Log.e(TAG, "Error al convertir datos del producto: " + e.getMessage(), e);
                                    Log.e(TAG, "Detalles del producto que causó el error: " + detallesProducto.toString());
                                }
                            }
                        } else {
                            Log.w(TAG, "No se encontraron productos en este pedido");
                        }
                    }
                    Log.d(TAG, "Total de productos añadidos a la lista: " + listaDetalles.size());
                    adapter.notifyDataSetChanged();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "Error al obtener detalles de pedido: " + e.getMessage());
                }
            });
        } else {
            Log.e(TAG, "Error: userId está vacío");
        }
    }



public class DetallesPedidoActivity extends AppCompatActivity {
    private static final String TAG = "DetallesPedidoActivity";
    private RecyclerView recyclerViewDetalles;
    private DetallesPedidosAdapter adapter;
    private List<DetalleProducto> listaDetalles;
    private FirebaseFirestore db;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_pedido);

        db = FirebaseFirestore.getInstance();
        userId = UserPreferences.getUserId(this);

        recyclerViewDetalles = findViewById(R.id.rcvDetallesPedido);
        recyclerViewDetalles.setLayoutManager(new LinearLayoutManager(this));

        listaDetalles = new ArrayList<>();
        adapter = new DetallesPedidosAdapter(listaDetalles);
        recyclerViewDetalles.setAdapter(adapter);

        obtenerDetallesPedido();
    }

    private void obtenerDetallesPedido() {
        String userId = UserPreferences.getUserId(this);
        Log.d(TAG, "Obteniendo detalles de pedido para userId: " + userId);

        if (userId != null && !userId.isEmpty()) {
            CollectionReference comprasRef = db.collection("usuarios").document(userId).collection("compras");

            comprasRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    Log.d(TAG, "Número de documentos recuperados: " + queryDocumentSnapshots.size());
                    listaDetalles.clear();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Log.d(TAG, "Procesando documento: " + documentSnapshot.getId());
                        // Obtener el mapa de productos
                        Map<String, Object> productosMap = (Map<String, Object>) documentSnapshot.get("productos");
                        if (productosMap != null) {
                            Log.d(TAG, "Número de productos en el mapa: " + productosMap.size());
                            for (Map.Entry<String, Object> entry : productosMap.entrySet()) {
                                Map<String, Object> detallesProducto = (Map<String, Object>) entry.getValue();
                                try {
                                    double precio = 0.0;
                                    double subtotal = 0.0;
                                    Object precioObj = detallesProducto.get("precio");
                                    Object subtotalObj = detallesProducto.get("subtotal");

                                    if (precioObj instanceof Double) {
                                        precio = (Double) precioObj;
                                    } else if (precioObj instanceof Long) {
                                        precio = ((Long) precioObj).doubleValue();
                                    } else if (precioObj instanceof String) {
                                        precio = Double.parseDouble((String) precioObj);
                                    }

                                    if (subtotalObj instanceof Double) {
                                        subtotal = (Double) subtotalObj;
                                    } else if (subtotalObj instanceof Long) {
                                        subtotal = ((Long) subtotalObj).doubleValue();
                                    } else if (subtotalObj instanceof String) {
                                        subtotal = Double.parseDouble((String) subtotalObj);
                                    }

                                    DetalleProducto detalle = new DetalleProducto(
                                            (String) detallesProducto.get("marca"),
                                            (String) detallesProducto.get("modelo"),
                                            ((Long) detallesProducto.get("cantidad")).intValue(),
                                            (String) detallesProducto.get("talla"),
                                            precio,
                                            subtotal
                                    );
                                    listaDetalles.add(detalle);
                                    Log.d(TAG, "Producto añadido: " + detalle.toString());
                                } catch (Exception e) {
                                    Log.e(TAG, "Error al convertir datos del producto: " + e.getMessage(), e);
                                    Log.e(TAG, "Detalles del producto que causó el error: " + detallesProducto.toString());
                                }
                            }
                        } else {
                            Log.w(TAG, "No se encontraron productos en este pedido");
                        }
                    }
                    Log.d(TAG, "Total de productos añadidos a la lista: " + listaDetalles.size());
                    adapter.notifyDataSetChanged();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "Error al obtener detalles de pedido: " + e.getMessage());
                }
            });
        } else {
            Log.e(TAG, "Error: userId está vacío");
        }
    }
}


                                    */
