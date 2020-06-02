package com.example.proyectocomidas;

public class Constants {


    public static final String DOMAIN = "http://35.181.4.122/back_proyectocomidas/public/";
    public static final String URL_REGISTER = DOMAIN + "api/registro";
    public static final String URL_LOGIN = DOMAIN + "api/login";
    public static final String URL_ADD_ORDER = DOMAIN + "api/pedidos/nuevo";
    public static final String URL_ADD_FAV = DOMAIN + "api/pedidos/favoritos/nuevo";
    public static final String URL_REMOVE_FAV = DOMAIN + "/api/pedidos/favoritos/cancelar\n";
    public static final String URL_FAV = DOMAIN + "api/pedidos/favoritos\n";
    public static final String URL_LAST_ORDER = DOMAIN + "api/pedidos/ultimos";
    public static final String URL_CANCEL_ORDER = DOMAIN + "api/pedidos/cancelar";
    public static final String URL_PRODUCTS = DOMAIN + "api/productos";
    public static final String URL_PRODUCTS_CAT = DOMAIN + "api/productos/idCategoría";
    public static final String URL_CATEGORIES = DOMAIN + "api/categorias";
    public static final String URL_COMMENTS = DOMAIN + "api/comentarios";
    public static final String URL_ADD_COMMENTS = DOMAIN + "api/comentarios/nuevo\n";
    public static final String URL_NEW_PASS = DOMAIN + "api/generar_contrasenia";
    public static final String URL_USER_EDIT = DOMAIN + "api/usuarios/editar";
    public static final String URL_EDIT_PASS = DOMAIN + "api/usuarios/editar_contrasenia";


    public static final String PREF = "my_prefs";
    public static final String PREF_LOG = "login";
    public static final String PREF_USER = "user";
    public static final String PREF_USER_TOKEN = "user_token";
    public static final String PREF_USER_ID = "user_id";
}
