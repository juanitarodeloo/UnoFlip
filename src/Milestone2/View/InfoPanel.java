package Milestone2.View;

import Milestone1.Player;
import Milestone2.Model.DeckModel;
import Milestone2.Model.PlayerModel;
import Milestone2.UnoController;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class InfoPanel extends JPanel{
    // This IndoPanel contains three part in the info panel -> start part, player info part and control game part

    private ArrayList<PlayerInfoPanel> playerInfoPanels;
    private JComboBox<Integer> playerNums;
    private JButton startButton = new JButton("Start");
    private JLabel roundLabel = new JLabel();
    private JLabel cardCount = new JLabel();
    private JLabel direction = new JLabel();
    private JLabel currentPlayer = new JLabel();
    private JButton drawOne = new JButton("Draw One");
    private JButton nextPlayer = new JButton("Next Player");
    private UnoController controller;


    public InfoPanel(UnoController controller){
        this.controller = controller;
        this.playerInfoPanels = new ArrayList<PlayerInfoPanel>();
        this.setLayout(new GridLayout(3, 1));
        this.setBorder(BorderFactory.createLineBorder(Color.black, 3));  // for test
        this.initStart();
    }

    /**
     * initStart creates panel for start part which contains the combobox for choosing the number of players
     */
    public void initStart(){
        JPanel startPanel = new JPanel();
        startPanel.setLayout(new GridLayout(5, 1));
        startPanel.add(new JLabel("Welcome to Uno!"), 0);
        startPanel.add(new JLabel("Please select the number of players!"), 1);
        startPanel.add(new JLabel("Click start to start the game!"), 2);
        // Initialize the combo box used for selecting number of players.
        Integer[] choices = {2, 3, 4};
        this.playerNums  = new JComboBox<Integer>(choices);
        this.playerNums.setEditable(false);
        this.playerNums.addActionListener(controller);
        startPanel.add(playerNums, 3);
        this.startButton.addActionListener(this.controller);
        startPanel.add(this.startButton, 4);
        this.add(startPanel, GridBagConstraints.NORTH, 0);
    }

    /**
     * initPlayerInfo creates a panel that contains the PlayerInfoPanel.
     * The number of PlayerInfoPanel is the same as the number of players
     * @param players
     */
    public void initPlayerInfo(ArrayList<PlayerModel> players){
        JPanel playerInfo = new JPanel();
        playerInfo.setLayout(new GridLayout(players.size() - 1, 1));
        for (PlayerModel player: players){  // Create player info panel for each player
            PlayerInfoPanel currentPlayer = new PlayerInfoPanel(player.getName());
            playerInfo.add(currentPlayer, players.indexOf(player));
            this.playerInfoPanels.add(currentPlayer);
        }
        this.add(playerInfo, 1);
    }

    /**
     * initGameInfo creates a panel that contains the game information like round number, turns order,
     * and buttons for drawing a card and pass the game to the next player
     */
    public void initGameInfo(){
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(6, 1));
        // set up components
        gamePanel.add(this.roundLabel, 0);
        gamePanel.add(this.direction, 1);
        gamePanel.add(this.cardCount, 2);
        gamePanel.add(this.currentPlayer, 3);
        this.drawOne.addActionListener(this.controller);
        gamePanel.add(this.drawOne, 4);
        this.nextPlayer.addActionListener(this.controller);
        gamePanel.add(this.nextPlayer, 5);
        this.add(gamePanel, 2);
    }

    /**
     * startGame is called after the player click start button,
     * It disables the start button and the combo box used for choosing the number of players
     * @param players
     */
    public void startGame(ArrayList<PlayerModel> players){
        this.startButton.setEnabled(false);  // Disable start button
        this.playerNums.setEnabled(false);  // Disable combo box used for selecting number of players
        this.initPlayerInfo(players);
        this.initGameInfo();
    }

    /**
     * updateRound updates the displayed round label text
     * @param newRound
     */
    public void updateRound(int newRound){
        this.roundLabel.setText("Round " + newRound +" --------");
    }

    /**
     * updateDirection updates the displayed direction label text
     * @param direction
     */
    public void updateDirection(String direction){
        this.direction.setText("Game order: " + direction);
    }

    /**
     * updateCurrPlayer updates the displayed current player label text
     * @param player
     */
    public void updateCurrPlayer(String player){
        this.currentPlayer.setText("Current player: " + player);
    }

    /**
     * setDrawOneState disables or enables the Draw One button
     * @param isEnable
     */
    public void setDrawOneState(boolean isEnable){
        this.drawOne.setEnabled(isEnable);
    }

    /**
     * setNextPlayerState disables or enables the Next Player button
     * @param isEnable
     */
    public void setNextPlayerState(boolean isEnable){
        this.nextPlayer.setEnabled(isEnable);
    }

    /**
     * updateScore calls the method to update the displayed score in specific playerInfoPanel
     * @param playerIndex
     * @param newScore
     */
    public void updateScore(int playerIndex, int newScore){
        this.playerInfoPanels.get(playerIndex).updateScore(newScore);
    }
}
