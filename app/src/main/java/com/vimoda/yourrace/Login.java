package com.vimoda.yourrace;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.vimoda.yourrace.crud.RegistroUsuario;
import com.vimoda.yourrace.crud.RenovarContra;
import com.vimoda.yourrace.tiendaVirtual.MainTienda;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {

    private Button login,crearCuenta;
    private EditText usuarioEdt,contraseniaEdt;
    private TextView renovarContra,respuesta;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usuarioEdt = findViewById(R.id.edtUsuario);
        contraseniaEdt = findViewById(R.id.edtContrasenia);
        login = findViewById(R.id.btnLogin);
        crearCuenta = findViewById(R.id.btnCrearCuenta);
        renovarContra = findViewById(R.id.txvrencontra);

        respuesta = findViewById(R.id.txvRespuesta);

        db = FirebaseFirestore.getInstance();

        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String usuario = usuarioEdt.getText().toString().trim();
                String contrasenia = contraseniaEdt.getText().toString().trim();

                if(usuario.isEmpty() || contrasenia.isEmpty()){
                    Toast.makeText(Login.this, "Campo de usuario o contraseña incompletos",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                //Autenticar el usuario en Firestore
                db.collection("usuarios").document(usuario).get()
                        .addOnCompleteListener(verificar -> {
                            if (verificar.isSuccessful()) {
                                DocumentSnapshot document = verificar.getResult();
                                if (document.exists()) {
                                    String contraAlmacenada = document.getString("contrasenia");
                                    if (contraAlmacenada.equals(contrasenia)) {
                                        Long rol = document.getLong("rol");
                                        if (rol != null) {
                                            // Guardar el ID del usuario
                                            UserPreferences.saveUserId(Login.this, usuario);
                                            Log.d("Login", "ID de usuario guardado: " + usuario);
                                            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = prefs.edit();
                                            editor.putString("userId", usuario);
                                            editor.apply();
                                            db.collection("roles").document(String.valueOf(rol)).get()
                                                    .addOnCompleteListener(roleCheck -> {
                                                        if (roleCheck.isSuccessful()) {
                                                            DocumentSnapshot roleDoc = roleCheck.getResult();
                                                            if (roleDoc.exists()) {
                                                                String rolNombre = roleDoc.getString("rol");
                                                                Intent i;
                                                                if ("trabajador".equals(rolNombre)) {
                                                                    i = new Intent(Login.this, MainActivity.class);
                                                                } else if ("cliente".equals(rolNombre)) {
                                                                    i = new Intent(Login.this, MainTienda.class);
                                                                } else if ("admin".equals(rolNombre)) {
                                                                    i = new Intent(Login.this, MainTienda.class);
                                                                } else {
                                                                    Toast.makeText(Login.this, "Rol no reconocido", Toast.LENGTH_LONG).show();
                                                                    Log.w("Firestore", "Rol no reconocido: " + rolNombre);
                                                                    return;
                                                                }
                                                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                startActivity(i);
                                                                finish();
                                                            } else {
                                                                Toast.makeText(Login.this, "Rol no encontrado", Toast.LENGTH_LONG).show();
                                                                Log.w("Firestore", "Rol no encontrado");
                                                            }
                                                        } else {
                                                            Toast.makeText(Login.this, "Error de obtención de rol: " + roleCheck.getException(), Toast.LENGTH_LONG).show();
                                                            Log.w("Firestore", "Error de obtención de rol", roleCheck.getException());
                                                        }
                                                    });
                                        } else {
                                            Toast.makeText(Login.this, "Error: Rol no especificado", Toast.LENGTH_LONG).show();
                                            Log.w("Firestore", "Rol no especificado");
                                        }
                                    } else {
                                        Toast.makeText(Login.this, "Usuario o contraseña inválidos", Toast.LENGTH_LONG).show();
                                        Log.w("Firestore", "Contraseña inválida");
                                    }
                                } else {
                                    Toast.makeText(Login.this, "El usuario no existe", Toast.LENGTH_LONG).show();
                                    Log.w("Firestore", "Usuario no encontrado");
                                }
                            } else {
                                Toast.makeText(Login.this, "" + verificar.getException(), Toast.LENGTH_LONG).show();
                                respuesta.setText(""+verificar.getException());
                                Log.w("Firestore", "Error de obtención de usuario", verificar.getException());
                            }
                        });
            }
        });

        crearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, RegistroUsuario.class);
                startActivity(i);
            }
        });
        renovarContra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, RenovarContra.class);
                startActivity(i);
            }
        });
    }
}