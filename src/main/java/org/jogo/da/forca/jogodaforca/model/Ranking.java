package org.jogo.da.forca.jogodaforca.model;

public class Ranking {
    private String nome;
    private int pontuacao_total;

    public Ranking(String nome, int pontuacao_total) {
        this.nome = nome;
        this.pontuacao_total = pontuacao_total;
    }

    public String getNome() {
        return nome;
    }

    public int getPontuacao_total() {
        return pontuacao_total;
    }
}


