package Milestone4.Model;

public class CardModel {

    /**
     * CardModel represents a card in this game, it has a light side and a dark side
     */
    private final CardSideModel lightSide;
    private final CardSideModel darkSide;

    public CardModel(CardSideModel lightSide, CardSideModel darkSide) {
        this.lightSide = lightSide;
        this.darkSide = darkSide;
    }

    /**
     * getCard() returns the current side of the card
     * @param isLight
     * @return
     */
    public CardSideModel getCard(boolean isLight) {
        if (isLight) {
            return this.lightSide;
        } else {
            return this.darkSide;
        }
    }

    /**
     * getColor() returns the current color of the card
     * @param isLight
     * @return
     */
    public CardSideModel.Color getColor(boolean isLight) {
        if (isLight) {
            return this.lightSide.getColor();
        } else {
            return this.darkSide.getColor();
        }
    }

    /**
     * getType() returns the type of the current card
     * @param isLight
     * @return
     */
    public CardSideModel.Type getType(boolean isLight) {
        if (isLight) {
            return this.lightSide.getType();
        } else {
            return this.darkSide.getType();
        }
    }

    /**
     * toString() returns the current card in a string format
     * @param isLight
     * @return
     */
    public String toString(boolean isLight) {
        if (isLight) {
            return this.lightSide.toString();
        } else {
            return this.darkSide.toString();
        }
    }

    /**
     * getLightSide() returns true if the side of the current card is light, false otherwise
     * @return
     */
    public CardSideModel getLightSide() {
        return this.lightSide;
    }

    /**
     * getDarkSide() returns true if the side of the current card is dark, false otherwise
     * @return
     */
    public CardSideModel getDarkSide() {
        return this.darkSide;
    }
}