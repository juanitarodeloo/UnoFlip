/**
 * MainGame class will contain the implementation of how the game is created, controlled and finished
 */
package Milestone1;

import com.sun.tools.javac.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainGame {

    private Deck myDeck;
    private List<Card> discardPile;
    private List<Player> players;
    private int currPlayer;
    private final int initNumOfCards = 7;
    private boolean gameDone;


    public MainGame(){
        myDeck = new Deck();
        discardPile = new ArrayList<Card>();
        currPlayer = 1; //TODO: idk about this
    }

    /**
     * run() will initiate the game by promting the user with number of players and their names,
     * initializing the players, displaying the starting card, controlling the current player,...
     * @return void
     */
    public void run(){
        Card currCard;
        System.out.println("Welcome to our UNO Game!");
        players = initPlayers();
        //TODO: display starting card

        while(!gameDone){
            //TODO: pick first player and have them take their turn
            // scan player's response (i.e BLUE NINE)
            // currCard = card played by player
            //  validate player's card
        }

        //have the rest of players go


    }

    /**
     * initPlayers() will initialize players based on the user's input
     * @return a list of players
     */
    private List<Player> initPlayers(){
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of players (2-10): ");
        int numOfPlayers = sc.nextInt();
        sc.nextLine();

        for(int i = 1; i < numOfPlayers; i++) {
            System.out.print("Enter name for Player " + i + ": ");
            String playerName = sc.nextLine();
            //Player currPlayer = new Player(playerName); //TODO: player constructor should just take in a name

//            for(int j = 0; j < initNumOfCards; j++) {
//                currPlayer.drawCard(myDeck.draw()); //TODO: player and deck should have respective methods
//            }

            //players.add(player);
        }

        return players;
    }

    private boolean validateCard(Card prevCard, Card cardPlayed){
        //TODO: check if card is basic or action then validate accordingly, use switch case
        // if valid, return true, else return false

        return false;
    }



    public static void main(String[] args) {
        System.out.println("hi");
    }
}
