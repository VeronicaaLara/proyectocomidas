package com.example.proyectocomidas.models;

public class UltimosPedidos {


    private String idUtimoPedido;
    private String nombre;
    private String idProducto;
    private String idPedido;


    public UltimosPedidos (String id, String nombre){

        this.idUtimoPedido = idUtimoPedido;
        this.nombre = nombre;
        this.idProducto = idProducto;
        this.idPedido = idPedido;
    }

    public String getIdUtimoPedido() {
        return idUtimoPedido;
    }

    public String getNombre() {
        return nombre;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public String getIdPedido() {
        return idPedido;
    }

    @Override
    public String toString() {
        return "UltimosPedidos{" +
                "idUtimoPedido='" + idUtimoPedido + '\'' +
                ", nombre='" + nombre + '\'' +
                ", idProducto='" + idProducto + '\'' +
                ", idPedido='" + idPedido + '\'' +
                '}';
    }
}
