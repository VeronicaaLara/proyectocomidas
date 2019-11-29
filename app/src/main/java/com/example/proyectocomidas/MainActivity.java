package com.example.proyectocomidas;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.proyectocomidas.adapters.CategoriaAdapter;
import com.example.proyectocomidas.adapters.ComentariosAdapter;
import com.example.proyectocomidas.models.Categoria;
import com.example.proyectocomidas.models.Comentario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Categoria> categorias;
    private RecyclerView rvCategorias;
    private CategoriaAdapter categoriaAdapter;
    private FirebaseFirestore mFirestore;


    Button prueba;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        obtenerCategorias();

    }

    private void initUI(){

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFirestore = FirebaseFirestore.getInstance();
        rvCategorias = findViewById(R.id.rvCategorias);
        rvCategorias.setHasFixedSize(true);
        rvCategorias.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        boolean flag = false; // valor por defecto si aun no se ha tomado el username


        prueba = (Button) findViewById(R.id.btnPrueba);

        prueba.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.e("llega","siiii");


                Intent intent = new Intent(MainActivity.this, ComentariosActivity.class);
                startActivity(intent);



            }
        });



    }

    private void obtenerCategorias(){

        categorias = new ArrayList<>();

        mFirestore.collection("Categorias").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document: task.getResult()){
                        String name = document.getString("nombre");
                        String urlFoto = document.getData().get("imagen").toString();
                        Log.e("error", document.getId());
                        String idCategoria = document.getId();
                        categorias.add(new Categoria(name, urlFoto, idCategoria));
                    }

                    categoriaAdapter = new CategoriaAdapter(MainActivity.this, categorias);
                    rvCategorias.setAdapter(categoriaAdapter);
                }
            }
        });





    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.btnPerfil:

                Intent intent = new Intent(MainActivity.this, PerfilUsuarioActivity.class);
                startActivity(intent);

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }



}
