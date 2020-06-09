package com.example.proyectocomidas.models;

public class UltimosPedidos {


    private String idUtimoPedido;
    private String nombre;
    private String idProducto;
    private String idPedido;


    public UltimosPedidos (String id, String nombre){

        this.idUtimoPedido = idUtimoPedido;
        this.nombre = nombre;
    }

    public String getIdUtimoPedido() {
        return idUtimoPedido;
    }

    public String getNombreUltimoPedido() {
        return nombre;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
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
