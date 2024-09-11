package com.vimoda.yourrace.adapter;

import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vimoda.yourrace.Fragments.Inventario.EditarInventario;
import com.vimoda.yourrace.R;
import com.vimoda.yourrace.modelos.Zapatos;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ZapatoAdapter extends FirestoreRecyclerAdapter<Zapatos,
        ZapatoAdapter.ViewHolder> {

    public ZapatoAdapter(@NonNull FirestoreRecyclerOptions<Zapatos> options) {
        super(options);
    }
    private OnZapatoDeletedListener onZapatoDeletedListener;

    //Mostrar el contenido del card
    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Zapatos zapato) {
        try {
            DocumentSnapshot snapshot = getSnapshots().getSnapshot(position);
            String codigoZapato = snapshot.getId();
            zapato.setCodigo(codigoZapato);

            holder.codigo.setText(codigoZapato);
            holder.marca.setText(zapato.getMarca());
            holder.color.setText(zapato.getColor());
            holder.talla.setText(zapato.getTalla());
            holder.modelo.setText(zapato.getModelo());
            holder.fecha.setText(zapato.getFechaIngreso());
            holder.precio.setText("S/. " + zapato.getPrecio());

            //Funcion del boton editar
            holder.btnEdit.setOnClickListener(v -> {
                Intent intent = new Intent(holder.itemView.getContext(), EditarInventario.class);
                intent.putExtra("codigoZapato", codigoZapato);
                holder.itemView.getContext().startActivity(intent);
            });
            //Funcion del boton eliminar
            holder.btnElim.setOnClickListener(v -> {
                mostrarConfirmacion(holder.itemView, codigoZapato, position);
            });
        } catch (Exception e) {
            Log.e("ZapatoAdapter", "Error al vincular el ViewHolder", e);
        }
    }

    //Vincular el cardview
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_zapato, parent, false);
        return new ViewHolder(v);
    }

    //Referenciar datos
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView codigo,marca,color,talla,modelo,fecha,precio;
        ImageButton btnEdit,btnElim;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            codigo = itemView.findViewById(R.id.Codigo);
            marca = itemView.findViewById(R.id.Nombre);
            color = itemView.findViewById(R.id.Color);
            talla = itemView.findViewById(R.id.Cel);
            modelo = itemView.findViewById(R.id.Modelo);
            fecha = itemView.findViewById(R.id.FechaIngreso);
            precio = itemView.findViewById(R.id.Precio);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnElim = itemView.findViewById(R.id.btnElim);
        }
    }

    private void mostrarConfirmacion(View itemView, String codigoZapato, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
        builder.setTitle("Confirmar Eliminación");
        builder.setMessage("¿Estás seguro de que deseas eliminar este zapato?");
        builder.setPositiveButton("Sí", (dialog, which) -> eliminarZapato(itemView, codigoZapato, position));
        builder.setNegativeButton("No", null);
        builder.show();
    }

    private void eliminarZapato(View itemView, String codigoZapato, int position) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("inventarios").document(codigoZapato)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(itemView.getContext(),
                            "Zapato eliminado correctamente", Toast.LENGTH_SHORT).show();
                    notifyItemRemoved(position);
                    if (onZapatoDeletedListener != null) {
                        onZapatoDeletedListener.onZapatoDeleted();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(itemView.getContext(),
                        "Error al eliminar el zapato", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onDataChanged() {
        // Este método se llama cuando los datos subyacentes han cambiado
        super.onDataChanged();
        notifyDataSetChanged();
    }
    public interface OnZapatoDeletedListener {
        void onZapatoDeleted();
    }

    public void setOnZapatoDeletedListener(OnZapatoDeletedListener listener) {
        this.onZapatoDeletedListener = listener;
    }
}
