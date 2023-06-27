package com.example.proconnect;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.DragEvent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.example.proconnect.placeholder.PlaceholderContent;
import com.example.proconnect.databinding.FragmentItemDetailBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;


/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListFragment}
 * in two-pane mode (on larger screen devices) or self-contained
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The placeholder content this fragment is presenting.
     */
    private FirebaseFirestore db;
    private PlaceholderContent.PlaceholderItem mItem;
    private CollapsingToolbarLayout mToolbarLayout;
    private TextView TextViewDescripcion;
    private TextView TextViewUbicacion;
    private TextView TextViewTipo;
    private ImageView ImageViewImagen;
    private String id;
    private String titulo;
    private String descripcion;
    private String ubicacion;
    private String tipo;
    private String imagen;
    private Button btnEliminar;
    private Button btnEditar;


    private final View.OnDragListener dragListener = (v, event) -> {
        if (event.getAction() == DragEvent.ACTION_DROP) {
            ClipData.Item clipDataItem = event.getClipData().getItemAt(0);
            mItem = PlaceholderContent.ITEM_MAP.get(clipDataItem.getText().toString());
            updateContent();
        }
        return true;
    };
    private FragmentItemDetailBinding binding;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the placeholder content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = PlaceholderContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
            id = mItem.id;
            titulo = mItem.titulo;
            descripcion = mItem.descripcion;
            ubicacion = mItem.ubicacion;
            tipo = mItem.tipo;
            imagen = mItem.imagen;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        db = FirebaseFirestore.getInstance();
        binding = FragmentItemDetailBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        mToolbarLayout = rootView.findViewById(R.id.toolbar_layout);
        TextViewDescripcion = binding.itemDetail;
        TextViewUbicacion = binding.ubicacion;
        TextViewTipo = binding.tipo;
        ImageViewImagen = binding.imagen;

        btnEliminar = rootView.findViewById(R.id.btnEliminar);
        btnEditar = rootView.findViewById(R.id.btnEditar);

        ImageView imageView;
        imageView = rootView.findViewById(R.id.imagen);


        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog();
            }
        });

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editar();
            }
        });

        // Show the placeholder content as text in a TextView & in the toolbar if available.
        updateContent();
        rootView.setOnDragListener(dragListener);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void updateContent() {
        if (mItem != null) {
            Picasso.get().load(mItem.imagen).into(ImageViewImagen);
            TextViewDescripcion.setText(mItem.descripcion);
            TextViewUbicacion.setText(mItem.ubicacion);
            TextViewTipo.setText(mItem.tipo);
            if (mToolbarLayout != null) {
                mToolbarLayout.setTitle(mItem.titulo);
            }
        }
    }

    private void editar() {
        Intent intent = new Intent(getActivity(), editActivity.class);
        intent.putExtra("idEmpleo", id);
        intent.putExtra("titulo", titulo);
        intent.putExtra("descripcion", descripcion);
        intent.putExtra("ubicacion", ubicacion);
        intent.putExtra("tipo", tipo);
        startActivity(intent);
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Eliminar objeto");
        builder.setMessage("¿Estás seguro de que deseas eliminar este objeto?");

        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Aquí puedes realizar la acción de eliminación del objeto
                CollectionReference collectionRef = db.collection("empleos"); // Reemplaza con el nombre de tu colección
                DocumentReference documentRef = collectionRef.document(id);

                // Elimina el documento
                documentRef.delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // El documento se eliminó correctamente
                                Toast.makeText(requireContext(), "El objeto se eliminó correctamente", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity(), AuthActivity.class);
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Ocurrió un error al eliminar el documento
                                Toast.makeText(requireContext(), "Error al eliminar el objeto", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }





































}