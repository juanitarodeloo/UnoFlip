/**
 * Player class represents the player in game and the name and cards in their hand
 * Author: Ayman Kamran
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
     * Player() creates a new Player object with the given name and a new hand of cards
     * @param name
     */
    public Player(String name){
        this.name = name;
        this.hand = new ArrayList<>();
        this.score = 0;
    }

    /**
     * playCard() removes a card from players hand
     * @param card
     */
    public void playCard(Card card){
        hand.remove(card);

    }

    /**
     * pickUpCard() adds a card to the players hand
     * @param card
     */
    public void pickUpCard(Card card){
        hand.add(card);

    }

    /**
     * toString() returns a string representation of the player and their hand of cards
     * @return
     */
    public String toString(){
        return "Player: " + name + "\nHand: " + hand;

    }

    /**
     * getName() returns the player name
     * @return
     */
    public String getName(){

        return name;
    }

    /**
     * setName() sets a players name
     * @param newName
     */
    public void setName(String newName){

        this.name = newName;
    }

    /**
     * getHand() gets the players hand of cards
     * @return
     */
    public List<Card> getHand(){

        return hand;
    }

    /**
     * emptyHand() empties a players hand
     */
    public void emptyHand(){

        this.hand = new ArrayList<Card>();
    }

    /**
     * getScore() gets the players score
     * @return
     */
    public int getScore(){

        return this.score;
    }

    /**
     * increaseScore() increases the Original score
     * @param addedScore
     */
    public void increaseScore(int addedScore){

        this.score += addedScore;
    }

    /**
     * printHand() prints the hand of the Player
     */
    public void printHand(){

        System.out.println("Your cards: \n" + getHand());
    }
}



