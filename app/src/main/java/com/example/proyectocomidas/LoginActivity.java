package com.example.proyectocomidas;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText txEmail, txPassword;
    private Button btnLogin, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUI();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { attemptLogin(); }});

    }

    private void initUI(){
        txEmail = findViewById(R.id.txEmail);
        txPassword = findViewById(R.id.txPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
    }

    private void attemptLogin() {
        txPassword.setError(null);
        txEmail.setError(null);

        boolean cancel = false;
        View focusView = null;

        String mail = txEmail.getText().toString();
        String pass = txPassword.getText().toString();

        if (TextUtils.isEmpty(pass)) {
            txPassword.setError(getString(R.string.field_empty));
            focusView = txPassword;
            cancel = true;
        }

        if (TextUtils.isEmpty(mail)) {
            txEmail.setError(getString(R.string.field_empty));
            focusView = txEmail;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            doLogin(mail, pass);
        }
    }

    private void doLogin(String mail, String pass) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = Constants.URL_LOGIN;
        final Map<String, String> jsonBody = new HashMap<>();
        jsonBody.put("email", mail);
        jsonBody.put("password", pass);

        CustomRequest request = new CustomRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("VOLLEY", response.toString());
                try {
                    if(response.getInt("code") != 1){
                        Toast.makeText(LoginActivity.this, response.getString("error"), Toast.LENGTH_LONG).show();
                    }else{
                        JSONObject jsonObject = response.getJSONObject("response");
                        JSONObject jsonData = jsonObject.getJSONObject("data");
                        JSONObject jsonUser = jsonData.getJSONObject("user");

                        SharedPreferences preferencias = getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferencias.edit();

                        editor.putBoolean(Constants.PREF_LOG,true);
                        editor.putString(Constants.PREF_USER_TOKEN, response.getString("token"));
                        // ESTO DA PROBLEMAS
                        editor.putInt(Constants.PREF_USER_ID, jsonUser.getInt("id"));
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
                Log.e("VOLLEY", error.toString());

            }
        });

        requestQueue.add(request);
    }
}
