package org.jogo.da.forca.jogodaforca.model.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameDatabase {
    private static final String URL = "jdbc:sqlite:forca.db";

    // Conexão ao banco de dados
    private Connection connect() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco de dados: " + e.getMessage());
            return null;
        }
    }

    // Inserir um novo usuário
    public boolean inserirUsuario(String nome) {
        String sql = "INSERT INTO usuario (nome, pontuacao_total) VALUES (?, 0)";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao inserir usuário: " + e.getMessage());
            return false;
        }
    }

    // Buscar uma palavra aleatória
    public String buscarPalavraAleatoria() {
        String sql = "SELECT texto FROM palavra ORDER BY RANDOM() LIMIT 1";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getString("texto");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar palavra: " + e.getMessage());
        }
        return null; // Caso não encontre nenhuma palavra
    }

    // Atualizar pontuação de um usuário
    public boolean atualizarPontuacao(String nome, int pontos) {
        String sql = "UPDATE usuario SET pontuacao_total = pontuacao_total + ? WHERE nome = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pontos);
            stmt.setString(2, nome);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar pontuação: " + e.getMessage());
            return false;
        }
    }

    // Listar usuários para validação
    public List<String> listarUsuarios() {
        String sql = "SELECT nome, pontuacao_total FROM usuario ORDER BY pontuacao_total DESC";
        List<String> usuarios = new ArrayList<>();
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                usuarios.add(rs.getString("nome") + " - Pontuação: " + rs.getInt("pontuacao_total"));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar usuários: " + e.getMessage());
        }
        return usuarios;
    }
}


