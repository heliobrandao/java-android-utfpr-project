package br.edu.utfpr.heliobrandao.viniveritas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VinhoRecyclerViewAdapter extends RecyclerView.Adapter<VinhoRecyclerViewAdapter.VinhoHolder> {

    private OnItemClickListener onItemClickListener;

    private Context context;

    private List<Vinho> listaVinhos;

    public interface OnItemClickListener {

        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public class VinhoHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

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
        }

        @Override
        public void onClick(View v) {

            if (onItemClickListener != null){

                int pos = getAdapterPosition();

                if (pos != RecyclerView.NO_POSITION){
                    onItemClickListener.onItemClick(v, pos);
                }
            }
        }

        @Override
        public boolean onLongClick(View v) {

            if (onItemClickListener != null){

                int pos = getAdapterPosition();

                if (pos != RecyclerView.NO_POSITION){
                    onItemClickListener.onItemLongClick(v, pos);
                    return true;
                }
            }
            return false;
        }
    }

    public VinhoRecyclerViewAdapter(Context context, List<Vinho> listaVinhos, OnItemClickListener listener) {
        this.context = context;
        this.listaVinhos = listaVinhos;
        this.onItemClickListener = listener;
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
}
