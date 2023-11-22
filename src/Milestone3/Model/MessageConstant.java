/**
 *Message Constant class holds all the messages that are shown to the user during the UNO game.
 * @Authors: Rebecca Li, Juanita Rodelo
 */
package Milestone3.Model;

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
    public static final String wildDrawTwoTurn = "You were skipped! Please draw two cards or challenge";
    // The current player plays a invalid card.
    public static final String invalidCard = "Invalid Card! Draw or play a valid card!";
    // The current player finishes his/her turn, teh game needs to goto the next player's turn
    public static final String nextPlayer = "You turn has finished! Please click Next Player";

    public static final String notGuiltyTwo = "Previous player was found not guilty! You were skipped! Please draw two cards!";
    public static final String guiltyTwo = "Previous player was found guilty! They have received two cards. " +
            "Now it's your turn, draw or play a valid card!";
    public static final String notGuiltyColor = "Previous player was found not guilty! " +
            "You were skipped! Please draw cards until the target color!";
    public static final String guiltyColor = "Previous player was found guilty! They have received cards." +
            " Now it's your turn, draw or play a valid card!";
    public static final String drawTwoTurn = "You were skipped! Please draw two cards!";  // distinguish with wild draw two
    public static final String drawThreeTurn = "You were skipped! Please draw three cards!";
    public static final String drawFourTurn = "You were skipped! Please draw four cards!";
    public static final String drawFiveTurn = "You were skipped! Please draw five cards!";
    public static final String drawColor = "You were skipped! Please draw cards until the target color!";

    public static final String aIplayed = "AI Played! Please click Next Player";

    public static final String aIPickedUp = "AI Played and chose to pick up a card!";

    public static final String aIDrawOne = "AI Played and picked up one card!";

    public static final String aIDrawTwo = "AI Played and picked up two card!";

    public static final String aIDrawFive = "AI Played and picked up five card!";

    public static final String aIdrawColor = "AI played and picked up cards until they received the target color!";

    public static final String aISkipped = "AI was skipped. Please click Next Player!";
}
