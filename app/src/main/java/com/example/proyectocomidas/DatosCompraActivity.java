package com.example.proyectocomidas;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class DatosCompraActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    EditText nombreText, emailText, direccionText, telefonoText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_compra);
        init();
    }

    private void init(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        nombreText = findViewById(R.id.nombreText);
        emailText = findViewById(R.id.emailText);
        direccionText = findViewById(R.id.direccionText);
        telefonoText = findViewById(R.id.telefonoText);

        if(firebaseAuth.getCurrentUser() != null){

            getUser(firebaseAuth.getCurrentUser().getEmail());

        }else{
            Log.e("Usuario", "No hay usuario logueado");
        }
    }

    private void getUser(final String email){
        firebaseFirestore.collection("Usuarios").whereEqualTo("email", email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().getDocuments().size() > 0) {

                      String nombre = task.getResult().getDocuments().get(0).getData().get("nombre").toString();
                      String email = task.getResult().getDocuments().get(0).getData().get("email").toString();
                      String direccion = task.getResult().getDocuments().get(0).getData().get("direccion").toString();
                      String telefono = task.getResult().getDocuments().get(0).getData().get("telefono").toString();

                      if(!nombre.isEmpty()){
                          nombreText.setText(nombre);
                          nombreText.setEnabled(false);
                      }

                      if(!email.isEmpty()){
                          emailText.setText(email);
                          emailText.setEnabled(false);
                      }

                      if(!direccion.isEmpty()){
                          direccionText.setText(direccion);
                          direccionText.setEnabled(false);
                      }

                      if(!telefono.isEmpty()){
                          telefonoText.setText(telefono);
                          telefonoText.setEnabled(false);
                      }

                      Log.e("Usuario", task.getResult().getDocuments().get(0).getData().toString());

                    } else {
                        if(!firebaseAuth.getCurrentUser().getDisplayName().isEmpty()){
                           nombreText.setText(firebaseAuth.getCurrentUser().getDisplayName());
                        }
                        emailText.setText(firebaseAuth.getCurrentUser().getEmail());
                        emailText.setEnabled(false);
                        Log.e("Usuario", "Logueado con google");
                    }
                }
            }
        });
    }
}
