package Milestone1;

import Milestone1.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    /**
     * Private member variable to hold the list of Card objects
     */
    private List<Card> cards;

    /**
     * Deck() creates a new Deck object with a list of cards and shuffles it
     */
    public Deck() {
        cards = new ArrayList<>();
        createDeck();
        shuffle();
    }

    /**
     * createDeck() fills the cards list with card objects representing the deck
     * 108 cards, 76 number cards, 24 action cards, and 8 wild cards.
     */
    public void createDeck() {

        this.cards.clear();  // make sure cards are empty

        for (Card.Color color : Card.Color.values()) {
            if (color != Card.Color.NONE) {

                this.cards.add(new Card(color, Card.Type.ZERO));

                for (int i = 0; i < 2; i++) {
                    for (int val = 1; val <= 9; val++) { // 1-9
                        this.cards.add(new Card(color, Card.Type.values()[val]));
                    }

                    this.cards.add(new Card(color, Card.Type.SKIP));
                    this.cards.add(new Card(color, Card.Type.REVERSE));
                    this.cards.add(new Card(color, Card.Type.DRAW_ONE));
                }
            }
        }

        for (int i = 0; i < 4; i++) {
            this.cards.add(new Card(Card.Color.NONE, Card.Type.WILD));
            this.cards.add(new Card(Card.Color.NONE, Card.Type.WILD_DRAW_TWO));
        }
    }


    /**
     * draw() removes and returns the top card form the deck.
     * @return Card
     */
    public Card draw() {
        if (cards.isEmpty()) {
            refill();
            shuffle();

        }
        return cards.remove(0);

    }

    /**
     * refill() refills the deck with cards
     */
    private void refill() {

        createDeck();

    }

    /**
     * shuffle() randomizes the order of the cards using the Collections utility class
     */
    public void shuffle() {

        Collections.shuffle(cards);
    }

    /**
     * getSize() gets the curr size of deck
     * @return int
     */
    public int getSize() {

        return cards.size();
    }
}
