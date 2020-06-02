package com.example.proyectocomidas;

import android.content.DialogInterface;
import android.content.Intent;
import androidx.annotation.NonNull;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.proyectocomidas.adapters.CategoriaAdapter;
import com.example.proyectocomidas.models.Categoria;

import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private List<Categoria> categorias;
    private RecyclerView rvCategorias;
    private CategoriaAdapter categoriaAdapter;
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences(Constants.PREF, MODE_PRIVATE);

        initMenu();
        initUI();
        obtenerCategorias();

    }

    private void initMenu(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Guardar token en sharedPreference

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        boolean isLogged = preferences.getBoolean(Constants.PREF_LOG, false);

        if(isLogged){
            navigationView.inflateMenu(R.menu.menu_usuario);
        }else{
            navigationView.inflateMenu(R.menu.menu_anonimo);
        }
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initUI(){
        /*
        SharedPreferences preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        */

        rvCategorias = findViewById(R.id.rvCategorias);
        rvCategorias.setHasFixedSize(true);
        rvCategorias.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        boolean flag = false; // valor por defecto si aun no se ha tomado el username
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.itemPerfil) {
            startActivity(new Intent(MainActivity.this, PerfilUsuarioActivity.class));
        } else if (id == R.id.itemLogin) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        } else if (id == R.id.itemFavoritos) {
            startActivity(new Intent(MainActivity.this, PedidosFavoritosActivity.class));
        } else if (id == R.id.itemComentarios) {
            startActivity(new Intent(MainActivity.this, ComentariosActivity.class));
        } else if (id == R.id.itemCarrito) {
            startActivity(new Intent(MainActivity.this, CestaCompraActivity.class));
        } else if (id == R.id.itemCerrarSesion) {
            SharedPreferences preferences = getSharedPreferences(Constants.PREF, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(Constants.PREF_LOG,false);
            editor.apply();
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private  void obtenerCategorias(){
        String URL = Constants.URL_CATEGORIES;

        categorias = new ArrayList<>();

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                Type type = new TypeToken<List<Categoria>>() {
                }.getType();
                try {
                    JSONObject jsonObject = response.getJSONObject("response");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    categorias = gson.fromJson(jsonArray.toString(), type);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                categoriaAdapter = new CategoriaAdapter(MainActivity.this, categorias);
                rvCategorias.setAdapter(categoriaAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Ad Response", error.getMessage());
                showErrorMsg(error.getMessage());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);;

        requestQueue.add(request);
    }

    private void showErrorMsg(String error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name);
        builder.setMessage(getString(R.string.error_msg) + error);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}
