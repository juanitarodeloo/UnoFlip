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

    @Override
    public void actionPerformed(ActionEvent e) {
        //save selected item
        Object source = e.getSource();
        if(source instanceof JComboBox){
            //if the source is a JComboBox that means the user is inputting the number of players
            JComboBox<Integer> jComboBox = (JComboBox<Integer>) source;
            numOfPlayers = (Integer) jComboBox.getSelectedItem();
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

    /**
     * handleChallengeAccepted interrupts the users decision from the JOptionPane from the view
     * @param decision
     * @return true if challenge was accepted, false if challenge was not accepted
     */
    public boolean handleChallengeAccepted(int decision){
        if(decision == 0){
            this.model.challengeAccepted();
            return true;
        }else{
//            System.out.println("Player does not want to challenge");
            return false;
        }
    }
}
