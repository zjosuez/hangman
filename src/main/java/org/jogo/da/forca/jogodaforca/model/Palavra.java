package org.jogo.da.forca.jogodaforca.model;

public class Palavra {
    public Palavra(int id, String texto, String dificuldade) {
        this.dificuldade = dificuldade;
        this.texto = texto;
        this.id = id;
    }

    private int id;
    private String texto;
    private String dificuldade;

    public String getDificuldade() {
        return dificuldade;
    }

    public void setDificuldade(String dificuldade) {
        this.dificuldade = dificuldade;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
