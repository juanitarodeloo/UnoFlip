package Milestone4.Model;

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
    private CardSideModel.Color color;
    private CardSideModel.Type type;

    /**
     * Card() creates a new Card object with specified color and type
     *
     * @param color
     * @param type
     */
    public CardSideModel(Color color, Type type) {
        this.color = color;
        this.type = type;
    }

    /**
     * getColor() returns the color of card
     *
     * @return color
     */
    public CardSideModel.Color getColor() {
        return color;
    }

    /**
     * getType() gets the card type
     *
     * @return
     */
    public CardSideModel.Type getType() {
        return type;
    }

    /**
     * toString() provides a string representation of the card
     *
     * @return
     */
    @Override
    public String toString() {
        if (color == CardSideModel.Color.NONE) {
            return type.toString();
        } else {
            return color + " " + type;
        }
    }
}
