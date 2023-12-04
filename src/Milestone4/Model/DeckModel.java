package Milestone4.Model;

/**
 * DeckModel represents the logic behind the deck.It holds a list of cards.
 *
 * @Author:Ayman Kamran, Rebecca Li
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DeckModel {
    /**
     * Private member variable to hold the list of Card objects
     */
    private List<CardModel> cards;
    private List<CardSideModel> lightSides;
    private List<CardSideModel> darkSides;
    private LinkedList<CardModel> deck;


    /**
     * Deck() creates a new Deck object with a list of cards and shuffles it
     */
    public DeckModel() {
        cards = new ArrayList<>();
        this.lightSides = new ArrayList<>();
        this.darkSides = new ArrayList<>();
        this.createSides();
        createDeck();
    }

    /**
     * createSides generate all light side of cards and all dark side of cards
     */
    public void createSides() {
        for (CardSideModel.Color color : CardSideModel.Color.values()) {
            if (color != CardSideModel.Color.NONE) {
//                this.cards.add(new Milestone3.Model.CardSideModel.CardModel(color, Milestone3.Model.CardSideModel.Type.ZERO));
                boolean isLight = color == CardSideModel.Color.BLUE || color == CardSideModel.Color.GREEN ||
                        color == CardSideModel.Color.RED || color == CardSideModel.Color.YELLOW;
                for (int i = 0; i < 2; i++) {
                    for (int val = 1; val <= 9; val++) { // 1-9
                        if (isLight) {
                            this.lightSides.add(new CardSideModel(color, CardSideModel.Type.values()[val]));
                        } else {
                            this.darkSides.add(new CardSideModel(color, CardSideModel.Type.values()[val]));
                        }
                    }
                    if (isLight) {
                        this.lightSides.add(new CardSideModel(color, CardSideModel.Type.SKIP));
                        this.lightSides.add(new CardSideModel(color, CardSideModel.Type.REVERSE));
                        this.lightSides.add(new CardSideModel(color, CardSideModel.Type.DRAW_ONE));
                        this.lightSides.add(new CardSideModel(color, CardSideModel.Type.FLIP));
                    } else {
                        this.darkSides.add(new CardSideModel(color, CardSideModel.Type.SKIP_EVERYONE));
                        this.darkSides.add(new CardSideModel(color, CardSideModel.Type.REVERSE));
                        this.darkSides.add(new CardSideModel(color, CardSideModel.Type.DRAW_FIVE));
                        this.darkSides.add(new CardSideModel(color, CardSideModel.Type.FLIP));
                    }
                }
            }
        }

        for (int i = 0; i < 4; i++) {
            this.lightSides.add(new CardSideModel(CardSideModel.Color.NONE,
                    CardSideModel.Type.WILD));
            this.lightSides.add(new CardSideModel(CardSideModel.Color.NONE,
                    CardSideModel.Type.WILD_DRAW_TWO));
            this.darkSides.add(new CardSideModel(CardSideModel.Color.NONE,
                    CardSideModel.Type.WILD));
            this.darkSides.add(new CardSideModel(CardSideModel.Color.NONE,
                    CardSideModel.Type.WILD_DRAW_COLOR));
        }
    }

    /**
     * createDeck() fills the cards list with card objects representing the deck
     * 108 cards, 76 number cards, 24 action cards, and 8 wild cards.
     */
    public void createDeck() {
        this.cards.clear();  // make sure cards are empty
        Collections.shuffle(this.lightSides);  // shuffle light sides
        Collections.shuffle(this.darkSides);  // shuffle dark sides
        for (int i = 0; i < this.darkSides.size(); i++) {
            this.cards.add(new CardModel(this.lightSides.get(i), this.darkSides.get(i)));
        }
    }


    /**
     * draw() removes and returns the top card form the deck.
     * @return Card
     */
    public CardModel draw() {
        if (cards.isEmpty()) {
            refill();

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
     * getSize() gets the curr size of deck
     * @return int
     */
    public int getSize() {

        return cards.size();
    }

    public void setDeck(LinkedList<CardModel> testDeck) {
        this.deck = testDeck; // Replace the current deck with the test deck
    }

    /**
     * addToTop inserts the card to the top of the deck
     * @param addedCard
     */
    public void addToTop(CardModel addedCard){
        this.cards.add(0, addedCard);
    }
}
