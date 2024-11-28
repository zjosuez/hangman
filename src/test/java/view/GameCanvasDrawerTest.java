package view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import org.jogo.da.forca.jogodaforca.view.GameCanvasDrawer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameCanvasDrawerTest {

    private GameCanvasDrawer gameCanvasDrawer;

    @BeforeEach
    void setUp() {
        Canvas canvas = new Canvas();
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        gameCanvasDrawer = new GameCanvasDrawer(canvas);
    }

    @Test
    void testDesenharForca() {
        gameCanvasDrawer.desenharForca();
    }

    @Test
    void testAtualizarBonecoCom1Erro() {
        gameCanvasDrawer.atualizarBoneco(1);
    }

    @Test
    void testAtualizarBonecoCom2Erros() {
        gameCanvasDrawer.atualizarBoneco(2);
    }

    @Test
    void testAtualizarBonecoCom3Erros() {
        gameCanvasDrawer.atualizarBoneco(3);
    }

    @Test
    void testAtualizarBonecoCom4Erros() {
        gameCanvasDrawer.atualizarBoneco(4);
    }

    @Test
    void testAtualizarBonecoCom5Erros() {
        gameCanvasDrawer.atualizarBoneco(5);
    }

    @Test
    void testAtualizarBonecoCom6Erros() {
        gameCanvasDrawer.atualizarBoneco(6);
    }
}
