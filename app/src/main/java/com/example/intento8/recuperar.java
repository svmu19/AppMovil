package com.example.intento8;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class recuperar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recuperar);

        // Cambia a R.id.recuperar para que coincida con el ID del LinearLayout en el XML
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.recuperar), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Botón para enviar solicitud
        Button enviarsolicitud = findViewById(R.id.recovery_button);
        enviarsolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegar a la actividad principal MainActivity
                Intent intent = new Intent(recuperar.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Texto para volver al login
        TextView backloginText = findViewById(R.id.backlogin);
        backloginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegar a la actividad principal MainActivity
                Intent intent = new Intent(recuperar.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
