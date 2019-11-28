package com.example.proyectocomidas;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    private String idUser = "kpKaRsBEO6Ma9MIi50H0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos_favoritos);

        initUI();
    }

    private void initUI(){
        mFirestore = FirebaseFirestore.getInstance();
        rvOrdersFav = findViewById(R.id.rvPedidosFav);
        rvOrdersFav.setHasFixedSize(true);
        rvOrdersFav.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        orders = new ArrayList<>();

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

                    ordersAdapter = new PedidosFavortitosAdapter(PedidosFavoritosActivity.this, orders);
                    rvOrdersFav.setAdapter(ordersAdapter);
                }
            }
        });
    }
}
