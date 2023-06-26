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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class addActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private EditText editTextTitulo;
    private EditText editTextDescripcion;
    private EditText editTextTipo;
    private EditText editTextUbicacion;

    private Button btn_Agregar;
    private Button btn_Volver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        db = FirebaseFirestore.getInstance();

        //Obtiene EditText
        editTextTitulo = findViewById(R.id.editTextTitulo);
        editTextDescripcion = findViewById(R.id.editTextDescripcion);
        editTextTipo = findViewById(R.id.editTextTipo);
        editTextUbicacion = findViewById(R.id.editTextUbicacion);

        //Obtiene botones
        btn_Agregar = findViewById(R.id.btnAgregar);
        btn_Volver = findViewById(R.id.btnVolver);

        //Asigna listener a los botones
        //Agregar empleo
        btn_Agregar.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onClick(View v) {

                String titulo = editTextTitulo.getText().toString();
                String descripcion = editTextDescripcion.getText().toString();
                String tipo = editTextTipo.getText().toString();
                String ubicacion = editTextUbicacion.getText().toString();

                Map<String, Object> empleo = new HashMap<>();
                empleo.put("id", UUID.randomUUID().toString());
                empleo.put("titulo", titulo);
                empleo.put("descripcion", descripcion);
                empleo.put("tipo", tipo);
                empleo.put("ubicacion", ubicacion);
                empleo.put("fecha", new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()));

                empleo.put("imagen", "https://play-lh.googleusercontent.com/Gs6kFTfe9wy0kp3RvMMhCEejwohHaVUEaY9mda3aweBM9S6BLjLo7Nu4uTNNDN9gPfk=w240-h480-rw");
                agregarEmpleo(empleo);
                Toast.makeText(addActivity.this, "Empleo agregado correctamente", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(addActivity.this, AuthActivity.class);
                startActivity(intent);
            }
        });

        btn_Volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(addActivity.this, AuthActivity.class);
                startActivity(intent);
            }
        });
    }

    //Agrega un empleo a la BD
    private void agregarEmpleo(Map<String, Object> empleo){
        db.collection("empleos").document((String) Objects.requireNonNull(empleo.get("id")))
                .set(empleo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }
}