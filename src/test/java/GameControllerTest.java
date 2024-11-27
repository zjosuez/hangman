import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.jogo.da.forca.jogodaforca.controller.GameController;
import org.jogo.da.forca.jogodaforca.controller.GameDatabaseController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

class GameControllerTest {

    @Mock
    private GameDatabaseController mockDatabase;

    private GameController gameController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa os mocks
        gameController = new GameController();
        gameController.database = mockDatabase; // Substitui a dependência de banco de dados pelo mock
    }

    @Test
    void testRegistrarUsuario() {
        when(mockDatabase.inserirUsuario("novoUsuario")).thenReturn(true);

        boolean resultado = gameController.registrarUsuario("novoUsuario");

        assertTrue(resultado, "Usuário deveria ser registrado com sucesso");
    }

    @Test
    void testEnviarLetraCorreta() {
        when(mockDatabase.buscarPalavraAleatoria()).thenReturn("forca");
        gameController.iniciarJogo();

        String progresso = gameController.enviarLetra("f");

        assertEquals("f _ _ _ _", progresso, "A letra correta 'f' deve ser refletida no progresso");
    }

    @Test
    void testEnviarLetraErrada() {
        when(mockDatabase.buscarPalavraAleatoria()).thenReturn("forca");
        gameController.iniciarJogo();

        String progresso = gameController.enviarLetra("z");

        assertEquals("_ _ _ _ _", progresso, "A letra errada 'z' não deve alterar o progresso");
        assertTrue(gameController.getLetrasErradas().contains("z"), "A letra errada 'z' deveria ser registrada");
    }

    @Test
    void testVerificarVitoria() {
        when(mockDatabase.buscarPalavraAleatoria()).thenReturn("forca");
        gameController.iniciarJogo();

        gameController.enviarLetra("f");
        gameController.enviarLetra("o");
        gameController.enviarLetra("r");
        gameController.enviarLetra("c");
        gameController.enviarLetra("a");

        boolean vitoria = gameController.verificarVitoria();

        assertTrue(vitoria, "O jogo deveria ser vencido quando a palavra é adivinhada corretamente");
    }

    @Test
    void testVerificarDerrota() {
        when(mockDatabase.buscarPalavraAleatoria()).thenReturn("forca");
        gameController.iniciarJogo();
        gameController.enviarLetra("x");
        gameController.enviarLetra("y");
        gameController.enviarLetra("z");
        gameController.enviarLetra("w");
        gameController.enviarLetra("v");
        gameController.enviarLetra("u");

        boolean derrota = gameController.verificarDerrota();

        assertTrue(derrota, "O jogo deveria ser perdido após 6 tentativas erradas");
    }

    @Test
    void testReiniciarJogo() {
        when(mockDatabase.buscarPalavraAleatoria()).thenReturn("forca");
        gameController.iniciarJogo();

        gameController.enviarLetra("f");
        gameController.enviarLetra("o");

        gameController.reiniciarJogo();

        assertNull(gameController.getPalavraAtual(), "A palavra atual deve ser nula após reiniciar o jogo");
        assertEquals(0, gameController.getErros(), "O número de erros deve ser 0 após reiniciar");
        assertTrue(gameController.getLetrasErradas().isEmpty(), "As letras erradas devem ser limpas após reiniciar");
    }

    @Test
    void testAtualizarPontuacao() {
        when(mockDatabase.buscarPontuacao("usuario1")).thenReturn(10);
        when(mockDatabase.atualizarPontuacao("usuario1", 20)).thenReturn(true);

        gameController.atualizarPontuacao("usuario1", 10);
        verify(mockDatabase).atualizarPontuacao("usuario1", 20);
    }
}
