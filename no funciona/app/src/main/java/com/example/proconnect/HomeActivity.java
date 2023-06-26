package com.example.proconnect;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class HomeActivity extends AppCompatActivity {
    private TextView textViewEmail;

    private Button btn_Agregarempleo;
    private Button btn_ListarEmpleos;
    private Button btn_CerrarSesion;

    private String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Obtiene parametros recibidos del insert y lo pinta en el TextView
        email = getIntent().getStringExtra("email");
        textViewEmail = findViewById(R.id.emailTextView);
        textViewEmail.setText("Usuario conectado: " + email);

        //Obtiene botones
        btn_ListarEmpleos = findViewById(R.id.btn_listarEmpleos);
        btn_Agregarempleo = findViewById(R.id.btn_agregarEmpleo);
        btn_CerrarSesion = findViewById(R.id.btn_CerrarSesion);

        //Asigna listener a los botones
        //Agregar Empleo
        btn_Agregarempleo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, addActivity.class);
                startActivity(intent);
            }
        });

        //Agregar Empleo
        btn_ListarEmpleos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ItemDetailHostActivity.class);
                startActivity(intent);
            }
        });

        //Cerrar sesion
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