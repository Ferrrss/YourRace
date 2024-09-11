package com.vimoda.yourrace.Fragments.Pedidos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vimoda.yourrace.R;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {
    private List<PedidosP> ordersList;

    public OrdersAdapter(List<PedidosP> ordersList) {
        this.ordersList = ordersList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pedido, parent, false);
        return new OrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        PedidosP pedido = ordersList.get(position);

        // Bind data to views
        holder.nombreTextView.setText(pedido.getNombre());
        holder.apellidoTextView.setText(pedido.getApellido());
        holder.telefonoTextView.setText(pedido.getTelefono());
        holder.dniTextView.setText(pedido.getDni());
        holder.precioTextView.setText(String.valueOf(pedido.getPrecioTotal()));
        holder.estadoTextView.setText(pedido.getEstado());

        // If there is a RecyclerView for products, handle it here
        // holder.productsRecyclerView.setAdapter(...);
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTextView, apellidoTextView, telefonoTextView, dniTextView, precioTextView, estadoTextView;
        RecyclerView productsRecyclerView;

        public OrderViewHolder(View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.name);
            telefonoTextView = itemView.findViewById(R.id.Cel);
            dniTextView = itemView.findViewById(R.id.Dni);
            precioTextView = itemView.findViewById(R.id.totPrecio);
            estadoTextView = itemView.findViewById(R.id.estado);
            productsRecyclerView = itemView.findViewById(R.id.rcvLista);
        }
    }
}
