package Milestone3.Model;
/**
 * PlayerModel class represents the model of a player.Each player has a name,a hand(list of cards) and a score.
 * @Author: Ayman Kamran, Rebecca Li
 */
import java.util.ArrayList;

public class PlayerModel {
    /**
     * Players name and cards in players hands
     */
    private String name;
    private ArrayList<CardModel> hand;
    private int score;

    private boolean isHuman;

    /**
     * Player() creates a new Player object with the given name and a new hand of cards
     * @param name
     */
    public PlayerModel(String name, Boolean isHuman){
        this.name = name;
        this.hand = new ArrayList<CardModel>();
        this.score = 0;
        this.isHuman = isHuman;
    }

    /**
     * playCard() removes a card from players hand
     * @param card
     */
    public void playCard(CardModel card){
        hand.remove(card);
    }

    /**
     * pickUpCard() adds a card to the players hand
     * @param card
     */
    public void pickUpCard(CardModel card){
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
     * getHand() gets the players hand of cards
     * @return
     */
    public ArrayList<CardModel> getHand(){

        return hand;
    }

    /**
     * emptyHand() empties a players hand
     */
    public void emptyHand(){

        this.hand = new ArrayList<CardModel>();
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

    public boolean isHuman() {
        return isHuman;
    }
}

