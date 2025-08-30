package br.edu.utfpr.heliobrandao.viniveritas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class VinhoAdapter extends ArrayAdapter<Vinho> {

    public VinhoAdapter(Context context, ArrayList<Vinho> vinhos) {
        super(context, 0, vinhos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Vinho vinho = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.linha_lista_vinhos, parent, false);
        }

        // Lookup view for data population
        TextView textViewNome = convertView.findViewById(R.id.textViewNome);
        TextView textViewTipoUva = convertView.findViewById(R.id.textViewTipoUva);
        TextView textViewVinicola = convertView.findViewById(R.id.textViewVinicola);

        // Populate the data into the template view using the data object
        textViewNome.setText(vinho.getNome() + " (" + vinho.getAno() + ")");
        textViewTipoUva.setText(getContext().getString(R.string.tipo_uva_format, vinho.getTipo().name(), vinho.getUva()));
        textViewVinicola.setText(vinho.getVinicola());

        // Return the completed view to render on screen
        return convertView;
    }
}

