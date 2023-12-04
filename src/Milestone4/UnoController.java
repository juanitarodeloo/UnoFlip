/**
 * UnoController is the controller component that connects the view and model. It holds the actions that happen when a
 * user interacts with the view. It processes all the logic and incoming requests.
 * @Authors: Rebecca Li, Juanita Rodelo
 */
package Milestone4;

import Milestone4.Model.CardSideModel;
import Milestone4.Model.UnoModel;
import Milestone4.View.UnoView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UnoController implements ActionListener{
    private UnoModel model;
    private int numOfPlayers;

    private UnoView view;

    public UnoController(UnoModel model, UnoView view){
        this.model = model;
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //save selected item
        Object source = e.getSource();
        if(source instanceof JComboBox){
            JComboBox<Integer> jComboBox = (JComboBox<Integer>) source;
            numOfPlayers = (Integer) jComboBox.getSelectedItem();
            if(e.getActionCommand().equals("human")){
                model.saveNumOfHumanPlayers(numOfPlayers);
            }else if(e.getActionCommand().equals("AI")){
                model.saveNumOfAIPlayers(numOfPlayers);
            }
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
                case "Replay":
                    System.out.println("replay was clicked");
                    this.view.confirmReplay();
                    break;
                case "Undo":  // If the player clicks Undo
                    System.out.println("click undo");
                    this.model.playerUndo();
                    break;
                case "Redo":  // If the player clicks Undo
                    System.out.println("click redo");
                    this.model.playerRedo();
                    break;
                default:  // If the player click a card button to draw a card
                    this.model.playerAction(Integer.parseInt(e.getActionCommand()));
            }

        }

    }

    /**
     * handleChallengeAccepted interrupts the users decision from the JOptionPane from the view
     * @param decision
     */
    public void handleChallengeAccepted(int decision){
        if(decision == 0){
            this.model.challengeAccepted();
        }
    }

    public void handleReplayGame(int decision){
        if(decision == 0){
            this.model.replayGame();
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
