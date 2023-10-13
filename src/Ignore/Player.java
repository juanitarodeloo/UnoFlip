//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package Ignore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Player {
    private String name;
    private List<Card> hand;

    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList();
    }

    public String getName() {

        return this.name;
    }

    public List<Card> getHand() {

        return this.hand;
    }

    public void drawCard(Card card) {

        this.hand.add(card);
    }

    public Card playCard(int index) {

        return (Card)this.hand.remove(index);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        int index = 1;
        Iterator var3 = this.hand.iterator();

        while(var3.hasNext()) {
            Card card = (Card)var3.next();
            sb.append(index++).append(". ").append(card).append("\n");
        }

        return sb.toString();
    }
}
