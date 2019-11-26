package com.example.proyectocomidas;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ProductosActivity extends AppCompatActivity {

    private List<Producto> products;
    private RecyclerView rvProducts;
    private ProductoAdapter productAdapter;
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;
    private FirebaseStorage mStorage;
    private String idCategory;
    private String nameCategory;
    private EditText etFilter;
    private Button btnMenu;
    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);
        Log.e("dentrifico",getIntent().getStringExtra("idCategoria"));
        nameCategory = getIntent().getStringExtra("nombreCategoria");
        idCategory = getIntent().getStringExtra("idCategoria");

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        initUI();
    }

    private void initUI(){
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance();
        rvProducts = findViewById(R.id.rvProductos);
        rvProducts.setHasFixedSize(true);
        rvProducts.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        products = new ArrayList<>();

        if(nameCategory.equals("Todo")){

            mFirestore.collection("Productos").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String id = document.getId();
                            String name = document.getString("nombre");
                            String description = document.getString("descripcion");
                            Boolean available = document.getBoolean("disponible");
                            String image = document.getString("foto");
                            String idCatgeory = document.getString("idcategorias");
                            Double precio = document.getDouble("precio");
                            products.add(new Producto(id, name, description, image, available, idCatgeory, precio));
                        }

                        productAdapter = new ProductoAdapter(ProductosActivity.this, products, mStorage, mAuth);
                        rvProducts.setAdapter(productAdapter);
                    }
                }
            });

        }else{

            mFirestore.collection("Productos").whereEqualTo("idCategorias", idCategory).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String id = document.getId();
                            String name = document.getString("nombre");
                            String description = document.getString("descripcion");
                            Boolean available = document.getBoolean("disponible");
                            String image = document.getString("foto");
                            String idCatgeory = document.getString("idcategorias");
                            Double precio = document.getDouble("precio");
                            products.add(new Producto(id, name, description, image, available, idCatgeory, precio));
                        }

                        productAdapter = new ProductoAdapter(ProductosActivity.this, products, mStorage, mAuth);
                        rvProducts.setAdapter(productAdapter);
                    }
                }
            });
        }

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

        btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final List<Alergeno> allergens = new ArrayList<>();
                final List<AlergenosIngredientes> alergenosIngredientes = new ArrayList<>();
                final List<IngredientesProducto> ingredientesProductos = new ArrayList<>();
                mFirestore.collection("Alergenos").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document: task.getResult()){
                                String id = document.getId();
                                String allergen = document.getString("nombre");
                                allergens.add(new Alergeno(id, allergen));
                            }

                            mFirestore.collection("AlergenosIngredientes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()){
                                        for (QueryDocumentSnapshot document: task.getResult()){
                                            String alergenosID = document.getString("idAlergeno");
                                            String ingredientesID = document.getString("idIngrediente");
                                            alergenosIngredientes.add(new AlergenosIngredientes(alergenosID, ingredientesID));
                                        }

                                        mFirestore.collection("IngredientesProductos").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()){
                                                    for (QueryDocumentSnapshot document: task.getResult()){
                                                        String ingredientesID = document.getString("idIngrediente");
                                                        String productoID = document.getString("idProducto");
                                                        ingredientesProductos.add(new IngredientesProducto(ingredientesID, productoID));
                                                    }
                                                    inflateMenuAllergens(allergens, alergenosIngredientes, ingredientesProductos);
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    private void inflateMenuAllergens(final List<Alergeno> allergens, final List<AlergenosIngredientes> alergenosIngredientes, final List<IngredientesProducto> ingredientesProductos){
        final ArrayList allergenFilter = new ArrayList();
        final CharSequence[] cs = new CharSequence[allergens.size()];

        for (int i = 0; i < allergens.size(); i++) {
            cs[i] = allergens.get(i).getNombre();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(ProductosActivity.this);
        builder.setTitle("Alérgenos");
        builder.setMultiChoiceItems(cs, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked){
                    allergenFilter.add(cs[which]);
                }
            }
        }).setPositiveButton("FILTRAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final ArrayList productFilter = new ArrayList();
                final List<Producto> result = new ArrayList<>();

                ArrayList<String> allergensProduct;
                boolean haveAllergen;

                if (allergenFilter.size() > 0) {

                    for (int i = 0; i < products.size(); i++) {

                        allergensProduct = new ArrayList<>();
                        haveAllergen = false;

                        String productId = products.get(i).getId();
                        for (int j = 0; j < ingredientesProductos.size(); j++) {
                            if (productId.equals(ingredientesProductos.get(j).getIdProducto())) {
                                String ingredientId = ingredientesProductos.get(j).idIngrediente;
                                for (int k = 0; k < alergenosIngredientes.size(); k++) {
                                    if (alergenosIngredientes.get(k).getIdIngrediente().equals(ingredientId)) {
                                        String allergenId = alergenosIngredientes.get(k).getIdAlergeno();
                                        for (int l = 0; l < allergens.size(); l++) {
                                            if (allergens.get(l).getId().equals(allergenId)) {
                                                allergensProduct.add(allergens.get(l).getNombre());
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (allergensProduct.size() != 0) {
                            for (int m = 0; m < allergenFilter.size(); m++) {
                                String allergen = allergenFilter.get(m).toString();

                                if (allergensProduct.contains(allergen)) {
                                    haveAllergen = true;
                                    break;
                                }
                            }

                            if (!haveAllergen) {
                                productFilter.add(products.get(i));
                            }
                        } else {
                            productFilter.add(products.get(i));
                        }
                    }

                    productAdapter.setProducts(productFilter);
                    productAdapter.notifyDataSetChanged();

                }else{
                    productAdapter.setProducts(products);
                    productAdapter.notifyDataSetChanged();
                }

            }
        });

        builder.show();
        builder.create();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_products, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_shop){
            Log.e("MENU", "Carrito");
        }
        return super.onOptionsItemSelected(item);
    }
}
