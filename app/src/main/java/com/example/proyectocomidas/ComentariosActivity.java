package com.example.proyectocomidas;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

//import com.example.proyectocomidas.Assistance;
//import com.example.proyectocomidas.Assists;
import com.example.proyectocomidas.MainActivity;
import com.example.proyectocomidas.R;
import com.example.proyectocomidas.adapters.CategoriaAdapter;
import com.example.proyectocomidas.adapters.ComentariosAdapter;
import com.example.proyectocomidas.models.Comentario;
import com.google.android.gms.common.util.Strings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class ComentariosActivity extends AppCompatActivity {

    private List<Comentario> comentarios;
    private RecyclerView rvComentarios;
    private ComentariosAdapter comentarioAdapter;
    private FirebaseFirestore mFirestore;

    EditText comentario;
    Button addComentario;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentarios);

        initUI();


    }

    @SuppressLint("WrongViewCast")
    private void initUI(){

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFirestore = FirebaseFirestore.getInstance();
        rvComentarios = findViewById(R.id.rvComentarios);
        rvComentarios.setHasFixedSize(true);
        rvComentarios.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        obtenerComentarios();

        comentario = findViewById(R.id.escribirComentario);

        addComentario = (Button) findViewById(R.id.addComentario_btn);

        addComentario.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.e("llega","siiii");

                Comentario user = new Comentario(comentario.getText().toString());

                addComentario(user);

            }
        });


        boolean flag = false; // valor por defecto si aun no se ha tomado el username



    }

    private void obtenerComentarios(){

        comentarios = new ArrayList<>();

        mFirestore.collection("Comentarios").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document: task.getResult()){
                        String comentario = document.getString("comentario");
                        Log.e("error", document.getId());
                        comentarios.add(new Comentario(comentario));
                    }

                    comentarioAdapter = new ComentariosAdapter(ComentariosActivity.this, comentarios);
                    rvComentarios.setAdapter(comentarioAdapter);
                }
            }
        });


    }


    private void addComentario(final Comentario user){

        dialog = ProgressDialog.show(this, "",
                "Cargando... espere por favor", true);

        mFirestore.collection("Comentarios").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                obtenerComentarios(); // actualizo el listView, obteniendo los datos añadidos y los que ya habia añadidos

                Toast.makeText(getApplicationContext(), "Se ha añadido el comentario", Toast.LENGTH_LONG).show();
                dialog.dismiss();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Error al añadir el comentario", Toast.LENGTH_LONG).show();
                Log.i("pruebas", e.getMessage());
            }
        });


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.btnPerfil:

                Intent intent = new Intent(ComentariosActivity.this, PerfilUsuarioActivity.class);
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
