package Milestone1;

public class DeckTest {

    public static void main(String[] args) {

        /**Test 1: Deck initialization
         *
         */
        Deck deck = new Deck();
        int initialSize = deck.getSize();
        System.out.println("Test 1 - Deck size: " + initialSize);

        /**Test2: Draw cards, reduce size
         */
        Card card = deck.draw();
        int DrawSize = deck.getSize();
        System.out.println("Test 2 - Deck size after draw: " + DrawSize);

        /**Test 3: Drawing all cards and check refil
         *
         */
        for (int i = 0; i < DrawSize; i++) {
            deck.draw();
        }

        int sizeAfterDraws = deck.getSize();
        System.out.println("Test 3 - Deck size after drawing all cards: " + sizeAfterDraws);

        /**Test 4: Shuffle changes order
         *
         */

        Deck newDeck = new Deck();
        Card topCardBeforeShuffle = newDeck.draw();
        newDeck.shuffle();
        Card topCardAfterShuffle = newDeck.draw();
        boolean shuffleChangesOrder = !topCardBeforeShuffle.equals(topCardAfterShuffle);
        System.out.println("test 4 - Shuffle changes order: " + shuffleChangesOrder);



    }
}

