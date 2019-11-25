package com.example.proyectocomidas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CestaCompraActivity extends AppCompatActivity {

    ListView miLista;
    ArrayAdapter<Producto> miAdapter;
    ProductosCompra productos;
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
        String json = preferences.getString("productos", "");
        Log.i("PRUEBA", json);
        productos = new ProductosCompra();

        if(! json.equals("")) {
            productos = new ProductosCompra(productos.fromJSON(json).getListaProductos());
        }

        miAdapter = new ProductosCarritoAdapter(this, productos.getListaProductos());
        miLista.setAdapter(miAdapter);

    }

    private void init(){
        miLista = findViewById(R.id.carritoLV);
        btnEliminar = findViewById(R.id.btnEliminarProductoCesta);
        preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        gson = new Gson();

        //Descomentar para volver a inicializar el carrito de prueba

        /*
        SharedPreferences.Editor editor = preferences.edit();

        Producto producto1 = new Producto("Hamburguesa con queso", "", "", true, "");
        Producto producto2 = new Producto("Hamburguesa vegana", "", "", true, "");
        Producto producto3 = new Producto("Hamburguesa bacon y queso", "", "", true, "");

        productos = new ProductosCompra();
        productos.añadirProducto(producto1);
        productos.añadirProducto(producto1);
        productos.añadirProducto(producto2);
        productos.añadirProducto(producto3);

        String json = productos.toJson();
        editor.putString("productos", json);
        editor.apply();
        */
    }
}
