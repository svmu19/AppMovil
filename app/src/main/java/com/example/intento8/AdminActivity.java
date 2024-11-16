package com.example.intento8;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {

    private EditText etCodigo, etNombre, etPrecio;
    private Button btnIngresar, btnBuscar, btnModificar, btnEliminar, btnListar;
    private ListView listViewProductos;

    private DatabaseReference database;
    private ArrayList<String> productosList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        etCodigo = findViewById(R.id.etCodigo);
        etNombre = findViewById(R.id.etNombre);
        etPrecio = findViewById(R.id.etPrecio);
        btnIngresar = findViewById(R.id.btnIngresar);
        btnBuscar = findViewById(R.id.btnBuscar);
        btnModificar = findViewById(R.id.btnModificar);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnListar = findViewById(R.id.btnListar);
        listViewProductos = findViewById(R.id.listViewProductos);

        productosList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productosList);
        listViewProductos.setAdapter(adapter);

        database = FirebaseDatabase.getInstance().getReference("productos");

        btnIngresar.setOnClickListener(v -> ingresarProducto());
        btnBuscar.setOnClickListener(v -> buscarProducto());
        btnModificar.setOnClickListener(v -> modificarProducto());
        btnEliminar.setOnClickListener(v -> eliminarProducto());
        btnListar.setOnClickListener(v -> listarProductos());
    }

    private void ingresarProducto() {
        String codigo = etCodigo.getText().toString().trim();
        String nombre = etNombre.getText().toString().trim();
        String precioStr = etPrecio.getText().toString().trim();

        if (TextUtils.isEmpty(codigo) || TextUtils.isEmpty(nombre) || TextUtils.isEmpty(precioStr)) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        double precio = Double.parseDouble(precioStr);
        Producto producto = new Producto(codigo, nombre, precio);
        database.child(codigo).setValue(producto)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Producto ingresado con éxito", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error al ingresar el producto", Toast.LENGTH_SHORT).show());
    }

    private void buscarProducto() {
        String codigo = etCodigo.getText().toString().trim();
        if (TextUtils.isEmpty(codigo)) {
            Toast.makeText(this, "Ingrese el código del producto a buscar", Toast.LENGTH_SHORT).show();
            return;
        }

        database.child(codigo).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    Producto producto = snapshot.getValue(Producto.class);
                    if (producto != null) {
                        etNombre.setText(producto.getNombre());
                        etPrecio.setText(String.valueOf(producto.getPrecio()));
                        Toast.makeText(this, "Producto encontrado", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Producto no encontrado", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Error al buscar el producto", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void modificarProducto() {
        String codigo = etCodigo.getText().toString().trim();
        String nombre = etNombre.getText().toString().trim();
        String precioStr = etPrecio.getText().toString().trim();

        if (TextUtils.isEmpty(codigo) || TextUtils.isEmpty(nombre) || TextUtils.isEmpty(precioStr)) {
            Toast.makeText(this, "Complete todos los campos para modificar el producto", Toast.LENGTH_SHORT).show();
            return;
        }

        double precio = Double.parseDouble(precioStr);
        Producto producto = new Producto(codigo, nombre, precio);
        database.child(codigo).setValue(producto)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Producto modificado con éxito", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error al modificar el producto", Toast.LENGTH_SHORT).show());
    }

    private void eliminarProducto() {
        String codigo = etCodigo.getText().toString().trim();
        if (TextUtils.isEmpty(codigo)) {
            Toast.makeText(this, "Ingrese el código del producto a eliminar", Toast.LENGTH_SHORT).show();
            return;
        }

        database.child(codigo).removeValue()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Producto eliminado con éxito", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error al eliminar el producto", Toast.LENGTH_SHORT).show());
    }

    private void listarProductos() {
        database.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                productosList.clear();
                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                    Producto producto = snapshot.getValue(Producto.class);
                    if (producto != null) {
                        String productoInfo = "Código: " + producto.getCodigo() + "\n"
                                + "Nombre: " + producto.getNombre() + "\n"
                                + "Precio: " + producto.getPrecio();
                        productosList.add(productoInfo);
                    }
                }
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Error al obtener productos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void limpiarCampos() {
        etCodigo.setText("");
        etNombre.setText("");
        etPrecio.setText("");
    }
}