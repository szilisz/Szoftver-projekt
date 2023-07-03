package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameLogicTest {
    int[][] gameBoard = {
            {0,1,0,3,0,0,0},
            {0,3,0,3,0,0,0},
            {0,3,0,0,0,2,0},
            {0,0,0,3,0,0,3},
            {0,3,0,0,3,3,3},
            {0,0,0,3,0,0,0}
    };

    int[][] gameBoard2 = {
            {0,0,3,3,0,3,3},
            {3,3,3,3,3,3,3},
            {3,1,3,0,3,2,3},
            {3,3,3,3,3,3,3},
            {0,3,0,0,3,3,3},
            {0,0,0,3,0,0,0}
    };

    @Test
    void getPlayerCoordinates() {
        GameLogic test = new GameLogic();
        test.gameBoard = gameBoard;

        int playerOnTurn = 1;
        int[] expectedCoords = {0, 1};
        int[] actualCoords = test.getPlayerCoordinates(playerOnTurn);

        int playerOnTurn2 = 2;
        int[] expectedCoords2 = {2, 5};
        int[] actualCoords2 = test.getPlayerCoordinates(playerOnTurn2);

        assertNotEquals (expectedCoords2, actualCoords);
        assertArrayEquals (expectedCoords, actualCoords);
        assertArrayEquals (expectedCoords2, actualCoords2);
    }

    @Test
    void getGameBoardTileCoord() {
        GameLogic test = new GameLogic();
        test.gameBoard = gameBoard;
        assertEquals(test.getGameBoardTileCoord(0,0), 0);
        assertEquals(test.getGameBoardTileCoord(5,0), 0);
        assertNotEquals(test.getGameBoardTileCoord(0,5), 1);
    }

    @Test
    void changePlayer() {
        GameLogic test = new GameLogic();
        test.playerOnTurn = 2;
        test.changePlayer(test.playerOnTurn);
        assertEquals(1,test.playerOnTurn);

        test.changePlayer(test.playerOnTurn);
        assertEquals(2,test.playerOnTurn);
    }

    @Test
    void canMove() {
        GameLogic test = new GameLogic();
        test.gameBoard = gameBoard;

        GameLogic test2 = new GameLogic();
        test2.gameBoard = gameBoard2;


        assertFalse(test2.canMove(2));
        assertFalse(test2.canMove(1));
        assertTrue(test.canMove(2));
        assertTrue(test.canMove(1));
    }

    @Test
    void movePlayer() {
        GameLogic test = new GameLogic();
        GameLogic test2 = new GameLogic();

        test.gameBoard = gameBoard;
        test2.gameBoard = gameBoard2;
        test.playerOnTurn = 2;
        test2.playerOnTurn = 1;

        test.movePlayer(2,4);
        test2.movePlayer(2, 4);

        assertFalse(test2.canMove(1));
        assertTrue(test.canMove(2));
        assertEquals(gameBoard[2][4], 2);
        assertNotEquals(gameBoard2[2][4], 1);
    }

    @Test
    void isGameOver() {
        GameLogic test = new GameLogic();
        assertFalse(test.isGameOver());
    }
}