package Milestone1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/** Test cases for class Player
 *
 */
class PlayerTest {
    /**
     *Test case for PlayerName
     */
    @Test
    void testPlayerName() {
        Player player = new Player("Ayman");
        assertEquals("Ayman", player.getName());
    }

    /** Test case for playing a card
     *
     */
    @Test
    void testPlayCard() {
        Player player = new Player("Ayman");
        Card card = new Card(Card.Color.RED, Card.Type.FIVE);
        player.pickUpCard(card);
        player.playCard(card);
        assertEquals(0, player.getHand().size());
    }

    /** Testing case for when a card is picked up
     *
     */
    @Test
    void testPickUpCard() {
        Player player = new Player("Ayman");
        Card card = new Card(Card.Color.RED, Card.Type.FIVE);
        player.pickUpCard(card);
        assertEquals(1, player.getHand().size());
        assertEquals(card, player.getHand().get(0));
    }

    /**Testing case for toString method
     *
     */
    @Test
    void testToString() {
        Player player = new Player("Ayman");
        Card card = new Card(Card.Color.RED, Card.Type.FIVE);
        player.pickUpCard(card);
        String expected = "Player: Ayman\nHand: [RED FIVE]";
        assertEquals(expected, player.toString());
    }
}
