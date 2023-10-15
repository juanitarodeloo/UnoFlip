/**
 * Player class represents the player in game and the name and cards in their hand
 */
package Milestone1;

import java.util.ArrayList;
import java.util.List;

public class Player {
    /** Players name and cards in players hands
     *
     */
    private String name;
    private List<Card> hand;

    /** Constructor that initializes the players with a name and a new hand of cards
     *
      * @param name
     */
    public Player(String name){
        this.name = name;
        this.hand = new ArrayList<>();
    }

    /** Plays a card from players hand, true if card was in players hand and is removed
     *
     * @param card
     * @return
     */
    public boolean playCard(Card card){
        return hand.remove(card);

    }

    /** Adds a card to the players hand
     *
     * @param card
     */
    public void pickUpCard(Card card){
        hand.add(card);

    }

    /**Returns a string representation of the player and their hand of cards
     *
     * @return
     */
    public String toString(){
        return "Player: " + name + "\nHand: " + hand;

    }

    /** Getter method to aquire the players name
     *
     * @return
     */
    public String getName(){
        return name;
    }

    /** Getter method for the player's hand of cards
     *
     * @return
     */
    public List<Card> getHand(){
        return hand;
    }







}


