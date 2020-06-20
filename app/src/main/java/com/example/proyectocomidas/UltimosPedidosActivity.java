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
import com.google.gson.JsonArray;

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


        if (logueado) {

            jsonBody.put("user_id", ultimoPedido.getNombre());

        } else {

            jsonBody.put("guest_token", ultimoPedido.toString());
        }


        CustomRequest request = new CustomRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Log.i("VOLLEY", response.toString());
                try {
                    if (response.getInt("code") == -1) {
                        Toast.makeText(UltimosPedidosActivity.this, response.getString("error"), Toast.LENGTH_LONG).show();
                    } else {
                        JSONObject jsonObject = response.getJSONObject("response");
                        JSONObject jsonData = jsonObject.getJSONObject("data");
                        JSONObject jsonUser = jsonData.getJSONObject("user");
                        //JSONObject jsonProducts = products.getJSONObject("products");
                        //JSONObject jsonIngredients = ingredients.getJSONObject("ingredients");
                        //JSONObject jsonAllergs = allergs.getJSONObject("allergs");

                        Toast.makeText(UltimosPedidosActivity.this, getString(R.string.okRegister), Toast.LENGTH_LONG).show();

                        SharedPreferences preferencias = getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferencias.edit();

                        editor.putBoolean(Constants.PREF_LOG, true);
                        editor.putString(Constants.PREF_USER_TOKEN, response.getString("token"));
                        editor.putInt(Constants.PREF_USER_ID, jsonUser.getInt("id"));
                        editor.apply();

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e("VOLLEY", error.toString());
                Toast.makeText(UltimosPedidosActivity.this, getString(R.string.errorRegister), Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(request);


    }



}
