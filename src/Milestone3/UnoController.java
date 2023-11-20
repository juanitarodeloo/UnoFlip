/**
 * UnoController is the controller component that connects the view and model. It holds the actions that happen when a
 * user interacts with the view. It processes all the logic and incoming requests.
 * @Authors: Rebecca Li, Juanita Rodelo
 */
package Milestone3;

import Milestone2.View.UnoView;
import Milestone3.Model.CardSideModel;
import Milestone3.Model.UnoModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UnoController implements ActionListener{
    private Milestone3.Model.UnoModel model;
    private int numOfPlayers;

    public UnoController(UnoModel model){
        this.model = model;
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
                    this.model.initGame();
                    break;
                case "Draw One":  // If the player click the button to draw a card
                    this.model.playerAction(-1);
                    break;
                case "Next Player":  // If the player clicks Next Player button
                    this.model.nextPlayer();
                    break;
                default:  // If the player click a card button to draw a card
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
            return false;
        }
    }

    /**
     * newColor interrupts the users selected color from the view
     * @param newColor
     */
    public void newColor(CardSideModel.Color newColor){
        this.model.setTargetColor(newColor);
    }
}
