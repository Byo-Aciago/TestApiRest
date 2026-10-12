package com.example.testapi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton fabAgregar;
    private CategoriaApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerViewCategorias);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fabAgregar = findViewById(R.id.fabAgregar);

        apiService = RetrofitClient.getInstance().create(CategoriaApiService.class);

        cargarCategorias();

        fabAgregar.setOnClickListener(v -> mostrarDialogoGestion(null));
    }

    private void cargarCategorias() {
        apiService.getCategorias().enqueue(new Callback<List<Categoria>>() {
            @Override
            public void onResponse(Call<List<Categoria>> call, Response<List<Categoria>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Categoria> categorias = response.body();
                    CategoriaAdapter adapter = new CategoriaAdapter(
                            categorias,
                            categoria -> mostrarDialogoGestion(categoria),
                            categoria -> eliminarCategoria(categoria.getId())
                    );
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(MainActivity.this, "Error al cargar categorías: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Categoria>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Fallo de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void mostrarDialogoGestion(Categoria categoriaExistente) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_categoria, null);
        builder.setView(dialogView);

        TextView tvDialogTitle = dialogView.findViewById(R.id.tvDialogTitle);
        EditText etNombre = dialogView.findViewById(R.id.etNombre);
        EditText etDescripcion = dialogView.findViewById(R.id.etDescripcion);

        boolean esEdicion = (categoriaExistente != null);
        if (esEdicion) {
            tvDialogTitle.setText("Editar Categoría");
            etNombre.setText(categoriaExistente.getNombre());
            etDescripcion.setText(categoriaExistente.getDescripcion());
        } else {
            tvDialogTitle.setText("Nueva Categoría");
        }

        builder.setPositiveButton(esEdicion ? "Actualizar" : "Crear", (dialog, which) -> {
            String nombre = etNombre.getText().toString().trim();
            String descripcion = etDescripcion.getText().toString().trim();

            if (nombre.isEmpty() || descripcion.isEmpty()) {
                Toast.makeText(MainActivity.this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (esEdicion) {
                Categoria categoriaActualizada = new Categoria(categoriaExistente.getId(), nombre, descripcion);
                actualizarCategoria(categoriaExistente.getId(), categoriaActualizada);
            } else {
                Categoria nuevaCategoria = new Categoria(nombre, descripcion);
                crearCategoria(nuevaCategoria);
            }
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void crearCategoria(Categoria categoria) {
        apiService.crearCategoria(categoria).enqueue(new Callback<Categoria>() {
            @Override
            public void onResponse(Call<Categoria> call, Response<Categoria> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Categoría creada exitosamente", Toast.LENGTH_SHORT).show();
                    cargarCategorias();
                } else {
                    Toast.makeText(MainActivity.this, "Error al crear: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Categoria> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actualizarCategoria(int id, Categoria categoria) {
        apiService.actualizarCategoria(id, categoria).enqueue(new Callback<Categoria>() {
            @Override
            public void onResponse(Call<Categoria> call, Response<Categoria> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Categoría actualizada exitosamente", Toast.LENGTH_SHORT).show();
                    cargarCategorias();
                } else {
                    Toast.makeText(MainActivity.this, "Error al actualizar: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Categoria> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void eliminarCategoria(int id) {
        apiService.eliminarCategoria(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Categoría eliminada", Toast.LENGTH_SHORT).show();
                    cargarCategorias();
                } else {
                    Toast.makeText(MainActivity.this, "Error al eliminar: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
