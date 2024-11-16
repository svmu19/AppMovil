package com.example.intento8;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class carrito extends AppCompatActivity {

    private ListView listViewCarrito;
    private ArrayList<CarritoItem> carritoList;
    private ArrayAdapter<CarritoItem> adaptador;
    private TextView tvTotal;
    private Button btnPagar;
    private Button btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        // Inicializamos las vistas
        listViewCarrito = findViewById(R.id.lvCarrito);
        tvTotal = findViewById(R.id.tvTotal);
        btnPagar = findViewById(R.id.btnPagar);
        btnVolver = findViewById(R.id.btnVolver);

        // Obtenemos el carrito desde el Intent
        carritoList = (ArrayList<CarritoItem>) getIntent().getSerializableExtra("carrito");

        // Configuramos el adaptador para mostrar los productos en el carrito
        adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, carritoList);
        listViewCarrito.setAdapter(adaptador);

        // Calculamos el total y lo mostramos
        calcularTotal();

        // Configuramos el botón "Pagar"
        btnPagar.setOnClickListener(v -> {
            if (carritoList.size() > 0) {
                // Crear el voucher
                Voucher voucher = crearVoucher();

                // Guardar el voucher en Firebase
                guardarVoucher(voucher);

                // Limpiamos el carrito después de realizar la compra
                carritoList.clear();
                adaptador.notifyDataSetChanged();
                calcularTotal();  // Actualizamos el total

                // Mostrar mensaje de éxito
                Toast.makeText(carrito.this, "Compra realizada con éxito", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(carrito.this, "El carrito está vacío", Toast.LENGTH_SHORT).show();
            }
        });

        // Configuramos el botón "Volver"
        btnVolver.setOnClickListener(v -> finish());
    }

    // Calcular el total del carrito
    private void calcularTotal() {
        double total = 0;
        for (CarritoItem item : carritoList) {
            total += item.getProducto().getPrecio() * item.getCantidad();
        }
        tvTotal.setText("Total: $" + total);
    }

    // Crear un objeto Voucher con los datos del carrito y la fecha/hora
    private Voucher crearVoucher() {
        double total = 0;
        for (CarritoItem item : carritoList) {
            total += item.getProducto().getPrecio() * item.getCantidad();
        }

        // Crear un ID único para el voucher basado en el tiempo actual
        String idVoucher = "V" + System.currentTimeMillis();  // ID único basado en el tiempo

        // Obtener la fecha y hora actual en formato completo (YYYY-MM-DD HH:mm:ss)
        String fechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        // Crear el objeto Voucher
        return new Voucher(idVoucher, carritoList, total, fechaHora);
    }

    // Guardar el voucher en Firebase
    private void guardarVoucher(Voucher voucher) {
        // Obtener referencia a Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("vouchers");

        // Guardar el voucher en Firebase
        ref.child(voucher.getIdVoucher()).setValue(voucher).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Mostrar mensaje de éxito
                Toast.makeText(carrito.this, "Compra realizada con éxito", Toast.LENGTH_SHORT).show();
            } else {
                // En caso de error
                Toast.makeText(carrito.this, "Error al realizar la compra", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
