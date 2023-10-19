package Milestone1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/** Testing cases for testing Deck Class methods
 *
 */
class DeckTest {
    /** Testing for deckSize at start of game (Size after creation)
     *
     */
    private Deck deck;
    private Deck shuffledDeck;


    @BeforeEach
    void setUp(){
        deck = new Deck();
        shuffledDeck = new Deck();
    }

    /**Testing case for when a card is drawn the decksize is decremented
     *
     */
    @Test
    void testDrawReducesDeckSize() {
        int originalSize = deck.getSize();
        deck.draw();
        assertEquals(originalSize - 1, deck.getSize(), "Drawing a card should reduce deck size by one.");
    }

    /**
     * Testing case for picking up a invalid card
     */
    @Test
    void testDrawReturnsValidCard() {
        Card card = deck.draw();
        assertNotNull(card, "Drawn card should not be null.");
        assertNotNull(card.getColor(), "Drawn card should have a valid color.");
        assertNotNull(card.getType(), "Drawn card should have a valid type.");
    }

    /** Testing case for deck size after it is created
     *
     */
    @Test
    void deckSizeAfterCreation() {
        int expectedSize = 108;
        assertEquals(expectedSize, deck.getSize());
    }

    /** Test case after a card is drawn from the deck
     *
     */
    @Test
    void deckSizeAfterDraw() {
        int initialSize = deck.getSize();
        deck.draw();
        assertEquals(initialSize -1, deck.getSize());
    }

    /** Testing case when all cards are drawn from deck
     *
     */
    @Test
    void deckSizeAfterDrawingAllCards() {
        int initialSize = deck.getSize();
        for (int i = 0; i < initialSize; i++) {
            deck.draw();
        }
        assertEquals(0, deck.getSize());
    }

    /** Testing case the deck is refilling after emptied
     *
     */
    @Test
    void deckRefillsAndShuffles(){
        while(deck.getSize() > 0){
            deck.draw();
        }
        deck.draw();
        assertTrue(deck.getSize()>0, "Deck should have been refilled after being emptied");
    }

    /** Testing case for shuffle to change order in deck
     *
     */
    @Test
    void testShuffleChangesOrder() {
        shuffledDeck.shuffle();
        boolean differentOrder = false;
        for (int i = 0; i < deck.getSize(); i++) {
            if (!deck.draw().toString().equals(shuffledDeck.draw().toString())) {
                differentOrder = true;
                break;
            }
        }
        assertTrue(differentOrder);
    }
}
