package com.example.testapi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder> {

    private final List<Categoria> categoriaList;
    private final OnItemClickListener listener;
    private final OnDeleteClickListener deleteListener;

    public interface OnItemClickListener {
        void onItemClick(Categoria categoria);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Categoria categoria);
    }

    public CategoriaAdapter(List<Categoria> categoriaList, OnItemClickListener listener, OnDeleteClickListener deleteListener) {
        this.categoriaList = categoriaList;
        this.listener = listener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public CategoriaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_categoria, parent, false);
        return new CategoriaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaViewHolder holder, int position) {
        Categoria categoria = categoriaList.get(position);
        holder.tvId.setText("ID: " + categoria.getId());
        holder.tvNombre.setText(categoria.getNombre());
        holder.tvDescripcion.setText(categoria.getDescripcion());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(categoria);
            }
        });

        holder.btnEliminar.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDeleteClick(categoria);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoriaList.size();
    }

    static class CategoriaViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvNombre, tvDescripcion;
        ImageView btnEliminar;

        public CategoriaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvId);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}
