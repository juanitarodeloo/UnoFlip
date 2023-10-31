package Milestone2;

import Milestone1.Card;
import Milestone1.Deck;
import Milestone1.Player;

import java.util.ArrayList;
import java.util.List;

public class UnoModel {
    private List<Player> players;
    private Deck myDeck;
    private List<Card> discardPile;

    public UnoModel(){
        myDeck = new Deck();
        discardPile = new ArrayList<>();
        players = new ArrayList<>();
    }

    public List<Player> initPlayers(String[] names){
        //for each name in the list provided, create a new player and deal them 7 cards to start the game
        for (String name : names) {
            Player p = new Player(name);
            players.add(p);
            drawCards(players.get(players.indexOf(p)), 7);
            System.out.println(p.toString()); //for testing
        }
        return players;
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

    public Card getTargetCard(){
        Card targetCard = myDeck.draw();
        discardPile.add(targetCard);
        return targetCard;
    }

}
