package com.example.proyectocomidas.models;

public class Categoria {

    private String name;
    private String urlFoto;


    public Categoria(String name, String urlFoto) {
        this.name = name;
        this.urlFoto = urlFoto;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    @Override
    public String toString() {
        return "Categoria{" +
                "name='" + name + '\'' +
                ", urlFoto='" + urlFoto + '\'' +
                '}';
    }
}
