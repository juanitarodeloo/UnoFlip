/**
 *CardModel class represents the model of the playing cards for an UNO game.
 *It defines the available colors and card types.
 * @Authors: Ayman Kamran, Rebecca Li
 */
package Milestone2.Model;

public class CardModel {
    /**
     * Define enum for card colors
     */
    public enum Color{
        RED, YELLOW, GREEN, BLUE, NONE
    }

    /**
     * Define enum for card types
     */
    public enum Type{
        ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, REVERSE, SKIP, WILD, WILD_DRAW_TWO, DRAW_ONE

    }

    /**
     * Private instance variables to store card color and type
     */
    private CardModel.Color color;
    private CardModel.Type type;

    /**
     * Card() creates a new Card object with specified color and type
     * @param color
     * @param type
     */
    public CardModel(CardModel.Color color, CardModel.Type type){
        this.color = color;
        this.type = type;

    }

    /**
     * getColor() returns the color of card
     * @return color
     */
    public CardModel.Color getColor(){
        return color;

    }

    /**
     * setColor() sets the color of the card
     * @param color
     */
    public void setColor(CardModel.Color color){

        this.color = color;
    }

    /**
     * getType() gets the card type
     * @return
     */
    public CardModel.Type getType(){
        return type;
    }

    /**
     * setType() sets card type with given card type
     * @param type
     */
    public void setType(CardModel.Type type){

        this.type = type;
    }

    /**
     * toString() provides a string representation of the card
     * @return
     */
    @Override
    public String toString() {
        if (color == CardModel.Color.NONE) {
            return type.toString();
        } else {
            return color + " " + type;
        }
    }
}
