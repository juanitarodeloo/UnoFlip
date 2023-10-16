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


    private final int initNumOfCards = 7;



    public MainGame(){
        myDeck = new Deck();
        discardPile = new ArrayList<Card>();
        players = new ArrayList<Player>();

    }

    /**
     * run() will initiate the game by prompting the user with number of players and their names,
     * initializing the players, displaying the starting card, controlling the current player,...
     * @return void
     */
    public void run(){
        int currPlayerIndex = 0;
        boolean gameDone = false;
        Card currCard;
        Player currentPlayer = null;
        boolean validCardPlayed;
        Scanner sc = new Scanner(System.in); //TODO: close sc at the end
        String cardResponse;
        Card chosenCard;
        Card pickUpCard;
        String playPickUpCardResponse;
        boolean skipCurrentPlayer; //this var holds whether the current player is not playing after picking up a card because they
        //did not have a matching card
        boolean skipNextPlayer; //this var holds whether the current player has a SKIP card
        boolean goBackToLastPerson; //this var holds whether the current player has a REVERSE card


        System.out.println("\nWelcome to our UNO Game!\n");
        // initialize players
        players = initPlayers();

        // display starting card
        currCard = myDeck.draw();
        discardPile.add(currCard);


        // Pick first player and display their hand to them
        currentPlayer = players.get(currPlayerIndex);

        // iterate through players to allow them to take their turn
        while(!gameDone){
            validCardPlayed = false;
            skipCurrentPlayer = false;
            chosenCard = null;
            skipNextPlayer = false;
            goBackToLastPerson = false;

            System.out.println("\nTop card: " + currCard);
            System.out.println(currentPlayer.getName() + ", it's your turn.");
            currentPlayer.printHand();

            while(!validCardPlayed) {
                System.out.println("Enter the card you wish to play (i.e BLUE NINE) or enter 'None' to pick up a card: ");
                cardResponse = sc.nextLine();


                if (cardResponse.equalsIgnoreCase("None")) {
                    pickUpCard = myDeck.draw(); //draw from top of the deck
                    currentPlayer.getHand().add(pickUpCard);
                    currentPlayer.printHand();
                    System.out.println("Would you like to play the card you just picked up? (Enter yes/no)");
                    playPickUpCardResponse = sc.nextLine();
                    if (playPickUpCardResponse.equalsIgnoreCase("yes")) {
                        chosenCard = pickUpCard;
                    } else {
                        skipCurrentPlayer = true;
                        validCardPlayed = true;
                    }

                } else {
                    //else player played a card in their hand without picking up
                    for (Card card : currentPlayer.getHand()) {
                        // check that the card they played exists in their hand
                        if (cardResponse.equalsIgnoreCase(card.toString())) {
                            chosenCard = card;
                            break;
                        }
                    }


                    if (chosenCard == null) {
                        System.out.println("Invalid card. Try again.");
                        continue;
                    }
                }

                if (!skipCurrentPlayer) {
                    // Validate players' card
                    validCardPlayed = validateCard(currCard, chosenCard);
                    if (validCardPlayed) {
                        currentPlayer.playCard(chosenCard);
                        discardPile.add(chosenCard);
                        currCard = chosenCard;
                        System.out.println(currentPlayer.getName() + " played " + chosenCard); //TODO: take this out at the end - helpful for us to debug
                        //checking if card is an action card:
                        //TODO: first check if card is a WILD card and handle that
                        skipNextPlayer = handleSkip(chosenCard);
                        if(!skipNextPlayer){
                            goBackToLastPerson = handleReverse(chosenCard);
                            //if player played a REVERSE and there are only two players, treat it like a SKIP
                            if(goBackToLastPerson && players.size() == 2){ //TODO: idk if this should be equals instead
                                skipNextPlayer = true;
                            }

                        }
                        

                    } else {
                        System.out.println("Invalid card, please try again!"); //TODO: you should only have one of these messages
                    }
                }
            } //end of validate while loop

            //have the rest of players go
            if(currentPlayer.getHand().isEmpty()) { // Assuming Player class has getHandSize method, can be adjusted
                //System.out.println("here1");
                gameDone = true;
                System.out.println(currentPlayer.getName() + " has won the game!");
            }else{

                if(skipNextPlayer){ //if player played a SKIP
                    
                    currPlayerIndex = (players.indexOf(currentPlayer) + 2) % players.size(); //Cycle through players - skip the next player
                
                }else if(goBackToLastPerson){ //if player played a REVERSE
                    
                    currPlayerIndex = (players.indexOf(currentPlayer) - 1) % players.size(); //Cycle through players - go back to prev player
                    if(currPlayerIndex < 0){
                        currPlayerIndex = players.size() - 1; 
                    }
                    
                }else{
                    currPlayerIndex = (players.indexOf(currentPlayer) + 1) % players.size(); //Cycle through players - go back to prev player
                }
                
                System.out.println("currplayerIndex = " + currPlayerIndex); //TODO: take out at the end
                currentPlayer = players.get(currPlayerIndex);
            }






        } //end of game while loop



    }

    /**
     * initPlayers() will initialize players based on the user's input
     * @return a list of players
     */
    private List<Player> initPlayers(){
        int numOfPlayers = 0;
        String playerName = null;
        Player currPlayer = null;
        Scanner sc = new Scanner(System.in); //TODO: close sc at the end 

        System.out.print("Enter number of players (2-10): ");
        numOfPlayers = sc.nextInt(); //TODO: there should be a try-catch around this
        sc.nextLine();

        for(int i = 0; i < numOfPlayers; i++) {
            System.out.print("Enter name for Player " + (i+1) + ": ");
            playerName = sc.nextLine();
            currPlayer = new Player(playerName);

            for(int j = 0; j < initNumOfCards; j++) {
                currPlayer.pickUpCard(myDeck.draw()); //TODO: player and deck should have respective methods
            }

            players.add(currPlayer);
        }

        return players;
    }


    /**
     * Validates if the card played is a valid move based on the previous card on the table.
     * @param prevCard The card that was previously played on the table.
     * @param cardPlayed The card that the player wishes to play.
     * @return true if the cardPlayed is a valid move, otherwise false.
     */
    private boolean validateCard(Card prevCard, Card cardPlayed){ //TODO: this isn't working


        //if card played is a wild card, return true
        if(cardPlayed.getType() == Card.Type.WILD || cardPlayed.getType() == Card.Type.WILD_DRAW_TWO){
            return true;
        }

        // else check if types or colours match return true
        if(cardPlayed.getType() == prevCard.getType() || cardPlayed.getColor() == prevCard.getColor()){
            return true;
        }

        // none conditions apply return false
        return false;
    }

    /**
     * handleSkip() determines whether the given card is of type SKIP
     * @param cardPlayed
     * @return true if yes, false if no
     */
    private boolean handleSkip(Card cardPlayed){
        
        if(cardPlayed.getType().equals(Card.Type.SKIP)){
            return true;
        }
        return false;
    }

    /**
     * handleRever() determines whether the given card is of typpe REVERSE
     * @param cardPlayed
     * @return true if yes, false if no
     */
    private boolean handleReverse(Card cardPlayed){

        if(cardPlayed.getType().equals(Card.Type.REVERSE)){
            return true;
        }
        return false;
    }



    public static void main(String[] args) {
        MainGame myGame = new MainGame();
        myGame.run();

    }
}
