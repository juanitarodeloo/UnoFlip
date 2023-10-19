/**
 * Player class represents the player in game and the name and cards in their hand
 */
package Milestone1;

import java.util.ArrayList;
import java.util.List;

public class Player {
    /**
     * Players name and cards in players hands
     */
    private String name;
    private List<Card> hand;
    private int score;

    /**
     * Constructor that initializes the players with a name and a new hand of cards
     * @param name
     */
    public Player(String name){
        this.name = name;
        this.hand = new ArrayList<>();
        this.score = 0;
    }

    /**
     * Removes a card from players hand
     * @param card
     */
    public void playCard(Card card){
        hand.remove(card);

    }

    /**
     * Adds a card to the players hand
     * @param card
     */
    public void pickUpCard(Card card){
        hand.add(card);

    }

    /**
     * Returns a string representation of the player and their hand of cards
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

    /**Setter for setting a players name
     *
     * @param newName
     */
    public void setName(String newName){
        this.name = newName;
    }

    /** Getter method for the player's hand of cards
     *
     * @return
     */
    public List<Card> getHand(){
        return hand;
    }

    /** Empties a players hand
     *
     */
    public void emptyHand(){
        this.hand = new ArrayList<Card>();
    }

    /**Getter gets the players score
     *
     * @return
     */
    public int getScore(){
        return this.score;
    }

    /** Increases the Original score
     *
     * @param addedScore
     */
    public void increaseScore(int addedScore){
        this.score += addedScore;
    }

    /**Prints the hand of the Player
     *
     */
    public void printHand(){
        System.out.println("Your cards: \n" + getHand());
    }
}



