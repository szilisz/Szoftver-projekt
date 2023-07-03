package util;

import lombok.Builder;
import lombok.NoArgsConstructor;

@lombok.Data
@Builder
@NoArgsConstructor

public class MatchResults {
    private String playerOne;
    private String playerTwo;
    private String gameWinner;

    public MatchResults(String playerOne, String playerTwo, String gameWinner) {

        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.gameWinner = gameWinner;
    }
}
