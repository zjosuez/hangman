package org.jogo.da.forca.jogodaforca.view;

import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.jogo.da.forca.jogodaforca.controller.GameController;
import org.jogo.da.forca.jogodaforca.model.database.GameDatabase;

public class GameUI {
    private BorderPane layout;
    private Label palavraLabel;
    private Label mensagemLabel;
    private TextField letraInput;
    private Canvas canvas;
    private int erros;
    private GameDatabase database;
    private String palavraAtual;
    private String nomeUsuario;
    private StringBuilder progressoPalavra;
    private GameController gameController = new GameController();

    public GameUI() {
        layout = new BorderPane();
        criarInterface();
    }

    private void criarInterface() {
        // Título
        Label tituloLabel = new Label("Jogo da Forca");
        tituloLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Painel superior (título e entrada de usuário)
        VBox topPane = new VBox(tituloLabel, criarPainelUsuario());
        topPane.setAlignment(Pos.CENTER);
        topPane.setSpacing(10);
        layout.setTop(topPane);

        // Área central (palavra e canvas para desenho)
        palavraLabel = new Label("_ _ _ _ _");
        palavraLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        palavraLabel.setAlignment(Pos.CENTER);

        canvas = new Canvas(300, 400);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        desenharForca(gc);

        VBox centerPane = new VBox(canvas, palavraLabel);
        centerPane.setAlignment(Pos.CENTER);
        centerPane.setSpacing(20);
        layout.setCenter(centerPane);

        // Painel inferior (entrada de letras e controles)
        letraInput = new TextField();
        letraInput.setPromptText("Digite uma letra");
        letraInput.setMaxWidth(100);

        Button enviarButton = new Button("Enviar");
        Button reiniciarButton = new Button("Reiniciar");

        enviarButton.setOnAction(e -> enviarLetra());
        reiniciarButton.setOnAction(e -> reiniciarJogo());

        HBox controlsPane = new HBox(letraInput, enviarButton, reiniciarButton);
        controlsPane.setAlignment(Pos.CENTER);
        controlsPane.setSpacing(10);

        mensagemLabel = new Label("Tentativas restantes: 6");
        mensagemLabel.setStyle("-fx-font-size: 14px;");

        VBox bottomPane = new VBox(controlsPane, mensagemLabel);
        bottomPane.setAlignment(Pos.CENTER);
        bottomPane.setSpacing(10);
        layout.setBottom(bottomPane);
    }

    public BorderPane getLayout() {
        return layout;
    }

    private HBox criarPainelUsuario() {
        TextField nomeInput = new TextField();
        nomeInput.setPromptText("Digite seu nome");
        Button iniciarButton = new Button("Iniciar Jogo");

        iniciarButton.setOnAction(e -> {
            nomeUsuario = nomeInput.getText();
            if (nomeUsuario.isEmpty()) {
                mensagemLabel.setText("Por favor, insira seu nome.");
                return;
            }

            if (gameController.registrarUsuario(nomeUsuario)) {
                mensagemLabel.setText("Bem-vindo, " + nomeUsuario + "!");
                iniciarJogo();
            } else {
                mensagemLabel.setText("Erro ao registrar usuário.");
            }
        });

        HBox userPane = new HBox(nomeInput, iniciarButton);
        userPane.setSpacing(10);
        userPane.setAlignment(Pos.CENTER);
        return userPane;
    }

    private void iniciarJogo() {
        String palavraProgresso = gameController.iniciarJogo();
        if (palavraProgresso != null) {
            palavraLabel.setText(palavraProgresso);
        } else {
            mensagemLabel.setText("Erro ao iniciar o jogo.");
        }
    }

    private void enviarLetra() {
        String letra = letraInput.getText().toLowerCase();
        letraInput.clear();

        if (letra.isEmpty() || letra.length() > 1) {
            mensagemLabel.setText("Digite apenas uma letra.");
            return;
        }

        String progresso = gameController.enviarLetra(letra);

        if (progresso != null) {
            palavraLabel.setText(progresso);

            if (gameController.verificarVitoria()) {
                mensagemLabel.setText("Parabéns, você venceu!");
                gameController.atualizarPontuacao(nomeUsuario, 10);
            } else if (gameController.verificarDerrota()) {
                mensagemLabel.setText("Você perdeu! A palavra era: " + gameController.getPalavraAtual());
            } else {
                mensagemLabel.setText("Tentativas restantes: " + (6 - gameController.getErros()));
            }
        }

        atualizarBoneco();
    }

    private void reiniciarJogo() {
        gameController.reiniciarJogo();
        iniciarJogo();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        desenharForca(gc);
        mensagemLabel.setText("Tentativas restantes: 6");
    }

    private void atualizarBoneco() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        switch (gameController.getErros()) {
            case 1 -> desenharCabeca(gc);
            case 2 -> desenharCorpo(gc);
            case 3 -> desenharBracoEsquerdo(gc);
            case 4 -> desenharBracoDireito(gc);
            case 5 -> desenharPernaEsquerda(gc);
            case 6 -> desenharPernaDireita(gc);
        }
    }

    // Desenho da forca e do boneco
    private void desenharForca(GraphicsContext gc) {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);

        // Base, pilar, topo e corda
        gc.strokeLine(50, 350, 250, 350);
        gc.strokeLine(150, 350, 150, 50);
        gc.strokeLine(150, 50, 220, 50);
        gc.strokeLine(220, 50, 220, 100);
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


