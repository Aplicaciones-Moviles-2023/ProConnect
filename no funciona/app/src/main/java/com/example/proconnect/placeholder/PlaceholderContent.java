package com.example.proconnect.placeholder;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class PlaceholderContent {
    private FirebaseFirestore db;

    public static final List<PlaceholderItem> ITEMS = new ArrayList<>();
    public static final Map<String, PlaceholderItem> ITEM_MAP = new HashMap<>();
    private static final int COUNT = 25;

    static {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        try {
            QuerySnapshot querySnapshot = Tasks.await(db.collection("empleos").get());

            for (QueryDocumentSnapshot document : querySnapshot) {
                Map<String, Object> elemento = document.getData();
                addItem(elemento);
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static PlaceholderItem createPlaceholderItem(String id, String titulo, String descripcion, String tipo, String fecha, String ubicacion) {
        return new PlaceholderItem(id, titulo, descripcion, fecha, tipo, ubicacion);
    }

    private static void addItem(Map<String, Object> elemento) {
        String id = (String) elemento.get("id");
        String titulo = (String) elemento.get("titulo");
        String descripcion = (String) elemento.get("descripcion");
        String tipo = (String) elemento.get("tipo");
        String fecha = (String) elemento.get("fecha");
        String ubicacion = (String) elemento.get("ubicacion");

        PlaceholderItem item = new PlaceholderItem(id, titulo, descripcion, fecha, tipo, ubicacion);
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

    public static class PlaceholderItem {
        public final String id;
        public final String titulo;
        public final String descripcion;
        public final String fecha;
        public final String tipo;
        public final String ubicacion;

        public PlaceholderItem(String id, String titulo, String descripcion, String fecha, String tipo, String ubicacion) {
            this.id = id;
            this.titulo = titulo;
            this.descripcion = descripcion;
            this.fecha = fecha;
            this.tipo = tipo;
            this.ubicacion = ubicacion;
        }

        @NonNull
        @Override
        public String toString() {
            return "hola";
        }
    }
}
