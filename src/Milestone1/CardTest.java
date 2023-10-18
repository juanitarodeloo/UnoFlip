package Milestone1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/** Testing class for methods in Card Class
 *
 */
class CardTest {

    /** Testing for getter getColor
     *
     */

    private Card card;
    private Card wild;

    @BeforeEach
    void setUp(){
        card = new Card(Card.Color.RED, Card.Type.FIVE);
        wild = new Card(Card.Color.NONE, Card.Type.WILD);

    }

    @Test
    void wildCardColor(){
        assertEquals(Card.Color.NONE, wild.getColor(), "Wild card should have NONE color.");

    }
    @Test
    void wildCardType(){
        assertEquals(Card.Type.WILD, wild.getType(), "Card type should be WILD ");
    }
    @Test
    void testGetColor() {
        assertEquals(Card.Color.RED, card.getColor());
    }

    /**Testing for getter getType
     *
     */
    @Test
    void testGetType() {
        assertEquals(Card.Type.FIVE, card.getType());
    }

    /** Testing for toString
     *
     */
    @Test
    void testToString() {
        String expected = "RED FIVE";
        assertEquals(expected, card.toString());
    }
}
