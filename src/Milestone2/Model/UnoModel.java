package Milestone2.Model;

import Milestone1.Card;
import Milestone2.View.UnoView;

import java.util.*;

public class UnoModel {
    private ArrayList<PlayerModel> players;
    private DeckModel myDeck;
    private List<CardModel> discardPile;
    // The target color for the next player
    // Use targetColor instead of card color when checking if the card is valid or not
    private CardModel.Color targetColor;
    private CardModel topCard;  // top card
    private boolean isClockWise;  // direction
    private PlayerModel currentPlayer;
    private int roundNum; // Round number
    private int needToDraw = 0;  // the number of cards the player need to draw
    // Indicate if the skip has finished -----
    // false means next player is skipped
    // true means current player is skipped and has finished the turn
    // set to false when player plays a skip card
    private boolean finishSkip = true;
    private String nextMessage;  // Message Constant
    private PlayerModel roundWinner = null;  // The winner of the current round
    private int tempPlayerNum = 2;  // used for initialize number of player
    private final int initNumOfCards = 7;
    private UnoView unoView;

    public UnoModel(){
        myDeck = new DeckModel();
        discardPile = new ArrayList<>();
        players = new ArrayList<>();
        this.isClockWise = true;
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
        this.roundWinner = null;  // No round winner yet
        this.nextMessage = null;
        this.myDeck.createDeck();  // make sure this is a new round -> new deck
        this.myDeck.shuffle(); //shuffle the deck
        this.initPlayerHands();  // Draw cards for each player
        this.topCard = this.getTargetCard();  // Get the start card in the current round
        this.targetColor = this.topCard.getColor();  // Get target color from the top card
        Random random = new Random();
        int currPlayerIndex = random.nextInt(this.players.size());  // choose the first player randomly
        this.currentPlayer = players.get(currPlayerIndex);  // Set current player
        this.unoView.updateRoundInfo(this.roundNum);  // Update round information
//        if(this.targetColor.equals(Card.Color.NONE)){ //TODO: if the starting card is a wild card, message should tell user to play whatever card they want
//        }
        this.unoView.setBeforeEachTurn(new UnoGameEvent(this, this.currentPlayer,
                MessageConstant.normalTurn, this.topCard, this.targetColor, this.directionString()));
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
                this.unoView.drawACard(drawnCard, MessageConstant.drawOneTurn);
            }else {  // Else if the player does not need to draw more card
                this.needToDraw = 0;
                if (this.roundWinner == null){  // If this round not finished yet -> got to next round
                    this.unoView.drawACard(drawnCard, MessageConstant.nextPlayer);  // Update the view for drawing card
                    this.nextMessage = MessageConstant.normalTurn;  // Next player has a normal turn
                }else {
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
        if (this.targetColor == CardModel.Color.NONE){
            return true;
        }
        // if card played has no color, return true
        if(cardPlayed.getColor() == CardModel.Color.NONE){
            return true;
        }

        // else check if types or colours match return true
        if(cardPlayed.getType() == this.topCard.getType() || cardPlayed.getColor() == this.targetColor){
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
        this.currentPlayer.playCard(playedCard);  // Remove played card from player
        this.topCard = playedCard;  // Update top card
        this.targetColor = playedCard.getColor();  // update target color
        this.needToDraw = 0;  // Reset the cards that next player needs to draw to 0

        if (this.isLastCard()){  // If the played card is the last card in current player's hand
            this.roundWinner = this.currentPlayer;  // current player is the winner of this round
        }

        // If the card is the last card and the card is not the action cards which cause next player draw cards ->
        // do not care the card function.
        if (this.isLastCard() &&
                playedCard.getType() != CardModel.Type.DRAW_ONE && playedCard.getType() != CardModel.Type.WILD_DRAW_TWO){
            this.finishRound();
        }else {  // If we care about the card function
            // If skip next player
            if (playedCard.getType() == CardModel.Type.SKIP ||
                    (playedCard.getType() == CardModel.Type.REVERSE && this.players.size() == 2)) {
                this.nextMessage = MessageConstant.skipTurn;  // next player cannot play or draw a card
                this.finishSkip = false;  // skip not finished yet
            } else if (playedCard.getType() == CardModel.Type.REVERSE) {  // If reverse the direction
                this.isClockWise = !this.isClockWise; // reverse the play order
                this.nextMessage = MessageConstant.normalTurn;  // next turn is a normal turn
            } else if (playedCard.getType() == CardModel.Type.WILD || playedCard.getType() == CardModel.Type.WILD_DRAW_TWO) {
                // If the card is a wild card
                this.targetColor = this.unoView.newColour();  // Get the new color
                if (playedCard.getType() == CardModel.Type.WILD_DRAW_TWO) {  // if the card is wild draw two card
                    this.nextMessage = MessageConstant.drawTwoTurn;  // next player needs to draw two cards
                    this.needToDraw = 2;
                }else{ //else the card is wild and the next message should be a normal message
                    this.nextMessage = MessageConstant.normalTurn;
                }
            } else if (playedCard.getType() == CardModel.Type.DRAW_ONE) {  // If the card is draw one card
                this.nextMessage = MessageConstant.drawOneTurn;  // next player needs to draw one cards
                this.needToDraw = 1;
            }else{  // Play a number card
                this.nextMessage = MessageConstant.normalTurn;  // next player can play or draw a card
            }
            // Update instructions and buttons in view
            this.unoView.updateGameMessageAndButtons(MessageConstant.nextPlayer);
        }
    }

    /**
     * nextPlayer updates the current player to the next player and updates the view for next turn
     */
    public void nextPlayer(){
        // If next player is in skip turn
        if (this.nextMessage.equals(MessageConstant.skipTurn)){ //TODO: system crashes when a wild is played first
            if (this.finishSkip){  // if the skip turn finished -> current player is skipped, next player is normal
                this.nextMessage = MessageConstant.normalTurn;
            }else {  // Next player needs to be skipped
                this.finishSkip = true;
            }
        }
        // Update current player
        this.currentPlayer = this.players.get(this.getNextPlayerIndex(this.players.indexOf(this.currentPlayer)));
        this.unoView.setBeforeEachTurn(new UnoGameEvent(this, this.currentPlayer,
                this.nextMessage, this.topCard, this.targetColor, this.directionString()));  // update the view
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
    public int getCardPoint(CardModel.Type cardType){
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
     */
    public void updatePlayerPoint(){
        int points = 0;  // The points which need to be added
        for (PlayerModel player: this.players){
            if (!player.equals(this.roundWinner)){  // If the player is not the winner of the current round
                for (CardModel card: player.getHand()) {  // For each card in the player's hand
                    points += getCardPoint(card.getType());  // add corresponding point
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



}
