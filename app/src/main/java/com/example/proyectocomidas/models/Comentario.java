package com.example.proyectocomidas.models;

public class Comentario {


    private String comentario;


    public Comentario(String comentario) {
        this.comentario = comentario;


    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    @Override
    public String toString() {
        return "Comentario{" +
                "comentario='" + comentario + '\'' +
                '}';
    }
}
