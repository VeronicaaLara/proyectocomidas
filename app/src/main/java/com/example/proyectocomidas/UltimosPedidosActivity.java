package com.example.proyectocomidas;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyectocomidas.models.Producto;
import com.example.proyectocomidas.models.UltimosPedidos;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;




public class UltimosPedidosActivity extends AppCompatActivity {


    private List<UltimosPedidos> pedidosUltimos;
    private RecyclerView idUltimoPedido;
    private UltimosPedidos pedidosUltimosAdapter;
    private String idUser;
    private List<String> products;
    private List<String> data;
    private List<String> ingredients;
    private List<String> allergs;
    private List<Producto> productsUlt;
    private  Boolean logueado;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ultimos_pedidos);

        initUI();

    }

    private void initUI(){

        pedidosUltimos = new ArrayList<>();
        products = new ArrayList<>();
        data = new ArrayList<>();
        productsUlt = new ArrayList<>();
        allergs = new ArrayList<>();
        ingredients = new ArrayList<>();
        idUltimoPedido = findViewById(R.id.idUltimoPedido);


    }


    private void ultimosPedidos(final UltimosPedidos ultimoPedido) {


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = Constants.URL_LAST_ORDER;
        final Map<String, String> jsonBody = new HashMap<>();
    }





}
