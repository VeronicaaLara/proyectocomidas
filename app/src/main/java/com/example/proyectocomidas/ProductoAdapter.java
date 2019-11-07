package com.example.proyectocomidas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ViewHolderProduct> {

    public class ViewHolderProduct extends RecyclerView.ViewHolder{

        ImageView imgProduct;
        TextView tvName;

        public ViewHolderProduct(View itemView){
            super(itemView);

            imgProduct = itemView.findViewById(R.id.imagenProducto);
            tvName = itemView.findViewById(R.id.nombreProducto);
        }
    }

    private Context context;
    private List<Producto> products;
    private FirebaseStorage mStorage;
    final long ONE_MEGABYTE = 1024 * 1024;

    public ProductoAdapter(Context context, List<Producto> products, FirebaseStorage mStorage){
        this.context = context;
        this.products = products;
        this.mStorage = mStorage;
    }

    @NonNull
    @Override
    public ViewHolderProduct onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_producto, viewGroup, false);
        final ViewHolderProduct vhp = new ViewHolderProduct(view);

        return vhp;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderProduct viewHolderProduct, int i) {
        viewHolderProduct.tvName.setText(products.get(i).getNombre());

        String image = products.get(i).getImagen();
        mStorage.getReference().child(image).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                viewHolderProduct.imgProduct.setImageBitmap(Bitmap.createScaledBitmap(bmp, viewHolderProduct.imgProduct.getWidth(), viewHolderProduct.imgProduct.getHeight(), false));
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}
