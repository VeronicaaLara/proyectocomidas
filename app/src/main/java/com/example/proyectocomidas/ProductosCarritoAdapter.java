package com.example.proyectocomidas;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ProductosCarritoAdapter extends ArrayAdapter<Producto> {

    LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    SharedPreferences preferences;


    public ProductosCarritoAdapter(@NonNull Context context, ArrayList<Producto> productos){
        super(context, 0, productos);
        preferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(null == convertView){
            convertView = inflater.inflate(R.layout.item_producto_cesta,parent, false);
        }

        ImageView imagenProducto = convertView.findViewById(R.id.imagenProductoCesta);
        TextView nombreProducto = convertView.findViewById(R.id.nombreProductoCesta);
        Button btnEliminarProductoCesta = convertView.findViewById(R.id.btnEliminarProductoCesta);

        final Producto producto = getItem(position);

        imagenProducto.setImageResource(R.drawable.hamburguesa);
        nombreProducto.setText(producto.getNombre());

        btnEliminarProductoCesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String json = preferences.getString("productos", "");
                SharedPreferences.Editor editor = preferences.edit();

                ProductosCompra productos = new ProductosCompra();

                if(! json.equals("")) {
                    productos = new ProductosCompra(productos.fromJSON(json).getListaProductos());
                }

                remove(getItem(position));
                notifyDataSetChanged();

                if(productos.getListaProductos().size() > 0){
                    productos.eliminarProducto(position);
                    Log.i("PRUEBA", productos.toString());
                    json = productos.toJson();
                    editor.putString("productos", json);
                    editor.apply();
                }
            }
        });

        return convertView;
    }

}
