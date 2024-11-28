package controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.jogo.da.forca.jogodaforca.controller.GameDatabaseController;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.*;
import java.util.*;

class GameDatabaseControllerTest {

    private GameDatabaseController gameDatabaseController;

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private Statement mockStatement;

    @Mock
    private ResultSet mockResultSet;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);

        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockConnection.createStatement()).thenReturn(mockStatement);

        gameDatabaseController = new GameDatabaseController() {
            @Override
            public Connection connect() {
                return mockConnection;
            }
        };
    }

    @Test
    void testInserirUsuario() throws SQLException {
        boolean result = gameDatabaseController.inserirUsuario("novoUsuario");

        assertTrue(result, "Usuário deveria ser inserido com sucesso");

        verify(mockPreparedStatement).setString(1, "novoUsuario");
        verify(mockPreparedStatement).executeUpdate();
    }



    @Test
    void testBuscarPalavraAleatoria() throws SQLException {
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getString("texto")).thenReturn("forca");

        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);

        String palavra = gameDatabaseController.buscarPalavraAleatoria();

        assertEquals("forca", palavra, "A palavra retornada deve ser 'forca'");
    }


    @Test
    void testAtualizarPontuacao() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        boolean result = gameDatabaseController.atualizarPontuacao("usuario", 10);

        assertTrue(result, "A pontuação deveria ser atualizada com sucesso");

        verify(mockPreparedStatement).setInt(1, 10);
        verify(mockPreparedStatement).setString(2, "usuario");
        verify(mockPreparedStatement).executeUpdate();
    }



    @Test
    void testBuscarPontuacao() throws SQLException {
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getInt("pontuacao_total")).thenReturn(100);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        int pontuacao = gameDatabaseController.buscarPontuacao("usuario");

        assertEquals(100, pontuacao, "A pontuação retornada deve ser 100");
    }


    @Test
    void testListarUsuarios() throws SQLException {
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false); // Simula um usuário retornado
        when(mockResultSet.getString("nome")).thenReturn("usuario");
        when(mockResultSet.getInt("pontuacao_total")).thenReturn(100);

        List<String> usuarios = gameDatabaseController.listarUsuarios();

        assertNotNull(usuarios, "A lista de usuários não pode ser nula");
        assertEquals(1, usuarios.size(), "Deve retornar 1 usuário");
        assertTrue(usuarios.get(0).contains("usuario"), "A lista de usuários deve conter o nome do usuário");
    }

    @Test
    void testBuscarRanking() throws SQLException {
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);  // Apenas 1 entrada
        when(mockResultSet.getString("nome")).thenReturn("usuario");
        when(mockResultSet.getInt("pontuacao_agrupada")).thenReturn(100);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        List<Map.Entry<String, Integer>> ranking = gameDatabaseController.buscarRanking();

        assertEquals(1, ranking.size(), "O ranking deve conter 1 entrada");
    }

}

