package com.example.proyectocomidas;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectocomidas.adapters.CategoriaAdapter;
import com.example.proyectocomidas.models.Categoria;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ViewHolderProduct> {

    public class ViewHolderProduct extends RecyclerView.ViewHolder{

        ImageView imgProduct;
        TextView tvName;
        Button btnAdd;

        public ViewHolderProduct(View itemView){
            super(itemView);

            imgProduct = itemView.findViewById(R.id.imagenProducto);
            tvName = itemView.findViewById(R.id.nombreProducto);
            btnAdd = itemView.findViewById(R.id.btnAñadir);

        }
    }

    private Context context;
    private List<Producto> products;
    private FirebaseStorage mStorage;
    private FirebaseAuth mAtuh;


    final long ONE_MEGABYTE = 1024 * 1024;

    public ProductoAdapter(Context context, List<Producto> products, FirebaseStorage mStorage, FirebaseAuth mAuth){
        this.context = context;
        this.products = products;
        this.mStorage = mStorage;
        this.mAtuh = mAuth;
        mAuth.signInAnonymously();
    }

    @NonNull
    @Override
    public ViewHolderProduct onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_producto, viewGroup, false);
        final ViewHolderProduct vhp = new ViewHolderProduct(view);

        return vhp;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolderProduct viewHolderProduct, final int i) {


        viewHolderProduct.tvName.setText(products.get(i).getNombre());

        String image = products.get(i).getImagen();
        mStorage.getReference().child(image).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                viewHolderProduct.imgProduct.setImageBitmap(Bitmap.createScaledBitmap(bmp, viewHolderProduct.imgProduct.getWidth(), viewHolderProduct.imgProduct.getHeight(), false));
            }
        });

        viewHolderProduct.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("PRODUCT", products.get(i).toString());
            }
        });


        // Si un producto no esta disponible, deshabilito el boton de añadir, cambio el color al nombre y dejo la imagen tal cual.

        if(!products.get(i).getDisponible()){

            //viewHolderProduct.tvName.setEnabled(false);

            viewHolderProduct.tvName.setTextColor(Color.parseColor("#F44336"));

            viewHolderProduct.btnAdd.setEnabled(false);

            viewHolderProduct.btnAdd.setBackgroundColor(Color.parseColor("#979797"));



        }




    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}
