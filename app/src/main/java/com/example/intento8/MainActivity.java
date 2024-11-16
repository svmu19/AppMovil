package com.example.intento8;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    EditText usernameEditText, passwordEditText;
    Button loginButton;
    TextView registerTextView, forgotPasswordTextView;

    private FirebaseAuth mAuth;  // Firebase Authentication

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar Firebase
        mAuth = FirebaseAuth.getInstance();

        // Vincular los EditText y botones con los campos del XML
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        registerTextView = findViewById(R.id.registrocuenta);
        forgotPasswordTextView = findViewById(R.id.olvidarcontra);

        // Configuración para el botón de Login
        loginButton.setOnClickListener(v -> {
            String correo = usernameEditText.getText().toString().trim();
            String contraseña = passwordEditText.getText().toString().trim();

            // Verificar si los campos están vacíos
            if (TextUtils.isEmpty(correo) || TextUtils.isEmpty(contraseña)) {
                Toast.makeText(MainActivity.this, "Por favor complete todos los campos", Toast.LENGTH_LONG).show();
                return;
            }

            // Iniciar sesión con Firebase Authentication
            mAuth.signInWithEmailAndPassword(correo, contraseña)
                    .addOnCompleteListener(MainActivity.this, task -> {
                        if (task.isSuccessful()) {
                            // Verificar si es el superusuario
                            if (correo.equals("admin@gmail.com") && contraseña.equals("123456")) {
                                // Si es el superusuario, redirigir a AdminActivity
                                Toast.makeText(MainActivity.this, "Bienvenido, Administrador", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                                startActivity(intent);
                            } else {
                                // Si no es el superusuario, redirigir a la actividad normal
                                Toast.makeText(MainActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(MainActivity.this, bienvenido.class);
                                startActivity(intent);
                            }
                            finish();
                        } else {
                            // Si el inicio de sesión falla
                            Toast.makeText(MainActivity.this, "Error en el inicio de sesión: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });

        // Configuración para el enlace de registro
        registerTextView.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Registro.class);  // Ir a la actividad de registro
            startActivity(intent);
        });

        // Configuración para el enlace de "Olvidé mi contraseña"
        forgotPasswordTextView.setOnClickListener(v -> {
            String correo = usernameEditText.getText().toString().trim();
            if (TextUtils.isEmpty(correo)) {
                Toast.makeText(MainActivity.this, "Por favor ingrese su correo para recuperar la contraseña", Toast.LENGTH_LONG).show();
                return;
            }

            mAuth.sendPasswordResetEmail(correo)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Correo de recuperación enviado", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Error al enviar el correo: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }
}
