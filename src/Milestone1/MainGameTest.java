package Milestone1;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.*;

public class MainGameTest {

    private MainGame newGame;
    @Before
    public void setup(){

        this.newGame = new MainGame();
        Player p1 = new Player("P1");
        Player p2 = new Player("P2");
        Player p3 = new Player("P3");
        Player p4 = new Player("P4");
        Deck deck = new Deck();
        deck.createDeck();
        this.newGame.addPlayer(p1);
        this.newGame.addPlayer(p2);
        this.newGame.addPlayer(p3);
        this.newGame.addPlayer(p4);

    }

    // Just a rough test -> not call the original method because it needs user input
    @Test
    public void testPickOrPlay(){
        Card c1 = new Card(Card.Color.YELLOW, Card.Type.SIX);
        Card c2 = new Card(Card.Color.BLUE, Card.Type.REVERSE);
        newGame.getPlayer().get(0).pickUpCard(c1);
        assertEquals(1, newGame.getPlayer().get(0).getHand().size());
        newGame.getPlayer().get(0).pickUpCard(c2);
        assertEquals(2, newGame.getPlayer().get(0).getHand().size());
        newGame.getPlayer().get(0).playCard(c1);
        assertEquals(1, newGame.getPlayer().get(0).getHand().size());
    }

    // Just a rough test -> not call the original method because it needs user input
    @Test
    public void testInitPlayer(){
        String p1Name = "p1";
        Player p1 = new Player("p1");
        assertEquals(p1Name, p1.getName());
        List<Player> playerList =new ArrayList<Player>();
        playerList.add(p1);
        assertEquals(1, playerList.size());
    }

    // Just a rough test -> not call the original method because it needs user input
    @Test
    public void testDealCards(){
        for (Player player: newGame.getPlayer()){
            for (int i = 0; i < 8; i++){
                Card drawnCard = newGame.getDeck().draw();
                player.getHand().add(drawnCard);
            }
            assertEquals(player.getHand().size(), 8);
        }
    }

    @Test
    public void testPlayACard(){
        Scanner sc = new Scanner(System.in);
        Card skipCard = new Card(Card.Color.RED, Card.Type.SKIP);
        Card reverseCard = new Card(Card.Color.BLUE, Card.Type.REVERSE);

        // not test wild card -> do not need Scanner
        // Assume the game has 5 players

        // Assume first player play a skip card, then the method should return the index of the third player (index 2)
        assertEquals(2, newGame.playACard(skipCard, newGame.getPlayer().get(0), sc));

        // Assume first player play a reverse card, then the method should return the index of the last player (index 3)
        assertEquals(3, newGame.playACard(reverseCard, newGame.getPlayer().get(0), sc));
    }

    @Test
    public void testValidColor(){
        String colorString1 = "G";
        String colorString2 = "c";
        assertTrue(newGame.validColor(colorString1));
        assertFalse(newGame.validColor(colorString2));
    }

    @Test
    public void testGetCardPoint(){
        Card testCard1 = new Card(Card.Color.YELLOW, Card.Type.REVERSE); // point 20
        Card testCard2 = new Card(Card.Color.YELLOW, Card.Type.EIGHT); // point 8
        assertEquals(20, newGame.getCardPoint(testCard1.getType()));
        assertEquals(8, newGame.getCardPoint(testCard2.getType()));
    }
    @Test
    public void testUpdatePlayerPoint(){
        Card testCard1 = new Card(Card.Color.YELLOW, Card.Type.SIX); // point 6
        newGame.getPlayer().get(0).pickUpCard(testCard1);
        // Assume second player in the list win the game and only the first player has one card in hand
        newGame.updatePlayerPoint(newGame.getPlayer().get(1));
        assertEquals(6, newGame.getPlayer().get(1).getScore());
    }

    @Test
    public void testValidateCard(){
        newGame.setTargetColor(Card.Color.YELLOW);
        Card testCard1 = new Card(Card.Color.YELLOW, Card.Type.SIX);
        Card testCard2 = new Card(Card.Color.BLUE, Card.Type.REVERSE);
        Card testCard3 = new Card(Card.Color.RED, Card.Type.SKIP);
        Card previousCard = new Card(Card.Color.YELLOW, Card.Type.REVERSE);

        assertTrue(newGame.validateCard(previousCard, testCard1));
        assertTrue(newGame.validateCard(previousCard, testCard2));
        assertFalse(newGame.validateCard(previousCard, testCard3));
    }

}
