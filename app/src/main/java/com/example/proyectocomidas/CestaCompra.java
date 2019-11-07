package com.example.proyectocomidas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CestaCompra extends AppCompatActivity {

    ListView miLista;
    ArrayAdapter<Producto> miAdapter;
    ArrayList<Producto> productos;
    Button btnEliminar;
    SharedPreferences preferences;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cesta_compra);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        String json = preferences.getString("Productos", null);
        ArrayList<Producto> productos = gson.fromJson(json, ArrayList.class);
        miAdapter = new ProductosCarritoAdapter(this, productos);
        miLista.setAdapter(miAdapter);
        miAdapter.notifyDataSetChanged();

    }

    private void init(){
        miLista = findViewById(R.id.carritoLV);
        btnEliminar = findViewById(R.id.btnEliminarProductoCesta);
        preferences = getSharedPreferences("Carrito", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        gson = new Gson();

        Producto producto1 = new Producto("Hamburguesa con queso", "", "", true, "");
        Producto producto2 = new Producto("Hamburguesa vegana", "", "", true, "");
        Producto producto3 = new Producto("Hamburguesa bacon y queso", "", "", true, "");

        productos = new ArrayList<>();
        productos.add(producto1);
        productos.add(producto2);
        productos.add(producto3);

        String json = gson.toJson(productos);
        editor.putString("Productos", json);
        editor.apply();

        miAdapter = new ProductosCarritoAdapter(this, productos);
        miLista.setAdapter(miAdapter);

    }
}
