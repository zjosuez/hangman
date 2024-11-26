package org.jogo.da.forca.jogodaforca.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jogo.da.forca.jogodaforca.controller.GameController;
import org.jogo.da.forca.jogodaforca.model.Ranking;
import org.jogo.da.forca.jogodaforca.model.database.GameDatabase;

import java.util.List;
import java.util.Map;

public class GameUI {
    private BorderPane layout;
    private Label palavraLabel;
    private Label mensagemLabel;
    private TextField letraInput;
    private Canvas canvas;
    private String nomeUsuario;
    private StringBuilder progressoPalavra;
    private GameController gameController = new GameController();
    private Label letrasErradasLabel;

    public GameUI() {
        layout = new BorderPane();
        criarInterface();
    }

    private void criarInterface() {
        letrasErradasLabel = new Label("Letras erradas: Nenhuma");
        letrasErradasLabel.setStyle("-fx-font-size: 14px;");

        // Título
        Label tituloLabel = new Label("Jogo da Forca");
        tituloLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Botão para exibir ranking
        Button rankingButton = new Button("Mostrar Ranking");
        rankingButton.setOnAction(e -> mostrarRanking());

        // Painel superior (título e entrada de usuário)
        VBox topPane = new VBox(tituloLabel, criarPainelUsuario(), rankingButton);
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

        VBox bottomPane = new VBox(controlsPane, mensagemLabel, letrasErradasLabel);
        bottomPane.setAlignment(Pos.CENTER);
        bottomPane.setSpacing(10);
        layout.setBottom(bottomPane);
    }

    public BorderPane getLayout() {
        Scene scene = new Scene(layout, 600, 600);
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

                // Desabilitar entrada de nome e botão após início do jogo
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
            return;
        }

        palavraLabel.setText(gameController.getProgresso());

        if (gameController.verificarVitoria()) {
            mensagemLabel.setText("Parabéns, você venceu!");
            exibirTelaVitoria(); // Chama a tela de vitória
        } else if (gameController.verificarDerrota()) {
            mensagemLabel.setText("Você perdeu!");
            exibirTelaDerrota(gameController.getPalavraAtual()); // Chama a tela de derrota
        } else {
            mensagemLabel.setText("Tentativas restantes: " + (6 - gameController.getErros()));
        }

        // Atualiza as letras erradas
        letrasErradasLabel.setText("Letras erradas: " + String.join(", ", gameController.getLetrasErradas()));

        atualizarBoneco();
    }



    private void exibirTelaVitoria() {
        Stage vitoriaStage = new Stage();
        vitoriaStage.setTitle("Vitória!");

        Label mensagem = new Label("Parabéns! Você acertou a palavra e ganhou +10 pontos!");
        mensagem.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        mensagem.setAlignment(Pos.CENTER);

        Button jogarNovamenteButton = new Button("Jogar Novamente");
        jogarNovamenteButton.setOnAction(e -> {
            vitoriaStage.close();
            reiniciarJogo();
        });

        VBox layout = new VBox(20, mensagem, jogarNovamenteButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        Scene scene = new Scene(layout, 500, 250); // Tamanho maior
        vitoriaStage.setScene(scene);
        vitoriaStage.show();
    }

    private void exibirTelaDerrota(String palavra) {
        Stage derrotaStage = new Stage();
        derrotaStage.setTitle("Derrota!");

        // Mensagem de derrota
        Label mensagem = new Label("Você perdeu! A palavra era: " + palavra);
        mensagem.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        mensagem.setAlignment(Pos.CENTER);

        // Botão para jogar novamente
        Button jogarNovamenteButton = new Button("Jogar Novamente");
        jogarNovamenteButton.setOnAction(e -> {
            derrotaStage.close();
            reiniciarJogo(); // Reinicia o jogo com uma nova palavra
        });

        // Layout da tela de derrota
        VBox layout = new VBox(20, mensagem, jogarNovamenteButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        // Configurar e mostrar a mini tela
        Scene scene = new Scene(layout, 400, 250); // Tamanho maior para melhor visibilidade
        derrotaStage.setScene(scene);
        derrotaStage.show();
    }


    private void mostrarRanking() {
        Stage rankingStage = new Stage();
        rankingStage.setTitle("Ranking - Top 10 Jogadores");

        TableView<Ranking> table = new TableView<>();
        table.setItems(getRankingData());

        TableColumn<Ranking, String> nomeCol = new TableColumn<>("Nome");
        nomeCol.setCellValueFactory(new PropertyValueFactory<>("nome"));
        nomeCol.setPrefWidth(150);

        TableColumn<Ranking, Integer> pontuacaoCol = new TableColumn<>("Pontuação");
        pontuacaoCol.setCellValueFactory(new PropertyValueFactory<>("pontuacao_total"));
        pontuacaoCol.setPrefWidth(100);

        table.getColumns().addAll(nomeCol, pontuacaoCol);

        VBox vbox = new VBox(table);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10));

        Scene scene = new Scene(vbox, 300, 400);
        rankingStage.setScene(scene);
        rankingStage.show();
    }

    private ObservableList<Ranking> getRankingData() {
        ObservableList<Ranking> data = FXCollections.observableArrayList();
        List<Map.Entry<String, Integer>> ranking = gameController.buscarRanking();
        for (Map.Entry<String, Integer> entry : ranking) {
            data.add(new Ranking(entry.getKey(), entry.getValue()));
        }
        return data;
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


