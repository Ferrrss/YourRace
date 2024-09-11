package com.vimoda.yourrace.tiendaVirtual.fragments.carrito;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.QuerySnapshot;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.vimoda.yourrace.R;
import com.vimoda.yourrace.UserPreferences;
import com.vimoda.yourrace.adapter.CarritoAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.vimoda.yourrace.modelos.Zapatos;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class FragmentShoppingCart extends Fragment {

    TextView precioTotal, textView21;
    Button pagarProducto;
    RecyclerView pCarrito;
    CarritoAdapter pAdapter;
    FirebaseFirestore db;
    ImageView imageView3;
    LinearLayout layoutCosto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_cart, container, false);

        // Inicialización de vistas
        pCarrito = view.findViewById(R.id.rcvProductos);
        precioTotal = view.findViewById(R.id.precioTotal);
        pagarProducto = view.findViewById(R.id.btnPagarProducto);
        imageView3 = view.findViewById(R.id.imageView3);
        textView21 = view.findViewById(R.id.textView21);
        layoutCosto = view.findViewById(R.id.layoutCosto);

        db = FirebaseFirestore.getInstance();

        SharedPreferences prefs = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String userId = prefs.getString("userId", "");

        if (!userId.isEmpty()) {
            pCarrito.setLayoutManager(new LinearLayoutManager(getContext()));
            Query query = db.collection("usuarios").document(userId).collection("carrito");

            FirestoreRecyclerOptions<Zapatos> firestoreRecyclerOptions =
                    new FirestoreRecyclerOptions.Builder<Zapatos>()
                            .setQuery(query, Zapatos.class)
                            .setLifecycleOwner(this)
                            .build();
            pAdapter = new CarritoAdapter(firestoreRecyclerOptions, precioTotal, getContext());
            pAdapter.notifyDataSetChanged();
            pCarrito.setAdapter(pAdapter);

            // Register adapter data observer to check if the cart is empty
            pAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    checkCartEmpty();
                }

                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    super.onItemRangeInserted(positionStart, itemCount);
                    checkCartEmpty();
                }

                @Override
                public void onItemRangeRemoved(int positionStart, int itemCount) {
                    super.onItemRangeRemoved(positionStart, itemCount);
                    checkCartEmpty();
                }
            });
        } else {
            Toast.makeText(getContext(), "Error: Usuario no identificado", Toast.LENGTH_SHORT).show();
        }

        pagarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoConfirmacion();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        pAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        pAdapter.stopListening();
    }

    private void mostrarDialogoConfirmacion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirmar compra");
        builder.setMessage("¿Estás seguro de que deseas realizar esta compra?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                confirmarCompra();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

    private void confirmarCompra() {
        // Obtener datos de productos y precio total
        HashMap<String, Zapatos> datosProductos = pAdapter.obtenerDatosProductos();
        double precioTotalCompra = pAdapter.obtenerPrecioTotal();

        // Generar boleta en PDF
        generarBoletaPDF(datosProductos, precioTotalCompra);

        // Mover los datos del carrito a la colección 'compras' en Firestore
        moverDatosAColeccionCompras(datosProductos, precioTotalCompra);

        // Eliminar los productos del carrito después de la compra
        vaciarCarrito();
    }

    private void generarBoletaPDF(HashMap<String, Zapatos> datosProductos, double precioTotalCompra) {
        String directoryPath = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS).getPath();
        File file = new File(directoryPath, "boleta_compra.pdf");

        try {
            PdfWriter writer = new PdfWriter(file);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            document.add(new Paragraph("Boleta de Compra"));
            document.add(new Paragraph("Fecha: " + obtenerFechaActual()));
            document.add(new Paragraph("Hora: " + obtenerHoraActual()));
            document.add(new Paragraph(""));

            // Encabezados de la tabla
            document.add(new Paragraph(
            "Producto        Cantidad        Marca        Modelo        Talla        Precio Unitario   "+
                    "     Subtotal"));

            // Contenido de la tabla
            for (Map.Entry<String, Zapatos> entry : datosProductos.entrySet()) {
                String nombreProducto = entry.getKey();
                Zapatos zapatos = entry.getValue();
                int cantidad = zapatos.getCantidad();
                String marca = zapatos.getMarca();
                String modelo = zapatos.getModelo();
                String talla = zapatos.getTalla();
                double precioUnitario = Double.parseDouble(zapatos.getPrecio());
                double subtotal = zapatos.getSubtotal();

                String lineaProducto = nombreProducto + "        " + cantidad + "        " + marca +
                        "        " + modelo + "        " + talla + "        " + precioUnitario + "        " +
                        subtotal;
                document.add(new Paragraph(lineaProducto));
            }

            document.add(new Paragraph(""));
            document.add(new Paragraph("Total: " + precioTotalCompra));

            document.close();

            Toast.makeText(getContext(), "Boleta de compra generada correctamente",
                    Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error al generar boleta de compra", Toast.LENGTH_SHORT).show();
            Log.e("PDF", "Error al generar boleta de compra: " + e.getMessage());
        }
    }

    private void moverDatosAColeccionCompras(HashMap<String, Zapatos> datosProductos,
                                             double precioTotalCompra) {
        String userId = UserPreferences.getUserId(requireContext());

        if (!userId.isEmpty()) {
            // Crear un documento nuevo en la colección 'compras' del usuario
            DocumentReference comprasRef = db.collection("usuarios").document(userId).collection(
                    "compras").document();
            HashMap<String, Object> compra = new HashMap<>();
            compra.put("fecha", obtenerFechaActual());
            compra.put("hora", obtenerHoraActual());
            compra.put("productos", new HashMap<String, Object>()); // Inicializar el HashMap de productos
            compra.put("precioTotal", precioTotalCompra);
            compra.put("estado", "En proceso");

            // Convertir datosProductos a HashMap<String, Object> para guardarlo en Firestore
            HashMap<String, Object> productosMap = new HashMap<>();
            for (Map.Entry<String, Zapatos> entry : datosProductos.entrySet()) {
                String nombreProducto = entry.getKey();
                Zapatos zapatos = entry.getValue();
                HashMap<String, Object> detallesProducto = new HashMap<>();
                detallesProducto.put("cantidad", zapatos.getCantidad());
                detallesProducto.put("marca", zapatos.getMarca());
                detallesProducto.put("modelo", zapatos.getModelo());
                detallesProducto.put("talla", zapatos.getTalla());
                detallesProducto.put("precio", zapatos.getPrecio());
                detallesProducto.put("subtotal", zapatos.getSubtotal());
                productosMap.put(nombreProducto, detallesProducto);
            }
            compra.put("productos", productosMap);

            comprasRef.set(compra)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Datos de compra guardados en 'compras'",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Error al guardar datos de compra",
                                        Toast.LENGTH_SHORT).show();
                                Log.e("Firestore", "Error al guardar datos de compra: " +
                                        task.getException().getMessage());
                            }
                        }
                    });
        } else {
            Log.e("Firestore", "Error: userId está vacío");
            Toast.makeText(getContext(), "Error: No se pudo identificar al usuario",
                    Toast.LENGTH_SHORT).show();
            // Aquí puedes manejar el caso cuando el userId está vacío
            // Por ejemplo, redirigiendo al usuario a la pantalla de login
        }
    }

    private void vaciarCarrito() {
        SharedPreferences prefs = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String userId = prefs.getString("userId", "");

        if (!userId.isEmpty()) {
            db.collection("usuarios").document(userId).collection("carrito")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {
                                    document.getReference().delete();
                                }
                                Toast.makeText(getContext(), "Carrito vaciado después de la compra",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e("Firestore", "Error al vaciar el carrito",
                                        task.getException());
                            }
                        }
                    });
        }
    }

    private String obtenerFechaActual() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        return dateFormat.format(new Date());
    }

    private String obtenerHoraActual() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss a", Locale.getDefault());
        return timeFormat.format(new Date());
    }

    // Método hipotético para obtener el precio unitario de un producto
    private double obtenerPrecioUnitario(String marca, String modelo) {
        // Aquí deberías implementar la lógica para obtener el precio unitario del producto desde tu base de datos
        return 50.0; // Ejemplo de precio unitario
    }

    private void checkCartEmpty() {
        if (pAdapter.getItemCount() == 0) {
            // Carrito vacío
            imageView3.setVisibility(View.VISIBLE);
            textView21.setVisibility(View.VISIBLE);
            pCarrito.setVisibility(View.INVISIBLE);
            layoutCosto.setVisibility(View.INVISIBLE);
        } else {
            // Carrito no vacío
            imageView3.setVisibility(View.INVISIBLE);
            textView21.setVisibility(View.INVISIBLE);
            pCarrito.setVisibility(View.VISIBLE);
            layoutCosto.setVisibility(View.VISIBLE);
        }
    }
}
