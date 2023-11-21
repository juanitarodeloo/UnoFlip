/**
 * UnoModel class constains the main model of the game. It contains the data-related logic that the user works with.
 * @Authors: Rebecca Li, Juanita Rodelo, Adham Elmahi
 */
package Milestone3.Model;

import Milestone3.View.UnoView;

import java.util.*;

public class UnoModel {
    private ArrayList<PlayerModel> players;
    private DeckModel myDeck;
    private List<CardModel> discardPile;
    // The target color for the next player
    // Use targetColor instead of card color when checking if the card is valid or not
    private CardSideModel.Color targetColor;
    private CardModel topCard;  // top card
    private boolean drawUntilColor = false;  // indicate if current player needs to draw until specific card
    private int numSkip = 0; // indicate how many players need to be skipped
    private boolean isLight;  // indicate if the current hands show light side of cards
    private boolean isClockWise;  // direction
    private PlayerModel currentPlayer;
    private int roundNum; // Round number
    private int needToDraw = 0;  // the number of cards the player need to draw
    // Indicate if the skip has finished -----
    // false means next player is skipped
    // true means current player is skipped and has finished the turn
    // set to false when player plays a skip card
//    private boolean finishSkip = true;
    private String nextMessage;  // Message Constant
    private PlayerModel roundWinner = null;  // The winner of the current round
    private int tempPlayerNum = 2;  // used for initialize number of player
    private final int initNumOfCards = 7;
    private UnoView unoView;
    private boolean valid_wild_draw_two; //holds whether the wild draw two was played properly
    private int numOfHumanPlayers;

    private int numOfAIplayers;

    public UnoModel(){
        myDeck = new DeckModel();
        discardPile = new ArrayList<>();
        players = new ArrayList<>();
        numOfHumanPlayers = 0;
        numOfAIplayers = 0;
    }

    /**
     * directionToString converts the direction information to String
     * @return
     */
    public String directionString(){
        if (this.isClockWise){
            return "Clockwise";
        }else {
            return "CounterClockwise";
        }
    }

    /**
     * sideString converts the card side information to String
     * @return
     */
    public String sideString(){
        if (this.isLight){
            return "Light Side";
        }else {
            return "Dark Side";
        }
    }

    /**
     * setUnoView assigns unoView
     * @param unoView
     */
    public void setUnoView(UnoView unoView){
        this.unoView = unoView;
    }

    /**
     * setTemPlayerNum updates the temporal player name, this is called after player choose the number of players
     * @param playerNum
     */
    public void setTempPlayerNum(int playerNum){
        this.tempPlayerNum = playerNum;
    }

    /**
     * initGame initializes the Uno game
     */
    public void initGame(){
        this.roundNum = 1;
        this.initPlayers();  // Initialize players in this game
        this.unoView.startGame(this.players);
        this.initRound();  // Initialize the first round in this game
    }

    /**
     * initRound init each round of the UNO game.
     */
    public void initRound(){
        this.isClockWise = true;
        this.isLight = true;
        this.roundWinner = null;  // No round winner yet
        this.nextMessage = null;
        this.myDeck.createDeck();  // make sure this is a new round -> new deck
        this.initPlayerHands();  // Draw cards for each player
        this.topCard = this.getTargetCard();  // Get the start card in the current round
        this.targetColor = this.topCard.getCard(this.isLight).getColor();  // Get target color from the top card
        Random random = new Random();
        int currPlayerIndex = random.nextInt(this.players.size());  // choose the first player randomly
        this.currentPlayer = players.get(currPlayerIndex);  // Set current player
        this.unoView.updateRoundInfo(this.roundNum);  // Update round information
        this.unoView.setBeforeEachTurn(new UnoGameEvent(this, this.currentPlayer,
                MessageConstant.normalTurn, this.topCard.getCard(this.isLight), this.targetColor,
                this.directionString(), this.isLight, this.sideString()));
    }
    /**
     * Gets the chosen target color after a wild card is played.
     * @return the currently set target color.
     */
    public CardSideModel.Color getTargetColor() {
        return this.targetColor;
    }

    /**
     * Sets the chosen target color after a wild card is played.
     * This method should be called when a player chooses a color after playing a wild card.
     * @param targetColor the color chosen by the player.
     */
    public void setTargetColor(CardSideModel.Color targetColor) {
        this.targetColor = targetColor;
    }


    /**
     * initPlayers initializes players in the UNO game.
     */
    public void initPlayers(){
        for (int i = 0; i < this.tempPlayerNum; i++) {
            PlayerModel p = new PlayerModel("Player " + (i + 1));
            this.players.add(p);
        }
    }

    /**
     * initPlayerHands initializes each player's cards at the beginning of each round
     */
    public void initPlayerHands(){
        // Each player draws initial cards
        for (PlayerModel player:this.players){
            player.emptyHand();  // empty player hands before draw cards
            drawCards(player, this.initNumOfCards);
        }
    }

    /**
     * drawCards() implements when a player draws one or two card from the deck
     * @param player  the player who will draw card
     * @param NumOfCards  the number of cards that player wants to draw
     */
    private void drawCards(PlayerModel player, int NumOfCards){
        for (int i = 0; i < NumOfCards; i++){
            CardModel drawnCard = this.myDeck.draw();
            player.pickUpCard(drawnCard);
            discardPile.add(drawnCard);
        }
    }

    /**
     * getTargetCard is used to draw the beginning card in each round
     * @return the drawn card
     */
    public CardModel getTargetCard(){
        CardModel targetCard = myDeck.draw();
        discardPile.add(targetCard);
        return targetCard;
    }

    /**
     * playerAction receives the action from the player -> draw a card or play a card
     * @param cardIndex  played card index
     */
    public void playerAction(int cardIndex){
        if (cardIndex >= 0){  // If player plays a card
            if (this.validateCard(this.currentPlayer.getHand().get(cardIndex))){  // If the card is valid
                this.unoView.playCard(cardIndex);  // updates the UNO game view
                this.playACard(this.currentPlayer.getHand().get(cardIndex));  // do/record the card action
            }else { // If the player plays an invalid card
                // Display the message and allow the player to draw or play a card again.
                this.unoView.updateGameMessageAndButtons(MessageConstant.invalidCard);
            }
        }else {  // If player draw a card
            this.drawCards(currentPlayer, 1);
            // Get the card the player just drawn
            CardModel drawnCard = this.currentPlayer.getHand().get(this.currentPlayer.getHand().size() - 1);
            this.needToDraw -= 1;
            // if the player still need to draw one card ->
            // this only happened when the previous player play wild draw two
            if (this.needToDraw > 0){ // if the player does need to draw more card
                this.unoView.drawACard(drawnCard.getCard(this.isLight), this.getDrawMessage());
            }else if (this.drawUntilColor){  // if the player does need to draw to get target color card
                if (drawnCard.getColor(this.isLight) != this.targetColor){
                    this.unoView.drawACard(drawnCard.getCard(this.isLight), MessageConstant.drawColor);
                }else {
                    this.drawUntilColor = false;
                }
            }
            if(this.needToDraw <= 0 && !this.drawUntilColor) { // Else if the player does not need to draw more card
                this.needToDraw = 0;
                if (this.roundWinner == null) {  // If this round not finished yet -> got to next round
                    this.unoView.drawACard(drawnCard.getCard(this.isLight), MessageConstant.nextPlayer);  // Update the view for drawing card
                    this.nextMessage = MessageConstant.normalTurn;  // Next player has a normal turn
                } else {
                    this.finishRound();  // finish this round
                }
            }
        }
    }

    /**
     * validateCard() validates if the card played is a valid move based on the previous card on the table.
     * @param cardPlayed The card that the player wishes to play.
     * @return true if the cardPlayed is a valid move, otherwise false.
     */
    public boolean validateCard(CardModel cardPlayed){

        // If the first card is Wild card, the player can play any card
        if (this.targetColor == CardSideModel.Color.NONE){
            return true;
        }
        // if card played has no color, return true
        if(cardPlayed.getCard(this.isLight).getColor() == CardSideModel.Color.NONE){
            return true;
        }

        // else check if types or colours match return true
        if(cardPlayed.getCard(this.isLight).getType() == this.topCard.getCard(this.isLight).getType() ||
                cardPlayed.getCard(this.isLight).getColor() == this.targetColor){
            return true;
        }
        // none conditions apply return false
        return false;
    }

    /**
     * playACard() implement the corresponding card action
     * @param playedCard the card which will be implemented
     */
    public void playACard(CardModel playedCard){
        valid_wild_draw_two = false; //should be reset to false every time
        this.currentPlayer.playCard(playedCard);  // Remove played card from player
        CardModel prevTopCard = this.topCard;
        CardSideModel playedSide = playedCard.getCard(this.isLight);
        this.topCard = playedCard;  // Update top card
        this.targetColor = playedSide.getColor();  // update target color
        this.needToDraw = 0;  // Reset the cards that next player needs to draw to 0

        if (this.isLastCard()){  // If the played card is the last card in current player's hand
            this.roundWinner = this.currentPlayer;  // current player is the winner of this round
        }

        // If the card is the last card and the card is not the action cards which cause next player draw cards ->
        // do not care the card function.
        if (this.isLastCard() &&
                playedSide.getType() != CardSideModel.Type.DRAW_ONE &&
                playedSide.getType() != CardSideModel.Type.WILD_DRAW_TWO &&
                playedSide.getType() != CardSideModel.Type.DRAW_FIVE &&
                playedSide.getType() != CardSideModel.Type.WILD_DRAW_COLOR){
            this.finishRound();
        }else {  // If we care about the card function
            // If skip next player
            if (playedSide.getType() == CardSideModel.Type.SKIP ||
                    (playedSide.getType() == CardSideModel.Type.REVERSE && this.players.size() == 2)) {
                this.nextMessage = MessageConstant.skipTurn;  // next player cannot play or draw a card
//                this.finishSkip = false;  // skip not finished yet
                this.numSkip = 1;
            } else if (playedSide.getType() == CardSideModel.Type.REVERSE) {  // If reverse the direction
                this.isClockWise = !this.isClockWise; // reverse the play order
                this.nextMessage = MessageConstant.normalTurn;  // next turn is a normal turn
            } else if (playedSide.getType() == CardSideModel.Type.WILD || playedSide.getType() == CardSideModel.Type.WILD_DRAW_TWO) {
                // If the card is a wild card
                this.unoView.newColour(this.getColourChoices());  // Get the new color
                if (playedSide.getType() == CardSideModel.Type.WILD_DRAW_TWO) {  // if the card is wild draw two card
                    this.valid_wild_draw_two = validate_wild_draw_two(prevTopCard);
                    //System.out.println("Valid wild draw two? " + valid_wild_draw_two); //for testing
                    this.nextMessage = MessageConstant.wildDrawTwoTurn;  // next player needs to draw two cards
                    this.needToDraw = 2;
                }else{ //else the card is wild and the next message should be a normal message
                    this.nextMessage = MessageConstant.normalTurn;
                }
            } else if (playedSide.getType() == CardSideModel.Type.DRAW_ONE) {  // If the card is draw one card
                this.nextMessage = MessageConstant.drawOneTurn;  // next player needs to draw one cards
                this.needToDraw = 1;
            }else if (playedSide.getType() == CardSideModel.Type.FLIP){  // If the card is flip
                this.isLight = !this.isLight;
                this.unoView.updateHandSides(this.isLight, this.currentPlayer.getHand());
                this.targetColor =  this.topCard.getCard(this.isLight).getColor();
                this.nextMessage = MessageConstant.normalTurn;
                System.out.println("top card: " + this.topCard.getCard(this.isLight).toString());
                if (this.topCard.getCard(this.isLight).getType() != CardSideModel.Type.FLIP){
                    // Update played card and color before doing the flip action
                    this.unoView.setAfterPlayACard(this.targetColor, this.topCard.getCard(this.isLight),
                            this.directionString(), this.sideString());
                    this.playACard(this.topCard);
                    return;
                }
            }else if (playedSide.getType() == CardSideModel.Type.DRAW_FIVE){
                this.needToDraw = 5;
                this.nextMessage = MessageConstant.drawFiveTurn;
            }else if (playedSide.getType() == CardSideModel.Type.WILD_DRAW_COLOR){
                this.unoView.newColour(this.getColourChoices());  // Get the new color
                this.nextMessage = MessageConstant.drawColor;
                this.drawUntilColor = true;
                System.out.println("color choose in draw color: " + this.targetColor);
            }else if (playedSide.getType() == CardSideModel.Type.SKIP_EVERYONE){
                this.numSkip = this.players.size() - 1;
                this.nextMessage = MessageConstant.skipTurn;
            }else{  // Play a number card
                this.nextMessage = MessageConstant.normalTurn;  // next player can play or draw a card
            }
            // Update instructions and buttons in view
            this.unoView.updateGameMessageAndButtons(MessageConstant.nextPlayer);
            this.unoView.setAfterPlayACard(this.targetColor, this.topCard.getCard(this.isLight),
                    this.directionString(), this.sideString());  // Update played card and color
        }
    }

    /**
     * validate_wild_draw_two determines if the wild_draw_two is valid or not
     * @return
     */
    public boolean validate_wild_draw_two(CardModel prevTopCard){
        for(CardModel card: currentPlayer.getHand()){
            if(card.getCard(this.isLight).getType() == prevTopCard.getCard(this.isLight).getType() ||
                    card.getCard(this.isLight).getColor() == prevTopCard.getCard(this.isLight).getColor()){
                System.out.println("Top card: " + topCard.toString() + " matches this card in hand: " + card.toString());
                return false;
            }
        }
        return true;
    }
    /**
     * nextPlayer updates the current player to the next player and updates the view for next turn
     */
    public void nextPlayer(){
        // If next player is in skip turn
        if (this.nextMessage.equals(MessageConstant.skipTurn)){
            if (this.numSkip == 0){  // if the skip turn finished -> current player is skipped, next player is normal
                this.nextMessage = MessageConstant.normalTurn;
            }else {  // Decrease number of player that needs to be skipped
                this.numSkip -= 1;
            }
        }

        // Update current player
        this.currentPlayer = this.players.get(this.getNextPlayerIndex(this.players.indexOf(this.currentPlayer)));
        this.unoView.setBeforeEachTurn(new UnoGameEvent(this, this.currentPlayer, this.nextMessage,
                this.topCard.getCard(this.isLight), this.targetColor, this.directionString(), this.isLight,
                this.sideString()));  // update the view
    }

    /**
     * nextPlayer() calculate and return the next player's index
     * @param curPlayIndex  the current player's index in
     * @return  the index of the player who play the next turn
     */
    private int getNextPlayerIndex(int curPlayIndex){
        int addedNum = 1;  // the number which is used to calculate
        if (!this.isClockWise){
            addedNum = this.players.size() - addedNum;
        }
        return (curPlayIndex + addedNum) % this.players.size();
    }

    /**
     * challengeAccepted is called when a player accepts a challenge to the previous player when they played a wild draw two card.
     * It will evaluate whether the previous player is guilty or not. If found guilty, it will add two cards to the previous player,
     * else it will make current player draw two cards
     */
    public void challengeAccepted(){
        //get previous player
        PlayerModel prevPlayer;
        if(isClockWise){ //if the direction is clockwise, the prev player is at index = index - 1
            if(players.indexOf(currentPlayer) == 0){ //when curr player index = 0, prev player is at index (size of players - 1)
                prevPlayer = players.get(players.size() - 1); //tested
            }else{
                prevPlayer = players.get(players.indexOf(currentPlayer) - 1); //tested
            }
        }else{//if the direction is counter-clockwise, the prev player is at index = index + 1
            if(players.indexOf(currentPlayer) == (players.indexOf(players.size() - 1))){
                prevPlayer = players.get(0);
            }else{
                prevPlayer = players.get(players.indexOf(currentPlayer) + 1); //tested
            }

        }
        if(valid_wild_draw_two){
            //System.out.println("not guilty"); //this works
            this.unoView.updateGameMessageAndButtons(MessageConstant.notGuilty);
        }else{
            //System.out.println("guilty"); //this works
            //add two cards to the prev player hand
            //System.out.println("prev player hand before found guilty: " + prevPlayer.getHand().toString()); //for testing
            this.drawCards(prevPlayer, 2);
            this.needToDraw = 0;  // Reset, then current player does not need to draw cards.
            this.unoView.updateGameMessageAndButtons(MessageConstant.guilty);
            //System.out.println("prev player hand after found guilty: " + prevPlayer.getHand().toString()); //for testing
        }

    }

    /**
     * isLastCard checks if the current player plays all cards in hand
     * @return  if the player has no cards in hand
     */
    public boolean isLastCard(){
        return this.currentPlayer.getHand().size() == 0;
    }

    /**
     * getCardPoint() returns the point for a input card type
     * @param cardType  the type of the uno card
     * @return the point of the uno card
     */
    public int getCardPoint(CardSideModel.Type cardType){
        return switch (cardType) {
            case SKIP, REVERSE, DRAW_FIVE, FLIP ->  // Skip, reverse, draw five, flip card
                    20;
            case WILD ->  // Wild card
                    40;
            case WILD_DRAW_TWO ->  // Wild draw two card
                    50;
            case DRAW_ONE -> // Draw one card
                    10;
            case SKIP_EVERYONE ->   // Skip everyone
                    30;
            case WILD_DRAW_COLOR ->   // Wild draw color
                    60;
            default ->  // all other cards (number cards)
                // Get the value of the number card by the type index!!! Do not change the order of the Type in Card!
                    CardSideModel.Type.valueOf(cardType.toString()).ordinal();
        };
    }

    /**
     * updatePlayerPoint() updates the score of the player who won the current round
     */
    public void updatePlayerPoint(){
        int points = 0;  // The points which need to be added
        for (PlayerModel player: this.players){
            if (!player.equals(this.roundWinner)){  // If the player is not the winner of the current round
                for (CardModel card: player.getHand()) {  // For each card in the player's hand
                    points += getCardPoint(card.getCard(this.isLight).getType());  // add corresponding point
                }
            }
        }
        this.roundWinner.increaseScore(points);  // Add the total points to the winner
    }

    /**
     * finishRound updates the winner's score and corresponding view.
     * it also set up the game information for the next round
     */
    public void finishRound(){
        this.updatePlayerPoint();  // Update the winner's point
        this.unoView.updateRoundFinished(new UnoFinishEvent(this, this.roundWinner,
                this.players.indexOf(this.roundWinner), this.roundNum));
        if (this.roundWinner.getScore() >= 500){  // If game finished
            this.finishGame();
        }else {
            this.roundNum += 1;  // Increase round number by 1
            this.initRound();  // init a new round
        }
    }

    /**
     * finishGame updates view to tell players that game has finished
     */
    public void finishGame(){
        this.unoView.updateGameFinished(new UnoFinishEvent(this, this.roundWinner,
                this.players.indexOf(this.roundWinner), -1));
    }

    public CardSideModel.Color[]  getColourChoices(){
        CardSideModel.Color[] choices;
        if(this.isLight){
            choices = new CardSideModel.Color[]{CardSideModel.Color.YELLOW, CardSideModel.Color.BLUE,
                    CardSideModel.Color.RED, CardSideModel.Color.GREEN};
        }else {
            choices = new CardSideModel.Color[]{CardSideModel.Color.PINK, CardSideModel.Color.PURPLE,
                    CardSideModel.Color.TEAL, CardSideModel.Color.ORANGE};
        }
        return choices;
    }

    /**
     * getDrawMessage returns the message constant depends number of cards the player needs to draw
     * @return
     */
    public String getDrawMessage(){
        if (this.needToDraw == 4){
            return MessageConstant.drawFourTurn;
        } else if (this.needToDraw == 3) {
            return MessageConstant.drawThreeTurn;
        } else if (this.needToDraw == 2) {
            return MessageConstant.drawTwoTurn;
        }else {  // this.needToDraw == 1
            return MessageConstant.drawOneTurn;
        }
    }

    public void saveNumOfHumanPlayers(int numOfHumanPlayers){
        System.out.println(" In model, num of human players: " + numOfHumanPlayers);
        this.numOfHumanPlayers = numOfHumanPlayers;
    }

    public void saveNumOfAIPlayers(int numOfAIPlayers){
        System.out.println(" In model, num of AI players: " + numOfAIPlayers);
        this.numOfAIplayers = numOfAIPlayers;
        setTempPlayerNum(numOfHumanPlayers + numOfAIPlayers);
        initGame();
    }

}
