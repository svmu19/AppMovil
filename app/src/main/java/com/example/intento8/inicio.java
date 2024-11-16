package com.example.intento8;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class inicio extends AppCompatActivity {

    private ListView listViewProductos;
    private ArrayList<Producto> productosList;  // Lista de productos cargados desde Firebase
    private ArrayList<CarritoItem> carritoList;  // Lista de productos en el carrito
    private ArrayAdapter<String> adapter;        // Adapter para el ListView
    private DatabaseReference database;          // Referencia a Firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        // Inicializamos el ListView y las listas
        listViewProductos = findViewById(R.id.listViewProductos);
        productosList = new ArrayList<>();
        carritoList = new ArrayList<>();

        // Configuramos el ArrayAdapter para el ListView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        listViewProductos.setAdapter(adapter);

        // Inicializamos la referencia a Firebase
        database = FirebaseDatabase.getInstance().getReference("productos");

        // Cargamos los productos desde Firebase
        cargarProductos();

        // Configuramos el evento para agregar productos al carrito cuando se hace clic
        listViewProductos.setOnItemClickListener((parent, view, position, id) -> {
            Producto productoSeleccionado = productosList.get(position);
            agregarAlCarrito(productoSeleccionado);
        });
    }

    // Cargar productos desde Firebase
    private void cargarProductos() {
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                productosList.clear(); // Limpiar la lista antes de cargar nuevos productos

                // Iteramos sobre los productos en Firebase
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Producto producto = snapshot.getValue(Producto.class);
                    if (producto != null) {
                        productosList.add(producto);
                    }
                }

                // Actualizamos el ListView con los productos
                ArrayList<String> displayList = new ArrayList<>();
                for (Producto producto : productosList) {
                    displayList.add("Código: " + producto.getCodigo() + "\n" +
                            "Nombre: " + producto.getNombre() + "\n" +
                            "Precio: $" + producto.getPrecio());
                }

                adapter.clear();
                adapter.addAll(displayList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(inicio.this, "Error al cargar los productos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Agregar producto al carrito
    private void agregarAlCarrito(Producto producto) {
        boolean encontrado = false;

        // Verificamos si el producto ya está en el carrito
        for (CarritoItem item : carritoList) {
            if (item.getProducto().getCodigo().equals(producto.getCodigo())) {
                item.setCantidad(item.getCantidad() + 1);  // Incrementamos la cantidad si ya está en el carrito
                encontrado = true;
                break;
            }
        }

        // Si no está en el carrito, lo agregamos
        if (!encontrado) {
            carritoList.add(new CarritoItem(producto, 1));
        }

        Toast.makeText(this, producto.getNombre() + " agregado al carrito", Toast.LENGTH_SHORT).show();
    }

    public void verCarrito(View view) {
        if (carritoList != null && !carritoList.isEmpty()) {
            Intent intent = new Intent(inicio.this, carrito.class);
            intent.putExtra("carrito", carritoList);  // Pasamos el carrito a la actividad carrito
            startActivity(intent);
        } else {
            Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show();
        }
    }
}
