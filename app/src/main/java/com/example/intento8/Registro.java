package com.example.intento8;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registro extends AppCompatActivity {

    EditText usuarioEditText, correoEditText, contraseñaEditText;
    Button registrarButton;

    private FirebaseAuth mAuth;  // Firebase Authentication
    private DatabaseReference mDatabase;  // Firebase Realtime Database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Inicializar Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Vincular los EditText con los campos del XML
        usuarioEditText = findViewById(R.id.register_username);
        correoEditText = findViewById(R.id.register_email);
        contraseñaEditText = findViewById(R.id.register_password);
        registrarButton = findViewById(R.id.register_button);

        registrarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreUsuario = usuarioEditText.getText().toString().trim();
                String correo = correoEditText.getText().toString().trim();
                String contraseña = contraseñaEditText.getText().toString().trim();

                // Verificar si los campos están vacíos
                if (TextUtils.isEmpty(nombreUsuario) || TextUtils.isEmpty(correo) || TextUtils.isEmpty(contraseña)) {
                    Toast.makeText(Registro.this, "Por favor complete todos los campos", Toast.LENGTH_LONG).show();
                    return;
                }

                // Registrar al usuario en Firebase Authentication
                mAuth.createUserWithEmailAndPassword(correo, contraseña)
                        .addOnCompleteListener(Registro.this, task -> {
                            if (task.isSuccessful()) {
                                // Si el registro fue exitoso, almacenar datos adicionales en Firebase Realtime Database
                                String userId = mAuth.getCurrentUser().getUid();
                                Usuario usuario = new Usuario(nombreUsuario, correo);

                                mDatabase.child("usuarios").child(userId).setValue(usuario)
                                        .addOnCompleteListener(Registro.this, task1 -> {
                                            if (task1.isSuccessful()) {
                                                // Si el registro en la base de datos fue exitoso
                                                Toast.makeText(Registro.this, "Usuario registrado con éxito", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(Registro.this, MainActivity.class);
                                                startActivity(intent);
                                            } else {
                                                // Si hubo un error al guardar en Firebase Realtime Database
                                                Toast.makeText(Registro.this, "Error al guardar los datos", Toast.LENGTH_LONG).show();
                                            }
                                        });
                            } else {
                                // Si el registro en Firebase Authentication falla
                                Toast.makeText(Registro.this, "Error en el registro: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }

    // Clase interna para representar al usuario
    public static class Usuario {
        public String nombreUsuario;
        public String correo;

        public Usuario(String nombreUsuario, String correo) {
            this.nombreUsuario = nombreUsuario;
            this.correo = correo;
        }
    }
}
