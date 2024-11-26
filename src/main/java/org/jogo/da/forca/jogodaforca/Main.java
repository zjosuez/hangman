package org.jogo.da.forca.jogodaforca;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jogo.da.forca.jogodaforca.view.GameUI;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Criar o layout principal
        BorderPane root = new BorderPane();

        // Adicionar o painel do jogo
        GameUI gameUI = new GameUI();
        root.setCenter(gameUI.getLayout());

        // Configurar a cena e o palco
        Scene scene = new Scene(root, 800, 750);
        primaryStage.setTitle("Jogo da Forca");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}