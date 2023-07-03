package controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import util.MatchResults;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class LeaderboardController {

    @FXML
    private TableView<MatchResults> tableView;
    @FXML
    private TableColumn<MatchResults, String> playerOne;
    @FXML
    private TableColumn<MatchResults, String> playerTwo;
    @FXML
    private TableColumn<MatchResults, String> gameWinner;

    @FXML
    private void initialize() throws IOException {
        playerOne.setCellValueFactory(new PropertyValueFactory<>("playerOne"));
        playerTwo.setCellValueFactory(new PropertyValueFactory<>("playerTwo"));
        gameWinner.setCellValueFactory(new PropertyValueFactory<>("gameWinner"));
        ObservableList<MatchResults> observableList = FXCollections.observableArrayList();
        observableList.addAll(readGameResults());
        tableView.setItems(observableList);
    }

    private List<MatchResults> readGameResults() throws IOException {
        File file = new File("statistics.json");
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .readValue(file, new TypeReference<>() {
                });
    }

    @FXML
    public void backToMain(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/FXML/menu.fxml"));
        Parent root = fxmlLoader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }
}