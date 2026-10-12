package com.example.testapi;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CategoriaApiService {

    @GET("api/categorias")
    Call<List<Categoria>> getCategorias();

    @GET("api/categorias/{id}")
    Call<Categoria> getCategoria(@Path("id") int id);

    @POST("api/categorias")
    Call<Categoria> crearCategoria(@Body Categoria categoria);

    @PUT("api/categorias/{id}")
    Call<Categoria> actualizarCategoria(@Path("id") int id, @Body Categoria categoria);

    @DELETE("api/categorias/{id}")
    Call<Void> eliminarCategoria(@Path("id") int id);
}
