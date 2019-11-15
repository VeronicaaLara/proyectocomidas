package com.example.proyectocomidas;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.proyectocomidas.adapters.CategoriaAdapter;
import com.example.proyectocomidas.models.Categoria;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PantallaCategorias extends AppCompatActivity {

    private List<Categoria> categorias;
    private RecyclerView rvCategorias;
    private CategoriaAdapter categoriaAdapter;
    private FirebaseFirestore mFirestore;
    private Button actualizar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_categorias);

        initUI();
        obtenerCategorias();

        actualizar = (Button) findViewById(R.id.btnActualizar);

        actualizar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                obtenerCategorias();

            }
        });

    }

    private void initUI(){

        mFirestore = FirebaseFirestore.getInstance();
        rvCategorias = findViewById(R.id.rvCategorias);
        rvCategorias.setHasFixedSize(true);
        rvCategorias.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

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

                    categoriaAdapter = new CategoriaAdapter(PantallaCategorias.this, categorias);
                    rvCategorias.setAdapter(categoriaAdapter);
                }
            }
        });
    }



}
