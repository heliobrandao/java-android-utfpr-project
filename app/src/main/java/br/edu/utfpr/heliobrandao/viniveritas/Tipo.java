package br.edu.utfpr.heliobrandao.viniveritas;

import android.content.Context;

public enum Tipo {
    Tinto, // Ordinal 0
    Branco, // Ordinal 1
    Rosé, // Ordinal 2
    Espumante; // Ordinal 3

    public String getTraducao(Context context) {
        switch (this) {
            case Tinto: return context.getString(R.string.tipo_tinto);
            case Branco: return context.getString(R.string.tipo_branco);
            case Rosé: return context.getString(R.string.tipo_rose);
            case Espumante: return context.getString(R.string.tipo_espumante);
            default: return name();
        }
    }


}
