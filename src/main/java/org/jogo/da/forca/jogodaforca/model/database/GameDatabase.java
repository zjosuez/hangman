package org.jogo.da.forca.jogodaforca.model.database;

import java.sql.*;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    // Buscar pontuação atual de um usuário
    public int buscarPontuacao(String nome) {
        String sql = "SELECT pontuacao_total FROM usuario WHERE nome = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("pontuacao_total");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar pontuação: " + e.getMessage());
        }
        return 0; // Caso o usuário não exista, retorna 0
    }

    // Listar usuários e suas pontuações
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

    // Buscar ranking dos 10 melhores usuários
    public List<Map.Entry<String, Integer>> buscarRanking() {
        List<Map.Entry<String, Integer>> ranking = new ArrayList<>();
        String sql = "SELECT nome, SUM(pontuacao_total) AS pontuacao_agrupada " +
                "FROM usuario " +
                "GROUP BY nome " +
                "ORDER BY pontuacao_agrupada DESC LIMIT 10";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ranking.add(new AbstractMap.SimpleEntry<>(rs.getString("nome"), rs.getInt("pontuacao_agrupada")));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar ranking: " + e.getMessage());
        }

        return ranking;
    }

}



