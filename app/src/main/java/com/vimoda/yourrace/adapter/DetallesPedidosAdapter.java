package com.vimoda.yourrace.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vimoda.yourrace.R;
import com.vimoda.yourrace.modelos.DetalleProducto;

import java.util.List;
import java.util.Locale;

public class DetallesPedidosAdapter extends RecyclerView.Adapter<DetallesPedidosAdapter.ViewHolder> {
    private static final String TAG = "DetallesPedidosAdapter";
    private List<DetalleProducto> listaDetalles;
    private Context context;

    public DetallesPedidosAdapter(List<DetalleProducto> listaDetalles) {
        this.listaDetalles = listaDetalles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_detalle_producto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DetalleProducto detalle = listaDetalles.get(position);
        //holder.tvNombre.setText(detalle.getNombre());
        Log.d(TAG, "Vinculando vista para la posición " + position + ": " + detalle.toString());
        holder.tvMarca.setText("Marca: " + detalle.getMarca());
        holder.tvModelo.setText("Modelo: " + detalle.getModelo());
        holder.tvCantidad.setText("Cantidad: " + detalle.getCantidad());
        holder.tvTalla.setText("Talla: " + detalle.getTalla());
        holder.tvPrecio.setText("Precio: S/." + String.format(Locale.getDefault(), "%.2f",
                detalle.getPrecio()));
        holder.tvSubtotal.setText("Subtotal: S/." + String.format(Locale.getDefault(), "%.2f",
                detalle.getSubtotal()));
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "Número de elementos en la lista: " + listaDetalles.size());
        return listaDetalles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvMarca, tvModelo, tvCantidad, tvTalla, tvPrecio, tvSubtotal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //tvNombre = itemView.findViewById(R.id.tvNombre);
            tvMarca = itemView.findViewById(R.id.tvMarca);
            tvModelo = itemView.findViewById(R.id.tvModelo);
            tvCantidad = itemView.findViewById(R.id.tvCantidad);
            tvTalla = itemView.findViewById(R.id.tvTalla);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            tvSubtotal = itemView.findViewById(R.id.tvSubtotal);
        }
    }
}
