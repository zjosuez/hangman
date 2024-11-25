package org.jogo.da.forca.jogodaforca.model.database;

public class InitializeDatabase {
    public static void main(String[] args) {
        String createUsuarioTable = """
            CREATE TABLE IF NOT EXISTS Usuario (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT NOT NULL,
                pontuacao_total INTEGER DEFAULT 0
            );
        """;

        String createPalavraTable = """
            CREATE TABLE IF NOT EXISTS Palavra (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                texto TEXT NOT NULL,
                dificuldade TEXT NOT NULL CHECK(dificuldade IN ('fácil', 'médio', 'difícil'))
            );
        """;

        String createPartidaTable = """
            CREATE TABLE IF NOT EXISTS Partida (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                usuario_id INTEGER NOT NULL,
                palavra_id INTEGER NOT NULL,
                tentativas_restantes INTEGER NOT NULL,
                vitoria BOOLEAN NOT NULL,
                FOREIGN KEY (usuario_id) REFERENCES Usuario(id),
                FOREIGN KEY (palavra_id) REFERENCES Palavra(id)
            );
        """;

        // Executar os comandos SQL
        DatabaseConnection.executeSQL(createUsuarioTable);
        DatabaseConnection.executeSQL(createPalavraTable);
        DatabaseConnection.executeSQL(createPartidaTable);
    }
}