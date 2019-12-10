package com.example.proyectocomidas;

import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PedidosFavoritosActivity extends AppCompatActivity {

    private List<PedidoFavorito> orders;
    private RecyclerView rvOrdersFav;
    private PedidosFavortitosAdapter ordersAdapter;
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;
    private String idUser;
    private List<Producto> products;
    private List<String> idProducts;
    private List<Producto> productsOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos_favoritos);

        initUI();
    }

    private void initUI(){
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        rvOrdersFav = findViewById(R.id.rvPedidosFav);
        rvOrdersFav.setHasFixedSize(true);
        rvOrdersFav.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        orders = new ArrayList<>();
        products = new ArrayList<>();
        idProducts = new ArrayList<>();
        productsOrder = new ArrayList<>();

        mFirestore.collection("Productos").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document: task.getResult()){
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
                    Log.e("ID", idUser);
                }
            }
        });

        mFirestore.collection("Pedidos").whereEqualTo("idUser", idUser).whereEqualTo("favorito", true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document: task.getResult()){
                        String id = document.getId();
                        String name = document.getString("nombrePedido");
                        String comments = document.getString("comentarios");
                        orders.add(new PedidoFavorito(id, name, comments));
                    }

                    ordersAdapter = new PedidosFavortitosAdapter(PedidosFavoritosActivity.this, orders, new CustomClickPedido() {
                        @Override
                        public void onClick(View view, int index) {
                            idProducts.clear();
                            productsOrder.clear();
                            String id = orders.get(index).getId();
                            mFirestore.collection("PedidoProductos").whereEqualTo("idPedido", id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()){
                                        for (QueryDocumentSnapshot document: task.getResult()){
                                            final String idProducto = document.getString("idProducto");
                                            idProducts.add(idProducto);
                                        }

                                        for (String idProduct: idProducts) {
                                            for (Producto product : products) {
                                                if (idProduct.equals(product.getId())) {
                                                    productsOrder.add(product);
                                                }
                                            }
                                        }

                                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(PedidosFavoritosActivity.this);
                                        View mView = getLayoutInflater().inflate(R.layout.dialog_detalle_pedidos, null);
                                        RecyclerView rvDetalle = mView.findViewById(R.id.rvDetallePedido);
                                        rvDetalle.setHasFixedSize(true);
                                        rvDetalle.setLayoutManager(new LinearLayoutManager(PedidosFavoritosActivity.this));
                                        DetallePedidoAdapter mAdapter = new DetallePedidoAdapter(PedidosFavoritosActivity.this, productsOrder);
                                        rvDetalle.setAdapter(mAdapter);

                                        mBuilder.setView(mView);
                                        AlertDialog alertDialog = mBuilder.create();
                                        alertDialog.show();
                                    }
                                }
                            });
                        }
                    });

                    rvOrdersFav.setAdapter(ordersAdapter);
                }
            }
        });
    }
}
