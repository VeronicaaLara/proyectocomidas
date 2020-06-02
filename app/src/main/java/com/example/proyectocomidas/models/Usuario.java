package com.example.proyectocomidas.models;


public class Usuario {
    private String nombre;
    private String apellido;
    private String email;
    private String direccion;
    private String telefono;
    private String password;


    public Usuario(String nombre, String email, String direccion, String telefono, String apellido, String password) {
        this.nombre = nombre;
        this.email = email;
        this.direccion = direccion;
        this.telefono = telefono;
        this.apellido = apellido;
        this.password = password;

    }

    public Usuario(String nombre, String apellido, String email, String password) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.direccion = "";
        this.telefono = "";
        this.password = password;
    }

    public Usuario(String email, String password) {
        this.nombre = "";
        this.apellido = "";
        this.email = email;
        this.direccion = "";
        this.telefono = "";
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getApellido() { return apellido; }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }


    @Override
    public String toString() {
        return "Usuario{" +
                "nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono='" + telefono + '\'' +
                ", apellido=" + apellido +
                ", password=" + password +
                '}';
    }
}
