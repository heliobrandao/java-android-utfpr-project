package br.edu.utfpr.heliobrandao.viniveritas.modelo;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Comparator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Entity
public class Vinho implements Cloneable{

    public static Comparator<Vinho> ordenacaoCrescente = new Comparator<Vinho>() {
        @Override
        public int compare(Vinho v1, Vinho v2) {
            return v1.getNome().compareToIgnoreCase(v2.getNome());
        }
    };

    public static Comparator<Vinho> ordenacaoDecrescente = new Comparator<Vinho>() {
        @Override
        public int compare(Vinho v1, Vinho v2) {
            return -1 * v1.getNome().compareToIgnoreCase(v2.getNome());
        }
    };
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private long id;
    @NonNull
    @ColumnInfo(index = true)
    private String nome;

    private int safra;
    private String pais;
    private Tipo tipo;
    private List<String> caracteristicas;
    private String comentarios;

    public Vinho(String nome, int safra, String pais, Tipo tipo, List<String> caracteristicas, String comentarios) {
        this.nome = nome;
        this.safra = safra;
        this.pais = pais;
        this.tipo = tipo;
        this.caracteristicas = caracteristicas != null ? caracteristicas : new ArrayList<>();
        this.comentarios = comentarios;
    }

    // Getters e Setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getSafra() {
        return safra;
    }

    public void setSafra(int safra) {
        this.safra = safra;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public List<String> getCaracteristicas() {
        return caracteristicas;
    }

    public void setCaracteristicas(List<String> caracteristicas) {
        this.caracteristicas = caracteristicas != null ? caracteristicas : new ArrayList<>();
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public String getCaracteristicasTexto() {
        if (caracteristicas == null || caracteristicas.isEmpty()) {
            return "Nenhuma característica";
        }
        return String.join(", ", caracteristicas);
    }

    @NonNull
    @Override
    public Object clone() throws CloneNotSupportedException {

        return super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Vinho vinho = (Vinho) o;
        return safra == vinho.safra &&
                nome == vinho.nome &&
                pais == vinho.pais &&
                tipo == vinho.tipo &&
                caracteristicas == vinho.caracteristicas &&
                comentarios == vinho.comentarios;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, safra, pais, tipo, caracteristicas, comentarios);
    }

    @Override
    public String toString() {
        return getNome() + " (" + getSafra() + ")";
    }
}
