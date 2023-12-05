/**
 * UnoController is the controller component that connects the view and model. It holds the actions that happen when a
 * user interacts with the view. It processes all the logic and incoming requests.
 * @Authors: Rebecca Li, Juanita Rodelo
 */
package Milestone4;

import Milestone4.Model.CardModel;
import Milestone4.Model.CardSideModel;
import Milestone4.Model.PlayerModel;
import Milestone4.Model.UnoModel;
import Milestone4.View.UnoView;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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





    // This method will be called when the user wants to save the game from the UI.
    public void promptUserToSaveGame() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Game State");
        fileChooser.setFileFilter(new FileNameExtensionFilter("XML Files", "xml"));

        int userSelection = fileChooser.showSaveDialog(null); // 'null' can be replaced with the view if needed

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".xml")) {
                fileToSave = new File(filePath + ".xml");
            }
            model.saveGame(fileToSave); // Call the model to save the game
        }
    }

    public void loadGame() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Load Game State");
        fileChooser.setFileFilter(new FileNameExtensionFilter("XML Files", "xml"));

        int userSelection = fileChooser.showOpenDialog(view);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToLoad = fileChooser.getSelectedFile();
            String filePath = fileToLoad.getAbsolutePath();

            // Check if the file exists and is readable
            if (fileToLoad.exists() && fileToLoad.isFile() && fileToLoad.canRead()) {
                model.loadGame(filePath); // Call the modified loadGame method in UnoModel
            } else {
                JOptionPane.showMessageDialog(view, "The file does not exist or cannot be read.", "File Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }





}