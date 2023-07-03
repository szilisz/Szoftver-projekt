package model;

import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * My main game class.
 */
public class GameLogic {
    /**
     * My game board that is represented by an 8*10 matrix.
     */
   int[][] gameBoard ={
        {4, 4, 4, 4, 4, 4, 4, 4, 4, 4},
        {4, 0, 0, 0, 0, 0, 0, 0, 0, 4},
        {4, 0, 0, 0, 0, 0, 0, 0, 0, 4},
        {4, 1, 0, 0, 0, 0, 0, 0, 0, 4},
        {4, 0, 0, 0, 0, 0, 0, 0, 2, 4},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 4},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 4},
        {4, 4, 4, 4, 4, 4, 4, 4, 4, 4}
    };
    private static final int[] Players = {1, 2};
    /**
     * This integer represents the King's value on the game board which changes every move.
     * The first value is the first player's King piece.
     */
    public int playerOnTurn = Players[0];
    /**
     * Stores the winners number value.
     */
    public int winner;
    /**
     * An array that stores a players coordinates on the game board.
     */
    int[] playerCoords;
    /**
     * This boolean stores the games state. Gets false when a player can't move.
     */
    public boolean isOver;
    /**
     * This array stores the coordinates that needs to be added to the player coordinates while checking the nearby cells, for possible movements.
     */
    int[][] offsets = {{-1, -1}, {0, -1}, {1, -1}, {-1, 0}, {1, 0}, {-1, 1}, {0, 1}, {1, 1}};
    /**
     * This integer stores the value of the tiles that can't be stepped on anymore.
     */
    int removedTileValue = 3;

    /**
     * Returns the value of the game board on that specific coordinate.
     * @param x the column index of the wanted value
     * @param y the row index of the wanted value
     * @return an integer value
     */
    public int getGameBoardTileCoord(int x, int y) {
        return gameBoard[y][x];
    }

    /**
     * This function return an array off coordinates of the searched player.
     * @param playerOnTurn which player's King value is on turn to move
     * @return an array with two values, or null if the set parameter is not valid
     */
    public int[] getPlayerCoordinates(int playerOnTurn) {
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[i].length; j++) {
                if (gameBoard[i][j] == playerOnTurn) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    /**
     * Changes the players turn.
     * @param lastPlayer the player that performed a valid movement last
     */
    public void changePlayer(int lastPlayer)
    {
        if (lastPlayer == Players[0]) {
            playerOnTurn = Players[1];
        } else {
            playerOnTurn = Players[0];
        }
    }
    private static boolean isValidCoordinate(int[][] gameBoard, int i, int j) {
        return i >= 0 && i < gameBoard.length && j >= 0 && j < gameBoard[0].length;
    }

    /**
     * The function parses through the initially declared offsets array to check the nearby cells of the player for possible movement options.
     * @param playerTurn the player whose potential movement possibility we are interested in
     * @return {@code true} if at least one movement can be performed, {@code false} if the player can't move
     */
    public boolean canMove(int playerTurn) {
        boolean canPlayerMove = false;
        playerCoords = getPlayerCoordinates(playerTurn);

        for (int[] offset : offsets) {
            int i = playerCoords[0] + offset[0];
            int j = playerCoords[1] + offset[1];


            if (isValidCoordinate(gameBoard, i, j) && gameBoard[i][j] == 0) {
                canPlayerMove = true;
                break;
            }
        }
        return canPlayerMove;
    }

    private boolean isValidMove(int oldI, int oldX, int newI, int newJ)
    {
        if (gameBoard[newI][newJ] != 0) {
            return false;
        }
        if (Math.abs(oldI - newI) > 1 || Math.abs(oldX - newJ) > 1) {
            return false;
        }
        return true;
    }

    private void destroyTile(int removedTileValue) {

        List<int[]> zeroTiles = new ArrayList<>();

        for (int i = 1; i < gameBoard.length-1; i++) {
            for (int j = 1; j < gameBoard[i].length - 1; j++) {
                if (gameBoard[i][j] == 0) {
                    zeroTiles.add(new int[]{i, j});
                }
            }
        }
            Random random = new Random();
            int randomIndex = random.nextInt(zeroTiles.size());
            int[] randomCoord = zeroTiles.get(randomIndex);

            int randomRow = randomCoord[0];
            int randomCol = randomCoord[1];

            gameBoard[randomRow][randomCol] = removedTileValue;
        Logger.info(String.format("A random tile got removed on tile [%s,%s]",randomRow-1,randomCol-1));

    }

    /**
     * This function retrieves the player's coordinates on turn,then checks whether if they can move.
     * If the player can't move the game will end, and the winner will be saved.
     * If the player can move then the function updates the game board with the new values, takes a tile away, and switches the players turn.
     * @param newI the column index of the players new coordinate (if movement is possible)
     * @param newJ the row index of the players new coordinate (if movement is possible)
     */

    public void movePlayer(int newI, int newJ) {
        {
            int[] playerCoordinates = getPlayerCoordinates(playerOnTurn);
            if (playerCoordinates != null) {
                int oldI = playerCoordinates[0];
                int oldJ = playerCoordinates[1];

                if (isValidMove(oldI, oldJ, newI, newJ)) {
                    gameBoard[newI][newJ] = playerOnTurn;
                    gameBoard[oldI][oldJ] = 0;
                    Logger.info(String.format("Player[%s] moved to tile [%s,%s]", playerOnTurn,newJ-1,newI-1));

                    destroyTile(removedTileValue);
                    changePlayer(playerOnTurn);

                    if (!canMove(playerOnTurn))
                    {
                        isOver = true;
                        changePlayer(playerOnTurn);
                        winner = playerOnTurn;
                    }
                }else Logger.error("Move not valid!");
            }
        }
    }

    /**
     * Returns a boolean value of the games current state.
     * @return {@code true} if game is still running and {@code false} if not
     */
    public boolean isGameOver()
    {
        return isOver;
    }
}