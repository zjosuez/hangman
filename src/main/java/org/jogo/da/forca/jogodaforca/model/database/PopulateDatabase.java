package org.jogo.da.forca.jogodaforca.model.database;

import java.util.Arrays;
import java.util.List;

public class PopulateDatabase {
    public static void main(String[] args) {
        List<String> palavrasFaceis = Arrays.asList("bola", "gato", "casa", "pato", "flor");
        List<String> palavrasMedias = Arrays.asList("tigre", "banho", "fruta", "bravo", "livro");
        List<String> palavrasDificeis = Arrays.asList("dinamite", "absurdo", "montanha", "esqueleto", "arquipélago");

        inserirPalavras(palavrasFaceis, "fácil");
        inserirPalavras(palavrasMedias, "médio");
        inserirPalavras(palavrasDificeis, "difícil");
    }

    private static void inserirPalavras(List<String> palavras, String dificuldade) {
        for (String palavra : palavras) {
            String insertSQL = String.format(
                    "INSERT INTO Palavra (texto, dificuldade) VALUES ('%s', '%s');",
                    palavra, dificuldade
            );
            DatabaseConnection.executeSQL(insertSQL);
        }
    }
}
