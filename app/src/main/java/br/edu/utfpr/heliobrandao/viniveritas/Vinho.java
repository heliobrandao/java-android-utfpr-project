package br.edu.utfpr.heliobrandao.viniveritas;

import java.util.ArrayList;
import java.util.List;

public class Vinho {

    public enum Tipo {
        TINTO,
        BRANCO,
        ROSE,
        ESPUMANTE
    }

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

    @Override
    public String toString() {
        return getNome() + " (" + getSafra() + ")";
    }
}
