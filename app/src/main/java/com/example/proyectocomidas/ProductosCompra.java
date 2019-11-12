package com.example.proyectocomidas;

import com.google.gson.Gson;

import java.util.ArrayList;

public class ProductosCompra {

    private ArrayList<Producto> listaProductos;

    public ProductosCompra() {
        this.listaProductos = new ArrayList<>();
    }

    public ProductosCompra(ArrayList<Producto> placesList) {
        this.listaProductos = placesList;
    }

    public String toJson(){
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json;
    }

    public ProductosCompra fromJSON(String json){
        Gson gson = new Gson();
        return gson.fromJson(json, ProductosCompra.class);
    }

    public ArrayList getListaProductos() {
        return listaProductos;
    }

    public void añadirProducto(Producto producto){
        listaProductos.add(producto);
    }

    public void eliminarProducto(Producto place){
        listaProductos.remove(place);
    }

    public void eliminarProducto(int index){
        listaProductos.remove(index);
    }

    @Override
    public String toString() {
        return "ProductosCompra{" +
                "productos=" + listaProductos +
                '}';
    }
}
