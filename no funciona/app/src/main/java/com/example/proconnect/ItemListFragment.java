package com.example.proconnect;

import android.content.ClipData;
import android.content.ClipDescription;
import android.os.Build;
import android.os.Bundle;

import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proconnect.databinding.FragmentItemListBinding;
import com.example.proconnect.databinding.ItemListContentBinding;

import com.example.proconnect.placeholder.PlaceholderContent;

import java.util.List;

public class ItemListFragment extends Fragment {

    ViewCompat.OnUnhandledKeyEventListenerCompat unhandledKeyEventListenerCompat = (v, event) -> {
        if (event.getKeyCode() == KeyEvent.KEYCODE_Z && event.isCtrlPressed()) {
            Toast.makeText(
                    v.getContext(),
                    "Undo (Ctrl + Z) shortcut triggered",
                    Toast.LENGTH_LONG
            ).show();
            return true;
        } else if (event.getKeyCode() == KeyEvent.KEYCODE_F && event.isCtrlPressed()) {
            Toast.makeText(
                    v.getContext(),
                    "Find (Ctrl + F) shortcut triggered",
                    Toast.LENGTH_LONG
            ).show();
            return true;
        }
        return false;
    };

    private FragmentItemListBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentItemListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewCompat.addOnUnhandledKeyEventListener(view, unhandledKeyEventListenerCompat);

        RecyclerView recyclerView = binding.itemList;
        View itemDetailFragmentContainer = view.findViewById(R.id.item_detail_nav_container);
        setupRecyclerView(recyclerView, itemDetailFragmentContainer);
    }

    private void setupRecyclerView(RecyclerView recyclerView, View itemDetailFragmentContainer) {
        if (PlaceholderContent.ITEMS.isEmpty()) {
            Toast.makeText(
                    requireContext(),
                    "No hay datos disponibles",
                    Toast.LENGTH_SHORT
            ).show();
        } else {
            recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(
                    PlaceholderContent.ITEMS,
                    itemDetailFragmentContainer
            ));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<PlaceholderContent.PlaceholderItem> mValues;
        private final View mItemDetailFragmentContainer;

        SimpleItemRecyclerViewAdapter(List<PlaceholderContent.PlaceholderItem> items, View itemDetailFragmentContainer) {
            mValues = items;
            mItemDetailFragmentContainer = itemDetailFragmentContainer;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ItemListContentBinding binding = ItemListContentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mTituloView.setText(mValues.get(position).titulo);
            holder.mFechaView.setText(mValues.get(position).fecha);

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(itemView -> {
                PlaceholderContent.PlaceholderItem item = (PlaceholderContent.PlaceholderItem) itemView.getTag();
                if (mItemDetailFragmentContainer != null) {
                    Bundle arguments = new Bundle();
                    arguments.putString(ItemDetailFragment.ARG_ITEM_ID, item.id);
                    Navigation.findNavController(requireActivity(), R.id.item_detail_fragment)
                            .navigate(R.id.item_detail_fragment, arguments);
                } else {
                    Toast.makeText(itemView.getContext(), "Elemento seleccionado: " + item.id, Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mTituloView;
            final TextView mFechaView;

            ViewHolder(ItemListContentBinding binding) {
                super(binding.getRoot());
                mTituloView = binding.titulo;
                mFechaView = binding.fecha;
            }
        }
    }
}
