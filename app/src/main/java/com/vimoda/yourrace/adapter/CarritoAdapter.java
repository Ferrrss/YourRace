package com.vimoda.yourrace.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.vimoda.yourrace.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.vimoda.yourrace.modelos.Zapatos;

import java.util.HashMap;

public class CarritoAdapter extends FirestoreRecyclerAdapter<Zapatos, CarritoAdapter.ViewHolder> {
    private final TextView precioTotal;
    private double precioTotalGlobal = 0.0;
    private final Context context;

    public CarritoAdapter(@NonNull FirestoreRecyclerOptions<Zapatos> options, TextView precioTotal,
                          Context context) {
        super(options);
        this.precioTotal = precioTotal;
        this.context = context;
        calcularPrecioTotalInicial();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Zapatos zapato) {
        holder.codigoProducto.setText(zapato.getCodigo());
        holder.nombreProducto.setText(zapato.getName());
        holder.tallaProducto.setText(zapato.getTalla());
        holder.precioProducto.setText(zapato.getPrecio());
        holder.cantidadProducto.setText(String.valueOf(zapato.getCantidad()));

        holder.agregarProducto.setOnClickListener(v -> {
            int nuevaCantidad = zapato.getCantidad() + 1;
            actualizarCantidad(zapato, nuevaCantidad, position);
            actualizarPrecioTotal();
        });

        holder.quitarProducto.setOnClickListener(v -> {
            if (zapato.getCantidad() > 1) {
                int nuevaCantidad = zapato.getCantidad() - 1;
                actualizarCantidad(zapato, nuevaCantidad, position);
                actualizarPrecioTotal();
            }
        });

        holder.eliminarTodo.setOnClickListener(v -> {
            eliminarProductoDeFirebase(getSnapshots().getSnapshot(position).getId());
            precioTotalGlobal -= calcularSubtotal(zapato);
            actualizarPrecioTotal();
        });
        // Log para verificar los valores en cada llamada a onBindViewHolder
        Log.d("CarritoAdapter", "onBindViewHolder - Subtotal: " + calcularSubtotal(zapato));
    }

    private double calcularSubtotal(Zapatos zapato) {
        String precioStr = zapato.getPrecio().replace("S/.", "").trim();
        double precio = Double.parseDouble(precioStr);
        double subtotal = precio * zapato.getCantidad();
        Log.d("CarritoAdapter", "Precio: " + precio + ", Cantidad: " + zapato.getCantidad() +
                ", Subtotal: " + subtotal);
        return subtotal;
    }

    private void actualizarCantidad(Zapatos zapato, int nuevaCantidad, int position) {
        double subtotalAnterior = calcularSubtotal(zapato);
        zapato.setCantidad(nuevaCantidad);
        double nuevoSubtotal = calcularSubtotal(zapato);

        precioTotalGlobal = precioTotalGlobal - subtotalAnterior + nuevoSubtotal;
        actualizarPrecioTotal();
        actualizarCantidadEnFirebase(position, nuevaCantidad, nuevoSubtotal);
    }

    private void actualizarPrecioTotal() {
        precioTotalGlobal = 0.0;
        for (int i = 0; i < getSnapshots().size(); i++) {
            Zapatos zapato = getSnapshots().get(i);
            precioTotalGlobal += calcularSubtotal(zapato);
        }
        Log.d("CarritoAdapter", "Actualizando precio total: " + precioTotalGlobal);
        precioTotal.setText(String.format("S/. %.2f", precioTotalGlobal));
    }

    private void calcularPrecioTotalInicial() {
        precioTotalGlobal = 0.0;
        for (int i = 0; i < getSnapshots().size(); i++) {
            Zapatos zapato = getSnapshots().get(i);
            precioTotalGlobal += calcularSubtotal(zapato);
            Log.d("CarritoAdapter", "Zapato: " + zapato.getName() + ", Subtotal: " +
                    calcularSubtotal(zapato) + ", PrecioTotalGlobal: " + precioTotalGlobal);
        }
        actualizarPrecioTotal();
    }

    private void actualizarCantidadEnFirebase(int position, int nuevaCantidad, double nuevoSubtotal) {
        SharedPreferences prefs = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String userId = prefs.getString("userId", "");

        if (!userId.isEmpty()) {
            String documentId = getSnapshots().getSnapshot(position).getId();
            FirebaseFirestore.getInstance()
                    .collection("usuarios")
                    .document(userId)
                    .collection("carrito")
                    .document(documentId)
                    .update("cantidad", nuevaCantidad, "subtotal", nuevoSubtotal)
                    .addOnSuccessListener(aVoid -> notifyDataSetChanged())
                    .addOnFailureListener(e -> Toast.makeText(context, "Error al actualizar cantidad",
                            Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(context, "Error: Usuario no identificado", Toast.LENGTH_SHORT).show();
        }
    }

    private void eliminarProductoDeFirebase(String productId) {
        SharedPreferences prefs = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String userId = prefs.getString("userId", "");

        if (!userId.isEmpty()) {
            FirebaseFirestore.getInstance()
                    .collection("usuarios")
                    .document(userId)
                    .collection("carrito")
                    .document(productId)
                    .delete()
                    .addOnSuccessListener(aVoid -> notifyDataSetChanged())
                    .addOnFailureListener(e -> Toast.makeText(context, "Error al eliminar producto",
                            Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(context, "Error: Usuario no identificado", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        calcularPrecioTotalInicial();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_product_carrito, parent,
                false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView codigoProducto, nombreProducto, precioProducto, cantidadProducto, tallaProducto;
        ImageButton agregarProducto, quitarProducto, eliminarTodo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            codigoProducto = itemView.findViewById(R.id.codigoProducto);
            nombreProducto = itemView.findViewById(R.id.nombre);
            precioProducto = itemView.findViewById(R.id.precioProducto);
            cantidadProducto = itemView.findViewById(R.id.cantidadProducto);
            tallaProducto = itemView.findViewById(R.id.txvTalla);
            agregarProducto = itemView.findViewById(R.id.btnMasProducto);
            quitarProducto = itemView.findViewById(R.id.btnMenosProducto);
            eliminarTodo = itemView.findViewById(R.id.btnBorrarProducto);
        }
    }
    // Método para obtener los datos de los productos en el carrito
    public HashMap<String, Zapatos> obtenerDatosProductos() {
        HashMap<String, Zapatos> datosProductos = new HashMap<>();
        for (int i = 0; i < getItemCount(); i++) {
            Zapatos zapatos = getItem(i);
            datosProductos.put(zapatos.getMarca() + " " + zapatos.getModelo(), zapatos);
        }
        return datosProductos;
    }

    public double obtenerPrecioTotal() {
        return precioTotalGlobal;
    }
}