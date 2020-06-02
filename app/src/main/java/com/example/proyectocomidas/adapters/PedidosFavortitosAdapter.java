package com.example.proyectocomidas.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.proyectocomidas.CustomClickPedido;
import com.example.proyectocomidas.models.PedidoFavorito;
import com.example.proyectocomidas.R;

import java.util.List;

public class PedidosFavortitosAdapter extends RecyclerView.Adapter<PedidosFavortitosAdapter.ViewHolderOrdersFav> {

    public class ViewHolderOrdersFav extends RecyclerView.ViewHolder{

        TextView tvNameOrder;
        TextView tvCommentsOrder;

        public ViewHolderOrdersFav(View itemView){
            super(itemView);

            tvNameOrder = itemView.findViewById(R.id.nombrePedido);
            tvCommentsOrder = itemView.findViewById(R.id.observacionesPedido);
        }
    }

    private Context context;
    private List<PedidoFavorito> orders;
    private CustomClickPedido listener;

    public PedidosFavortitosAdapter(Context context, List<PedidoFavorito> orders, CustomClickPedido listener){
        this.context = context;
        this.orders = orders;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolderOrdersFav onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_pedido_favorito, viewGroup, false);
        final ViewHolderOrdersFav vhof = new ViewHolderOrdersFav(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(view, vhof.getAdapterPosition());
            }
        });

        return vhof;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderOrdersFav viewHolderOrdersFav, int i) {
        viewHolderOrdersFav.tvNameOrder.setText(orders.get(i).getNombre());
        viewHolderOrdersFav.tvCommentsOrder.setText(orders.get(i).getComments());
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }
}
