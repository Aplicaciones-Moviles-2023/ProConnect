package com.example.proconnect.placeholder;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class PlaceholderContent {

    private FirebaseFirestore db;

    /**
     * An array of sample (placeholder) items.
     */
    public static final List<PlaceholderItem> ITEMS = new ArrayList<PlaceholderItem>();

    /**
     * A map of sample (placeholder) items, by ID.
     */
    public static final Map<String, PlaceholderItem> ITEM_MAP = new HashMap<String, PlaceholderItem>();

    private static final int COUNT = 25;

    static {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("empleos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> elemento = document.getData();
                                addItem(elemento);
                            }

                            //mostrarDatos(task.getResult());
                        }
                    }
                });

        // Add some sample items.
        //for (int i = 1; i <= COUNT; i++) {
        //    addItem(createPlaceholderItem(i));
        //}
    }

    //Se obtienen los datos y se hace algo con ellos
    //private static void mostrarDatos(QuerySnapshot datos){
    //    for (QueryDocumentSnapshot document : datos) {
    //        Object elemento = document.getData();
    //    }
    //}

    private static PlaceholderItem createPlaceholderItem(String id,String titulo,String descripcion,String tipo,String fecha, String ubicacion, String imagen) {
        return new PlaceholderItem(id,titulo,descripcion,fecha,tipo,ubicacion, imagen);
    }

    public static void addItem(Map<String, Object> elemento) {
        String id = (String) elemento.get("id");
        String titulo = (String) elemento.get("titulo");
        String descripcion = (String) elemento.get("descripcion");
        String tipo = (String) elemento.get("tipo");
        String fecha = (String) elemento.get("fecha");
        String ubicacion = (String) elemento.get("ubicacion");
        String imagen = (String) elemento.get("imagen");

        PlaceholderItem item = new PlaceholderItem(id, titulo, descripcion, fecha, tipo, ubicacion, imagen);

        // Agregamos el nuevo empleo a la lista
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A placeholder item representing a piece of content.
     */
    public static class PlaceholderItem {
        public final String id;
        public final String titulo;
        public final String descripcion;
        public final String fecha;
        public final String tipo;
        public final String ubicacion;
        public final String imagen;

        public PlaceholderItem(String id, String titulo, String descripcion, String fecha, String tipo, String ubicacion, String imagen) {
            this.id = id;
            this.titulo = titulo;
            this.descripcion = descripcion;
            this.fecha = fecha;
            this.tipo = tipo;
            this.ubicacion = ubicacion;
            this.imagen = imagen;
        }

        @NonNull
        @Override
        public String toString() {
            return "hola";
        }
    }
}