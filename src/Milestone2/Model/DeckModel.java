/**
 *DeckModel represents the logic behind the deck.It holds a list of cards.
 *@Author:Ayman Kamran, Rebecca Li
 */
package Milestone2.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeckModel {
    /**
     * Private member variable to hold the list of Card objects
     */
    private List<CardModel> cards;

    /**
     * Deck() creates a new Deck object with a list of cards and shuffles it
     */
    public DeckModel() {
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

        for (CardModel.Color color : CardModel.Color.values()) {
            if (color != CardModel.Color.NONE) {

                this.cards.add(new CardModel(color, CardModel.Type.ZERO));

                for (int i = 0; i < 2; i++) {
                    for (int val = 1; val <= 9; val++) { // 1-9
                        this.cards.add(new CardModel(color, CardModel.Type.values()[val]));
                    }

                    this.cards.add(new CardModel(color, CardModel.Type.SKIP));
                    this.cards.add(new CardModel(color, CardModel.Type.REVERSE));
                    this.cards.add(new CardModel(color, CardModel.Type.DRAW_ONE));
                }
            }
        }

        for (int i = 0; i < 4; i++) {
            this.cards.add(new CardModel(CardModel.Color.NONE, CardModel.Type.WILD));
            this.cards.add(new CardModel(CardModel.Color.NONE, CardModel.Type.WILD_DRAW_TWO));
        }
    }


    /**
     * draw() removes and returns the top card form the deck.
     * @return Card
     */
    public CardModel draw() {
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
