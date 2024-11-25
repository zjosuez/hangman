package org.jogo.da.forca.jogodaforca.model.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:forca.db";

    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco de dados: " + e.getMessage());
            return null;
        }
    }

    public static void executeSQL(String sql) {
        try (Connection conn = connect()) {
            assert conn != null;
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
                System.out.println("Comando SQL executado com sucesso.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao executar SQL: " + e.getMessage());
        }
    }
}
