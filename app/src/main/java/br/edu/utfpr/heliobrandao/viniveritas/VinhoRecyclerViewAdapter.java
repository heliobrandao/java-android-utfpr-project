package br.edu.utfpr.heliobrandao.viniveritas;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VinhoRecyclerViewAdapter extends RecyclerView.Adapter<VinhoRecyclerViewAdapter.VinhoHolder> {

    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    private OnCreateContextMenu onCreateContextMenu;
    private OnContextMenuClickListener onContextMenuClickListener;
    private Context context;

    private List<Vinho> listaVinhos;

    interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    interface OnCreateContextMenu {
        void onCreateContextMenu  (ContextMenu menu,
                                    View v,
                                    ContextMenu.ContextMenuInfo menuInfo,
                                    int position,
                                    MenuItem.OnMenuItemClickListener onMenuItemClickListener);
    }

    interface OnContextMenuClickListener {
        boolean onContextMenuItemClick(MenuItem item, int position);
    }

    public class VinhoHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
                                                                        View.OnLongClickListener,
                                                                        View.OnCreateContextMenuListener {
        public TextView textViewNome;
        public TextView textViewSafra;
        public TextView textViewPais;
        public TextView textViewTipo;
        public TextView textViewCaracteristicas;
        public TextView textViewComentarios;

        public VinhoHolder(@NonNull View itemView) {
            super(itemView);

            textViewNome = itemView.findViewById(R.id.textViewNome);
            textViewSafra = itemView.findViewById(R.id.textViewSafra);
            textViewPais = itemView.findViewById(R.id.textViewPais);
            textViewTipo = itemView.findViewById(R.id.textViewTipo);
            textViewCaracteristicas = itemView.findViewById(R.id.textViewCaracteristicas);
            textViewComentarios = itemView.findViewById(R.id.textViewComentarios);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {

            if (onItemClickListener != null){
                onItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {

            if (onItemLongClickListener != null){
                onItemLongClickListener.onItemLongClick(v, getAdapterPosition());
                return true;
                }
            return false;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            if(onCreateContextMenu != null){
                onCreateContextMenu.onCreateContextMenu(menu,
                                                        v,
                                                        menuInfo,
                                                        getAdapterPosition(),
                                                        onMenuItemClickListener);
            }
        }
        MenuItem.OnMenuItemClickListener onMenuItemClickListener = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                if(onContextMenuClickListener != null){
                    onContextMenuClickListener.onContextMenuItemClick(item, getAdapterPosition());
                    return true;
                }
                return false;
            }
        };

    }

    public VinhoRecyclerViewAdapter(Context context, List<Vinho> listaVinhos) {
        this.context = context;
        this.listaVinhos = listaVinhos;
    }

    @NonNull
    @Override
    public VinhoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.linha_lista_vinhos, parent, false);

        return new VinhoHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull VinhoHolder holder, int position) {

        Vinho vinho = listaVinhos.get(position);

        holder.textViewNome.setText(vinho.getNome());
        holder.textViewSafra.setText(String.valueOf(vinho.getSafra()));
        holder.textViewPais.setText(vinho.getPais());
        holder.textViewTipo.setText(vinho.getTipo().name());
        holder.textViewCaracteristicas.setText("Características: " + vinho.getCaracteristicasTexto());
        holder.textViewComentarios.setText(vinho.getComentarios());
    }

    /* Importante não esquecer de retornar a quantidade de elementos na lista */
    @Override
    public int getItemCount() {
        return listaVinhos.size();
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public OnItemLongClickListener getOnItemLongClickListener() {
        return onItemLongClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public OnCreateContextMenu getOnCreateContextMenu() {
        return onCreateContextMenu;
    }

    public void setOnCreateContextMenu(OnCreateContextMenu onCreateContextMenu) {
        this.onCreateContextMenu = onCreateContextMenu;
    }

    public OnContextMenuClickListener getOnContextMenuClickListener() {
        return onContextMenuClickListener;
    }

    public void setOnContextMenuClickListener(OnContextMenuClickListener onContextMenuClickListener) {
        this.onContextMenuClickListener = onContextMenuClickListener;
    }
}
