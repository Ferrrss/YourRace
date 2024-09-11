package com.vimoda.yourrace.Fragments.Inventario;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.vimoda.yourrace.Fragments.MainFragment;
import com.vimoda.yourrace.R;
import com.vimoda.yourrace.modelos.Zapatos;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditarInventario extends AppCompatActivity {
    private EditText marcaEdt,colorEdt,tallaEdt,modeloEdt,precioEdt,itemAgregar,stockEdt;
    private Button editarZapato,agregarS,quitarS,editStock;
    private ImageButton regresar;
    private String stockActual = "0";
    private FirebaseFirestore db;
    private String codigoZapato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_inventario);
        marcaEdt = findViewById(R.id.edtMarca);
        colorEdt = findViewById(R.id.edtColor);
        tallaEdt = findViewById(R.id.edtTalla);
        modeloEdt = findViewById(R.id.edtModelo);
        precioEdt = findViewById(R.id.edtPrecio);
        stockEdt = findViewById(R.id.edtStock);
        editStock = findViewById(R.id.btnEdit1);
        agregarS = findViewById(R.id.btnMax);
        itemAgregar = findViewById(R.id.edtAgregar);
        quitarS = findViewById(R.id.btnMin);
        editarZapato = findViewById(R.id.btnEditarZapato);
        regresar = findViewById(R.id.btnVolver8);
        db = FirebaseFirestore.getInstance();

        //bloquear el editText para el stock
        stockEdt.setEnabled(false);
        itemAgregar.setText("0");

        editStock.setOnClickListener(v -> {
            stockEdt.setEnabled(true);
            itemAgregar.setEnabled(false);
            agregarS.setEnabled(false);
            quitarS.setEnabled(false);
        });
        agregarS.setOnClickListener(v -> cambiarCantidad(1));
        quitarS.setOnClickListener(v -> cambiarCantidad(-1));

        // Obtener el código del zapato del Intent
        codigoZapato = getIntent().getStringExtra("codigoZapato");
        if (codigoZapato == null) {
            Toast.makeText(this, "Error: No se proporcionó el código del zapato", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cargar datos del zapato
        cargarDatosZapato();

        editarZapato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarCambios();
            }
        });

        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("codigoZapato", codigoZapato);
                intent.putExtra("datosActualizados", true);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }
    private void cargarDatosZapato() {
        db.collection("inventarios").document(codigoZapato)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Zapatos zapato = documentSnapshot.toObject(Zapatos.class);
                        if (zapato != null) {
                            marcaEdt.setText(zapato.getMarca());
                            colorEdt.setText(zapato.getColor());
                            tallaEdt.setText(zapato.getTalla());
                            modeloEdt.setText(zapato.getModelo());
                            precioEdt.setText(zapato.getPrecio());
                            stockActual = zapato.getStock();
                            stockEdt.setText(stockActual);
                        }
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error al cargar datos", Toast.LENGTH_SHORT).show());
    }

    private void cambiarCantidad(int cambio) {
        int cantidad = Integer.parseInt(itemAgregar.getText().toString());
        cantidad += cambio;
        if (cantidad < 0) cantidad = 0;
        itemAgregar.setText(String.valueOf(cantidad));
    }

    private void guardarCambios() {

            String marca = marcaEdt.getText().toString().trim();
            String color = colorEdt.getText().toString().trim();
            String talla = tallaEdt.getText().toString().trim();
            String modelo = modeloEdt.getText().toString().trim();
            String precio = precioEdt.getText().toString().trim();
            String nuevoStock;

            if (stockEdt.isEnabled()) {
                nuevoStock = stockEdt.getText().toString().trim();
            } else {
                int stockActualInt = Integer.parseInt(stockActual);
                int cantidadAgregar = Integer.parseInt(itemAgregar.getText().toString());
                nuevoStock = String.valueOf(stockActualInt + cantidadAgregar);
            }

            if (marca.isEmpty() || color.isEmpty() || talla.isEmpty() || modelo.isEmpty() || precio.isEmpty()
                    || nuevoStock.isEmpty()) {
                Toast.makeText(this, "Por favor no dejar campos vacíos", Toast.LENGTH_LONG).show();
                return;
            }

            // Obtener la fecha actual
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String fecha = dateFormat.format(calendar.getTime());

            Map<String, Object> zapatoData = new HashMap<>();
            zapatoData.put("marca",marca);
            zapatoData.put("talla",talla);
            zapatoData.put("color",color);
            zapatoData.put("modelo",modelo);
            zapatoData.put("fechaIngreso",fecha);
            zapatoData.put("precio",precio);
            zapatoData.put("stock",nuevoStock);

            db.collection("inventarios").document(codigoZapato)
                    .update(zapatoData)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(EditarInventario.this, "Zapato actualizado con éxito", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(EditarInventario.this, "Error al actualizar zapato", Toast.LENGTH_SHORT).show());
    }
}