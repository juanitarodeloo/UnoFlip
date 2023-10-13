//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package Ignore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> cards = new ArrayList();

    public Deck() {
        this.initializeDeck();
    }

    public void addCardToBottom(Card card) {

        this.cards.add(card);
    }

    private void initializeDeck() {
        int j;
        for(j = 0; j < 2; ++j) {
            for(int i = 1; i <= 9; ++i) {
                this.cards.add(new Card(Color.BLUE, Value.values()[i], Color.PINK, Value.values()[i]));
                this.cards.add(new Card(Color.GREEN, Value.values()[i], Color.TEAL, Value.values()[i]));
                this.cards.add(new Card(Color.RED, Value.values()[i], Color.ORANGE, Value.values()[i]));
                this.cards.add(new Card(Color.YELLOW, Value.values()[i], Color.PURPLE, Value.values()[i]));
            }
        }

        for(j = 0; j < 8; ++j) {
            this.cards.add(new Card(Color.RED, Value.DRAW_ONE, Color.PINK, Value.DRAW_FIVE));
            this.cards.add(new Card(Color.YELLOW, Value.REVERSE, Color.TEAL, Value.REVERSE));
            this.cards.add(new Card(Color.GREEN, Value.SKIP, Color.ORANGE, Value.SKIP_EVERYONE));
        }

        for(j = 0; j < 4; ++j) {
            this.cards.add(new Card(Color.WILD, Value.WILD, Color.WILD, Value.WILD));
            this.cards.add(new Card(Color.WILD, Value.WILD_DRAW_TWO, Color.WILD, Value.WILD_DRAW_COLOR));
        }

        this.shuffleDeck();
    }

    public void shuffleDeck() {

        Collections.shuffle(this.cards);
    }

    public Card draw() {

        return (Card)this.cards.remove(this.cards.size() - 1);
    }

    public boolean isEmpty() {

        return this.cards.isEmpty();
    }

    public void refill(List<Card> discardPile) {
        this.cards.addAll(discardPile);
        Collections.shuffle(this.cards);
    }
}
