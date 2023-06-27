package com.example.proconnect;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class addActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private EditText editTextTitulo;
    private EditText editTextDescripcion;
    private EditText editTextTipo;
    private EditText editTextUbicacion;

    private Button btn_Agregar;
    private Button btn_Volver;
    private Button btn_SelectImage;

    private String urlImagen = "";


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
        btn_SelectImage = findViewById(R.id.btnSelectImage);

        //Asigna listener a los botones
        //Seleccionar imagen
        btn_SelectImage.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    int requestCode = 1;
                    startActivityForResult(intent, requestCode);
            }
        });

        //Agregar empleo
        btn_Agregar.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onClick(View v) {
                String titulo = editTextTitulo.getText().toString();
                String descripcion = editTextDescripcion.getText().toString();
                String tipo = editTextTipo.getText().toString();
                String ubicacion = editTextUbicacion.getText().toString();

                Boolean validacionesAdd = true;
                String mensaje = "";

                //Validaciones
                if(!selectImagen)
                {
                    validacionesAdd = false;
                    mensaje = "ERROR Debe seleccionar una imagen";
                }

                //Si se pasaron las validaciones se agrega el empleo
                if(validacionesAdd){
                    Map<String, Object> empleo = new HashMap<>();
                    empleo.put("id", UUID.randomUUID().toString());
                    empleo.put("titulo", titulo);
                    empleo.put("descripcion", descripcion);
                    empleo.put("tipo", tipo);
                    empleo.put("ubicacion", ubicacion);
                    empleo.put("fecha", new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()));

                    agregarEmpleo(empleo);

                    Intent intent = new Intent(addActivity.this, AuthActivity.class);
                    startActivity(intent);
                }
                //Si NO se pasaron las validaciones se adjunta mensaje de error
                else{
                    Toast.makeText(addActivity.this, mensaje, Toast.LENGTH_SHORT).show();
                }

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

    private Boolean selectImagen = false;
    private int requestCode;
    private int resultCode;
    private Intent data;
    private Boolean uploadImage = false;

    //Seleccionar imagen
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        selectImagen = false;
        if (requestCode == 1 && resultCode == RESULT_OK) {
            selectImagen = true;
            this.requestCode = requestCode;
            this.resultCode = resultCode;
            this.data = data;
        }
    }



    //Agrega un empleo a la BD
    private void agregarEmpleo(Map<String, Object> empleo){

        Uri imagenUri = data.getData();

        // Crear una referencia al almacenamiento en Firestore donde deseas almacenar la imagen
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("imagenes").child(imagenUri.getLastPathSegment());

        // Subir la imagen a Firebase Storage
        UploadTask uploadTask = storageRef.putFile(imagenUri);

        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Obtener la URL de descarga de la imagen
                return storageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    // La imagen se ha subido correctamente y se obtuvo la URL de descarga
                    String urlImagen = task.getResult().toString();
                    empleo.put("imagen", urlImagen);
                    uploadEmpleo(empleo);
                } else {
                    // Ocurrió un error al subir la imagen o al obtener la URL de descarga
                    Exception exception = task.getException();
                    Toast.makeText(addActivity.this, "ERROR al subir la imagen", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void uploadEmpleo(Map<String, Object> empleo)
    {
            db.collection("empleos").document((String) Objects.requireNonNull(empleo.get("id")))
                    .set(empleo)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(addActivity.this, "Empleo agregado correctamente", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "DocumentSnapshot successfully written!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(addActivity.this, "ERROR al agregar el empleo", Toast.LENGTH_SHORT).show();
                            Log.w(TAG, "Error writing document", e);
                        }
                    });
        }

}
