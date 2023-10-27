package Milestone2;

import Milestone1.Card;
import Milestone1.Deck;
import Milestone1.Player;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class UnoController implements ActionListener {
    private UnoModel model;
    private Deck myDeck;
    private List<Card> discardPile;
    private List<Player> players;
    private List<JTextField> playerNames;



    public UnoController(UnoModel model){
        this.model = model;
        playerNames = new ArrayList<>();
    }

    public void startGame(){
        boolean gameDone = false;
        //players = initPlayers();

        //while game isn't done...

    }

    public void savePlayerNames(List<JTextField> playerNameFields){
        //System.out.println("in savePlayer");
        for(JTextField p: playerNameFields){
            this.playerNames.add(p);
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        //save selected item
        Object source = e.getSource();

        //TODO: idk if the following needs to all be put in the model or kept here?
        if(source instanceof JComboBox){
            JComboBox<Integer> jComboBox = (JComboBox<Integer>) source;
            Integer numOfPlayers = (Integer) jComboBox.getSelectedItem();
            System.out.println("Selected Item: " + numOfPlayers); //just for testing
            //model.startGame(); //may have to move this
            model.initPlayers(numOfPlayers);
        }else if(source instanceof JButton){
            //int numOfPlayersInt = numOfPlayers;
            String[] playerNamesText = new String[playerNames.size()];
            for(int i = 0; i < playerNames.size(); i++){
                playerNamesText[i] = playerNames.get(i).getText();
            }
            //System.out.println("num of players from controller:" + playerNamesText.length);
            model.savePlayerNames(playerNamesText);
        }

    }
}
