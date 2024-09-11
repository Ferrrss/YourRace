package com.vimoda.yourrace.tiendaVirtual;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.vimoda.yourrace.R;
import com.vimoda.yourrace.modelos.Zapatos;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.vimoda.yourrace.tiendaVirtual.fragments.MainFragmentTv;
import com.vimoda.yourrace.tiendaVirtual.fragments.carrito.FragmentShoppingCart;

import java.util.HashMap;
import java.util.Map;

public class ZapatillaAdapter extends FirestoreRecyclerAdapter<Zapatos,
        ZapatillaAdapter.ViewHolder> {

    private Context context;
    private MainFragmentTv mainFragmentTv; // Variable para almacenar la referencia a MainFragmentTv

    public ZapatillaAdapter(@NonNull FirestoreRecyclerOptions<Zapatos> options, Context context,
                            MainFragmentTv mainFragmentTv) {
        super(options);
        this.context = context;
        this.mainFragmentTv = mainFragmentTv; // Inicializar la referencia
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int i, @NonNull Zapatos zapato) {
        String codigoZapato = getSnapshots().getSnapshot(i).getId();
        zapato.setCodigo(codigoZapato);

        holder.talla.setText(zapato.getTalla());
        holder.nombre.setText(zapato.getName());
        holder.precio.setText("S/. " + zapato.getPrecio());

        holder.btnAgregar.setOnClickListener(v -> {
            showAddToCartDialog(zapato);
        });
    }

    private void showAddToCartDialog(Zapatos zapato) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_cart, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        TextView tvMarca =dialogView.findViewById(R.id.tvMarcaModal);
        TextView tvModelo = dialogView.findViewById(R.id.tvModeloModal);
        TextView tvPrecio = dialogView.findViewById(R.id.tvPrecioModal);
        TextView tvTalla = dialogView.findViewById(R.id.tvTallaModal);
        TextView tvCantidad = dialogView.findViewById(R.id.tvCantidad);
        Button btnMenos = dialogView.findViewById(R.id.btnMenos);
        Button btnMas = dialogView.findViewById(R.id.btnMas);
        Button btnIrAlCarrito = dialogView.findViewById(R.id.btnIrAlCarrito);
        Button btnSeguirComprando = dialogView.findViewById(R.id.btnSeguirComprando);


        tvMarca.setText(zapato.getMarca());
        tvModelo.setText(zapato.getModelo());
        double precio = Double.parseDouble(zapato.getPrecio());
        tvPrecio.setText("S/. "+ precio);
        tvTalla.setText(zapato.getTalla());

        btnMas.setOnClickListener(v -> {
            int cantidad = Integer.parseInt(tvCantidad.getText().toString());
            int max=24;
            tvCantidad.setText(String.valueOf(cantidad + 1));
            if (cantidad==max){
                tvCantidad.setText(String.valueOf(max));
            }
        });

        btnMenos.setOnClickListener(v -> {
            int cantidad = Integer.parseInt(tvCantidad.getText().toString());
            if (cantidad > 1) {
                tvCantidad.setText(String.valueOf(cantidad - 1));
            }
        });

        btnIrAlCarrito.setOnClickListener(v -> {
            agregarCart(zapato, Integer.parseInt(tvCantidad.getText().toString()), true,dialog);
        });

        btnSeguirComprando.setOnClickListener(v -> {
            agregarCart(zapato, Integer.parseInt(tvCantidad.getText().toString()), false,dialog);
        });

        dialog.show();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_zapatilla,
                parent, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Button btnAgregar;
        TextView talla, nombre, precio;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            talla = itemView.findViewById(R.id.Cel);
            nombre = itemView.findViewById(R.id.Nombre);
            precio = itemView.findViewById(R.id.Precio);
            btnAgregar = itemView.findViewById(R.id.btnAgregar);
        }
    }

    private void agregarCart(Zapatos zapato, int cantidad, boolean navigateToCart,AlertDialog dialog) {
        SharedPreferences prefs = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String userId = prefs.getString("userId", "");

        if (!userId.isEmpty()) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Calculamos el subtotal
            double precio = Double.parseDouble(zapato.getPrecio());
            double subtotal = precio * cantidad;

            Map<String, Object> cartItem = new HashMap<>();
            cartItem.put("marca", zapato.getMarca());
            cartItem.put("modelo", zapato.getModelo());
            cartItem.put("precio", zapato.getPrecio());
            cartItem.put("talla", zapato.getTalla());
            cartItem.put("cantidad", cantidad);
            cartItem.put("subtotal", subtotal);

            db.collection("usuarios").document(userId).collection("carrito")
                    .add(cartItem)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(context, "Artículo añadido al carrito", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        if (navigateToCart && mainFragmentTv != null) {
                            mainFragmentTv.replaceFragment(new FragmentShoppingCart());
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Error al añadir artículo al carrito", Toast.LENGTH_SHORT
                        ).show();
                    });
        } else {
            Toast.makeText(context, "Error: Usuario no identificado", Toast.LENGTH_SHORT).show();
        }
    }
}