package com.vimoda.yourrace.Fragments.Inventario;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.vimoda.yourrace.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AgregarInventario extends AppCompatActivity {

    private EditText marcaEdt,tallaEdt,colorEdt,modeloEdt,precioEdt,stockEdt;
    private Button agregarZapato;
    private ImageButton regresar;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_inventario);
        marcaEdt = findViewById(R.id.edtMarca);
        tallaEdt = findViewById(R.id.edtTalla);
        colorEdt = findViewById(R.id.edtColor);
        modeloEdt = findViewById(R.id.edtModelo);
        precioEdt = findViewById(R.id.edtPrecio);
        stockEdt = findViewById(R.id.edtCantidad);
        agregarZapato = findViewById(R.id.btnAgregarZapato);
        regresar = findViewById(R.id.btnVolver7);
        db = FirebaseFirestore.getInstance();

        agregarZapato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String marca, talla,color,modelo,precio,stock;
                marca = marcaEdt.getText().toString().trim();
                color = colorEdt.getText().toString().trim();
                talla = tallaEdt.getText().toString().trim();
                modelo = modeloEdt.getText().toString().trim();
                precio = precioEdt.getText().toString().trim();
                stock = stockEdt.getText().toString().trim();

                if(marca.isEmpty() || talla.isEmpty() || color.isEmpty() || modelo.isEmpty() ||
                        precio.isEmpty() || stock.isEmpty()){
                    Toast.makeText(AgregarInventario.this, "Por favor no dejar campos vacíos",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                // Obtener la fecha actual
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String fecha = dateFormat.format(calendar.getTime());

                //añadir el zapato al inventario
                agregarInventario(marca,talla,color,modelo,fecha,precio,stock);
            }
        });

        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void agregarInventario(String marca,String talla,String color,String modelo,
                                   String fecha, String precio,String stock){
        //Referencia al documento del contador en Firestore
        final DocumentReference contadorRef = db.collection("contadores")
                .document("contadorZapato");

        contadorRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> verificar) {
                if(verificar.isSuccessful()){
                    DocumentSnapshot contadorBd= verificar.getResult();
                    long[] nuevoContador = {1};
                    if(contadorBd.exists()){
                        nuevoContador[0] = contadorBd.getLong("contador")+1;
                    }

                    contadorRef.update("contador",nuevoContador[0]).addOnCompleteListener(
                            new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                String codigoZapato = formatearCodigo((int) nuevoContador[0]);

                                Map<String,Object> zapatoData=new HashMap<>();
                                zapatoData.put("marca",marca);
                                zapatoData.put("talla",talla);
                                zapatoData.put("color",color);
                                zapatoData.put("modelo",modelo);
                                zapatoData.put("fechaIngreso",fecha);
                                zapatoData.put("precio",precio);
                                zapatoData.put("stock",stock);
                                zapatoData.put("id",nuevoContador[0]);

                                db.collection("inventarios").document(codigoZapato)
                                        .set(zapatoData)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(AgregarInventario.this,
                                                            "Zapato añadido con éxito",
                                                            Toast.LENGTH_LONG).show();
                                                    finish();//
                                                }else{
                                                    Toast.makeText(AgregarInventario.this, "Error" +
                                                            " al añadir zapato", Toast.LENGTH_LONG).show();
                                                    Log.e("Firestore","Error agregar zapato",
                                                            task.getException());
                                                }
                                            }
                                        });
                            }else{
                                Toast.makeText(AgregarInventario.this, "Error al crear código de " +
                                        "zapato", Toast.LENGTH_LONG).show();
                                Log.e("Firestore","Error creación de código",task.getException());
                            }
                        }
                    });
                }
            }
        });
    }

    public String formatearCodigo(int numero) {
        if (numero >= 0 && numero < 10) {
            return "Z000" + numero;
        } else if (numero >= 10 && numero < 100) {
            return "Z00" + numero;
        } else if (numero >= 100 && numero < 1000) {
            return "Z0" + numero;
        } else if (numero >= 1000 && numero < 10000) {
            return "Z" + numero;
        } else {
            return "Error";
        }
    }
}