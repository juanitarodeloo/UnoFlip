package Milestone2.Model;
/**
 *UnoFinishEvent class contains everything the view needs to know to pop up a finish confirm dialog.
 * @Author: Rebecca Li
 */

import java.util.EventObject;

public class UnoFinishEvent extends EventObject {
    // UnoFinishEvent contains everything the view needs to know to pop up a finish confirm dialog.

    private PlayerModel winner;
    private int winnerIndex;  // the player index in the player list
    private String message;  // Indicate round finish or game finish
    private String title;

    /**
     * Constructs a prototypical Event.
     *
     * @param model the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public UnoFinishEvent(UnoModel model, PlayerModel winner, int winnerIndex, int roundNum) {
        super(model);
        this.winner = winner;
        this.winnerIndex = winnerIndex;
        if (roundNum > 0){ // if round number is bigger than 0 -> round finish
            this.title = "Round " + roundNum + " finished confirm";
            this.message = winner.getName() + " has won round " + roundNum + "!";
        }else {  // Else if the game finish
            this.title = "Game finished confirm";
            this.message = winner.getName() + "has won the game!";
        }
    }

    public PlayerModel getWinner() {
        return winner;
    }

    public int getWinnerIndex() {
        return winnerIndex;
    }

    public String getMessage() {
        return message;
    }

    public String getTitle() {
        return title;
    }
}
