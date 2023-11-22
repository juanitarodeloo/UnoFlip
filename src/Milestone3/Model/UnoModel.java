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
    private CardSideModel.Color previousColor;  // used when challenge the draw color
    private CardModel topCard;  // top card
    private boolean drawUntilColor = false;  // indicate if current player needs to draw until specific card
    private int numSkip = 0; // indicate how many players need to be skipped
    private boolean isLight;  // indicate if the current hands show light side of cards
    private boolean isClockWise;  // direction
    private PlayerModel currentPlayer;
    private int roundNum; // Round number
    private int needToDraw = 0;  // the number of cards the player need to draw
    private String nextMessage;  // Message Constant
    private PlayerModel roundWinner = null;  // The winner of the current round
    private int tempPlayerNum = 2;  // used for initialize number of player
    private final int initNumOfCards = 15; //changed for testing
    private UnoView unoView;

    //private boolean valid_wild_draw_two; //holds whether the wild draw two was played properly
    private boolean valid_wild_draw_two_or_color; //holds whether the wild draw two/color was played properly

    private int numOfHumanPlayers;

    private int numOfAIplayers;

    private CardModel cardAIPlayed;

    private int totalNumOfPlayers;  // used for initialize number of player

    public UnoModel(){
        myDeck = new DeckModel();
        discardPile = new ArrayList<>();
        players = new ArrayList<>();
        numOfHumanPlayers = 1;
        numOfAIplayers = 1;
        totalNumOfPlayers = 2;
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
//        Random random = new Random();
//        int currPlayerIndex = random.nextInt(this.players.size());  // choose the first player randomly
        this.currentPlayer = players.get(0);  // Set current player to first player which is always a human player
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
        int i;
        for (i = 0; i < numOfHumanPlayers; i++) {
            PlayerModel p = new PlayerModel("H" + (i + 1), true);
            this.players.add(p);
        }
        for (int j = 0; j < numOfAIplayers; j++) {
            i++;
            PlayerModel p = new PlayerModel("A" + (i), false);
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
        //System.out.println("In here, card index: " + cardIndex);

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
        valid_wild_draw_two_or_color = false; //should be reset to false every time
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
                this.numSkip = 1;
            } else if (playedSide.getType() == CardSideModel.Type.REVERSE) {  // If reverse the direction
                this.isClockWise = !this.isClockWise; // reverse the play order
                this.nextMessage = MessageConstant.normalTurn;  // next turn is a normal turn
            } else if (playedSide.getType() == CardSideModel.Type.WILD || playedSide.getType() == CardSideModel.Type.WILD_DRAW_TWO) {
                if(!currentPlayer.isHuman()){
                    //choose new color for AI
                    setTargetColor(pickColorForAI());
                }else{
                    // If the card is a wild card
                    this.unoView.newColour(this.getColourChoices());  // Get the new color if current player is human
                }
                if (playedSide.getType() == CardSideModel.Type.WILD_DRAW_TWO) {  // if the card is wild draw two card
                    this.valid_wild_draw_two_or_color = validate_wild_draw_two_or_color(prevTopCard);
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
                this.valid_wild_draw_two_or_color = validate_wild_draw_two_or_color(prevTopCard);
                System.out.println("color choose in draw color: " + this.targetColor);
            }else if (playedSide.getType() == CardSideModel.Type.SKIP_EVERYONE){
                this.numSkip = this.players.size() - 1;
                this.nextMessage = MessageConstant.skipTurn;
            }else{  // Play a number card
                this.nextMessage = MessageConstant.normalTurn;  // next player can play or draw a card
            }
            // Update instructions and buttons in view
            this.unoView.updateGameMessageAndButtons(MessageConstant.nextPlayer);

            //if current play is AI we don't want to update their view after they play a card, it should be the same
//            if(!currentPlayer.isHuman()){
//                System.out.println("In here");
//                return;
//            }

            this.unoView.setAfterPlayACard(this.targetColor, this.topCard.getCard(this.isLight),
                    this.directionString(), this.sideString());  // Update played card and color
        }
    }

    public CardModel pickCardForAI(PlayerModel aiPlayer){
        System.out.println("Current top card: " + topCard.toString(isLight));
        System.out.println("Current target color: " + targetColor.toString());
        System.out.println("Next player: " + aiPlayer.getName() + "\nHand Before: ");
        for(int i = 0; i < aiPlayer.getHand().size(); i++){
            System.out.print(aiPlayer.getHand().get(i).toString(isLight) + ", ");
        }
        for(int i = 0; i < aiPlayer.getHand().size(); i++){
            if(validateCard(aiPlayer.getHand().get(i))){
                System.out.println(aiPlayer.getHand().get(i).toString(isLight) + " matches!");
                topCard = aiPlayer.getHand().get(i);
                targetColor = topCard.getColor(isLight);
                this.nextMessage = MessageConstant.aIplayed;
                return aiPlayer.getHand().get(i);
            }
        }
        System.out.println("Nothing matched! ");
        drawCards(aiPlayer, 1);
        this.nextMessage = MessageConstant.aIPickedUp;

        //just for testing:
        System.out.println("Hand after: ");
        for(int i = 0; i < aiPlayer.getHand().size(); i++){
            System.out.print(aiPlayer.getHand().get(i).toString(isLight) + ", ");
        }

        return null;

    }

    public CardSideModel.Color pickColorForAI(){
        return currentPlayer.getHand().get(0).getColor(isLight); //TODO: test when AI player has no more cards
        //TODO: what if card at 0 is wild?
    }

    /**
     * validate_wild_draw_two determines if the wild_draw_two or wild_draw_color is valid or not
     * @return
     */
    public boolean validate_wild_draw_two_or_color(CardModel prevTopCard){
        for(CardModel card: currentPlayer.getHand()){
            if(card.getCard(this.isLight).getType() == prevTopCard.getCard(this.isLight).getType() ||
                    card.getCard(this.isLight).getColor() == this.previousColor){
                System.out.println("Top card: " + topCard.toString() + " matches this card in hand: " + card.getCard(this.isLight).toString());
                return false;
            }
        }
        return true;
    }
    /**
     * nextPlayer updates the current player to the next player and updates the view for next turn
     */
    public void nextPlayer(){
        System.out.println("Current next message before if statements: " + this.nextMessage);
        //if the current player is AI play the card previously picked for them
        if(this.nextMessage.equals(MessageConstant.aIplayed)) {
            System.out.println("AI played a card");
            if (cardAIPlayed != null) {
                playACard(cardAIPlayed);
            }

            //else if the previous player was AI and picked up cards then the next message should be a normal turn
        }else if(this.nextMessage.equals(MessageConstant.aIPickedUp) ||
                this.nextMessage.equals(MessageConstant.aIDrawOne) ||
                this.nextMessage.equals(MessageConstant.aIDrawTwo) ||
                this.nextMessage.equals(MessageConstant.aIDrawFive)){
            this.nextMessage = MessageConstant.normalTurn;
        }else if (this.nextMessage.equals(MessageConstant.skipTurn) || this.nextMessage.equals(MessageConstant.aISkipped)){ // If next player is in skip turn
            if (this.numSkip <= 0){  // if the skip turn finished -> current player is skipped, next player is normal
                this.nextMessage = MessageConstant.normalTurn;
            }else {  // Decrease number of player that needs to be skipped
                this.numSkip -= 1;
            }
        }
        System.out.println("numSkip: " + this.numSkip);
//        this.nextMessage = MessageConstant.normalTurn;

        // Update current player
        this.currentPlayer = this.players.get(this.getNextPlayerIndex(this.players.indexOf(this.currentPlayer)));
        System.out.println("---next player is " + this.currentPlayer.getName());
        System.out.println("next player is Human ---" + this.currentPlayer.isHuman());

        //if the next player is AI and they are not skipped, pick a card for them to play
        if(this.nextMessage.equals(MessageConstant.normalTurn) //|| (this.nextMessage.equals(MessageConstant.skipTurn))
                && !this.currentPlayer.isHuman()){
            System.out.println("next player is AI");
            cardAIPlayed = pickCardForAI(currentPlayer);
        }else if(!this.currentPlayer.isHuman()){ //else if the next player is AI and they are going to be skipped, handle this
            handleAIReaction();
        }


        System.out.println("Current next message after if statements: " + this.nextMessage + "/n");
        //System.out.println("In next player: the next message = " + this.nextMessage);
        this.unoView.setBeforeEachTurn(new UnoGameEvent(this, this.currentPlayer, this.nextMessage,
                this.topCard.getCard(this.isLight), this.targetColor, this.directionString(), this.isLight,
                this.sideString()));  // update the view
    }

    public void handleAIReaction(){
        System.out.println("\nAI player will receive this message: " + this.nextMessage);
        //TODO: handle the AI reaction to action cards that were played previous to their turn
        if(this.nextMessage.equals(MessageConstant.drawOneTurn)){
            this.nextMessage = MessageConstant.aIDrawOne;
            System.out.println("AI is going to pick up one card, they're hand before: "); //just for testing
            for(int i = 0; i < this.currentPlayer.getHand().size(); i++){
                System.out.print(this.currentPlayer.getHand().get(i).toString(isLight) + ", ");
            }
            drawCards(this.currentPlayer, 1);
            System.out.println("AI Picked up one card, they're hand after: "); //just for testing
            for(int i = 0; i < this.currentPlayer.getHand().size(); i++){
                System.out.print(this.currentPlayer.getHand().get(i).toString(isLight) + ", ");
            }
        }else if(this.nextMessage.equals(MessageConstant.drawTwoTurn)){
            this.nextMessage = MessageConstant.aIDrawTwo;
            System.out.println("AI is going to pick up two cards, they're hand before: "); //just for testing
            for(int i = 0; i < this.currentPlayer.getHand().size(); i++){
                System.out.print(this.currentPlayer.getHand().get(i).toString(isLight) + ", ");
            }
            drawCards(this.currentPlayer, 2);
            System.out.println("AI Picked up two cards, they're hand after: "); //just for testing
            for(int i = 0; i < this.currentPlayer.getHand().size(); i++){
                System.out.print(this.currentPlayer.getHand().get(i).toString(isLight) + ", ");
            }
        }else if(this.nextMessage.equals(MessageConstant.drawFiveTurn)){
            this.nextMessage = MessageConstant.aIDrawFive;
            System.out.println("AI is going to pick up five cards, they're hand before: "); //just for testing
            for(int i = 0; i < this.currentPlayer.getHand().size(); i++){
                System.out.print(this.currentPlayer.getHand().get(i).toString(isLight) + ", ");
            }
            drawCards(this.currentPlayer, 5);
            System.out.println("AI Picked up five cards, they're hand after: "); //just for testing
            for(int i = 0; i < this.currentPlayer.getHand().size(); i++){
                System.out.print(this.currentPlayer.getHand().get(i).toString(isLight) + ", ");
            }
        }else if(this.nextMessage.equals(MessageConstant.skipTurn)){
            this.nextMessage = MessageConstant.aISkipped;
            this.numSkip -= 1;
        }

        //if message is draw one, add one card to AI's hand and change next message to normal
        //in a game of two people: when an AI player plays a skip, it skips the next person but then skips itself again
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
        PlayerModel prevPlayer = this.getPrevPlayer();
        if(valid_wild_draw_two_or_color){
            if (this.drawUntilColor) {
                this.unoView.updateGameMessageAndButtons(MessageConstant.notGuiltyColor);
            }else {
                this.unoView.updateGameMessageAndButtons(MessageConstant.notGuiltyTwo);
            }
        }else{
            if (this.drawUntilColor){  // if challenge draw color
                while (true) {
                    this.drawCards(prevPlayer, 1);  // previous player draw one card
                    CardModel justDraw = prevPlayer.getHand().get(prevPlayer.getHand().size() - 1);
                    System.out.println("previous draw: " + justDraw.getCard(this.isLight).toString());
                    if (justDraw.getCard(isLight).getColor() == this.previousColor) {
                        this.unoView.updateGameMessageAndButtons(MessageConstant.guiltyColor);
                        this.drawUntilColor = false;
                        break;
                    }
                    System.out.println("finish draw color");
                }
            }else {  // if challenge draw two
                //add two cards to the prev player hand
                this.drawCards(prevPlayer, 2);
                this.needToDraw = 0;  // Reset, then current player does not need to draw cards.
                this.unoView.updateGameMessageAndButtons(MessageConstant.guiltyTwo);
            }
        }

    }

    /**
     * getPrecPlayer returns previous player
     * @return
     */
    public PlayerModel getPrevPlayer(){
        if(isClockWise){ //if the direction is clockwise, the prev player is at index = index - 1
            if(players.indexOf(currentPlayer) == 0){ //when curr player index = 0, prev player is at index (size of players - 1)
                return players.get(players.size() - 1); //tested
            }else{
                return players.get(players.indexOf(currentPlayer) - 1); //tested
            }
        }else{//if the direction is counter-clockwise, the prev player is at index = index + 1
            if(players.indexOf(currentPlayer) == (players.indexOf(players.size() - 1))){
                return players.get(0);
            }else{
                return players.get(players.indexOf(currentPlayer) + 1); //tested
            }
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

    /**
     * setTemPlayerNum updates the temporal player name, this is called after player choose the number of players
     * @param playerNum
     */
    public void setTotalNumOfPlayers(int playerNum){
        System.out.println("Total number of players: " + playerNum);
        this.totalNumOfPlayers = playerNum;
    }

    public void saveNumOfAIPlayers(int numOfAIPlayers){
        System.out.println(" In model, num of AI players: " + numOfAIPlayers);
        this.numOfAIplayers = numOfAIPlayers;
        setTotalNumOfPlayers(numOfHumanPlayers + numOfAIPlayers);
    }


}
