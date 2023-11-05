package Milestone2;

import Milestone2.Model.UnoModel;
import Milestone2.View.UnoView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class UnoController implements ActionListener{
    private UnoModel model;
    private List<JTextField> playerNames;
    private UnoView view;
    private int numOfPlayers;

    public UnoController(UnoModel model, UnoView view){
        this.model = model;
        this.view = view;
        playerNames = new ArrayList<>();
    }

//    public void savePlayerNames(List<JTextField> playerNameFields){
//        //System.out.println("in savePlayer");
//        for(JTextField p: playerNameFields){
//            this.playerNames.add(p);
//        }
//    }

//    @Override
//    public void actionPerformed(ActionEvent e) {
//        //save selected item
//        Object source = e.getSource();
//        if(source instanceof JComboBox){
//            //if the source is a JComboBox that means the user is
//            //inputting the number of players
//            JComboBox<Integer> jComboBox = (JComboBox<Integer>) source;
//            numOfPlayers = (Integer) jComboBox.getSelectedItem();
//            System.out.println("Selected Item: " + numOfPlayers); //just for testing
//            view.initPlayers(numOfPlayers);
//        }else if(e.getActionCommand().equals("Save")){
//            String[] playerNamesText = new String[playerNames.size()];
//            for(int i = 0; i < playerNames.size(); i++){
//                playerNamesText[i] = playerNames.get(i).getText();
//            }
//            List<Player> players = model.initPlayers(playerNamesText);
//            Card topCard = model.getTargetCard();
//            view.gameView(players, topCard); //TODO: view should already have numofplayers
//        }
//
//    }
    @Override
    public void actionPerformed(ActionEvent e) {
        //save selected item
        Object source = e.getSource();
        if(source instanceof JComboBox){
            //if the source is a JComboBox that means the user is inputting the number of players
            JComboBox<Integer> jComboBox = (JComboBox<Integer>) source;
            numOfPlayers = (Integer) jComboBox.getSelectedItem();
//            System.out.println("Selected Item: " + numOfPlayers); //just for testing
            this.model.setTempPlayerNum(numOfPlayers);  // Save the number of player in model
        }else if(source instanceof JButton){
            switch (e.getActionCommand()){
                case "Start":  // If click to start the game.
//                    System.out.println("click start");
                    this.model.initGame();
                    break;
                case "Draw One":  // If the player click the button to draw a card
//                    System.out.println("click draw one");
                    this.model.playerAction(-1);
                    break;
                case "Next Player":  // If the player clicks Next Player button
//                    System.out.println("click Next Player");
                    this.model.nextPlayer();
                    break;
                default:  // If the player click a card button to draw a card
//                    System.out.println("Play a card");
                    this.model.playerAction(Integer.parseInt(e.getActionCommand()));
            }

    }

}
}
