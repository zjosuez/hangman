package org.jogo.da.forca.jogodaforca.controller;

import org.jogo.da.forca.jogodaforca.model.database.GameDatabase;

public class GameController {
    private GameDatabase database;
    private String palavraAtual;
    private StringBuilder progresso;
    private int erros;
    private final int maxErros = 6;

    public GameController() {
        database = new GameDatabase();
        erros = 0;
    }

    public boolean registrarUsuario(String nomeUsuario) {
        return database.inserirUsuario(nomeUsuario);
    }

    public String iniciarJogo() {
        palavraAtual = database.buscarPalavraAleatoria();
        if (palavraAtual != null) {
            progresso = new StringBuilder("_ ".repeat(palavraAtual.length()));
            return progresso.toString().trim();
        }
        return null; // Erro ao buscar palavra
    }

    public String enviarLetra(String letra) {
        if (palavraAtual == null) return null;

        boolean acertou = false;

        for (int i = 0; i < palavraAtual.length(); i++) {
            if (palavraAtual.charAt(i) == letra.charAt(0)) {
                progresso.setCharAt(i * 2, letra.charAt(0)); // Atualiza a posição correta no progresso
                acertou = true;
            }
        }

        if (!acertou) {
            erros++;
        }

        return progresso.toString().trim();
    }

    public boolean verificarVitoria() {
        return progresso.toString().replace(" ", "").equalsIgnoreCase(palavraAtual);
    }

    public boolean verificarDerrota() {
        return erros >= maxErros;
    }

    public int getErros() {
        return erros;
    }

    public String getPalavraAtual() {
        return palavraAtual;
    }

    public void reiniciarJogo() {
        erros = 0;
        palavraAtual = null;
        progresso = null;
    }

    public void atualizarPontuacao(String nomeUsuario, int pontos) {
        database.atualizarPontuacao(nomeUsuario, pontos);
    }
}

