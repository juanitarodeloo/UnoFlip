/**
 * Testing class for methods in Card Class
 * Author: Ayman Kamran
 */
package Milestone1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    private Card card;
    private Card wild;

    @BeforeEach
    void setUp(){
        card = new Card(Card.Color.RED, Card.Type.FIVE);
        wild = new Card(Card.Color.NONE, Card.Type.WILD);

    }

    /**
     * Tests the case for wild card color(No color)
     */
    @Test
    void wildCardColor(){

        assertEquals(Card.Color.NONE, wild.getColor(), "Wild card should have NONE color.");

    }

    /**
     * Tests the case for WildCard
     */
    @Test
    void wildCardType(){

        assertEquals(Card.Type.WILD, wild.getType(), "Card type should be WILD ");
    }

    /**
     * Tests the case for getting color
     */
    @Test
    void testGetColor() {

        assertEquals(Card.Color.RED, card.getColor());
    }

    /**
     * Testing for getter getType
     */
    @Test
    void testGetType() {

        assertEquals(Card.Type.FIVE, card.getType());
    }

    /**
     * Testing case for setting colors
     */
    @Test
    void testSetColor() {
        card.setColor(Card.Color.BLUE);
        assertEquals(Card.Color.BLUE, card.getColor(), "Card color should be updated to BLUE.");
    }

    /**
     * Testing case for setting Type
     */
    @Test
    void testSetType() {
        card.setType(Card.Type.ZERO);
        assertEquals(Card.Type.ZERO, card.getType(), "Card type should be updated to ZERO.");
    }
    /**
     * Testing for toString card
     */
    @Test
    void testToString() {
        String expected = "RED FIVE";
        assertEquals(expected, card.toString());
    }
}

