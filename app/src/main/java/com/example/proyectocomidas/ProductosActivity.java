package com.example.proyectocomidas;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class ProductosActivity extends AppCompatActivity {

    private List<Producto> products;
    private RecyclerView rvProducts;
    private ProductoAdapter productAdapter;
    private FirebaseFirestore mFiresotre;
    private FirebaseAuth mAuth;
    private FirebaseStorage mStorage;
    private String idCategory;
    private EditText etFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);
        Log.e("dentrifico",getIntent().getStringExtra("idCategoria"));
        idCategory = getIntent().getStringExtra("idCategoria");

        initUI();
    }

    private void initUI(){
        mAuth = FirebaseAuth.getInstance();
        mFiresotre = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance();
        rvProducts = findViewById(R.id.rvProductos);
        rvProducts.setHasFixedSize(true);
        rvProducts.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        products = new ArrayList<>();

        mFiresotre.collection("Productos").whereEqualTo("idCategorias", idCategory).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document: task.getResult()){
                        String name = document.getString("nombre");
                        String description = document.getString("descripcion");
                        Boolean available = document.getBoolean("disponible");
                        String image = document.getString("foto");
                        String idCatgeory = document.getString("idcategorias");
                        products.add(new Producto(name, description, image, available, idCatgeory));
                    }

                    productAdapter = new ProductoAdapter(ProductosActivity.this, products, mStorage, mAuth);
                    rvProducts.setAdapter(productAdapter);
                }
            }
        });

        etFilter = findViewById(R.id.etFilter);
        etFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                productAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
