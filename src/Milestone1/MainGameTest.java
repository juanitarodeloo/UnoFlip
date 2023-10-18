package Milestone1;
import org.junit.Before;
import org.junit.Test;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MainGameTest {

    private ArrayList<Player> players;
    private MainGame newGame;

    private Player p1;
    private Player p2;

    private Deck deck;

    @Before
    public void setup(){

        this.newGame = new MainGame();
        p1 = new Player("P1");
        p2 = new Player("P2");
        deck = new Deck();
        deck.createDeck();
        this.players = new ArrayList<Player>();
        players.add(p1);
        players.add(p2);

    }

    @Test
    public void testInitPlayer(){
        String p1Name = "p1";
        Player p1 = new Player("p1");
        assertEquals(p1Name, p1.getName());

        List<Player> playerList =new ArrayList<Player>();
        playerList.add(p1);
        assertEquals(1, playerList.size());
    }

    @Test
    public void playGame(){

    }

    @Test
    public void testDealCards(){
        for (Player player: this.players){
            for (int i = 0; i < 8; i++){
                Card drawnCard = this.deck.draw();
                player.getHand().add(drawnCard);
            }
            assertEquals(player.getHand().size(), 8);
        }
    }

    @Test
    public void testValidate(){
        Card.Color targetColor = Card.Color.YELLOW;
        Card c1 = new Card(Card.Color.YELLOW, Card.Type.SIX);
        Card c2 = new Card(Card.Color.BLUE, Card.Type.REVERSE);
//        Card targetCard = new Card(targetColor, Card.Type.FIVE);

        assertTrue(targetColor == c1.getColor());
        assertFalse(targetColor == c2.getColor());

    }

    @Test
    public void testPickOrPlay(){
        Card c1 = new Card(Card.Color.YELLOW, Card.Type.SIX);
        Card c2 = new Card(Card.Color.BLUE, Card.Type.REVERSE);
        p1.pickUpCard(c1);
        assertEquals(1, p1.getHand().size());
        p1.pickUpCard(c2);
        assertEquals(2, p1.getHand().size());
        p1.playCard(c1);
        assertEquals(1, p1.getHand().size());
    }

    @Test
    public void testValidColor(){
        String colorString1 = "G";
        String colorString2 = "c";

        assertTrue(newGame.validColor(colorString1));
        assertFalse(newGame.validColor(colorString2));
    }


}
