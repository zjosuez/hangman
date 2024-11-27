package org.jogo.da.forca.jogodaforca.controller;

import java.util.*;

public class GameController {
    private GameDatabaseController database;
    private String palavraAtual;
    private StringBuilder progresso;
    private int erros;
    private final int maxErros = 6;
    private Set<String> letrasUsadas;
    private List<String> letrasErradas;
    private String nomeUsuario;

    public GameController() {
        database = new GameDatabaseController();
        erros = 0;
        letrasUsadas = new HashSet<>();
        letrasErradas = new ArrayList<>();
    }

    public boolean registrarUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
        return database.inserirUsuario(nomeUsuario);
    }

    public String getProgresso() {
        return progresso.toString().trim();
    }

    public String iniciarJogo() {
        palavraAtual = database.buscarPalavraAleatoria();
        if (palavraAtual != null) {
            progresso = new StringBuilder("_ ".repeat(palavraAtual.length()));
            return progresso.toString().trim();
        }
        return null;
    }

    public String enviarLetra(String letra) {
        if (palavraAtual == null) return null;

        // Verifica se a letra já foi usada
        if (letrasUsadas.contains(letra)) {
            return "Letra já utilizada!";
        }
        letrasUsadas.add(letra);

        boolean acertou = false;

        for (int i = 0; i < palavraAtual.length(); i++) {
            if (palavraAtual.charAt(i) == letra.charAt(0)) {
                progresso.setCharAt(i * 2, letra.charAt(0));
                acertou = true;
            }
        }

        if (!acertou) {
            erros++;
            letrasErradas.add(letra);
        }

        return progresso.toString().trim();
    }

    public List<String> getLetrasErradas() {
        return letrasErradas;
    }

    public boolean verificarVitoria() {
        if (progresso.toString().replace(" ", "").equalsIgnoreCase(palavraAtual)) {
            atualizarPontuacao(nomeUsuario, 10); // Adiciona 10 pontos para cada vitória
            return true;
        }
        return false;
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
        letrasUsadas.clear();
        letrasErradas.clear();
    }

    public List<Map.Entry<String, Integer>> buscarRanking() {
        return database.buscarRanking();
    }

    private void atualizarPontuacao(String nomeUsuario, int pontos) {
        int pontuacaoAtual = database.buscarPontuacao(nomeUsuario);
        database.atualizarPontuacao(nomeUsuario, pontuacaoAtual + pontos);
    }
}



