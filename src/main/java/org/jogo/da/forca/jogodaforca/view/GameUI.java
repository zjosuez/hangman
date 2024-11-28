package org.jogo.da.forca.jogodaforca.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jogo.da.forca.jogodaforca.controller.GameController;

public class GameUI {
    private BorderPane layout;
    private Label palavraLabel;
    private Label mensagemLabel;
    private TextField letraInput;
    private Canvas canvas;
    private Label letrasErradasLabel;
    private String nomeUsuario;
    private final GameController gameController;
    private final GameCanvasDrawer canvasDrawer;
    private final RankingView rankingView;

    public GameUI() {
        gameController = new GameController();
        canvas = new Canvas(300, 400);
        canvasDrawer = new GameCanvasDrawer(canvas);
        rankingView = new RankingView(gameController);
        layout = new BorderPane();
        criarInterface();
    }

    private void criarInterface() {
        layout.setTop(criarPainelSuperior());
        layout.setCenter(criarPainelCentral());
        layout.setBottom(criarPainelInferior());
    }

    private Node criarPainelSuperior() {
        Label tituloLabel = new Label("Jogo da Forca");
        tituloLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Button rankingButton = new Button("Mostrar Ranking");
        rankingButton.setOnAction(e -> rankingView.mostrarRanking());

        VBox topPane = new VBox(tituloLabel, criarPainelUsuario(), rankingButton);
        topPane.setAlignment(Pos.CENTER);
        topPane.setSpacing(10);
        return topPane;
    }

    private Node criarPainelCentral() {
        palavraLabel = new Label("_ _ _ _ _");
        palavraLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        canvasDrawer.desenharForca();

        VBox centerPane = new VBox(canvas, palavraLabel);
        centerPane.setAlignment(Pos.CENTER);
        centerPane.setSpacing(20);
        return centerPane;
    }

    private Node criarPainelInferior() {
        letraInput = new TextField();
        letraInput.setPromptText("Digite uma letra");
        letraInput.setMaxWidth(100);

        Button enviarButton = new Button("Enviar");
        enviarButton.setOnAction(e -> enviarLetra());

        Button reiniciarButton = new Button("Reiniciar");
        reiniciarButton.setOnAction(e -> reiniciarJogo());

        HBox controlsPane = new HBox(letraInput, enviarButton, reiniciarButton);
        controlsPane.setAlignment(Pos.CENTER);
        controlsPane.setSpacing(10);

        mensagemLabel = new Label("Tentativas restantes: 6");
        letrasErradasLabel = new Label("Letras erradas: Nenhuma");

        VBox bottomPane = new VBox(controlsPane, mensagemLabel, letrasErradasLabel);
        bottomPane.setAlignment(Pos.CENTER);
        bottomPane.setSpacing(10);
        return bottomPane;
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
                nomeInput.setDisable(true);
                iniciarButton.setDisable(true);
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

        String resultado = gameController.enviarLetra(letra);

        if (resultado.equals("Letra já utilizada!")) {
            mensagemLabel.setText("Você já usou essa letra.");
        } else {
            palavraLabel.setText(gameController.getProgresso());
            mensagemLabel.setText("Tentativas restantes: " + (6 - gameController.getErros()));
            letrasErradasLabel.setText("Letras erradas: " + String.join(", ", gameController.getLetrasErradas()));
            canvasDrawer.atualizarBoneco(gameController.getErros());
        }

        if (gameController.verificarVitoria()) {
            mostrarTelaVitoria();
        } else if (gameController.verificarDerrota()) {
            mostrarTelaDerrota(gameController.getPalavraAtual());
        }
    }



    private void mostrarTelaVitoria() {
        Stage vitoriaStage = new Stage();
        vitoriaStage.setTitle("Vitória!");

        Label mensagemVitoria = new Label("Parabéns, você venceu!");
        mensagemVitoria.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Button jogarNovamenteButton = new Button("Jogar Novamente");
        jogarNovamenteButton.setOnAction(e -> {
            vitoriaStage.close();
            reiniciarJogo();
        });

        VBox layout = new VBox(mensagemVitoria, jogarNovamenteButton);
        layout.setAlignment(Pos.CENTER);
        layout.setSpacing(20);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 400, 200);
        vitoriaStage.setScene(scene);
        vitoriaStage.show();
    }


    private void mostrarTelaDerrota(String palavraCorreta) {
        Stage derrotaStage = new Stage();
        derrotaStage.setTitle("Derrota!");

        Label mensagemDerrota = new Label("Que pena, você perdeu!");
        mensagemDerrota.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label palavraLabel = new Label("A palavra correta era: " + palavraCorreta);
        palavraLabel.setStyle("-fx-font-size: 16px;");

        Button jogarNovamenteButton = new Button("Jogar Novamente");
        jogarNovamenteButton.setOnAction(e -> {
            derrotaStage.close();
            reiniciarJogo();
        });

        VBox layout = new VBox(mensagemDerrota, palavraLabel, jogarNovamenteButton);
        layout.setAlignment(Pos.CENTER);
        layout.setSpacing(20);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 400, 250);
        derrotaStage.setScene(scene);
        derrotaStage.show();
    }


    private void reiniciarJogo() {
        gameController.reiniciarJogo();
        iniciarJogo();
        canvasDrawer.desenharForca();
        mensagemLabel.setText("Tentativas restantes: 6");
    }

    public BorderPane getLayout() {
        return layout;
    }
}
