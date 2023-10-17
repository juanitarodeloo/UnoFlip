/**
 * Card class represents playing cards for an UNO game.
 * It defines the available colors and card types.
 */

package Milestone1;

public class Card {

    /**Define enum for card colors
     *
     */
    public enum Color{
        RED, YELLOW, GREEN, BLUE, NONE
    }

    /**Define enum for card types
     *
     */
    public enum Type{
        ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, REVERSE, SKIP, WILD, WILD_DRAW_TWO

        }

    /**Private instance variables to store card color and type
     *
     */
    private Color color;
    private Type type;

    /**Constructor to create a new Card object with specified color and type
     *
     * @param color
     * @param type
     */
    public Card(Color color, Type type){
        this.color = color;
        this.type = type;

    }

    /**Getter method to retrieve card's color
     *
     * @return
     */
    public Color getColor(){
        return color;

    }

    /**Setter method to set card's color
     *
     * @param color
     */
    public void setColor(Color color){
        this.color = color;
    }

    /**Getter method to retrieve card's type
     *
     * @return
     */
    public Type getType(){
        return type;
    }

    /**Setter method to set card's type
     *
     * @param type
     */
    public void setType(Type type){
        this.type = type;
    }

    /**toString() method to provide a string representation of the card
     *
     * @return
     */
    public String toString(){

        return color + " " + type;
    }



}
