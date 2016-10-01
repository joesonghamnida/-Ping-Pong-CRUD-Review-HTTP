/**
 * Created by joe on 27/09/2016.
 */
public class Game {

    int gameId;

    String playerOne;
    String playerTwo;
    String playerOneScore;
    String playerTwoScore;

    String gameOwner;
    String modifyRecord;

    public Game() {
    }

    public Game(int gameId, String gameOwner, String playerOneName) {
        this.gameId=gameId;
        this.gameOwner = gameOwner;
        this.playerOne = playerOneName;
    }

    public Game(int gameId, String gameOwner, String playerOne, String playerTwo, String playerOneScore,
                String playerTwoScore) {
        this.gameId=gameId;

        this.gameOwner=gameOwner;

        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.playerOneScore = playerOneScore;
        this.playerTwoScore = playerTwoScore;

        //this.gameOwner = gameOwner;
        //this.modifyRecord = modifyRecord;
    }

    @Override
    public String toString() {
        return "player one: "+playerOne;
    }
}
