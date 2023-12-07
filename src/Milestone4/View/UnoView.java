/**
 * UnoView is the main frame of the UNO game, it contains all panels
 * @Authors: Rebecca Li, Juanita Rodelo, Adham Elmahi
 */
package Milestone4.View;

import Milestone4.Model.MessageConstant;
import Milestone4.Model.CardSideModel;
import Milestone4.UnoController;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;



public class UnoView extends JFrame {
    private static final int FRAME_WIDTH = 1200;
    private static final int FRAME_HEIGHT = 800;
    private String lastMessage = "";
    Milestone4.Model.UnoModel model;
    UnoController controller;
    private GamePanel gamePanel;  // main game panel contains top card, instructions, target color and cards in hand
    private InfoPanel infoPanel;  // Info game panel contains the number of player and corresponding information

    private JMenuBar menuBar;
    private JMenuItem loadMenuItem;
    private JMenuItem saveMenuItem;

    public UnoView(){
        this.model = new Milestone4.Model.UnoModel();
        controller = new UnoController(model, this);

        this.model.setUnoView(this);
        this.initUNOView();
        initMenuBar();

    }

    /**
     * initUNOView initializes the view of the UNO game
     */
    public void initUNOView(){
        //init frame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setTitle("UNO GAME");
        this.setResizable(false);
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setLocationRelativeTo(null); // Center the frame on the screen

        // Initialize the InfoPanel which will fill the entire window before the game starts
        this.intiInfoPanel();

        // Initialize the GamePanel but do not add it to the layout yet
        this.initGamePanel();

        // Make the frame visible
        this.setVisible(true);
    }

    /**
     * initGamePanel initializes the game panel, hide the game panel as default and add it to the main frame
     */
    public void initGamePanel(){
        this.gamePanel = new GamePanel(this.controller, this.model);
        this.gamePanel.setBorder(BorderFactory.createLineBorder(Color.black, 3)); // For testing
        this.gamePanel.setVisible(false); // It's invisible until the game starts

    }

    private void initMenuBar() {
        menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        loadMenuItem = new JMenuItem("Load");
        loadMenuItem.addActionListener(e -> controller.loadGame());
        fileMenu.add(loadMenuItem);

        saveMenuItem = new JMenuItem("Save");
        saveMenuItem.addActionListener(e -> controller.promptUserToSaveGame());
        fileMenu.add(saveMenuItem);

        setJMenuBar(menuBar);
    }


    /**
     * initInfoPanel initializes the information panel and add it to the main frame
     */
    public void intiInfoPanel(){
        this.infoPanel = new InfoPanel(this.controller);
        this.add(this.infoPanel, BorderLayout.CENTER);
    }

    /**
     * startGame setups information panel and shows the game panel
     * @param players
     */
    public void startGame(ArrayList<Milestone4.Model.PlayerModel> players){
        this.getContentPane().removeAll(); // Remove all components from the frame
        this.setLayout(new BorderLayout()); // Reset the layout
        this.add(gamePanel, BorderLayout.CENTER); // Add the GamePanel to the center
        this.add(infoPanel, BorderLayout.EAST); // Move the InfoPanel to the right side
        this.gamePanel.setVisible(true); // Make the GamePanel visible
        this.infoPanel.startGame(players); // Configure the InfoPanel for the game
        this.revalidate(); // Revalidate the layout
        this.repaint(); // Repaint the frame
    }

    /**
     * updateRoundInfo calls method in infoPanel to update the round label
     * @param roundNum
     */
    public void updateRoundInfo(int roundNum){
        this.infoPanel.updateRound(roundNum);
    }

    /**
     * setBeforeEachTurn updates corresponding information and components before each turn
     * @param e
     */
    public void setBeforeEachTurn(Milestone4.Model.UnoGameEvent e){
        this.infoPanel.updateCurrPlayer(e.getCurrPlayer().getName());
        this.infoPanel.updateDirection(e.getDirection());
        this.infoPanel.updateCardSide(e.getSideString());

        if(e.getCurrPlayer().isHuman()){
            // Update target card, target color, and corresponding player's hand
            // Pass the topCardModel directly instead of calling toString() on it.
            this.gamePanel.beforeEachHumanTurn(e.getTopCard(), e.getCurrPlayer().getHand(), e.isLight(), e.getTargetColour());
        }else{
            this.gamePanel.beforeEachAITurn(e.getTopCard(), e.getCurrPlayer().getHand(), e.isLight(), e.getTargetColour());
        }

        this.updateGameMessageAndButtons(e.getMessage());  // Update instructions and button states
        this.enableUndo(false);  // disable undo
        this.enableRedo(false);  // disable redo
        // If it is wild draw two turn or wild  draw color turn and current player is human -> ask for challenge
        if ((e.getMessage().equals(Milestone4.Model.MessageConstant.wildDrawTwoTurn) ||
                e.getMessage().equals(Milestone4.Model.MessageConstant.drawColor)) && e.getCurrPlayer().isHuman()) {
            this.chanceToChallenge(e.getMessage().equals(Milestone4.Model.MessageConstant.wildDrawTwoTurn));
        }

    }

    public void setAfterPlayACard(CardSideModel.Color color, CardSideModel card, String direction, String side){
        this.gamePanel.updateColor(color);
        this.gamePanel.updateTopCard(card);
        this.infoPanel.updateDirection(direction);
        this.infoPanel.updateCardSide(side);
    }

    /**
     * updateGameMessageAndButtons updates the instructions message and buttons states
     * @param message
     */
    public void updateGameMessageAndButtons(String message){
        if (message.equals(Milestone4.Model.MessageConstant.normalTurn) ||
                (message.equals(Milestone4.Model.MessageConstant.invalidCard)) ||
                (message.equals(Milestone4.Model.MessageConstant.guiltyTwo)) ||
                (message.equals(Milestone4.Model.MessageConstant.guiltyColor))){
            this.gamePanel.setHandEnable(true);  // Enable hand panel -> player can click to play card
            this.setUpButtonsState(true, false);
        }else { // If the player cannot play a card (only draw card or do nothing)
            this.gamePanel.setHandEnable(false);  // Disable hand panel -> player cannot click to play card

            // If current player does not need to do something or the player has finished
            if (message.equals(Milestone4.Model.MessageConstant.skipTurn) || message.equals(MessageConstant.nextPlayer)
                    || message.equals(Milestone4.Model.MessageConstant.aIplayed) ||
                    message.equals(Milestone4.Model.MessageConstant.aIPickedUp) ||
                    message.equals(Milestone4.Model.MessageConstant.aIDrawOne) ||
                    message.equals(Milestone4.Model.MessageConstant.aIDrawFive) ||
                    message.equals(Milestone4.Model.MessageConstant.aISkipped) ||
                    message.equals(Milestone4.Model.MessageConstant.aIdrawColor) ||
                    message.equals(Milestone4.Model.MessageConstant.aIDrawTwo)) {
                this.setUpButtonsState(false, true);
            } else {  // else draw one or draw two or draw five or draw color, not guilty draw five, not guilty draw color
                this.setUpButtonsState(true, false);
            }
        }
        this.gamePanel.updateMessage(message);
        lastMessage = message;
    }

    /**
     * setUpButtonsState calls methods in infoPanel to disable or enable buttons
     * @param drawButton
     * @param nextButton
     */
    private void setUpButtonsState(boolean drawButton, boolean nextButton){
        this.infoPanel.setDrawOneState(drawButton);
        this.infoPanel.setNextPlayerState(nextButton);
    }

    /**
     * enableUndo calls method in infoPanel to disable or enable Undo button
     * @param isEnable
     */
    public void enableUndo(boolean isEnable){
        this.infoPanel.setUndoState(isEnable);
    }

    /**
     * enableRedo calls method in infoPanel to disable or enable Redo button
     * @param isEnable
     */
    public void enableRedo(boolean isEnable){
        this.infoPanel.setRedoState(isEnable);
    }

    /**
     * addNewCard add just drawn card to the game display
     * @param newCard
     */
    public void addNewCard(CardSideModel newCard){
        this.gamePanel.playerDrawCard(newCard);  // Add new card to the game panel
    }

    /**
     * drawACard pops up a confirm dialog to show the player the drawn card.
     * It also calls method in gamePanel to add the drawn card to player's hand, and updates instructions.
     * @param newCard
     * @param message
     */
    public void drawACard(CardSideModel newCard, String message){
        this.addNewCard(newCard);  // Add new card to the game panel
        UIManager.put("OptionPane.okButtonText", "Ok");
        JOptionPane.showMessageDialog(null,
                new JLabel("You draw: " + newCard.toString()),"Draw confirm", JOptionPane.INFORMATION_MESSAGE);
        this.updateGameMessageAndButtons(message);
    }

    /**
     * playCard calls method in gamePanel to remove the played card.
     * @param cardIndex
     */
    public void playCard(int cardIndex){
        this.gamePanel.playerRemoveCard(cardIndex);  // Remove the playedCard
    }

    /**
     * updateHandSides flip the current player's hand
     * @param isLight  new card side
     * @param hands cards in player's hand
     */
    public void updateHandSides(boolean isLight, ArrayList<Milestone4.Model.CardModel> hands){
        this.gamePanel.flipCards(isLight, hands);
    }

    /**
     * newColor pops up a dialog for player to choose the new color
     * @param choices selection
     */
    public void newColour(CardSideModel.Color[] choices){
        UIManager.put("OptionPane.cancelButtonText", "Cancel");
        UIManager.put("OptionPane.okButtonText", "Ok");
        JPanel newColor = new JPanel();
        JComboBox colorChoices = new JComboBox<CardSideModel.Color>(choices);  // Combo box used for choosing new color
        colorChoices.setEditable(false);
        newColor.add(colorChoices);
        JOptionPane.showMessageDialog(null,
                newColor,"Select New Color", JOptionPane.INFORMATION_MESSAGE);
        this.controller.newColor((CardSideModel.Color) colorChoices.getSelectedItem());

    }

    /**
     * chanceToChallenge pops up an option dialog allowing the user to challenge the wild draw two from previous player
     */
    public void chanceToChallenge(boolean isDrawTwo){
        String message;
        if (isDrawTwo){
            message = "wild draw two";
        }else {
            message = "wild draw color";
        }
        int result = JOptionPane.showOptionDialog(
                null,
                "The last player played a " + message + ", would you like to challenge them?",
                "Challenge " + message,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null, // icon
                new Object[]{"Yes", "No"}, //result = Yes = 0 or No = 1
                "default");
        this.controller.handleChallengeAccepted(result);
    }

    /**
     * updateRoundFinished calls method in infoPanel to update the winner's score.
     * It also pops up a confirm dialog to tell the current round is finish.
     * @param e
     */
    public void updateRoundFinished(Milestone4.Model.UnoFinishEvent e){
        this.infoPanel.updateScore(e.getWinnerIndex(), e.getWinner().getScore());  // Update the winner's score
        // Round finish confirm dialog
        this.finishConfirm(e);
    }

    /**
     * clearPlayerPoints() resets all the player points in the info panel
     * @param numOfPlayers
     */
    public void clearPlayerPoints(int numOfPlayers){
        for(int i = 0; i < numOfPlayers; i++){
            this.infoPanel.updateScore(i, 0);
        }
    }

    /**
     * confirmReplay() pops up a confirmation window for the user to select if they want to restart the game
     */
    public void confirmReplay(){
        int result = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to restart the game?", "Replay Confirmation",
                JOptionPane.YES_NO_OPTION);
        this.controller.handleReplayGame(result);
    }

    /**
     * updateGameFinished pops up a confirm dialog to tell the game is finished
     * @param e
     */
    public void updateGameFinished(Milestone4.Model.UnoFinishEvent e){
        // Game finish confirm dialog
        this.finishConfirm(e);
        this.dispose();
    }

    /**
     * finishConfirm pops up a confirm window to show the round or the gams has finished
     * @param e
     */
    public void finishConfirm(Milestone4.Model.UnoFinishEvent e){
        UIManager.put("OptionPane.okButtonText", "Ok");
        JOptionPane.showMessageDialog(null,
                new JLabel( e.getMessage()), e.getTitle(), JOptionPane.INFORMATION_MESSAGE);
    }
    public String getLastMessageForTest() {
        return this.lastMessage;
    }

    private void triggerLoadGame() {
        controller.loadGame();
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> new UnoView());
    }



}