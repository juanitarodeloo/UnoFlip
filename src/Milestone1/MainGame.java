/**
 * MainGame class will contain the implementation of how the game is created, controlled and finished
 */
package Milestone1;

import com.sun.tools.javac.Main;

import java.util.*;

public class MainGame {

    private Deck myDeck;
    private List<Card> discardPile;
    private List<Player> players;

    // The target color for the next player
    // Use targetColor instead of card color when checking if teh card is valid or not
    private Card.Color targetColor;

    // The order of the game
    // Assume isClockWise is true at beginning
    // Every time the player plays a reverse card, isClockWise = !isClockWise
    // Using 4 players and their index as the example: -----
    // If clockWise is true -> order: 0->1->2->3->0     calculation: (index + 1) % size
    // Else if clockWise is false -> order: 0->3->2->1->0  calculation: (index + (size - 1)) % size
    private boolean isClockWise;
    private final int initNumOfCards = 7;


    public MainGame(){
        myDeck = new Deck();
        discardPile = new ArrayList<Card>();
        players = new ArrayList<Player>();
        this.isClockWise = true;
    }

    /**
     * playGame() initiate the game by prompting the user with number of players and their names,
     * initializing the players,
     * repeat the game (different rounds) until someone wins (one player has more than 500 score)
     */
    public void playGame(){
        boolean gameDone = false;
        Scanner sc = new Scanner(System.in); //TODO: close sc at the end
        System.out.println("\nWelcome to our UNO Game!\n");
        // initialize players
        players = initPlayers();
        int roundNum = 1;  // Round number

        while (!gameDone){
            playARound(roundNum, sc);
            for (Player player: this.players){
                if (player.getScore() >= 500){  // If a player has more than 500 points -> close the game
                    System.out.println(player.getName() + " has won the game!");
                    gameDone = true;
                    break;
                }
            }
            if (!gameDone) { // If the game continues -> start a new round
                // Empty all players' hand, get ready for the next round
                for (Player player : this.players) {
                    player.emptyHand();
                }
            }
            roundNum += 1;
        }
        System.out.println("Game Closed !");
        sc.close();
    }

    /**
     * playARound() plays UNO until one player get rid of his cards
     * @param roundNum
     * @param sc
     */
    private void playARound(int roundNum, Scanner sc){
        System.out.println("   UNO GAME ROUND " + roundNum + "!");
        boolean roundDone = false;
        this.myDeck.createDeck();  // Generate a new deck
        this.myDeck.shuffle(); //shuffle it

        Random random = new Random();
        int currPlayerIndex = random.nextInt(this.players.size());  // choose the first player randomly
        Player currentPlayer = players.get(currPlayerIndex);

        Card currCard;  // the top card
        Card chosenCard;  // the card that current player wants to play

        dealCards();  // Deals cards to each player
        // display starting card
        currCard = this.myDeck.draw();
        discardPile.add(currCard);
        this.targetColor = currCard.getColor();  // Assign current card color to targetColor

        while(!roundDone) {
            System.out.println("\nTop card: " + currCard);
            System.out.println("Target color: " + this.targetColor);
            System.out.println(this.myDeck.getSize() + " cards left in deck\n"); //TODO: remove after testing! only use to test
            System.out.println(currentPlayer.getName() + ", it's your turn.");
            currentPlayer.printHand();

            // This method must return a valid card or null!
            chosenCard = pickOrPlay(sc, currentPlayer, currCard);  // The card player wants to play

            if (chosenCard == null){  // If the player draw a card without play, get next player index
                currPlayerIndex = nextPlayer(players.indexOf(currentPlayer), false);
            }else {  // If the player play a card-> do the card action and get next player index
                currCard = chosenCard;  // updates the top card
                System.out.println(currentPlayer.getName() + " played " + chosenCard); //TODO: take this out at the end - helpful for us to debug
                currPlayerIndex = playACard(chosenCard, currentPlayer, sc);
            }

            if (currentPlayer.getHand().isEmpty()){
                roundDone = true;
                System.out.println(currentPlayer.getName() + " has won the game!");
                updatePlayerPoint(currentPlayer);  // Update the points for the winner of this round
            }else {  // If game continues
                System.out.println("currplayerIndex = " + currPlayerIndex); //TODO: take out at the end
                currentPlayer = players.get(currPlayerIndex);  // Update 'currentPlayer'
            }
        }
    }

    /**
     * dealCard() deal initial cards to each player. This method only called at the beginning of each round
     */
    private void dealCards(){
        for (Player player: this.players){
            drawCards(player, this.initNumOfCards);
        }
    }


    /**
     * drawCards() implements when a player draws one or two card from the deck
     * @param player  the player who will draw card
     * @param NumOfCards  the number of cards that player wants to draw
     */
    private void drawCards(Player player, int NumOfCards){
        for (int i = 0; i < NumOfCards; i++){
            Card drawnCard = this.myDeck.draw();
            player.getHand().add(drawnCard);
            discardPile.add(drawnCard);
        }
    }

    /**
     * pickOrPlay() implements if the current player try to play a card until the valid one or just pick a card
     * @param sc  the scanner
     * @param currentPlayer   the player who will play or draw a card
     * @param currCard the last played card
     * @return  returns null if the player only pick a card, otherwise returns the valid card played by the player.
     */
    private Card pickOrPlay(Scanner sc, Player currentPlayer, Card currCard) {
        boolean validCard = false;
        String actionResponse;
        Card playedCard = null;
        Card pickUpCard;
        String playPickUpCard;
        // If the player choose to pick a card and then play an invalid card -> skip his turn (playedCard = null)
        boolean alreadyPick = false;

        while (!validCard) {
            System.out.println("Enter the card you wish to play (i.e BLUE NINE) or enter 'None' to pick up a card: ");

            actionResponse = sc.nextLine();

            // If the player choose to draw a card
            if (actionResponse.equalsIgnoreCase("None")) {
                pickUpCard = this.myDeck.draw(); //draw from top of the deck
                currentPlayer.getHand().add(pickUpCard);
                currentPlayer.printHand();
                alreadyPick = true;
                System.out.println("Would you like to play the card you just picked up? (Enter yes/no)");
                playPickUpCard = sc.nextLine();
                // If the player choose to play the drawn card
                if (playPickUpCard.equalsIgnoreCase("yes")) {
                    playedCard = pickUpCard;
                }else{
                    validCard = true;
                }
            }else {
                //else player played a card in their hand without picking up
                for (Card card : currentPlayer.getHand()) {
                    // check that the card they played exists in their hand
                    if (actionResponse.equalsIgnoreCase(card.toString())) {
                        playedCard = card;
                        break;
                    }
                }
                if (playedCard == null) {
                    System.out.println("Invalid card. Try again.");
                    continue;
                }
            }
            if (playedCard != null) {
                // Validate players' card
                validCard = validateCard(currCard, playedCard);
                if (!validCard) {
                    if (alreadyPick){
                        // If the player plays the invalid drawn card, he finishes his term without play a card
                        validCard = true;
                        playedCard = null;
                        System.out.println("Invalid card! Your turn has finished!");
                    }else { // If the player doesn't draw a card in this term, continue the loop
                        System.out.println("Invalid card, please try again!");
                    }
                }
            }
        }
        if(playedCard != null){
            System.out.println("PLAYED CARD: " + playedCard.toString());
        }

        return playedCard;
    }

    /**
     * playACard() implement the corresponding card action
     * @param playedCard the card which will be implemented
     * @param currPlayer current player
     * @param sc the scanner
     * @return  the next player's index
     */
    public int playACard(Card playedCard, Player currPlayer, Scanner sc){
        boolean skipNext = false;  // If the next player need to be skipped
        int currPlayerIndex = this.players.indexOf(currPlayer);
        currPlayer.playCard(playedCard);  // Remove played card from player
        switch(playedCard.getType()){
            case SKIP:  // Skip card -> skip next player
                skipNext = true;
                break;
            case REVERSE:  // Reverse card
                if (this.players.size() == 2){  // skip next player
                    skipNext = true;
                }
                this.isClockWise = !this.isClockWise; // reverse the play order
                break;
            case WILD:  // Wild card -> chose target color
                chooseColor(sc);  // player chooses a color
                break;
            case WILD_DRAW_TWO:  // Wild draw two card -> chose target color, next player draw 2 cards, skip next player
                chooseColor(sc);  // player chooses a color
                // Next player draw two cards
                drawCards(this.players.get(nextPlayer(currPlayerIndex, false)), 2);
                skipNext = true;  // skip next player
                break;
            case DRAW_ONE: // Draw one card -> next player draw a card, skip next player
                skipNext = true;  // skip next player
                // Next player draw a card
                drawCards(this.players.get(nextPlayer(currPlayerIndex, false)), 1);
                break;
            //default:  // All other cards (number cards)
        }
        if (playedCard.getColor() != Card.Color.NONE && playedCard.getType() != Card.Type.WILD_DRAW_TWO){  // If the played card is not Wild, update the target color
            this.targetColor = playedCard.getColor();
        }
        return nextPlayer(currPlayerIndex, skipNext);

    }

    /**
     * chooseColor() allows the player to choose the target color, it updates the targetColor
     * @param sc the scanner
     */
    private void chooseColor(Scanner sc){
        String selectedColor;
        System.out.println("Enter the color you want - type g for GREEN, r for RED, b for BLUE, y for YELLOW");
        while (true) {
            selectedColor = sc.nextLine();
            if (validColor(selectedColor)){
                break;
            }else{
                System.out.println("Invalid Color, try again");
            }
        }
    }

    /**
     * validColor() checks and returns if the player input a valid color choice
     * @param selectedColor   the color input by player
     * @return  true if color is valid otherwise false.
     */
    public boolean validColor(String selectedColor){
        if (selectedColor.equalsIgnoreCase("g")){
            this.targetColor = Card.Color.GREEN;
            return true;
        }
        if(selectedColor.equalsIgnoreCase("b")){
            this.targetColor = Card.Color.BLUE;
            return true;
        }
        if( selectedColor.equalsIgnoreCase("r")){
            this.targetColor = Card.Color.RED;
            return true;
        }
        if(selectedColor.equalsIgnoreCase("y")){
            this.targetColor = Card.Color.YELLOW;
            return true;
        }
        return false;
    }

    /**
     * nextPlayer() calculate and return the next player's index
     * @param curPlayIndex  the current player's index in
     * @param skipNext  if the next player is skipped
     * @return  the index of the player who play the next turn
     */
    private int nextPlayer(int curPlayIndex, boolean skipNext){
        // clockwise : (index + 1) % size
        // clockwise + skip: (index + 2) % size
        // counterclockwise : (index + (size - 1)) % size
        // counterclockwise + skip: (index + (size - 2)) % size
        int addedNum = 1;  // the number which is used to calculate
        if (skipNext){
            addedNum = 2;
        }
        if (!this.isClockWise){
            addedNum = this.players.size() - addedNum;
        }
        return (curPlayIndex + addedNum) % this.players.size();
    }

    /**
     * getCardPoint() returns the point for a input card type
     * @param cardType  the type of the uno card
     * @return the point of the uno card
     */
    public int getCardPoint(Card.Type cardType){
        return switch (cardType) {
            case SKIP, REVERSE ->  // Skip, reverse card
                    20;
            case WILD ->  // Wild card
                    40;
            case WILD_DRAW_TWO ->  // Wild draw two card
                    50;
            case DRAW_ONE -> // Draw one card
                    10;
            default ->  // all other cards (number cards)
                // Get the value of the number card by the type index!!! Do not change the order of the Type in Card!
                    Card.Type.valueOf(cardType.toString()).ordinal();
        };
    }

    /**
     * updatePlayerPoint() updates the score of the player who won the current round
     * @param winner player who won current round
     */
    public void updatePlayerPoint(Player winner){
        int points = 0;  // The points which need to be added
        for (Player player: this.players){
            if (!player.equals(winner)){  // If the player is not the winner
                for (Card card: player.getHand()) {  // For each card in the player's hand
                    points += getCardPoint(card.getType());  // add corresponding point
                }
            }
        }
        System.out.println(winner.getName() + " get " + points + "points in this turn.");
        winner.increaseScore(points);  // Add the total points to the winner
        System.out.println(winner.getName() + " has " + winner.getScore() + "points in total.");
    }

    /**
     * initPlayers() will initialize players based on the user's input
     * @return a list of players
     */
    private List<Player> initPlayers(){
        int numOfPlayers = 0;
        boolean validNumOfPlayers = false;
        String playerName = null;
        Player currPlayer = null;
        Scanner sc = new Scanner(System.in); //TODO: close sc at the end 

        while(!validNumOfPlayers){
            System.out.print("Enter number of players (2-4): ");
            try{
                numOfPlayers = sc.nextInt();
                sc.nextLine();

                if(numOfPlayers >= 2 && numOfPlayers <= 4){
                    validNumOfPlayers = true;
                }else{
                    System.out.println("Invalid # of players. Please enter a number between 2 and 4: ");
                }
            }catch(Exception e){
                System.out.println("Invalid input. Try again: ");
                sc.nextLine();
            }
        }



        for(int i = 0; i < numOfPlayers; i++) {
            System.out.print("Enter name for Player " + (i+1) + ": ");
            playerName = sc.nextLine();
            currPlayer = new Player(playerName);
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
    public
    boolean validateCard(Card prevCard, Card cardPlayed){ //TODO: this isn't working

        // If the first card is Wild card, the player can play any card
        if (this.targetColor == Card.Color.NONE){
            return true;
        }

        // if card played has no color, return true
        if(cardPlayed.getColor() == Card.Color.NONE){
            return true;
        }

        // else check if types or colours match return true
        if(cardPlayed.getType() == prevCard.getType() || cardPlayed.getColor() == this.targetColor){
            return true;
        }

        // none conditions apply return false
        return false;
    }

    /**
     * setTargetColor() assigns the new color to targetTarget.
     * This method only used in MainGameTest
     * @param newColor  the color which will be assigned to the target color
     */
    public void setTargetColor(Card.Color newColor){
       this.targetColor = newColor;
    }

    /**
     * addPlayer() adds player to the player list
     * This method only used in MainGameTest
     * @param player  the player which will be added to the player list
     */
    public void addPlayer(Player player){
        this.players.add(player);
    }

    /**
     * getPlayer() returns a list of players in this game.
     * Only used in MainGameTest
     * @return a list of players in this game
     */
    public List<Player> getPlayer(){
        return this.players;
    }

    /**
     * getDeck() returns the deck using in this game.
     * Only used in MainGameTest
     * @return the deck which is used in this game
     */
    public Deck getDeck(){
        return this.myDeck;
    }

    public static void main(String[] args) {
        MainGame myGame = new MainGame();
        myGame.playGame();
    }
}
