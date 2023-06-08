package com.example.proconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    private Button btn_CerrarSesion;
    private TextView textViewEmail;

    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Obtiene parametros recibidos del insert y lo pinta en el TextView
        email = getIntent().getStringExtra("email");
        textViewEmail = findViewById(R.id.emailTextView);
        textViewEmail.setText("Email: " + email);

        //Obtiene boton de cerrar sesion
        btn_CerrarSesion = findViewById(R.id.btn_CerrarSesion);

        //Asigna listener de click al boton de cerrar sesion
        btn_CerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion();
            }
        });

    }

    //Cierra sesion y muestra la pantalla de Auth
    private void cerrarSesion(){
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(HomeActivity.this, "Se ha cerrado sesion correctamente", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(HomeActivity.this, AuthActivity.class);
        startActivity(intent);
    }
}