package org.jogo.da.forca.jogodaforca.model;

public class Usuario {
    private int id;
    private String nome;
    private int pontuacaoTotal;

    public Usuario(int id, String nome, int pontuacaoTotal) {
        this.id = id;
        this.nome = nome;
        this.pontuacaoTotal = pontuacaoTotal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getPontuacaoTotal() {
        return pontuacaoTotal;
    }

    public void setPontuacaoTotal(int pontuacaoTotal) {
        this.pontuacaoTotal = pontuacaoTotal;
    }
}