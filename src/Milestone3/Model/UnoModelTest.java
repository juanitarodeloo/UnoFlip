package Milestone3.Model;

import Milestone3.View.UnoView;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class UnoModelTest {

    private UnoModel unoModel;

    @Before
    public void setUp() {
        unoModel = new UnoModel();

        // Stubbing the UnoView to prevent NullPointerException during tests
        UnoView viewStub = new UnoView() {
            @Override
            public void startGame(ArrayList<PlayerModel> players) {
            }

        };

        unoModel.setUnoView(viewStub);
        // Assuming setPlayersForTest is a method in UnoModel to set the players for testing
        unoModel.setPlayersForTest(Arrays.asList(new PlayerModel("Player1", true), new PlayerModel("Player2", false)));

        unoModel.initGame();


    }

    @Test
    public void testInitGame() {
        // Test that game initializes correctly
        assertNotNull("Deck should be initialized", unoModel.getMyDeck());
        assertNotNull("Discard pile should be initialized", unoModel.getDiscardPile());
        assertEquals("Initial round number should be 1", 1, unoModel.getRoundNum());
        assertNotNull("Top card should be set", unoModel.getTopCard());
        assertEquals("Initial target color should be set", unoModel.getTopCard().getCard(true).getColor(), unoModel.getTargetColor());
    }

    @Test
    public void testPlayCard() {
        PlayerModel currentPlayer = unoModel.getCurrentPlayer();
        assertNotNull("Current player should not be null", currentPlayer);

        CardModel cardToPlay = new CardModel(new CardSideModel(CardSideModel.Color.RED, CardSideModel.Type.FOUR),
                new CardSideModel(CardSideModel.Color.GREEN, CardSideModel.Type.FIVE));
        currentPlayer.getHand().add(cardToPlay);
        unoModel.setTopCard(new CardModel(new CardSideModel(CardSideModel.Color.RED, CardSideModel.Type.ONE),
                new CardSideModel(CardSideModel.Color.GREEN, CardSideModel.Type.TWO)));
        unoModel.setTargetColor(CardSideModel.Color.RED);

        unoModel.playACard(cardToPlay); // Should remove 'cardToPlay' from the player's hand

        assertFalse("Player's hand should not contain the played card", currentPlayer.getHand().contains(cardToPlay));
        assertEquals("Top card should be the played card", cardToPlay, unoModel.getTopCard());
    }
    @Test
    public void testDirectionString() {
        unoModel.setIsClockWise(true);
        assertEquals("Should return Clockwise", "Clockwise", unoModel.directionString());

        unoModel.setIsClockWise(false);
        assertEquals("Should return CounterClockwise", "CounterClockwise", unoModel.directionString());
    }

    @Test
    public void testSideString() {
        unoModel.setIsLight(true);
        assertEquals("Should return Light Side", "Light Side", unoModel.sideString());

        unoModel.setIsLight(false);
        assertEquals("Should return Dark Side", "Dark Side", unoModel.sideString());
    }

    @Test
    public void testInitRound() {
        unoModel.initRound();
        assertTrue("Game should start in a clockwise direction.", unoModel.getIsClockWise());
        assertTrue("Game should start with the light side of cards.", unoModel.isLight());
        assertNull("There should be no round winner at the start of a round.", unoModel.getRoundWinner());
    }




    @Test
    public void testNextPlayer() {
        // Assume it's currently player 1's turn
        unoModel.setCurrentPlayer(unoModel.getPlayers().get(0));
        unoModel.nextPlayer();
        assertEquals("Next player should be player 2.", unoModel.getPlayers().get(1), unoModel.getCurrentPlayer());

        // If a skip card was played
        unoModel.setNumSkip(1);
        unoModel.nextPlayer();
        // Should skip to the next player after player 2
        assertEquals("Should skip to the next player after player 2.", unoModel.getPlayers().get(2), unoModel.getCurrentPlayer());
    }

    @Test
    public void testInitPlayers() {
        // Clear the list before setting the number of players
        unoModel.getPlayers().clear();

        // Setting the number of human and AI players
        unoModel.setNumOfHumanPlayers(2);
        unoModel.setNumOfAIplayers(2);

        // Call the method to test
        unoModel.initPlayers();

        // Retrieve the list of players
        List<PlayerModel> players = unoModel.getPlayers();
        assertNotNull("Players list should not be null", players);
        assertEquals("Total number of players should be 4", 4, players.size());

        // Check the type of each player
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
        // Set the number of cards that should be dealt to each player
        int numberOfCardsToDeal = 7;
        unoModel.setInitNumOfCards(numberOfCardsToDeal);

        // Make sure we have players to deal cards to
        unoModel.setNumOfHumanPlayers(2);
        unoModel.setNumOfAIplayers(2);
        unoModel.initPlayers();

        // Call the method to test
        unoModel.initPlayerHands();

        // Retrieve the list of players
        List<PlayerModel> players = unoModel.getPlayers();
        for (PlayerModel player : players) {
            // Retrieve the hand for each player
            List<CardModel> hand = unoModel.getPlayerHand(player);
            assertNotNull("Player hand should not be null", hand);
            assertEquals("Player hand should have the correct number of cards",
                    numberOfCardsToDeal, hand.size());
        }
    }

    @Test
    public void testHandleAIReaction_DrawOne() {
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
        PlayerModel aiPlayer = new PlayerModel("AIPlayer", false);
        unoModel.setCurrentPlayer(aiPlayer);
        int initialHandSize = aiPlayer.getHand().size();
        CardSideModel.Color targetColor = CardSideModel.Color.BLUE; // Define the target color

        // Prepare a test deck where the target color appears after a few cards
        LinkedList<CardModel> testDeck = new LinkedList<>();
        testDeck.add(new CardModel(new CardSideModel(CardSideModel.Color.GREEN, CardSideModel.Type.ONE),
                new CardSideModel(CardSideModel.Color.GREEN, CardSideModel.Type.ONE)));
        testDeck.add(new CardModel(new CardSideModel(CardSideModel.Color.YELLOW, CardSideModel.Type.TWO),
                new CardSideModel(CardSideModel.Color.YELLOW, CardSideModel.Type.TWO)));
        // ... Add more cards of non-target colors
        testDeck.add(new CardModel(new CardSideModel(targetColor, CardSideModel.Type.THREE),
                new CardSideModel(targetColor, CardSideModel.Type.THREE))); // This is the target color card

        // Set the test deck in the UnoModel (assuming you have a method to do this)
        unoModel.setTestDeck(testDeck);
        unoModel.setTargetColor(targetColor); // Set the target color in the model

        unoModel.drawColorAction(aiPlayer);

        assertTrue("AI hand should contain the target color card",
                aiPlayer.getHand().stream().anyMatch(card -> card.getColor(true).equals(targetColor)));
        assertTrue("AI hand size should have increased by the number of cards drawn until the target color is drawn",
                aiPlayer.getHand().size() > initialHandSize);
    }

    @Test
    public void testGetNextPlayerIndexClockwise() {
        // Given a game set in a clockwise direction
        unoModel.setIsClockWise(true);
        unoModel.setPlayersForTest(Arrays.asList(
                new PlayerModel("Player1", true),
                new PlayerModel("Player2", false),
                new PlayerModel("Player3", true),
                new PlayerModel("Player4", false)
        ));

        // When the current player is the first player
        int currentPlayerIndex = 0;
        int nextPlayerIndex = unoModel.getNextPlayerIndex(currentPlayerIndex);

        // Then the next player index should be 1
        assertEquals("Next player index should be 1 in clockwise direction.", 1, nextPlayerIndex);
    }

    @Test
    public void testGetNextPlayerIndexCounterClockwise() {
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