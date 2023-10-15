/**
 * The Deck class represent a deck of cards in the game of UNO
 * This class allows a collection of card objects, with operations that allow us to.
 * create a full deck, shuffle, drawing a card, refilling an empty deck
 */

package Milestone1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    /** Private member variable to hold the list of Card objects
     *
     */
    private List<Card> cards;

    /**Public constructor for the Deck class
     *
     */
    public Deck() {
        cards = new ArrayList<>();
        createDeck();
        shuffle();
    }

    /** Method createDeck to fill the cards list with card objects representing the deck
     *
     */
    private void createDeck() {

        for (Card.Color color : Card.Color.values()) {
            for (Card.Type type : Card.Type.values()) {
                if (!type.equals(Card.Type.WILD) && !type.equals(Card.Type.WILD_DRAW_TWO)) {
                    for (int i = 0; i < 2; i++) {
                        cards.add(new Card(color, type));
                    }
                }
            }
        }
        for (int i = 0; i < 4; i++) {
            cards.add(new Card(null, Card.Type.WILD));
            cards.add(new Card(null, Card.Type.WILD_DRAW_TWO));
        }

    }

    /** Method draw to remove and retunr the top card form the deck
     *
     * @return
     */
    public Card draw() {
        if (cards.isEmpty()) {
            refill();
            shuffle();

        }
        return cards.remove(0);

    }

    /** Private method to refil the deck with cards
     *
     */
    private void refill() {
        createDeck();

    }

    /**Private method to shuffle the deck using the Collections utility class
     *
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**Public method to get the curr size of deck, basically what the number of cards left
     *
     * @return
     */
    public int getSize() {
        return cards.size();
    }


}
