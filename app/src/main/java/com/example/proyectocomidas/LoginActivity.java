package com.example.proyectocomidas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,GoogleApiClient.ConnectionCallbacks, View.OnClickListener {

    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authstateListener;
    private static final int CODE = 1;
    private SignInButton buttonGoogle;
    private LoginButton buttonFacebook;
    private CallbackManager callbackmanager;
    private String btnPressed = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        callbackmanager = CallbackManager.Factory.create();

        initUI();

        buttonFacebook.registerCallback(callbackmanager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(),"Inicio de sesión cancelado",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(),"¡Error!",Toast.LENGTH_SHORT).show();
            }
        });



        buttonGoogle.setOnClickListener(this);

        configureSignIn();

        firebaseAuth = FirebaseAuth.getInstance();
        authstateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    Toast.makeText(getApplicationContext(),"Iniciando sesión",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Cerrando sesión",Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private void configureSignIn(){
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,options)
                .build();
    }

    private void initUI(){
        buttonGoogle = findViewById(R.id.button_google);
        buttonGoogle.setSize(SignInButton.SIZE_WIDE);

        buttonFacebook = findViewById(R.id.button_facebook);
        buttonFacebook.setReadPermissions(Arrays.asList("email"));
    }

    private void signIn(){
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(intent,CODE);
    }

    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);


        if(btnPressed == getString(R.string.google_pressed_code)){
            if (requestCode == CODE){
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (result.isSuccess()){
                    GoogleSignInAccount account = result.getSignInAccount();
                    Toast.makeText(getApplicationContext(),"¡Has iniciado sesión correctamente!",Toast.LENGTH_SHORT).show();
                    AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
                    firebaseAuthWithGoogle(credential,account);
                }else{
                    Toast.makeText(getApplicationContext(),"¡Error!",Toast.LENGTH_SHORT).show();
                }
            }
        }else{
            callbackmanager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void firebaseAuthWithGoogle(AuthCredential credential, final GoogleSignInAccount account){
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    String id = account.getId();
                    String email = account.getEmail();
                    String name = account.getDisplayName();
                    Log.i("Account", id + ", " + email + ", " + name);
                }else{
                    Toast.makeText(getApplicationContext(),"Autentiación fallida",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected void onStart(){
        super.onStart();

        if (firebaseAuth != null){
            FirebaseAuth.getInstance().signOut();
        }
        firebaseAuth.addAuthStateListener(authstateListener);
    }

    protected void onStop(){
        super.onStop();

        if (firebaseAuth != null){
            firebaseAuth.removeAuthStateListener(authstateListener);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_google:
                btnPressed = getString(R.string.google_pressed_code);
                signIn();
                break;

        }
    }

    private void handleFacebookAccessToken(AccessToken accessToken){
        final AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Toast.makeText(getApplicationContext(),"¡Has iniciado sesión correctamente!",Toast.LENGTH_SHORT).show();
            }
        });
    }
}

