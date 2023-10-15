/**
 * MainGame class will contain the implementation of how the game is created, controlled and finished
 */
package Milestone1;

import com.sun.tools.javac.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class XMainGame {

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
        // Displaying starting card
        currCard = myDeck.draw();
        discardPile.add(currCard);
        System.out.println("Starting card: " + currCard);

        //TODO: display starting card DONE!

        while(!gameDone){
            // Pick first player and prompt them to take their turn
            Player currentPlayer = players.get(currPlayer);
            System.out.println(currentPlayer.getName() + ", it's your turn.");
            // Scans player response
            Scanner sc = new Scanner(System.in);
            boolean validCardPlayed = false;
            while(!validCardPlayed){
                System.out.print("Enter the card you wish to play (i.e BLUE NINE)");
                String cardResponse = sc.nextLine();
                Card chosenCard = currentPlayer.getCard(cardResponse);
                // Validate player's card
                validCardPlayed = validateCard(currCard, chosenCard);

                if(validCardPlayed){
                    discardPile.add(chosenCard);
                    currCard = chosenCard;
                    System.out.println(currentPlayer.getName() + " played " + chosenCard);
                    currentPlayer.removeCard(chosenCard); //Assuming player has removeCard method, can be adjusted

                } else{
                    System.out.println("Invalid card, please try again!");
                }
            }

            //TODO: pick first player and have them take their turn DONE!
            // scan player's response (i.e BLUE NINE) DONE!
            // currCard = card played by player DONE!
            //  validate player's card DONE!
        }

        //have the rest of players go
        if(currentPlayer.getHandSize() == 0) { // Assuming Player class has getHandSize method, can be adjusted
            gameDone = true;
            System.out.println(currentPlayer.getName() + " has won the game!");
        }else{
            currPlayer = (currPlayer + 1) % players.size(); //Cycle through players
        }

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
        // if valid, return true, else return false DONE!

        if(cardPlayed.getType() == Card.Type.WILD){
            return True;
        }
        if(cardPlayed.getType() == Card.Type.WILD_DRAW_TWO){
            return True;
        }
        // if types match, valid
        if(cardPlayed.getType() == prevCard.getType()){
            return True;
        }
        // if colors match, valid
        if (cardPlayed.getColor() == prevCard.getColor()){
            return True;
        }
        // none conditions apply
        return false;
    }



    public static void main(String[] args) {
        System.out.println("hi");
    }
}
