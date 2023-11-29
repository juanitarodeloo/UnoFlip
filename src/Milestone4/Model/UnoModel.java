/**
 * UnoModel class constains the main model of the game. It contains the data-related logic that the user works with.
 *
 * @Authors: Rebecca Li, Juanita Rodelo, Adham Elmahi
 */
package Milestone4.Model;

import Milestone4.View.UnoView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class UnoModel {
    private ArrayList<PlayerModel> players;
    private DeckModel myDeck;
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
    private int initNumOfCards = 7; //changed for testing
    private UnoView unoView;
    private boolean valid_wild_draw_two_or_color; //holds whether the wild draw two/color was played properly

    private int numOfHumanPlayers;

    private int numOfAIplayers;
    // Redo Event contains all information the UnoModel needs to know for the redo functionality.
    // Redo Event should be reset to null before each player's turn.
    // If the redoEvent is null, the undo is not clicked yet.
    // This also means, the redoEvent is updated after the player click undo.
    // Need to store this to the save file!!!
    private DoEvent redoEvent = null;
    // Similar to Redo Event.
    // This undoEvent is updated before next turn -> in nextPlayer method.
    // This undoEvent only update when the next player is human and the next player hasn't been skipped.
    // The undoEvent should be set to null if the next player is AI or the next player is skipped.
    // If the undoEvent is null -> the undo button is set to disabled in the view.
    private DoEvent undoEvent = null;
    // number of cards the player draw in the turn -> only used for undo and redo!!!
    // Not the same as needToDraw attribute!!!
    // Reset to 0 in nextPlayer method
    private int numOfDraw = 0;
    private boolean addDrawNum = false;  // if true, increase numOfDraw


    /**
     * UnoModel is the constructor of the class
     */
    public UnoModel() {
        myDeck = new DeckModel();
        players = new ArrayList<>();
        numOfHumanPlayers = 1;
        numOfAIplayers = 1;
    }

    /**
     * directionToString converts the direction information to String
     * @return game direction in string
     */
    public String directionString() {
        if (this.isClockWise) {
            return "Clockwise";
        } else {
            return "CounterClockwise";
        }
    }

    /**
     * sideString converts the card side information to String
     * @return
     */
    public String sideString() {
        if (this.isLight) {
            return "Light Side";
        } else {
            return "Dark Side";
        }
    }

    /**
     * setUnoView assigns unoView
     * @param unoView
     */
    public void setUnoView(UnoView unoView) {
        this.unoView = unoView;
    }

    /**
     * initGame initializes the Uno game
     */
    public void initGame() {
        this.roundNum = 1;
        this.initPlayers();  // Initialize players in this game
        this.unoView.startGame(this.players);
        this.initRound();  // Initialize the first round in this game
    }

    /**
     * initRound init each round of the UNO game.
     */
    public void initRound() {
        this.isClockWise = true;
        this.isLight = true;
        this.roundWinner = null;  // No round winner yet
        this.nextMessage = MessageConstant.normalTurn;  // update message to normal turn
        this.myDeck.createDeck();  // make sure this is a new round -> new deck
        this.initPlayerHands();  // Draw cards for each player
        this.topCard = this.getTargetCard();  // Get the start card in the current round
        this.targetColor = this.topCard.getCard(this.isLight).getColor();  // Get target color from the top card
        this.currentPlayer = players.get(0);  // Set current player to first player which is always a human player

        // Set undoEvent because the first player must be human
        this.undoEvent = new DoEvent(this, this.topCard, this.isClockWise, this.isLight, this.targetColor,
                this.previousColor, this.needToDraw, this.numSkip, this.valid_wild_draw_two_or_color,
                this.drawUntilColor, this.nextMessage);

        this.unoView.updateRoundInfo(this.roundNum);  // Update round information
        this.unoView.setBeforeEachTurn(new UnoGameEvent(this, this.currentPlayer,
                this.nextMessage, this.topCard.getCard(this.isLight), this.targetColor,
                this.directionString(), this.isLight, this.sideString()));
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
    public void initPlayers() {
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
    public void initPlayerHands() {
        // Each player draws initial cards
        for (PlayerModel player : this.players) {
            player.emptyHand();  // empty player hands before draw cards
            drawCards(player, this.initNumOfCards);
        }
    }

    /**
     * drawCards() implements when a player draws one or two card from the deck
     * @param player  the player who will draw card
     * @param NumOfCards  the number of cards that player wants to draw
     */
    private void drawCards(PlayerModel player, int NumOfCards) {
        for (int i = 0; i < NumOfCards; i++) {
            CardModel drawnCard = this.myDeck.draw();
            player.pickUpCard(drawnCard);
        }
    }

    /**
     * getTargetCard is used to draw the beginning card in each round
     * @return the drawn card
     */
    public CardModel getTargetCard() {
        CardModel targetCard = myDeck.draw();
        return targetCard;
    }

    /**
     * playerAction receives the action from the player -> draw a card or play a card
     * @param cardIndex  played card index
     */
    public void playerAction(int cardIndex) {
        //System.out.println("In here, card index: " + cardIndex);

        if (cardIndex >= 0) {  // If player plays a card
            if (this.validateCard(this.currentPlayer.getHand().get(cardIndex))) {  // If the card is valid
                this.unoView.playCard(cardIndex);  // updates the UNO game view
                System.out.println("previous Color: " + this.previousColor);
                this.playACard(this.currentPlayer.getHand().get(cardIndex));  // do/record the card action
            } else { // If the player plays an invalid card
                // Display the message and allow the player to draw or play a card again.
                this.unoView.updateGameMessageAndButtons(MessageConstant.invalidCard);
            }
        } else {  // If player draw a card
            if(this.addDrawNum){  // Draw before undo
                this.numOfDraw += 1;  // update number of cards the player has drawn
            }
            this.drawCards(currentPlayer, 1);
            // Get the card the player just drawn
            CardModel drawnCard = this.currentPlayer.getHand().get(this.currentPlayer.getHand().size() - 1);
            this.needToDraw -= 1;
            // if the player still need to draw one card ->
            // this only happened when the previous player play wild draw two
            if (this.needToDraw > 0) { // if the player does need to draw more card
                this.unoView.drawACard(drawnCard.getCard(this.isLight), this.getDrawMessage());
            } else if (this.drawUntilColor) {  // if the player does need to draw to get target color card
                if (drawnCard.getColor(this.isLight) != this.targetColor) {
                    this.unoView.drawACard(drawnCard.getCard(this.isLight), MessageConstant.drawColor);
                } else {
                    this.drawUntilColor = false;
                }
            }
            if (this.needToDraw <= 0 && !this.drawUntilColor) { // Else if the player does not need to draw more card
                this.needToDraw = 0;
                if (this.roundWinner == null) {  // If this round not finished yet -> got to next player
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
    public boolean validateCard(CardModel cardPlayed) {

        // If the first card is Wild card, the player can play any card
        if (this.targetColor == CardSideModel.Color.NONE) {
            return true;
        }
        // if card played has no color, return true
        if (cardPlayed.getCard(this.isLight).getColor() == CardSideModel.Color.NONE) {
            return true;
        }

        // else check if types or colours match return true
        if (cardPlayed.getCard(this.isLight).getType() == this.topCard.getCard(this.isLight).getType() ||
                cardPlayed.getCard(this.isLight).getColor() == this.targetColor) {
            return true;
        }
        // none conditions apply return false
        return false;
    }

    /**
     * playACard() implement the corresponding card action
     * @param playedCard the card which will be implemented
     */
    public void playACard(CardModel playedCard) {
        String updatedMessage;
        valid_wild_draw_two_or_color = false; //should be reset to false every time

        this.previousColor = this.targetColor;
        this.currentPlayer.playCard(playedCard);  // Remove played card from player
        // prevTopCard is only used to check if the wild_draw_two or wild_draw_color card is guilty.
        // If such cards are in the other side of the clip card, it must be valid to play
        CardModel prevTopCard = this.topCard;  // update the previous top card
        CardSideModel playedSide = playedCard.getCard(this.isLight);
        this.topCard = playedCard;  // Update top card
        this.targetColor = playedSide.getColor();  // update target color
        this.needToDraw = 0;  // Reset the cards that next player needs to draw to 0
        System.out.println("Previous color: " + this.previousColor);

        if (this.isLastCard()) {  // If the played card is the last card in current player's hand
            this.roundWinner = this.currentPlayer;  // current player is the winner of this round
        }

        // If the card is the last card and the card is not the action cards which cause next player draw cards ->
        // do not care the card function.
        if (this.isLastCard() &&
                playedSide.getType() != CardSideModel.Type.DRAW_ONE &&
                playedSide.getType() != CardSideModel.Type.WILD_DRAW_TWO &&
                playedSide.getType() != CardSideModel.Type.DRAW_FIVE &&
                playedSide.getType() != CardSideModel.Type.WILD_DRAW_COLOR) {
            this.finishRound();
        } else {  // If we care about the card function
            // If skip next player
            if (playedSide.getType() == CardSideModel.Type.SKIP ||
                    (playedSide.getType() == CardSideModel.Type.REVERSE && this.players.size() == 2)) {
                this.nextMessage = MessageConstant.skipTurn;  // next player cannot play or draw a card
                this.numSkip = 1;
            } else if (playedSide.getType() == CardSideModel.Type.REVERSE) {  // If reverse the direction
                this.isClockWise = !this.isClockWise; // reverse the play order
                this.nextMessage = MessageConstant.normalTurn;  // next turn is a normal turn
            } else if (playedSide.getType() == CardSideModel.Type.WILD || playedSide.getType() == CardSideModel.Type.WILD_DRAW_TWO) {
                this.getNewColor();  // get new target color
                if (playedSide.getType() == CardSideModel.Type.WILD_DRAW_TWO) {  // if the card is wild draw two card
                    this.valid_wild_draw_two_or_color = validate_wild_draw_two_or_color(prevTopCard);
                    //System.out.println("Valid wild draw two? " + valid_wild_draw_two); //for testing
                    this.nextMessage = MessageConstant.wildDrawTwoTurn;  // next player needs to draw two cards
                    this.needToDraw = 2;
                } else { //else the card is wild and the next message should be a normal message
                    this.nextMessage = MessageConstant.normalTurn;
                }
            } else if (playedSide.getType() == CardSideModel.Type.DRAW_ONE) {  // If the card is draw one card
                this.nextMessage = MessageConstant.drawOneTurn;  // next player needs to draw one cards
                this.needToDraw = 1;
            } else if (playedSide.getType() == CardSideModel.Type.FLIP) {  // If the card is flip
                this.isLight = !this.isLight;
                this.unoView.updateHandSides(this.isLight, this.currentPlayer.getHand());
                this.targetColor = this.topCard.getCard(this.isLight).getColor();
                this.nextMessage = MessageConstant.normalTurn;
                System.out.println("top card: " + this.topCard.getCard(this.isLight).toString());
                if (this.topCard.getCard(this.isLight).getType() != CardSideModel.Type.FLIP) {
                    // Update played card and color before doing the flip action
                    this.unoView.setAfterPlayACard(this.targetColor, this.topCard.getCard(this.isLight),
                            this.directionString(), this.sideString());
                    this.playACard(this.topCard);
                    return;
                }
            } else if (playedSide.getType() == CardSideModel.Type.DRAW_FIVE) {
                this.needToDraw = 5;
                this.nextMessage = MessageConstant.drawFiveTurn;
            } else if (playedSide.getType() == CardSideModel.Type.WILD_DRAW_COLOR) {
                this.getNewColor();
                this.nextMessage = MessageConstant.drawColor;
                this.drawUntilColor = true;
                this.valid_wild_draw_two_or_color = validate_wild_draw_two_or_color(prevTopCard);
                System.out.println("color choose in draw color: " + this.targetColor);
            } else if (playedSide.getType() == CardSideModel.Type.SKIP_EVERYONE) {
                this.numSkip = this.players.size() - 1;
                this.nextMessage = MessageConstant.skipTurn;
            } else {  // Play a number card
                this.nextMessage = MessageConstant.normalTurn;  // next player can play or draw a card
            }

            if (this.currentPlayer.isHuman()) {
                updatedMessage = MessageConstant.nextPlayer;
            } else {
                updatedMessage = MessageConstant.aIplayed;
            }
            // Update instructions and buttons in view
            this.unoView.updateGameMessageAndButtons(updatedMessage);
            this.unoView.setAfterPlayACard(this.targetColor, this.topCard.getCard(this.isLight),
                    this.directionString(), this.sideString());  // Update played card and color
        }
    }

    /**
     * getNewColor calls uno view to pop up the dialog for player to select the next color, when current player is human.
     * If current player is Ai, it calls the method which choose a random color.
     */
    public void getNewColor() {
        if (!currentPlayer.isHuman()) {  // AI choose a random color
            this.AIChooseColor();
        } else {
            this.unoView.newColour(this.getColourChoices());  // Get the new color if current player is human
        }
    }

    /**
     * pickCardForAI chooses played card for AI player
     * @param aiPlayer
     * @return
     */
    public CardModel pickCardForAI(PlayerModel aiPlayer) {
        System.out.println("Current top card: " + topCard.toString(isLight));
        System.out.println("Current target color: " + targetColor.toString());
        System.out.println("Next player: " + aiPlayer.getName() + "\nHand Before: ");
        for (int i = 0; i < aiPlayer.getHand().size(); i++) {
            System.out.print(aiPlayer.getHand().get(i).toString(isLight) + ", ");
        }
        for (int i = 0; i < aiPlayer.getHand().size(); i++) {
            if (validateCard(aiPlayer.getHand().get(i))) {
                System.out.println(aiPlayer.getHand().get(i).toString(isLight) + " matches!");
                return aiPlayer.getHand().get(i);
            }
        }
        System.out.println("Nothing matched! ");
        drawCards(aiPlayer, 1);
        this.unoView.addNewCard(this.currentPlayer.getHand().get(this.currentPlayer.getHand().size() - 1).getCard(this.isLight));  // add card to display

        //just for testing:
        System.out.println("Hand after: ");
        for (int i = 0; i < aiPlayer.getHand().size(); i++) {
            System.out.print(aiPlayer.getHand().get(i).toString(isLight) + ", ");
        }

        return null;

    }

    /**
     * validate_wild_draw_two determines if the wild_draw_two or wild_draw_color is valid or not
     * @return
     */
    public boolean validate_wild_draw_two_or_color(CardModel prevTopCard) {
        for (CardModel card : currentPlayer.getHand()) {
            if (card.getCard(this.isLight).getType() == prevTopCard.getCard(this.isLight).getType() ||
                    card.getCard(this.isLight).getColor() == this.previousColor) {
                System.out.println("Top card: " + topCard.toString() + " matches this card in hand: " + card.getCard(this.isLight).toString());
                return false;
            }
        }
        return true;
    }

    /**
     * nextPlayer updates the current player to the next player and updates the view for next turn
     */
    public void nextPlayer() {
        if (this.nextMessage == null) {
            this.nextMessage = MessageConstant.normalTurn; // or some other default message constant
        }
        System.out.println("skip number before: " + this.numSkip);
        // If next player is in skip turn
        if (this.nextMessage.equals(MessageConstant.skipTurn) || this.nextMessage.equals(MessageConstant.aISkipped)) {
            if (this.numSkip == 0) {  // if the skip turn finished -> current player is skipped, next player is normal
                this.nextMessage = MessageConstant.normalTurn;
            } else {  // Decrease number of player that needs to be skipped
                this.numSkip -= 1;
                if (this.players.get(this.getNextPlayerIndex(this.players.indexOf(this.currentPlayer))).isHuman()) {  // If current player is human -> set human skip message
                    this.nextMessage = MessageConstant.skipTurn;
                } else {  // Else if current player is AI -> set AI skip message
                    this.nextMessage = MessageConstant.aISkipped;
                }
            }
        }
        System.out.println("skip number after: " + this.numSkip);
        // Update current player
        this.currentPlayer = this.players.get(this.getNextPlayerIndex(this.players.indexOf(this.currentPlayer)));
        this.numOfDraw = 0; // Reset number of cards the player has drawn to 0
        this.addDrawNum = true;

        // If the next player is human and the turn is not skip
        if (this.currentPlayer.isHuman() && !(this.nextMessage.equals(MessageConstant.skipTurn) ||
                this.nextMessage.equals(MessageConstant.aISkipped))){
            this.undoEvent = new DoEvent(this, this.topCard, this.isClockWise, this.isLight, this.targetColor,
                    this.previousColor, this.needToDraw, this.numSkip, this.valid_wild_draw_two_or_color,
                    this.drawUntilColor, this.nextMessage);  // Set undoEvent - store current game info
        }else {
            this.undoEvent = null;
        }

        this.unoView.setBeforeEachTurn(new UnoGameEvent(this, this.currentPlayer, this.nextMessage,
                this.topCard.getCard(this.isLight), this.targetColor, this.directionString(), this.isLight,
                this.sideString()));  // update the view

        // If next player is AI and the turn is not skip
        if (!this.currentPlayer.isHuman() &&
                !(this.nextMessage.equals(MessageConstant.skipTurn) ||
                        this.nextMessage.equals(MessageConstant.aISkipped))) {
            this.AITurn();  // AI does its turn
        }

    }

    /**
     * AITurn does the things that AI need to do for this turn (draw cards or plays a card)
     */
    public void AITurn() {
        System.out.println("in AI turn");
        if (this.nextMessage.equals(MessageConstant.normalTurn)) {  // If Ai can draw or play a card
            CardModel playedCard = this.pickCardForAI(this.currentPlayer);  // AI plays a card or draw a card
            if (playedCard != null) {
                System.out.println("AI plays a card");
                this.unoView.playCard(this.currentPlayer.getHand().indexOf(playedCard));  // updates the UNO game view
                this.playACard(playedCard);  // AI plays card
            } else {
                System.out.println("AI draws a card");
                this.unoView.updateGameMessageAndButtons(MessageConstant.aIPickedUp);
            }
        } else {  // If AI needs to do something -> not a normal turn
            System.out.println("AI handle an action");
            this.handleAIReaction();
        }
    }

    /**
     * AIChooseColor chooses a random color for AI
     */
    public void AIChooseColor() {
        CardSideModel.Color[] colors = this.getColourChoices();
        Random random = new Random();
        System.out.println("color length " + colors.length);
        int newColorIndex = random.nextInt(colors.length);
        this.targetColor = colors[newColorIndex];
    }

    /**
     * HandleAIReaction does correspond actions for previous played card
     */
    public void handleAIReaction() {
        String AIMessage;
        int cardsBefore = this.currentPlayer.getHand().size();  // Number of cards in player's hand before it draw
        System.out.println("AI hand before action:\n");
        for (int i = 0; i < this.currentPlayer.getHand().size(); i++) {
            System.out.print(this.currentPlayer.getHand().get(i).toString(isLight) + ", ");
        }
        System.out.println("\n");
        if (this.nextMessage.equals(MessageConstant.drawOneTurn)) {
            drawCards(this.currentPlayer, 1);
            AIMessage = MessageConstant.aIDrawOne;
        } else if (this.nextMessage.equals(MessageConstant.wildDrawTwoTurn)) {
            drawCards(this.currentPlayer, 2);
            AIMessage = MessageConstant.aIDrawTwo;
        } else if (this.nextMessage.equals(MessageConstant.drawFiveTurn)) {
            drawCards(this.currentPlayer, 5);
            AIMessage = MessageConstant.aIDrawFive;
        } else {  // draw color
            this.drawColorAction(this.currentPlayer);
            AIMessage = MessageConstant.aIdrawColor;
        }
        System.out.println("AI hand after action:\n");
        for (int i = 0; i < this.currentPlayer.getHand().size(); i++) {
            System.out.print(this.currentPlayer.getHand().get(i).toString(isLight) + ", ");
        }
        System.out.println("\n");

        this.needToDraw = 0;  // all action cards there are draw cards action -> reset need to draw back to 0
        for (int i = this.currentPlayer.getHand().size() - cardsBefore - 1; i < this.currentPlayer.getHand().size(); i++) {
            this.unoView.addNewCard(this.currentPlayer.getHand().get(i).getCard(this.isLight));  // add card to display
        }
        this.unoView.updateGameMessageAndButtons(AIMessage);
        this.nextMessage = MessageConstant.normalTurn;  // Next player has a normal turn
    }

    /**
     * nextPlayer() calculate and return the next player's index
     * @param curPlayIndex  the current player's index in
     * @return the index of the player who play the next turn
     */
    int getNextPlayerIndex(int curPlayIndex) {
        int addedNum = 1;  // the number which is used to calculate
        if (!this.isClockWise) {
            addedNum = this.players.size() - addedNum;
        }
        return (curPlayIndex + addedNum) % this.players.size();
    }

    /**
     * challengeAccepted is called when a player accepts a challenge to the previous player when they played a wild draw two card.
     * It will evaluate whether the previous player is guilty or not. If found guilty, it will add two cards to the previous player,
     * else it will make current player draw two cards
     */
    public void challengeAccepted() {
        //get previous player
        PlayerModel prevPlayer = this.getPrevPlayer();
        if (valid_wild_draw_two_or_color) {
            if (this.drawUntilColor) {
                this.unoView.updateGameMessageAndButtons(MessageConstant.notGuiltyColor);
            } else {
                this.unoView.updateGameMessageAndButtons(MessageConstant.notGuiltyTwo);
            }
        } else {
            if (this.drawUntilColor) {  // if challenge draw color
                this.drawColorAction(prevPlayer);
                this.unoView.updateGameMessageAndButtons(MessageConstant.guiltyColor);
                System.out.println("finish draw color");
            } else {  // if challenge draw two
                //add two cards to the prev player hand
                this.drawCards(prevPlayer, 2);
                this.needToDraw = 0;  // Reset, then current player does not need to draw cards.
                this.unoView.updateGameMessageAndButtons(MessageConstant.guiltyTwo);
            }
        }

    }

    /**
     * drawColorAction allows the player keep drawing cards until get the target color
     * @param player
     */
    public void drawColorAction(PlayerModel player) {
        while (true) {
            this.drawCards(player, 1);  // previous player draw one card
            CardModel justDraw = player.getHand().get(player.getHand().size() - 1);
            System.out.println("previous/AI draw: " + justDraw.getCard(this.isLight).toString());
            if (justDraw.getCard(isLight).getColor() == this.targetColor) {
                this.drawUntilColor = false;
                break;
            }
        }
    }

    /**
     * getPrecPlayer returns previous player
     * @return
     */
    public PlayerModel getPrevPlayer() {
        if (isClockWise) { //if the direction is clockwise, the prev player is at index = index - 1
            if (players.indexOf(currentPlayer) == 0) { //when curr player index = 0, prev player is at index (size of players - 1)
                return players.get(players.size() - 1); //tested
            } else {
                return players.get(players.indexOf(currentPlayer) - 1); //tested
            }
        } else {//if the direction is counter-clockwise, the prev player is at index = index + 1
            if (players.indexOf(currentPlayer) == (players.indexOf(players.size() - 1))) {
                return players.get(0);
            } else {
                return players.get(players.indexOf(currentPlayer) + 1); //tested
            }
        }
    }

    /**
     * isLastCard checks if the current player plays all cards in hand
     * @return if the player has no cards in hand
     */
    public boolean isLastCard() {
        return this.currentPlayer.getHand().size() == 0;
    }

    /**
     * getCardPoint() returns the point for a input card type
     * @param cardType  the type of the uno card
     * @return the point of the uno card
     */
    public int getCardPoint(CardSideModel.Type cardType) {
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
    public void updatePlayerPoint() {
        int points = 0;  // The points which need to be added
        for (PlayerModel player : this.players) {
            if (!player.equals(this.roundWinner)) {  // If the player is not the winner of the current round
                for (CardModel card : player.getHand()) {  // For each card in the player's hand
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
    public void finishRound() {
        this.updatePlayerPoint();  // Update the winner's point
        this.unoView.updateRoundFinished(new UnoFinishEvent(this, this.roundWinner,
                this.players.indexOf(this.roundWinner), this.roundNum));
        if (this.roundWinner.getScore() >= 500) {  // If game finished
            this.finishGame();
        } else {
            this.roundNum += 1;  // Increase round number by 1
            this.initRound();  // init a new round
        }
    }

    /**
     * finishGame updates view to tell players that game has finished
     */
    public void finishGame() {
        this.unoView.updateGameFinished(new UnoFinishEvent(this, this.roundWinner,
                this.players.indexOf(this.roundWinner), -1));
    }

    public CardSideModel.Color[] getColourChoices() {
        CardSideModel.Color[] choices;
        if (this.isLight) {
            choices = new CardSideModel.Color[]{CardSideModel.Color.YELLOW, CardSideModel.Color.BLUE,
                    CardSideModel.Color.RED, CardSideModel.Color.GREEN};
        } else {
            choices = new CardSideModel.Color[]{CardSideModel.Color.PINK, CardSideModel.Color.PURPLE,
                    CardSideModel.Color.TEAL, CardSideModel.Color.ORANGE};
        }
        return choices;
    }

    /**
     * getDrawMessage returns the message constant depends number of cards the player needs to draw
     * @return
     */
    public String getDrawMessage() {
        if (this.needToDraw == 4) {
            return MessageConstant.drawFourTurn;
        } else if (this.needToDraw == 3) {
            return MessageConstant.drawThreeTurn;
        } else if (this.needToDraw == 2) {
            return MessageConstant.drawTwoTurn;
        } else {  // this.needToDraw == 1
            return MessageConstant.drawOneTurn;
        }
    }

    public void saveNumOfHumanPlayers(int numOfHumanPlayers) {
        System.out.println(" In model, num of human players: " + numOfHumanPlayers);
        this.numOfHumanPlayers = numOfHumanPlayers;
    }

    public void saveNumOfAIPlayers(int numOfAIPlayers) {
        System.out.println(" In model, num of AI players: " + numOfAIPlayers);
        this.numOfAIplayers = numOfAIPlayers;
    }

    public CardSideModel.Color getTargetColor() {
        return this.targetColor;
    }

    /**
     * Gets the current player's deck.
     *
     * @return the current player's deck
     */
    public DeckModel getMyDeck() {
        return myDeck;
    }

    /**
     * Gets the top card of the deck.
     *
     * @return the top card
     */
    public CardModel getTopCard() {
        return topCard;
    }

    /**
     * Gets the current round number.
     *
     * @return the round number
     */
    public int getRoundNum() {
        return roundNum;
    }

    /**
     * Gets the current player.
     *
     * @return the current player
     */
    public PlayerModel getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Sets the top card of the deck.
     *
     * @param topCard the top card to be set
     */
    public void setTopCard(CardModel topCard) {
        this.topCard = topCard;
    }

    /**
     * Sets the players for testing purposes.
     *
     * @param players the list of players to be set
     */
    public void setPlayersForTest(List<PlayerModel> players) {
        this.players = new ArrayList<>(players);
    }

    /**
     * Checks if the current state is light.
     *
     * @return true if light, false otherwise
     */
    public boolean isLight() {
        return isLight;
    }

    /**
     * Sets the light state.
     *
     * @param light the light state to be set
     */
    public void setIsLight(boolean light) {
        isLight = light;
    }

    /**
     * Gets the winner of the round.
     *
     * @return the round winner
     */
    public PlayerModel getRoundWinner() {
        return roundWinner;
    }

    /**
     * Sets the winner of the round.
     *
     * @param roundWinner the round winner to be set
     */
    public void setRoundWinner(PlayerModel roundWinner) {
        this.roundWinner = roundWinner;
    }

    /**
     * Sets the clockwise rotation state.
     *
     * @param isClockWise the clockwise state to be set
     */
    public void setIsClockWise(boolean isClockWise) {
        this.isClockWise = isClockWise;
    }

    /**
     * Gets the clockwise rotation state.
     *
     * @return true if clockwise, false otherwise
     */
    public boolean getIsClockWise() {
        return isClockWise;
    }

    /**
     * Gets the list of players.
     *
     * @return the list of players
     */
    public List<PlayerModel> getPlayers() {
        return this.players;
    }

    /**
     * Sets the number of human players.
     *
     * @param numOfHumanPlayers the number of human players to be set
     */
    public void setNumOfHumanPlayers(int numOfHumanPlayers) {
        this.numOfHumanPlayers = numOfHumanPlayers;
    }

    /**
     * Sets the number of AI players.
     *
     * @param numOfAIplayers the number of AI players to be set
     */
    public void setNumOfAIplayers(int numOfAIplayers) {
        this.numOfAIplayers = numOfAIplayers;
    }

    /**
     * Sets the current player.
     *
     * @param player the current player to be set
     */
    public void setCurrentPlayer(PlayerModel player) {
        this.currentPlayer = player;
    }

    /**
     * Sets the number of skips.
     *
     * @param num the number of skips to be set
     */
    public void setNumSkip(int num) {
        this.numSkip = num;
    }

    /**
     * Gets the initial number of cards.
     *
     * @return the initial number of cards
     */
    public int getInitNumOfCards() {
        return this.initNumOfCards;
    }

    /**
     * Sets the initial number of cards.
     *
     * @param initNumOfCards the initial number of cards to be set
     */
    public void setInitNumOfCards(int initNumOfCards) {
        this.initNumOfCards = initNumOfCards;
    }

    /**
     * Gets the hand of a specific player.
     *
     * @param player the player whose hand is to be retrieved
     * @return the hand of the specified player
     */
    public List<CardModel> getPlayerHand(PlayerModel player) {
        return player.getHand();
    }

    /**
     * Checks if the game is finished.
     *
     * @return true if the game is finished, false otherwise
     */
    public boolean isGameFinished() {
        for (PlayerModel player : players) {
            if (player.getScore() >= 500) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets the next message for testing purposes.
     *
     * @param nextMessage the next message to be set
     */
    public void setNextMessage(String nextMessage) {
        this.nextMessage = nextMessage;
    }

    /**
     * Gets the UnoView instance for testing purposes.
     *
     * @return the UnoView instance
     */
    public UnoView getUnoView() {
        return this.unoView;
    }

    /**
     * Sets a predefined deck for testing purposes.
     *
     * @param testDeck the predefined deck to be set
     */
    public void setTestDeck(LinkedList<CardModel> testDeck) {
        myDeck.setDeck(testDeck); // Assuming you have a method in DeckModel to set the deck
    }

    /**
     * updateGameState updates the uno model information.
     * @param doEvent
     */
    public void updateGameState(DoEvent doEvent){
        this.topCard = doEvent.getTopCard();
        this.isClockWise = doEvent.isClockwise();
        this.isLight = doEvent.isLight();
        this.targetColor = doEvent.getTargetColor();
        this.previousColor = doEvent.getPreviousColor();
        this.needToDraw = doEvent.getNumOfDraw();
        this.numSkip = doEvent.getNumOfSkip();
        this.valid_wild_draw_two_or_color = doEvent.isValidWild();
        this.drawUntilColor = doEvent.isDrawColor() ;
        this.nextMessage = doEvent.getMessage();
    }

    /**
     * playerUndo sets the game state back to when the player did nothing.
     */
    public void playerUndo(){
        this.addDrawNum = false;
        if (this.undoEvent != null) {  // Undo event must not be null -> just for ouble check
            this.redoEvent = new DoEvent(this, this.topCard, this.isClockWise, this.isLight, this.targetColor,
                    this.previousColor, this.needToDraw, this.numSkip, this.valid_wild_draw_two_or_color,
                    this.drawUntilColor, this.nextMessage);  // Set redoEvent - store current game info
            if (this.numOfDraw > 0) {  // If the player draw card(s) in this turn
                System.out.println("number of draw in undo: " + this.numOfDraw);
                System.out.println("hand size in undo: " + this.currentPlayer.getHand().size());
                // remove each drawn card and add to the top of the deck
                for (int i = 0; i < this.numOfDraw; i++) {
                    CardModel drawnCard = this.currentPlayer.getHand().get(this.currentPlayer.getHand().size() - 1);
                    this.currentPlayer.playCard(drawnCard);
                    this.myDeck.addToTop(drawnCard);
                }
            }else { // else the player must play a card  (if the player is skipped, cannot do undo and redo)
                // Add current top card back to player's hand
                this.currentPlayer.pickUpCard(this.topCard);
                this.unoView.addNewCard(this.topCard.getCard(this.isLight));
            }
            this.updateGameState(this.undoEvent);  // Update game state
            this.unoView.setBeforeEachTurn(new UnoGameEvent(this, this.currentPlayer, this.nextMessage,
                    this.topCard.getCard(this.isLight), this.targetColor, this.directionString(), this.isLight,
                    this.sideString()));
            this.unoView.enableRedo(true);  // enable Redo button (Undo button is disabled in setBeforeEachTurn)
        }
    }

    public void playerRedo(){
        if (this.numOfDraw > 0){  // If player draw cards before undo
            // remove each drawn card from deck and add back to player's hand
            this.drawCards(this.currentPlayer, this.numOfDraw);
            for (int i = 0; i < this.numOfDraw; i++) {  // add each card back to view
                CardModel drawnCard = this.currentPlayer.getHand().
                        get(this.currentPlayer.getHand().size() - this.numOfDraw + i);
                this.unoView.addNewCard(drawnCard.getCard(this.isLight));
            }
        }else {  // If player play a card
            // remove the card from player's hand
            // the removed card is the card the player played, which is the top card in redo event

            // remove from view
            this.unoView.playCard(this.currentPlayer.getHand().indexOf(this.redoEvent.getTopCard()));
            this.currentPlayer.playCard(this.redoEvent.getTopCard());  // remove from hand

            if (this.redoEvent.isLight() != this.isLight){  // If play flip card -> update card side
                this.unoView.updateHandSides(this.redoEvent.isLight(), this.currentPlayer.getHand());
            }
        }
        this.updateGameState(this.redoEvent);
        this.unoView.enableRedo(false);  // disable redo function
        // Update instructions and buttons in view
        this.unoView.updateGameMessageAndButtons(MessageConstant.nextPlayer);
        this.unoView.setAfterPlayACard(this.targetColor, this.topCard.getCard(this.isLight),
                this.directionString(), this.sideString());  // Update played card and color
    }
    


}
