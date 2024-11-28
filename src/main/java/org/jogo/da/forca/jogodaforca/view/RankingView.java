package org.jogo.da.forca.jogodaforca.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jogo.da.forca.jogodaforca.controller.GameController;
import org.jogo.da.forca.jogodaforca.model.Ranking;

import java.util.List;
import java.util.Map;

public class RankingView {
    private final GameController gameController;

    public RankingView(GameController gameController) {
        this.gameController = gameController;
    }

    public void mostrarRanking() {
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
}

