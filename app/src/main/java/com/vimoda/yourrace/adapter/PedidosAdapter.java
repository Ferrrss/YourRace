package com.vimoda.yourrace.adapter;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vimoda.yourrace.R;
import com.vimoda.yourrace.modelos.Pedido;
import com.vimoda.yourrace.tiendaVirtual.fragments.pedidos_compras.DetallesPedidoActivity;

import java.util.List;

public class PedidosAdapter extends RecyclerView.Adapter<PedidosAdapter.PedidoViewHolder> {

    private List<Pedido> listaPedidos;

    // Constructor
    public PedidosAdapter(List<Pedido> listaPedidos) {
        this.listaPedidos = listaPedidos;
    }

    // ViewHolder para cada elemento de pedido
    public static class PedidoViewHolder extends RecyclerView.ViewHolder {
        TextView textViewFecha, textViewHora, textViewPrecioTotal, textViewEstado,btnVerDetalles;

        public PedidoViewHolder(View itemView) {
            super(itemView);
            textViewFecha = itemView.findViewById(R.id.Fecha);
            textViewHora = itemView.findViewById(R.id.Hora);
            textViewPrecioTotal = itemView.findViewById(R.id.totalPrecio);
            textViewEstado = itemView.findViewById(R.id.Estado);
            btnVerDetalles = itemView.findViewById(R.id.btnDetalles);
        }
    }

    @NonNull
    @Override
    public PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el layout item_pedido.xml
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pedido, parent,
                false);
        return new PedidoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoViewHolder holder, int position) {
        // Obtener el pedido en la posición 'position'
        Pedido pedido = listaPedidos.get(position);

        // Asignar los valores a los elementos del ViewHolder
        holder.textViewFecha.setText("Fecha: " + pedido.getFecha());
        holder.textViewHora.setText("Hora: " + pedido.getHora());
        holder.textViewPrecioTotal.setText("Precio Total: " + pedido.getPrecioTotal());
        holder.textViewEstado.setText("Estado: " + pedido.getEstado());
        holder.btnVerDetalles.setOnClickListener(v->{
            // Crear un Intent para abrir DetallesPedidoActivity
            Context context = holder.itemView.getContext();
            Intent intent = new Intent(context, DetallesPedidoActivity.class);
            intent.putExtra("COMPRA_ID", pedido.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listaPedidos.size();
    }
}


