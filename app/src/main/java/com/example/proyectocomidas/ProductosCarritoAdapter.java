package com.example.proyectocomidas;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ProductosCarritoAdapter extends RecyclerView.Adapter<ProductosCarritoAdapter.ViewHolderCestaCarrito> {


    public class ViewHolderCestaCarrito extends RecyclerView.ViewHolder{

        ImageView imgProduct;
        TextView tvName;
        Button btnDelete;

        public ViewHolderCestaCarrito(View itemView){
            super(itemView);

            imgProduct = itemView.findViewById(R.id.imagenProductoCesta);
            tvName = itemView.findViewById(R.id.nombreProductoCesta);
            btnDelete = itemView.findViewById(R.id.btnEliminarProductoCesta);
        }
    }

    private Context context;
    private List<Producto> products;
    private FirebaseStorage mStorage;
    private FirebaseAuth mAtuh;

    final long ONE_MEGABYTE = 1024 * 1024;

    public ProductosCarritoAdapter(Context context, List<Producto> products, FirebaseStorage mStorage, FirebaseAuth mAuth){
        this.context = context;
        this.products = products;
        this.mStorage = mStorage;
        this.mAtuh = mAuth;
    }

    @NonNull
    @Override
    public ProductosCarritoAdapter.ViewHolderCestaCarrito onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_producto_cesta, viewGroup, false);
        final ViewHolderCestaCarrito vhcc = new ViewHolderCestaCarrito(view);

        return vhcc;
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductosCarritoAdapter.ViewHolderCestaCarrito viewHolderCestaCarrito, final int i) {

        viewHolderCestaCarrito.tvName.setText(products.get(i).getNombre());

        String image = products.get(i).getImagen();
        mStorage.getReference().child(image).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                viewHolderCestaCarrito.imgProduct.setImageBitmap(Bitmap.createScaledBitmap(bmp, viewHolderCestaCarrito.imgProduct.getWidth(), viewHolderCestaCarrito.imgProduct.getHeight(), false));
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

}
