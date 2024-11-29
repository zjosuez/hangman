package org.jogo.da.forca.jogodaforca.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GameCanvasDrawer {
    private final Canvas canvas;

    public GameCanvasDrawer(Canvas canvas) {
        this.canvas = canvas;
    }

    public void desenharForca() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);

        gc.strokeLine(50, 350, 250, 350); // Base
        gc.strokeLine(150, 350, 150, 50); // Pilar
        gc.strokeLine(150, 50, 220, 50);  // Topo
        gc.strokeLine(220, 50, 220, 100); // Corda
    }

    public void atualizarBoneco(int erros) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        switch (erros) {
            case 1 -> desenharCabeca(gc);
            case 2 -> desenharCorpo(gc);
            case 3 -> desenharBracoEsquerdo(gc);
            case 4 -> desenharBracoDireito(gc);
            case 5 -> desenharPernaEsquerda(gc);
            case 6 -> desenharPernaDireita(gc);
        }
    }

    public void limparCanvas() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }


    private void desenharCabeca(GraphicsContext gc) {
        gc.strokeOval(200, 100, 40, 40);
    }

    private void desenharCorpo(GraphicsContext gc) {
        gc.strokeLine(220, 140, 220, 240);
    }

    private void desenharBracoEsquerdo(GraphicsContext gc) {
        gc.strokeLine(220, 160, 190, 200);
    }

    private void desenharBracoDireito(GraphicsContext gc) {
        gc.strokeLine(220, 160, 250, 200);
    }

    private void desenharPernaEsquerda(GraphicsContext gc) {
        gc.strokeLine(220, 240, 190, 300);
    }

    private void desenharPernaDireita(GraphicsContext gc) {
        gc.strokeLine(220, 240, 250, 300);
    }
}

