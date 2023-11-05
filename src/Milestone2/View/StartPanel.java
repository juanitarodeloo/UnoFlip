package Milestone2.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

// Previous design! Not inuse!!!

public class StartPanel extends JPanel {
    // This is the start panel which is used to set the number of players in the game.
    private ArrayList<JLabel> nameLabels;
    private ArrayList<JTextField> inputNames;
    private JComboBox<Integer> playerNums;
    private JButton startButton;
    private int lastAddedIndex = 0;  // Recording last added item index in the grid layout
    private ActionListener controller;

    public StartPanel(ActionListener controller){
        this.controller = controller;
        this.setPreferredSize(new Dimension(500, 500));
        this.setLayout(new GridLayout(13, 1));
        this.nameLabels = new ArrayList<JLabel>();
        this.inputNames = new ArrayList<JTextField>();
        this.add(new JLabel("Welcome to Uno!"), 0);
        this.add(new JLabel("Please select the number of players and enter their name!"), 1);
        this.add(new JLabel("No more than 10 letters in each name!"), 2);
        // Initialize the combo box used for selecting number of players.
        Integer[] choices = {2, 3, 4};
        this.playerNums  = new JComboBox<Integer>(choices);
        this.playerNums.setEditable(false);
        this.playerNums.addActionListener(controller);
        this.add(playerNums, 3);
        this.startButton = new JButton("Start");
        this.startButton.addActionListener(this.controller);
        this.add(startButton, 4);
        this.lastAddedIndex = 4;
        this.updateInputs(2);  // Set default player to 2
    }

    public void updateInputs(int numberOfPlayers){
        // If existed name input fields less than we need -> need to add more

        int existPlayer = this.inputNames.size();  // number of player that exists before update
        if (existPlayer < numberOfPlayers){
            // For the number of new name input fields need to be added
            for (int i = 0; i < numberOfPlayers - existPlayer; i++){
                String playerNameLabelText = "Player " + (1 + this.inputNames.size()) + " Name: ";
                JLabel playerNameLabel = new JLabel(playerNameLabelText, JLabel.TRAILING);
                this.add(playerNameLabel, this.lastAddedIndex);
                JTextField playerNameField = new JTextField(); //TODO: make textfield smaller
                playerNameField.addActionListener(this.controller);
                playerNameLabel.setLabelFor(playerNameField);
                this.add(playerNameField, this.lastAddedIndex + 1);
                this.lastAddedIndex += 2;
                this.inputNames.add(playerNameField);
                System.out.println(i);
            }
        }else{  // If existed name input more than we need -> remove extra
            for (int i = 0; i < existPlayer - numberOfPlayers; i++){
                this.remove(this.lastAddedIndex - 1);  // remove last JTextField
                this.remove(this.lastAddedIndex - 2);  // remove last label
                this.lastAddedIndex -= 2;
                this.inputNames.remove(this.inputNames.size() - 1);
            }
        }
        this.validate();
    }
}
