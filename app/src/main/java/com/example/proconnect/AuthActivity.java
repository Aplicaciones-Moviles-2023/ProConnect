package com.example.proconnect;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class AuthActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button btn_signUp;
    private Button btn_loging;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        //Inicializa Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //Inicializa los botones e inputs
        btn_signUp = findViewById(R.id.signUpButton);
        btn_loging = findViewById(R.id.loginButton);
        EditText emailEditText = findViewById(R.id.emailEditText);
        EditText passwordEditText = findViewById(R.id.PasswordEditText);

        //Asigna listener de click al boton de Registrarse
        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarEmail(emailEditText.getText().toString())){
                    String pass = passwordEditText.getText().toString();
                    if(validarPassword(passwordEditText.getText().toString())) {
                        crearCuenta(emailEditText.getText().toString(),passwordEditText.getText().toString());
                    }
                    else {
                        Toast.makeText(AuthActivity.this, "La contraseña debe tener al menos 8 caracteres",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(AuthActivity.this, "El email ingresado NO es correcto",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        //Asigna listener de click al boton de Iniciar Sesion
        btn_loging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarEmail(emailEditText.getText().toString())){
                    if(validarPassword(passwordEditText.getText().toString())) {
                        iniciarSesion(emailEditText.getText().toString(),passwordEditText.getText().toString());
                    }
                    else {
                        Toast.makeText(AuthActivity.this, "La contraseña debe tener al menos 8 caracteres",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(AuthActivity.this, "El email ingresado NO es correcto",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        //Al iniciar la actividad se verifica si el usuario ya ha iniciado sesion
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Toast.makeText(AuthActivity.this, "El usuario ya se encuentra autenticado", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AuthActivity.this, HomeActivity.class);
            intent.putExtra("email", currentUser.getEmail());
            startActivity(intent);
        }
    }

    void crearCuenta(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
             public void onComplete(@NonNull Task<AuthResult> task) {
                 if (task.isSuccessful()) {
                     //Cuenta creada correctamente
                     Log.d(TAG, "createUserWithEmail:success");
                     FirebaseUser user = mAuth.getCurrentUser();
                     Toast.makeText(AuthActivity.this, "Cuenta creada correctamente",
                             Toast.LENGTH_SHORT).show();
                 }
                 else {
                     //Error al crear la cuenta
                     Log.w(TAG, "createUserWithEmail:failure", task.getException());
                     Toast.makeText(AuthActivity.this, "Los datos ingresados ya se encuentran registrados.",
                             Toast.LENGTH_SHORT).show();
                 }
             }
        });
    }

    void iniciarSesion(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
             @Override
             public void onComplete(@NonNull Task<AuthResult> task) {
                 if (task.isSuccessful()) {
                     //Sesion iniciada correctamente
                     Log.d(TAG, "signInWithEmail:success");
                     FirebaseUser user = mAuth.getCurrentUser();
                     Toast.makeText(AuthActivity.this, "Se ha inciado sesion correctamente",
                             Toast.LENGTH_SHORT).show();
                     Intent intent = new Intent(AuthActivity.this, HomeActivity.class);
                     intent.putExtra("email", email);
                     startActivity(intent);
                 } else {
                     //ERROR al iniciar sesion.
                     Log.w(TAG, "signInWithEmail:failure", task.getException());
                     Toast.makeText(AuthActivity.this, "ERROR. Los datos ingresados NO son correctos",
                             Toast.LENGTH_SHORT).show();
                 }
             }
        });
    }

    boolean validarEmail(String email){
        String patternString = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(patternString);
        return pattern.matcher(email).matches();
    }

    boolean validarPassword(String password){
        return password.length() >= 8;
    }
}