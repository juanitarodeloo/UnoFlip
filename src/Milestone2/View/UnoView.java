package Milestone2.View;

import Milestone2.Model.*;
import Milestone2.UnoController;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class UnoView extends JFrame {
    // The main frame of the UNO game, it contains all panels
    private static final int FRAME_WIDTH = 1200;
    private static final int FRAME_HEIGHT = 800;
    private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    private static final int CENTER_X = Math.round(((int)SCREEN_SIZE.getWidth()) - FRAME_WIDTH) / 2;  // center x of screen
    private static final int CENTER_Y = Math.round(((int)SCREEN_SIZE.getHeight()) - FRAME_HEIGHT) / 2;  // center y of screen
    UnoModel model;
    UnoController controller;
    private GamePanel gamePanel;  // main game panel contains top card, instructions, target color and cards in hand
    private InfoPanel infoPanel;  // Info game panel contains the number of player and corresponidng inforamtion

    public UnoView(){
        this.model = new UnoModel();
        controller = new UnoController(model, this);
        this.model.setUnoView(this);
        this.initUNOView();
        this.validate();
        this.pack();
    }

    /**
     * initUNOView initializes the view of the UNO game
     */
    public void initUNOView(){
        //init frame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setTitle("UNO GAME");
        // Center the Frame
        this.setMinimumSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        this.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        this.setLocation(CENTER_X, CENTER_Y);  // Set the window location on the screen
        this.intiInfoPanel();   // Initial information panel
        this.initGamePanel();   // Initial game panel
        this.setVisible(true);
    }

    /**
     * initGamePanel initializes the game panel, hide the game panel as default and add it to the main frame
     */
    public void initGamePanel(){
        this.gamePanel = new GamePanel(this.controller);
        this.add(this.gamePanel, BorderLayout.CENTER);
        this.gamePanel.setBorder(BorderFactory.createLineBorder(Color.yellow, 3));  // for test
        this.gamePanel.setVisible(false);  // Hide game panel at beginning
    }

    /**
     * initInfoPanel initializes the information panel and add it to the main frame
     */
    public void intiInfoPanel(){
        this.infoPanel = new InfoPanel(this.controller);
        this.add(this.infoPanel, BorderLayout.EAST);
    }

    /**
     * startGame setups information panel and shows the game panel
     * @param players
     */
    public void startGame(ArrayList<PlayerModel> players){
        this.infoPanel.startGame(players);  // init player information and add game information to info panel
        this.gamePanel.setVisible(true);
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
    public void setBeforeEachTurn(UnoGameEvent e){

        this.infoPanel.updateCurrPlayer(e.getCurrPlayer().getName());
        this.infoPanel.updateDirection(e.getDirection());
        // update target card, target color, and corresponding player's hand
        this.gamePanel.beforeEachTurn(e.getTopCard().toString(), e.getTargetColour(), e.getCurrPlayer().getHand());
        this.updateGameMessageAndButtons(e.getMessage());  // Update instructions
    }

    /**
     * updateGameMessageAndButtons updates the instructions message and buttons states
     * @param message
     */
    public void updateGameMessageAndButtons(String message){
        // If player can play a card
        if (message.equals(MessageConstant.normalTurn) || (message.equals(MessageConstant.invalidCard)) ||
                (message.equals(MessageConstant.guilty))){
            this.gamePanel.setHandEnable(true);  // Enable hand panel -> player can click to play card
            this.setUpButtonsState(true, false);
        }else { // If the player cannot play a card (only draw card or do nothing)
            this.gamePanel.setHandEnable(false);  // Disable hand panel -> player cannot click to play card

            // If current player does not need to do something or the player has finished
            if (message.equals(MessageConstant.skipTurn) || message.equals(MessageConstant.nextPlayer)) {
                this.setUpButtonsState(false, true);
            } else {  // else draw one or draw two
                this.setUpButtonsState(true, false);
                if (message.equals(MessageConstant.drawTwoTurn)) {
                    boolean challengeAccepted = chanceToChallenge();
                    if (challengeAccepted) { //if user accepted challenge, interrupt regular draw two message for challenge response message
                        return;
                    }//else continue with regular wild draw two message
                }
            }
        }
        this.gamePanel.updateMessage(message);
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
     * drawACard pops up a confirm dialog to show the player the drawn card.
     * It also calls method in gamePanel to add the drawn card to player's hand, and updates instructions.
     * @param newCard
     * @param message
     */
    public void drawACard(CardModel newCard, String message){
        this.gamePanel.playerDrawCard(newCard);  // Add new card to the game panel
        UIManager.put("OptionPane.okButtonText", "Ok");
        //TODO: add actual card image
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
     * newColor pops up a dialog for player to choose the new color
     * @return  the target color
     */
    public CardModel.Color newColour(){
        UIManager.put("OptionPane.cancelButtonText", "Cancel"); //TODO: I don't think it makes sense to have an ok and cancel button
        UIManager.put("OptionPane.okButtonText", "Ok");
        JPanel newColor = new JPanel();
        CardModel.Color[] choices = {CardModel.Color.YELLOW, CardModel.Color.BLUE,
                CardModel.Color.RED, CardModel.Color.GREEN};
        JComboBox colorChoices = new JComboBox<CardModel.Color>(choices);  // Combo box used for choosing new color
        colorChoices.setEditable(false);
        newColor.add(colorChoices);
        JOptionPane.showMessageDialog(null,
                newColor,"Select New Color", JOptionPane.INFORMATION_MESSAGE);
        return (CardModel.Color) colorChoices.getSelectedItem();
    }

    /**
     * chanceToChallenge pops up an option dialog allowing the user to challenge the wild draw two from previous player
     * @return true if challenge was accepted, false otherwise
     */
    public boolean chanceToChallenge(){
        int result = JOptionPane.showOptionDialog(
                null,
                "The last player played a wild draw two, would you like to challenge them?",
                "Challenge Wild Draw Two",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null, // icon
                new Object[]{"Yes", "No"}, //result = Yes = 0 or No = 1
                "default");
        return this.controller.handleChallengeAccepted(result);
    }

    /**
     * updateRoundFinished calls method in infoPanel to update the winner's score.
     * It also pops up a confirm dialog to tell the current round is finish.
     * @param e
     */
    public void updateRoundFinished(UnoFinishEvent e){
        this.infoPanel.updateScore(e.getWinnerIndex(), e.getWinner().getScore());  // Update the winner's score
        // Round finish confirm dialog
        this.finishConfirm(e);
    }

    /**
     * updateGameFinished pops up a confirm dialog to tell the game is finished
     * @param e
     */
    public void updateGameFinished(UnoFinishEvent e){
        // Game finish confirm dialog
        this.finishConfirm(e);
        // TODO: add some function to disable panels or reset the game
    }

    /**
     * finishConfirm pops up a confirm window to show the round or the gams has finished
     * @param e
     */
    public void finishConfirm(UnoFinishEvent e){
        UIManager.put("OptionPane.okButtonText", "Ok");
        JOptionPane.showMessageDialog(null,
                new JLabel( e.getMessage()), e.getTitle(), JOptionPane.INFORMATION_MESSAGE);
    }


    public static void main(String[] args){
        new UnoView();
    }
}
