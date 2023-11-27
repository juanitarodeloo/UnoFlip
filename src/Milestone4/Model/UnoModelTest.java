package Milestone4.Model;

/**
 * UnoModelTest contains Testing for the UnoFlip integration, Milestone 3
 *
 * @Author: Ayman Kamran, Adham Elmahi
 */

import Milestone4.View.UnoView;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class UnoModelTest {

    private UnoModel unoModel;

    @Before
    public void setUp() {
        // Initial setup for tests, creating an UnoModel instance and a mock UnoView
        unoModel = new UnoModel();
        UnoView viewStub = new UnoView() {
            @Override
            public void startGame(ArrayList<PlayerModel> players) {
            }
        };
        unoModel.setUnoView(viewStub);
        unoModel.setPlayersForTest(Arrays.asList(new PlayerModel("Player1", true), new PlayerModel("Player2", false)));
        unoModel.initGame();
    }

    @Test
    public void testInitGame() {
        // Tests if the game initializes correctly with the necessary components like deck and discard pile
        assertNotNull("Deck should be initialized", unoModel.getMyDeck());
        assertEquals("Initial round number should be 1", 1, unoModel.getRoundNum());
        assertNotNull("Top card should be set", unoModel.getTopCard());
        assertEquals("Initial target color should be set", unoModel.getTopCard().getCard(true).getColor(), unoModel.getTargetColor());
    }

    @Test
    public void testPlayCard() {
        // Tests if a card can be played correctly and its effects like changing the top card
        PlayerModel currentPlayer = unoModel.getCurrentPlayer();
        assertNotNull("Current player should not be null", currentPlayer);
        CardModel cardToPlay = new CardModel(new CardSideModel(CardSideModel.Color.RED, CardSideModel.Type.FOUR),
                new CardSideModel(CardSideModel.Color.GREEN, CardSideModel.Type.FIVE));
        currentPlayer.getHand().add(cardToPlay);
        unoModel.setTopCard(new CardModel(new CardSideModel(CardSideModel.Color.RED, CardSideModel.Type.ONE),
                new CardSideModel(CardSideModel.Color.GREEN, CardSideModel.Type.TWO)));
        unoModel.setTargetColor(CardSideModel.Color.RED);
        unoModel.playACard(cardToPlay);
        assertFalse("Player's hand should not contain the played card", currentPlayer.getHand().contains(cardToPlay));
        assertEquals("Top card should be the played card", cardToPlay, unoModel.getTopCard());
    }

    @Test
    public void testDirectionString() {
        // Tests if the directionString method returns the correct string representation of the game direction
        unoModel.setIsClockWise(true);
        assertEquals("Should return Clockwise", "Clockwise", unoModel.directionString());
        unoModel.setIsClockWise(false);
        assertEquals("Should return CounterClockwise", "CounterClockwise", unoModel.directionString());
    }

    @Test
    public void testSideString() {
        // Tests if the sideString method correctly identifies the current side of the cards (light or dark)
        unoModel.setIsLight(true);
        assertEquals("Should return Light Side", "Light Side", unoModel.sideString());
        unoModel.setIsLight(false);
        assertEquals("Should return Dark Side", "Dark Side", unoModel.sideString());
    }

    @Test
    public void testInitRound() {
        // Tests if a new round is initialized correctly with the appropriate game settings
        unoModel.initRound();
        assertTrue("Game should start in a clockwise direction.", unoModel.getIsClockWise());
        assertTrue("Game should start with the light side of cards.", unoModel.isLight());
        assertNull("There should be no round winner at the start of a round.", unoModel.getRoundWinner());
    }

    @Test
    public void testNextPlayer() {
        // Tests if the nextPlayer method correctly updates the current player to the next in sequence
        unoModel.setCurrentPlayer(unoModel.getPlayers().get(0));
        unoModel.nextPlayer();
        assertEquals("Next player should be player 2.", unoModel.getPlayers().get(1), unoModel.getCurrentPlayer());
        unoModel.setNumSkip(1);
        unoModel.nextPlayer();
        assertEquals("Should skip to the next player after player 2.", unoModel.getPlayers().get(2), unoModel.getCurrentPlayer());
    }

    @Test
    public void testInitPlayers() {
        // Tests if players are initialized correctly, including the division between human and AI players
        unoModel.getPlayers().clear();
        unoModel.setNumOfHumanPlayers(2);
        unoModel.setNumOfAIplayers(2);
        unoModel.initPlayers();
        List<PlayerModel> players = unoModel.getPlayers();
        assertNotNull("Players list should not be null", players);
        assertEquals("Total number of players should be 4", 4, players.size());
        int humanCount = 0;
        int aiCount = 0;
        for (PlayerModel player : players) {
            if (player.isHuman()) {
                humanCount++;
            } else {
                aiCount++;
            }
        }
        assertEquals("Number of human players should be 2", 2, humanCount);
        assertEquals("Number of AI players should be 2", 2, aiCount);
    }

    @Test
    public void testInitPlayerHands() {
        // Tests if players' hands are initialized correctly with the specified number of cards
        int numberOfCardsToDeal = 7;
        unoModel.setInitNumOfCards(numberOfCardsToDeal);
        unoModel.setNumOfHumanPlayers(2);
        unoModel.setNumOfAIplayers(2);
        unoModel.initPlayers();
        unoModel.initPlayerHands();
        List<PlayerModel> players = unoModel.getPlayers();
        for (PlayerModel player : players) {
            List<CardModel> hand = unoModel.getPlayerHand(player);
            assertNotNull("Player hand should not be null", hand);
            assertEquals("Player hand should have the correct number of cards", numberOfCardsToDeal, hand.size());
        }
    }

    @Test
    public void testHandleAIReaction_DrawOne() {
        // Tests AI's reaction to a draw one turn, ensuring the AI player's hand increases by one card and the correct message is shown
        unoModel.setNextMessage(MessageConstant.drawOneTurn);
        PlayerModel aiPlayer = new PlayerModel("AIPlayer", false);
        unoModel.setCurrentPlayer(aiPlayer);
        int initialHandSize = aiPlayer.getHand().size();
        unoModel.handleAIReaction();
        assertEquals("AI hand should increase by one card", initialHandSize + 1, aiPlayer.getHand().size());
        assertEquals("The last message should be draw one", MessageConstant.aIDrawOne, unoModel.getUnoView().getLastMessageForTest());
    }

    @Test
    public void testHandleAIReaction_DrawTwo() {
        // Tests AI's reaction to a wild draw two turn, checking if the AI player's hand correctly increases by two cards and the appropriate message is displayed
        unoModel.setNextMessage(MessageConstant.wildDrawTwoTurn);
        PlayerModel aiPlayer = new PlayerModel("AIPlayer", false);
        unoModel.setCurrentPlayer(aiPlayer);
        int initialHandSize = aiPlayer.getHand().size();
        unoModel.handleAIReaction();
        assertEquals("AI hand should increase by two cards", initialHandSize + 2, aiPlayer.getHand().size());
        assertEquals("The last message should be draw two", MessageConstant.aIDrawTwo, unoModel.getUnoView().getLastMessageForTest());
    }

    @Test
    public void testHandleAIReaction_DrawFive() {
        // Verifies AI's response when required to draw five cards, ensuring the AI player's hand increases accordingly and the correct message is displayed
        unoModel.setNextMessage(MessageConstant.drawFiveTurn);
        PlayerModel aiPlayer = new PlayerModel("AIPlayer", false);
        unoModel.setCurrentPlayer(aiPlayer);
        int initialHandSize = aiPlayer.getHand().size();
        unoModel.handleAIReaction();
        assertEquals("AI hand should increase by five cards", initialHandSize + 5, aiPlayer.getHand().size());
        assertEquals("The last message should be draw five", MessageConstant.aIDrawFive, unoModel.getUnoView().getLastMessageForTest());
    }

    @Test
    public void testDrawColorAction() {
        // Tests the AI's ability to draw cards until a specific target color is drawn
        PlayerModel aiPlayer = new PlayerModel("AIPlayer", false);
        unoModel.setCurrentPlayer(aiPlayer);
        int initialHandSize = aiPlayer.getHand().size();
        CardSideModel.Color targetColor = CardSideModel.Color.BLUE;
        LinkedList<CardModel> testDeck = new LinkedList<>();
        // Setting up a test deck with predefined cards
        testDeck.add(new CardModel(new CardSideModel(CardSideModel.Color.GREEN, CardSideModel.Type.ONE),
                new CardSideModel(CardSideModel.Color.GREEN, CardSideModel.Type.ONE)));
        testDeck.add(new CardModel(new CardSideModel(CardSideModel.Color.YELLOW, CardSideModel.Type.TWO),
                new CardSideModel(CardSideModel.Color.YELLOW, CardSideModel.Type.TWO)));
        testDeck.add(new CardModel(new CardSideModel(targetColor, CardSideModel.Type.THREE),
                new CardSideModel(targetColor, CardSideModel.Type.THREE)));
        unoModel.setTestDeck(testDeck);
        unoModel.setTargetColor(targetColor);
        unoModel.drawColorAction(aiPlayer);
        assertTrue("AI hand should contain the target color card", aiPlayer.getHand().stream().anyMatch(card -> card.getColor(true).equals(targetColor)));
        assertTrue("AI hand size should have increased by the number of cards drawn until the target color is drawn", aiPlayer.getHand().size() > initialHandSize);
    }

    @Test
    public void testGetNextPlayerIndexClockwise() {
        // Tests if the method correctly calculates the next player's index in a clockwise direction
        unoModel.setIsClockWise(true);
        unoModel.setPlayersForTest(Arrays.asList(
                new PlayerModel("Player1", true),
                new PlayerModel("Player2", false),
                new PlayerModel("Player3", true),
                new PlayerModel("Player4", false)
        ));
        int currentPlayerIndex = 0;
        int nextPlayerIndex = unoModel.getNextPlayerIndex(currentPlayerIndex);
        assertEquals("Next player index should be 1 in clockwise direction.", 1, nextPlayerIndex);
    }

    @Test
    public void testGetNextPlayerIndexCounterClockwise() {
        // Tests if the method accurately computes the next player's index in a counter-clockwise direction
        unoModel.setIsClockWise(false);
        unoModel.setPlayersForTest(Arrays.asList(
                new PlayerModel("Player1", true),
                new PlayerModel("Player2", false),
                new PlayerModel("Player3", true),
                new PlayerModel("Player4", false)
        ));
        int currentPlayerIndex = 0;
        int nextPlayerIndex = unoModel.getNextPlayerIndex(currentPlayerIndex);
        assertEquals("Next player index should be last player index in counter-clockwise direction.", 3, nextPlayerIndex);
    }

    @Test
    public void testGetNextPlayerIndexAtEndOfList() {
        // Tests whether the getNextPlayerIndex method correctly wraps around to the first player when reaching the end of the player list in a clockwise direction
        unoModel.setIsClockWise(true);
        unoModel.setPlayersForTest(Arrays.asList(
                new PlayerModel("Player1", true),
                new PlayerModel("Player2", false),
                new PlayerModel("Player3", true),
                new PlayerModel("Player4", false)
        ));
        int currentPlayerIndex = unoModel.getPlayers().size() - 1;
        int nextPlayerIndex = unoModel.getNextPlayerIndex(currentPlayerIndex);
        assertEquals("Next player index should wrap around to 0.", 0, nextPlayerIndex);
    }


}