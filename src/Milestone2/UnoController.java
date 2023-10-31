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
    private List<JTextField> playerNames;
    private UnoView view;
    private int numOfPlayers;

    public UnoController(UnoModel model, UnoView view){
        this.model = model;
        this.view = view;
        playerNames = new ArrayList<>();
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
        if(source instanceof JComboBox){
            //if the source is a JComboBox that means the user is
            //inputting the number of players
            JComboBox<Integer> jComboBox = (JComboBox<Integer>) source;
            numOfPlayers = (Integer) jComboBox.getSelectedItem();
            System.out.println("Selected Item: " + numOfPlayers); //just for testing
            view.initPlayers(numOfPlayers);
        }else if(e.getActionCommand().equals("Save")){
            String[] playerNamesText = new String[playerNames.size()];
            for(int i = 0; i < playerNames.size(); i++){
                playerNamesText[i] = playerNames.get(i).getText();
            }
            List<Player> players = model.initPlayers(playerNamesText);
            Card topCard = model.getTargetCard();
            view.gameView(players, topCard); //TODO: view should already have numofplayers
        }

    }
}
