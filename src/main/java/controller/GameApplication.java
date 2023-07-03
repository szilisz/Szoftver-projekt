package controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.GameLogic;
import javafx.scene.input.MouseEvent;
import org.tinylog.Logger;
import util.MatchResults;
import util.WinnerRepository;

import java.io.File;
import java.io.IOException;
import java.util.Objects;


public class GameApplication {
    private model.GameLogic game;
    public static String[] playerNames = new String[2];
    @FXML
    private AnchorPane addNames;
    @FXML
    public TextField firstPlayerField;
    @FXML
    public TextField secondPlayerField;
    private final Image Tile = new Image(Objects.requireNonNull(getClass().getResource("/Images/tile.jpg")).toExternalForm());
    private final Image RemovedTile = new Image(Objects.requireNonNull(getClass().getResource("/Images/removedTile.jpg")).toExternalForm());
    private final Image WhiteKing = new Image(Objects.requireNonNull(getClass().getResource("/Images/whiteKing.jpg")).toExternalForm());
    private final Image BlackKing = new Image(Objects.requireNonNull(getClass().getResource("/Images/blackKing.jpg")).toExternalForm());
    @FXML
    private GridPane gridPane;
    @FXML
    private AnchorPane gameWindow;
    @FXML
    private Button backToMain;
    @FXML
    private Label gameTurn;


    @FXML
    private void initialize() {

        game = new GameLogic();
        gameWindow.setVisible(false);
        backToMain.setVisible(false);
        drawBoard();
    }

    private void drawBoard() {
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 7; j++) {
                ImageView image = switch (game.getGameBoardTileCoord(i,j)) {
                    case 1 -> new ImageView(WhiteKing);
                    case 2 -> new ImageView(BlackKing);
                    case 3 -> new ImageView(RemovedTile);
                    default -> new ImageView(Tile);
                };
                Pane pane = new Pane(image);
                image.fitWidthProperty().bind(pane.widthProperty());
                image.fitHeightProperty().bind(pane.heightProperty());

                gridPane.add(pane, i, j);
                pane.setOnMouseClicked(this::moveOnClick);
                gameTurn.setText(playerNames[game.playerOnTurn-1] + "'s turn!");
            }
        }
    }

    @FXML
    private void moveOnClick(MouseEvent event) {
        Node source = (Node) event.getSource();
        int row = GridPane.getRowIndex(source);
        int col = GridPane.getColumnIndex(source);

        game.movePlayer(row,col);
        drawBoard();
        if(game.isGameOver())
        {
            backToMain.setVisible(true);
            gameTurn.setText(playerNames[game.winner-1] + " WON!");
            Logger.info("Game is over, congrats to the winner!");
            try {
                saveMatchResults(playerNames[0],playerNames[1],playerNames[game.winner-1]);
                Logger.info("Updating statistics!");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void saveMatchResults(String playerOne, String playerTwo, String winner) throws IOException{
        var repository = new WinnerRepository();
        var data = MatchResults.builder().playerOne(playerOne).playerTwo(playerTwo).gameWinner(winner).build();
        File file = new File("statistics.json");
        if(file.exists())
            repository.loadFromFile(file);
        repository.add(data);
        repository.saveToFile(file);
    }

    @FXML
    private void saveTheNames() {
        playerNames[0] = firstPlayerField.getText();
        playerNames[1] = secondPlayerField.getText();
        if (playerNames[0].length() > 0 && playerNames[1].length() > 0 && !playerNames[0].equals(playerNames[1]) ) {
            gameWindow.setVisible(true);
            gameTurn.setText(playerNames[0] + "'s turn!");
            addNames.setDisable(true);
            addNames.setVisible(false);
            Logger.info("Names are saved! The game started");
        }else Logger.error("The names are not adequate. Use TWO different names!" );
    }

    @FXML
    public void backToMenu(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/FXML/menu.fxml"));
        Parent root = fxmlLoader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }
}