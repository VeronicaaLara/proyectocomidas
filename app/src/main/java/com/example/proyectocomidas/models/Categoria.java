package com.example.proyectocomidas.models;

public class Categoria {

    private String name;
    private String image;
    private int id;
    private String min;


    public Categoria(String name, String urlFoto, int id, String min) {
        this.name = name;
        this.image = urlFoto;
        this.id = id;

        this.min = min;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String urlFoto) {
        this.image = urlFoto;
    }

    public String getMin() { return min; }

    public void setMin(String min) { this.min = min; }

    @Override
    public String toString() {
        return "Categoria{" +
                "name='" + name + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

}
