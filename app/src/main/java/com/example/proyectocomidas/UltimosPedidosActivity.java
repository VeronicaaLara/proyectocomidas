package com.example.proyectocomidas;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectocomidas.adapters.DetallePedidoAdapter;
import com.example.proyectocomidas.adapters.PedidosFavortitosAdapter;
import com.example.proyectocomidas.models.Producto;
import com.example.proyectocomidas.models.ProductosCompra;
import com.example.proyectocomidas.models.UltimosPedidos;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UltimosPedidosActivity extends AppCompatActivity {


    private List<UltimosPedidos> pedidosUltimos;
    private RecyclerView idUltimoPedido;
    private UltimosPedidos pedidosUltimosAdapter;
    private String idUser;
    private List<String> products;
    private List<String> idProducto;
    private List<Producto> productsUlt;
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ultimos_pedidos);

        initUI();
    }

    private void initUI() {
        preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        idUltimoPedido = findViewById(R.id.idUltimoPedido);
        idUltimoPedido.setHasFixedSize(true);
        idUltimoPedido.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        pedidosUltimos = new ArrayList<>();
        products = new ArrayList<String>();
        idProducto = new ArrayList<>();
        productsUlt = new ArrayList<>();

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
                }
            }
        });

        mFirestore.collection("Usuarios").whereEqualTo("email", mAuth.getCurrentUser().getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    idUser = task.getResult().getDocuments().get(0).getId();

                    mFirestore.collection("Pedidos").whereEqualTo("idUser", idUser).whereEqualTo("favorito", true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                for (QueryDocumentSnapshot document: task.getResult()){
                                    String idUtimoPedido = document.getId();
                                    String nombre = document.getString("nombrePedido");

                                    pedidosUltimos.add(new UltimosPedidos(idUtimoPedido, nombre));
                                }

                                pedidosUltimosAdapter = new PedidosFavortitosAdapter(UltimosPedidosActivity.this, pedidosUltimos, new CustomClickPedido() {
                                    @Override
                                    public void onClick(View view, int index) {
                                        idProducto.clear();
                                        productsUlt.clear();
                                        String id = pedidosUltimos.get(index).getId();
                                        final String comments = pedidosUltimos.get(index).getComments();
                                        mFirestore.collection("PedidoProducto").whereEqualTo("idPedido", id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()){
                                                    for (QueryDocumentSnapshot document: task.getResult()){
                                                        final String idProducto = document.getString("idProducto");
                                                        products.add(idProducto);
                                                    }

                                                    for (String idProduct: idProducto) {
                                                        for (Producto product : idProducto) {
                                                            if (idProduct.equals(product.getId())) {
                                                                productsUlt.add(product);
                                                            }
                                                        }
                                                    }

                                                    final AlertDialog.Builder mBuilder = new AlertDialog.Builder(UltimosPedidosActivity.this);
                                                    final View mView = getLayoutInflater().inflate(R.layout.dialog_detalle_pedidos, null);
                                                    RecyclerView rvDetalle = mView.findViewById(R.id.rvDetallePedido);
                                                    rvDetalle.setHasFixedSize(true);
                                                    rvDetalle.setLayoutManager(new LinearLayoutManager(UltimosPedidosActivity.this));
                                                    DetallePedidoAdapter mAdapter = new DetallePedidoAdapter(UltimosPedidosActivity.this, productsUlt);
                                                    rvDetalle.setAdapter(mAdapter);

                                                    Button addProductsButton = mView.findViewById(R.id.btnAddProducts);
                                                    addProductsButton.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            SharedPreferences.Editor editor = preferences.edit();
                                                            editor.clear();
                                                            ProductosCompra pc = new ProductosCompra(productsUlt);
                                                            String json = pc.toJson();
                                                            editor.putString("productos", json);
                                                            editor.putString("observaciones", comments);
                                                            editor.commit();

                                                            Snackbar snackbar = Snackbar.make(mView, "¡Productos añadidos con éxito!", Snackbar.LENGTH_LONG);
                                                            snackbar.show();
                                                            //Intent intent = new Intent(PedidosFavoritosActivity.this, CestaCompraActivity.class);
                                                            //startActivity(intent);
                                                        }
                                                    });

                                                    mBuilder.setView(mView);
                                                    AlertDialog alertDialog = mBuilder.create();
                                                    alertDialog.show();
                                                }
                                            }
                                        });
                                    }
                                });

                                idUltimoPedido.setAdapter(pedidosUltimosAdapter);
                            }
                        }
                    });
                }
            }
        });
    }







}
