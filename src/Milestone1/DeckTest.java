package Milestone1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/** Testing cases for testing Deck Class
 *
 */
class DeckTest {
    /** Testing for deckSize at start of game (Size after creation)
     *
     */
    @Test
    void deckSizeAfterCreation() {
        Deck deck = new Deck();
        assertEquals(108, deck.getSize());
    }

    /** Test case after a card is drawn from the deck
     *
     */
    @Test
    void deckSizeAfterDraw() {
        Deck deck = new Deck();
        deck.draw();
        assertEquals(107, deck.getSize());
    }

    /** Testing case when all cards are drawn from deck
     *
     */
    @Test
    void deckSizeAfterDrawingAllCards() {
        Deck deck = new Deck();
        for (int i = 0; i < 108; i++) {
            deck.draw();
        }
        assertEquals(0, deck.getSize());
    }

    /** Testing case for shuffle to change order
     *
     */
    @Test
    void testShuffleChangesOrder() {
        Deck originalDeck = new Deck();
        Deck shuffledDeck = new Deck();
        shuffledDeck.shuffle();

        boolean differentOrder = false;
        for (int i = 0; i < originalDeck.getSize(); i++) {
            if (!originalDeck.draw().toString().equals(shuffledDeck.draw().toString())) {
                differentOrder = true;
                break;
            }
        }
        assertTrue(differentOrder);
    }
}
