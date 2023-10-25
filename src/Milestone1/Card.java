/**
 * Card class represents playing cards for an UNO game.
 * It defines the available colors and card types.
 * Author: Ayman Kamran
 */

package Milestone1;

public class Card {

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
    private Color color;
    private Type type;

    /**
     * Card() creates a new Card object with specified color and type
     * @param color
     * @param type
     */
    public Card(Color color, Type type){
        this.color = color;
        this.type = type;

    }

    /**
     * getColor() returns the color of card
     * @return color
     */
    public Color getColor(){
        return color;

    }

    /**
     * setColor() sets the color of the card
     * @param color
     */
    public void setColor(Color color){

        this.color = color;
    }

    /**
     * getType() gets the card type
     * @return
     */
    public Type getType(){

        return type;
    }

    /**
     * setType() sets card type with given card type
     * @param type
     */
    public void setType(Type type){

        this.type = type;
    }

    /**
     * toString() provides a string representation of the card
     * @return
     */
    @Override
    public String toString(){
        if(color == Color.NONE) {
            return type.toString();
        } else {
            return color + " " + type;
        }
    }




}
