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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;
import java.util.Objects;

public class editActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private EditText editTextTitulo;
    private EditText editTextDescripcion;
    private EditText editTextTipo;
    private EditText editTextUbicacion;

    private Button btn_Editar;
    private Button btn_Volver;

    private String idEmpleo;
    private String titulo;
    private String descripcion;
    private String tipo;
    private String ubicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Intent intent = getIntent();
        idEmpleo = intent.getStringExtra("idEmpleo");
        titulo = intent.getStringExtra("titulo");
        descripcion = intent.getStringExtra("descripcion");
        tipo = intent.getStringExtra("tipo");
        ubicacion = intent.getStringExtra("ubicacion");

        db = FirebaseFirestore.getInstance();

        //Obtiene EditText
        editTextTitulo = findViewById(R.id.editTextTitulo);
        editTextDescripcion = findViewById(R.id.editTextDescripcion);
        editTextTipo = findViewById(R.id.editTextTipo);
        editTextUbicacion = findViewById(R.id.editTextUbicacion);

        //Setea textos en EditText
        editTextTitulo.setText(titulo);
        editTextDescripcion.setText(descripcion);
        editTextTipo.setText(tipo);
        editTextUbicacion.setText(ubicacion);

        //Obtiene botones
        btn_Editar = findViewById(R.id.btnEditar);
        btn_Volver = findViewById(R.id.btnVolver);

        //Asigna listener a los botones
        //Agregar empleo
        btn_Editar.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onClick(View v) {

                String titulo = editTextTitulo.getText().toString();
                String descripcion = editTextDescripcion.getText().toString();
                String tipo = editTextTipo.getText().toString();
                String ubicacion = editTextUbicacion.getText().toString();

                CollectionReference collectionRef = db.collection("empleos");
                DocumentReference documentRef = collectionRef.document(idEmpleo);

                documentRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {

                                // El documento existe
                                documentRef.update("titulo", titulo);
                                documentRef.update("descripcion", descripcion);
                                documentRef.update("ubicacion", ubicacion);
                                documentRef.update("tipo", tipo);

                            } else {
                                // El documento no existe
                            }
                        } else {
                            // Error al obtener el documento
                        }
                    }
                });
                
                Toast.makeText(editActivity.this, "Empleo editado correctamente", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(editActivity.this, AuthActivity.class);
                startActivity(intent);
            }
        });

        btn_Volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(editActivity.this, AuthActivity.class);
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