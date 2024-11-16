package com.example.intento8;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class bienvenido extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bienvenido); // Asegúrate de que sea el archivo XML correcto

        // Corrige el ID utilizado en el código para que coincida con el XML
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.bienvenido), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Manejar clic en "Iniciar Sesión"
        Button continuarButton = findViewById(R.id.login);
        continuarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegar a la actividad inicio
                Intent intent = new Intent(bienvenido.this, inicio.class);
                startActivity(intent);
            }
        });
    }
}

