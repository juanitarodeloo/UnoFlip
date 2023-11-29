package Milestone4.Model;

import java.util.EventObject;
/**
 * RedoEvent class contains everything the UnoModel needs to know to do the redo function.
 *
 * @Author: Rebecca Li
 */
public class DoEvent extends EventObject {
    private final CardModel topCard;
    private final boolean isClockwise;
    private final boolean isLight;
    private final CardSideModel.Color targetColor;
    private final CardSideModel.Color previousColor;
    private final int numOfDraw;
    private final int numOfSkip;
    private final boolean validWild;
    private final boolean drawColor;
    private final String message;

    public DoEvent(UnoModel model, CardModel topCard, boolean isClockwise, boolean isLight,
                   CardSideModel.Color targetColor, CardSideModel.Color previousColor, int numOfDraw,
                   int numOfSkip, boolean validWild, boolean drawColor, String message){
        super(model);
        this.topCard = topCard;
        this.isClockwise = isClockwise;
        this.isLight = isLight;
        this.targetColor = targetColor;
        this.previousColor = previousColor;
        this.numOfDraw = numOfDraw;
        this.numOfSkip = numOfSkip;
        this.validWild = validWild;
        this.drawColor = drawColor;
        this.message = message;
    }

    public CardModel getTopCard() {
        return topCard;
    }

    public boolean isClockwise() {
        return isClockwise;
    }

    public boolean isLight() {
        return isLight;
    }

    public CardSideModel.Color getTargetColor() {
        return targetColor;
    }

    public CardSideModel.Color getPreviousColor() {
        return previousColor;
    }

    public int getNumOfDraw() {
        return numOfDraw;
    }

    public int getNumOfSkip() {
        return numOfSkip;
    }

    public boolean isValidWild() {
        return validWild;
    }

    public boolean isDrawColor() {
        return drawColor;
    }

    public String getMessage() {
        return message;
    }
}
