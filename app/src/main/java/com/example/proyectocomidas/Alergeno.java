package com.example.proyectocomidas;

public class Alergeno {

    private String name;
    private String id;

    public Alergeno(String name, String id) {
        this.id = id;
        this.name = name;
    }

    public String getNombre() {
        return name;
    }

    public String getId(){ return id; }

    public void setNombre(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "Allergen{" +
                "nombre='" + name + '\'' +
                '}';
    }
}
