package Milestone2.Model;

public class MessageConstant {
    // MessageConstant contains all instruction messages in different situations.
    // Constants in this class also used to distinguish different functions.

    // Normal turn player can choose to draw or play a card
    public static final String normalTurn = "Draw or play a valid card!";
    // Previous player plays a skip card, current player can do nothing.
    public static final String skipTurn = "You were skipped! Please click Next Player!";
    // Previous player plays a draw one card, the current player must draw a card.
    public static final String drawOneTurn = "You were skipped! Please draw one card!";
    // Previous player plays a wild draw two card, the current player must draw two cards.
    public static final String drawTwoTurn = "You were skipped! Please draw two cards!";
    // The current player plays a invalid card.
    public static final String invalidCard = "Invalid Card! Draw or play a valid card!";
    // The current player finishes his/her turn, teh game needs to goto the next player's turn
    public static final String nextPlayer = "You turn has finished! Please click Next Player";

    public static final String notGuilty = "Previous player was found not guilty! You were skipped! Please draw two cards!";

    public static final String guilty = "Previous player was found guilty! They have received two cards. Now it's your turn, draw or play a valid card!";
}
