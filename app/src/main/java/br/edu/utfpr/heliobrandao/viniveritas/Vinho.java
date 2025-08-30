package br.edu.utfpr.heliobrandao.viniveritas;

public class Vinho {

    public enum Tipo {
        TINTO,
        BRANCO,
        ROSE,
        ESPUMANTE
    }

    private String nome;
    private int ano;
    private Tipo tipo;
    private String uva;
    private String vinicola;

    public Vinho(String nome, int ano, Tipo tipo, String uva, String vinicola) {
        this.nome = nome;
        this.ano = ano;
        this.tipo = tipo;
        this.uva = uva;
        this.vinicola = vinicola;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public String getUva() {
        return uva;
    }

    public void setUva(String uva) {
        this.uva = uva;
    }

    public String getVinicola() {
        return vinicola;
    }

    public void setVinicola(String vinicola) {
        this.vinicola = vinicola;
    }

    @Override
    public String toString() {
        return getNome() + " (" + getAno() + ")";
    }
}

