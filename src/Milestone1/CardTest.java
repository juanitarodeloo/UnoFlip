package Milestone1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/** Testing class for methods in Card Class
 *
 */
class CardTest {

    /** Testing for getter getColor
     *
     */
    @Test
    void testGetColor() {
        Card card = new Card(Card.Color.RED, Card.Type.FIVE);
        assertEquals(Card.Color.RED, card.getColor());
    }

    /**Testing for getter getType
     *
     */
    @Test
    void testGetType() {
        Card card = new Card(Card.Color.RED, Card.Type.FIVE);
        assertEquals(Card.Type.FIVE, card.getType());
    }

    /** Testing for toString
     *
     */
    @Test
    void testToString() {
        Card card = new Card(Card.Color.RED, Card.Type.FIVE);
        String expected = "RED FIVE";
        assertEquals(expected, card.toString());
    }
}
