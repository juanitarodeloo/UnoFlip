package Milestone3.Model;

public class CardSideModel {
    /**
     * Define enum for card colors (include both light side and dark side)
     */
    public enum Color {
        RED, YELLOW, GREEN, BLUE, NONE, PINK, TEAL, ORANGE, PURPLE
    }

    /**
     * Define enum for card types (include both light side and dark side)
     */
    public enum Type {
        ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE,
        REVERSE, SKIP, WILD, WILD_DRAW_TWO, DRAW_ONE, FLIP, DRAW_FIVE, WILD_DRAW_COLOR, SKIP_EVERYONE
    }

    /**
     * Private instance variables to store card color and type
     */
    private Milestone3.Model.CardSideModel.Color color;
    private Milestone3.Model.CardSideModel.Type type;

    /**
     * Card() creates a new Card object with specified color and type
     *
     * @param color
     * @param type
     */
    public CardSideModel(CardSideModel.Color color, CardSideModel.Type type) {
        this.color = color;
        this.type = type;
    }

    /**
     * getColor() returns the color of card
     *
     * @return color
     */
    public Milestone3.Model.CardSideModel.Color getColor() {
        return color;
    }

    /**
     * getType() gets the card type
     *
     * @return
     */
    public Milestone3.Model.CardSideModel.Type getType() {
        return type;
    }

    /**
     * toString() provides a string representation of the card
     *
     * @return
     */
    @Override
    public String toString() {
        if (color == Milestone3.Model.CardSideModel.Color.NONE) {
            return type.toString();
        } else {
            return color + " " + type;
        }
    }
}
