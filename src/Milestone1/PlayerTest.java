/**
 * Testing class for methods in Player Class
 * Author: Ayman Kamran
 */
package Milestone1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    private Player player;
    private Card card;
    private Card cardW;

    @BeforeEach
    void setUp() {
        player = new Player("Ayman");
        card = new Card(Card.Color.RED, Card.Type.FIVE);
        cardW = new Card(Card.Color.NONE, Card.Type.WILD);
    }

    /** Testing case for player name being added correctly
     *
     */
    @Test
    void testPlayerName() {

        assertEquals("Ayman", player.getName());
    }

    @Test
    void testSetName() {
        assertEquals("Ayman", player.getName(), "Initial name should be Ayman.");
        player.setName("John");
        assertEquals("John", player.getName(), "The name should be updated to John.");
    }

    /**Testing case for hand at the start of game(empty)
     *
     */
    @Test
    void testPlayerHandInitiallyEmpty() {
        assertEquals(0, player.getHand().size(), "Ayman's hand should be initially empty.");
    }

    /**Testing case for when multiple cards are picked up
     *
     */
    @Test
    void testPlayerPicksUpMultipleCards() {
        player.pickUpCard(card);
        player.pickUpCard(cardW);
        assertEquals(2, player.getHand().size(), "Ayman's hand should have two cards.");
    }

    /**
     * Testing case for a card getting picked up and played
     */
    @Test
    void testPlayCard() {
        player.pickUpCard(card);
        player.playCard(card);
        assertEquals(0, player.getHand().size());
    }

    /** Testing case for a card being picked up and added to hand
     *
     */
    @Test
    void testPickUpCard() {
        player.pickUpCard(card);
        assertEquals(1, player.getHand().size());
        assertEquals(card, player.getHand().get(0));
    }
    /**Testing toString method for when a card is picked up and put into hand
     *
     */
    @Test
    void testToString() {
        player.pickUpCard(card);
        String expected = "Player: Ayman\nHand: [RED FIVE]";
        assertEquals(expected, player.toString());
    }
    /**Testing case for toString method when a WILD card is picked up from deck.
     *
     */
    @Test
    void testToStringWILD(){
        player.pickUpCard(cardW); // assuming cardW is a wild card with Type.WILD
        String expected = "Player: Ayman\nHand: [WILD]";
        String actual = player.toString();
        assertEquals(expected, actual, "Ayman's String rep should match expected");
    }

    /**Testing case for toString method when multiple cards are picked up and added to hand
     *
     */
    @Test
    void testToStringMultiple(){
        player.pickUpCard(card);
        player.pickUpCard(cardW);
        String expected = "Player: Ayman\nHand: [RED FIVE, WILD]";
        String actual = player.toString();
        assertEquals(expected, actual, "Ayman's String rep should match the expected format given mutltiple cards");
    }
}
