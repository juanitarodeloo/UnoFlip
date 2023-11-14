package Milestone2.Model;
/**
 * UnoGameEvent contains everything the view needs to know to set up the next turn
 *@Author: Rebecca Li
 */
import java.util.EventObject;

public class UnoGameEvent extends EventObject {
    // UnoGameEvent contains everything the view needs to know to set up the next turn

    private PlayerModel currPlayer;
    private String message;  // instructions
    private String direction;  // "Clockwise" or "CounterClockwise"
    private CardModel topCard;
    private CardModel.Color targetColour;


    /**
     * Constructs a prototypical Event.
     *
     * @param model the UnoModel on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public UnoGameEvent(UnoModel model, PlayerModel currPlayer, String message,
                        CardModel topCard, CardModel.Color targetColour, String direction) {
        super(model);
        this.currPlayer = currPlayer;
        this.message = message;
        this.topCard = topCard;
        this.targetColour = targetColour;
        this.direction = direction;
    }


    public PlayerModel getCurrPlayer() {
        return currPlayer;
    }

    public String getMessage() {
        return message;
    }

    public String getDirection() {
        return direction;
    }

    public CardModel getTopCard() {
        return topCard;
    }

    public CardModel.Color getTargetColour() {
        return targetColour;
    }
}
