package com.example.proyectocomidas;


import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.proyectocomidas.models.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/*import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;*/

public class RegisterActivity extends AppCompatActivity {

    private EditText txName, txApellido, txEmail, txAddress, txPhone, txPassword, txPasswordAgain;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initUI();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = txName.getText().toString().trim();
                final String apellido = txApellido.getText().toString().trim();
                final String email = txEmail.getText().toString().trim();
                final String address = txAddress.getText().toString().trim();
                final String phone = txPhone.getText().toString().trim();
                final String password = txPassword.getText().toString().trim();
                String passwordAgain = txPasswordAgain.getText().toString().trim();

                txName.setError(null);
                txEmail.setError(null);
                txAddress.setError(null);
                txPhone.setError(null);
                txPassword.setError(null);
                txPasswordAgain.setError(null);

                boolean cancel = false;
                View focusView = null;

                if (TextUtils.isEmpty(name)) {
                    txName.setError(getString(R.string.field_empty));
                    focusView = txName;
                    cancel = true;
                }

                if (TextUtils.isEmpty(password)) {
                    txPassword.setError(getString(R.string.field_empty));
                    focusView = txPassword;
                    cancel = true;
                }

                if (TextUtils.isEmpty(passwordAgain)) {
                    txPasswordAgain.setError(getString(R.string.field_empty));
                    focusView = txPasswordAgain;
                    cancel = true;
                }

                if (TextUtils.isEmpty(email)) {
                    txEmail.setError(getString(R.string.field_empty));
                    focusView = txEmail;
                    cancel = true;
                }

                if (TextUtils.isEmpty(address)) {
                    txAddress.setError(getString(R.string.field_empty));
                    focusView = txAddress;
                    cancel = true;
                }

                if (TextUtils.isEmpty(phone)) {
                    txPhone.setError(getString(R.string.field_empty));
                    focusView = txPhone;
                    cancel = true;
                }

                if (cancel) {
                    focusView.requestFocus();
                } else {
                    if (password.equals(passwordAgain)){
                        Usuario user = new Usuario(name, email, address, phone, apellido, password);
                        doRegister(user);
                    }else{
                        Toast.makeText(getApplicationContext(), "¡Las contraseñas no coinciden!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void initUI(){
        txName = findViewById(R.id.txName);
        txApellido.findViewById(R.id.txApellido);
        txEmail = findViewById(R.id.txEmail);
        txAddress = findViewById(R.id.txAddress);
        txPhone = findViewById(R.id.txPhone);
        txPassword = findViewById(R.id.txPassword);
        txPasswordAgain = findViewById(R.id.txPasswordAgain);
        btnSave = findViewById(R.id.btnSave);
    }

    private void doRegister(Usuario user) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = Constants.URL_REGISTER;
        final Map<String, String> jsonBody = new HashMap<>();

        jsonBody.put("first_name", user.getNombre());
        jsonBody.put("last_name", user.getApellido());
        jsonBody.put("email", user.getEmail());
        jsonBody.put("password", user.getPassword());
        jsonBody.put("address", user.getDireccion());
        jsonBody.put("phone", user.getTelefono());

        CustomRequest request = new CustomRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Log.i("VOLLEY", response.toString());
                try {
                    if(response.getInt("code") == -1){
                        Toast.makeText(RegisterActivity.this, response.getString("error"), Toast.LENGTH_LONG).show();
                    }else{
                        JSONObject jsonObject = response.getJSONObject("data");


                        Toast.makeText(RegisterActivity.this, getString(R.string.okRegister), Toast.LENGTH_LONG).show();
                        SharedPreferences preferencias = getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferencias.edit();

                        editor.putBoolean(Constants.PREF_LOG,true);
                        editor.putString(Constants.PREF_USER_TOKEN, response.getString("token"));
                        editor.putString(Constants.PREF_USER, jsonObject.getString("public_name"));
                        editor.putInt(Constants.PREF_USER_ID, jsonArray.getInt("id"));
                        editor.apply();

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e("VOLLEY", error.toString());
                Toast.makeText(RegisterActivity.this, getString(R.string.errorRegister), Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(request);
    }

}
