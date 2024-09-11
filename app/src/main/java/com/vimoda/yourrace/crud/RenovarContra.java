package com.vimoda.yourrace.crud;

import android.annotation.SuppressLint;
import android.os.Bundle;
import com.vimoda.yourrace.R;

import androidx.appcompat.app.AppCompatActivity;

//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;

import android.widget.Button;
import android.view.View;
import android.widget.ImageButton;

public class RenovarContra extends AppCompatActivity {

    private ImageButton regresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renovar_contra);

        regresar = findViewById(R.id.btnVolver5);
        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //Pruebas que se implementaran despues...
/*
        emailEditText = findViewById(R.id.txcorreo);
        newPasswordEditText = findViewById(R.id.txcontranueva);
        confirmPasswordEditText = findViewById(R.id.txcontranueva2);
        resetPasswordButton = findViewById(R.id.btnrescontra);

        auth = FirebaseAuth.getInstance();

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String newPassword = newPasswordEditText.getText().toString().trim();
                String confirmPassword = confirmPasswordEditText.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(RenovarContra.this, "Por favor, ingrese su correo electrónico", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(newPassword)) {
                    Toast.makeText(RenovarContra.this, "Por favor, ingrese su nueva contraseña", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!newPassword.equals(confirmPassword)) {
                    Toast.makeText(RenovarContra.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseUser user = auth.getCurrentUser();

                if (user != null) {
                    user.updatePassword(newPassword)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RenovarContra.this, "Contraseña cambiada exitosamente", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(RenovarContra.this, "Error al cambiar la contraseña", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(RenovarContra.this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
                }
            }
        });*/
    }
}