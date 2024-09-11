package com.vimoda.yourrace.crud;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.vimoda.yourrace.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistroUsuario extends AppCompatActivity {

    private EditText usuarioEdt, contraseniaEdt, nombreEdt, apellidoEdt, dniEdt, telefonoEdt;
    private Button registrar;
    private ImageButton regresar;

    // Variables para conectar con Firebase
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);

        usuarioEdt = findViewById(R.id.edtUsuario);
        contraseniaEdt = findViewById(R.id.edtContrasenia);
        nombreEdt = findViewById(R.id.edtNombre1);
        apellidoEdt = findViewById(R.id.edtApellido);
        dniEdt = findViewById(R.id.edtDni);
        telefonoEdt = findViewById(R.id.edtTelefono);
        registrar = findViewById(R.id.btnRegistrar);
        regresar = findViewById(R.id.btnVolver5);

        db = FirebaseFirestore.getInstance();

        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        registrar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String usuario = usuarioEdt.getText().toString().trim();
                String contrasenia = contraseniaEdt.getText().toString().trim();
                String nombre = nombreEdt.getText().toString().trim();
                String apellido = apellidoEdt.getText().toString().trim();
                String dni = dniEdt.getText().toString().trim();
                String telefono = telefonoEdt.getText().toString().trim();
                int rol = 2;

                if(usuario.isEmpty() || contrasenia.isEmpty() || nombre.isEmpty() || apellido.isEmpty() ||
                        dni.isEmpty() || telefono.isEmpty()){
                    Toast.makeText(RegistroUsuario.this, "Todos los campos son obligatorios",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                if(dni.length() != 8){
                    Toast.makeText(RegistroUsuario.this, "El DNI debe tener 8 dígitos",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                if(telefono.length() != 9){
                    Toast.makeText(RegistroUsuario.this, "El teléfono debe tener 9 dígitos",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                // Registrar el usuario en Firestore
                Map<String, Object> user = new HashMap<>();
                user.put("usuario", usuario);
                user.put("contrasenia", contrasenia);
                user.put("rol", rol);

                // Crear el subdocumento perfil
                Map<String, Object> perfil = new HashMap<>();
                perfil.put("nombre", nombre);
                perfil.put("apellido", apellido);
                perfil.put("dni", dni);
                perfil.put("telefono", telefono);

                user.put("perfil", perfil);

                db.collection("usuarios").document(usuario)
                        .set(user)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(RegistroUsuario.this,
                                    "Usuario registrado satisfactoriamente", Toast.LENGTH_LONG).show();
                            Log.d("Firestore","Usuario registrado satisfactoriamente");
                        })
                        .addOnFailureListener(error -> {
                            Toast.makeText(RegistroUsuario.this, "Error de registro: " +
                                    error.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.w("Firestore","Error registro de usuario", error);
                        });
                finish();
            }
        });
    }
}