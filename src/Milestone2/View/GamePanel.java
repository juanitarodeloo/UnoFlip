package Milestone2.View;

import Milestone2.Model.CardModel;
import Milestone2.Model.UnoModel;
import Milestone2.UnoController;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import Milestone2.Model.UnoModel;


public class GamePanel extends JPanel {
    // This class is the panel for the actual game part - the place shows top card, target color, and player's hand

    private JLabel topCard;  //TODO: change to actual card
    private JLabel targetColour;
    private JLabel errorMessage;
    private PlayerHandPanel currentHand;
    private UnoModel unomodel;
    private UnoController controller;

    public GamePanel(UnoController controller, UnoModel model){
        this.unomodel = model;
        this.setLayout(new BorderLayout());

        this.topCard = new JLabel(); //TODO: label it top card
        //   this.topCard.setBorder(new LineBorder(Color.BLACK)); //for test
        this.targetColour = new JLabel(); //TODO: label this target colour
        //  this.targetColour.setBorder(new LineBorder(Color.BLACK)); //for test
        this.errorMessage = new JLabel();
        //  this.errorMessage.setBorder(new LineBorder(Color.BLACK)); //for test
        this.currentHand = new PlayerHandPanel(controller);
        JPanel gameCenter = new JPanel();
        //  gameCenter.setBackground(Color.RED);
        gameCenter.setBackground(new Color(238, 132, 132)); // A more vibrant color
// Set the background color to red
        //   gameCenter.setBorder(BorderFactory.createLineBorder(Color.black, 3));
        gameCenter.setLayout(new GridLayout(3, 1));
        gameCenter.add(this.topCard, 0);
        gameCenter.add(this.targetColour, 1);
        gameCenter.add(this.errorMessage, 2);
        this.add(gameCenter, BorderLayout.CENTER);
        // this.currentHand.setBackground(Color.RED);
        this.currentHand.setBackground(new Color(238, 132, 132)); // A more vibrant color


        this.add(this.currentHand, BorderLayout.SOUTH);
        this.controller = controller;
    }

    /**
     * updateColor updates the information about target color
     * @param targetColour  new target color
     */
    public void updateColor(CardModel.Color targetColour){
        if(!this.targetColour.toString().equals(targetColour.toString())){  // If target color has changed
            this.targetColour.setText(targetColour.toString());
        }
    }

    /**
     * updateTopCard updates the top card information
     * @param topCard  new played (top) card
     */
    public void updateTopCard(String topCard){
        if (!this.topCard.toString().equals(topCard)){  // If top card has changed
            this.topCard.setText(topCard);
        }
    }

    /**
     * beforeEachTurn method updates the displayed top card, target colour for the current player

     * @param topCardModel
     * @param cards
     */
    public void beforeEachTurn(CardModel topCardModel, ArrayList<CardModel> cards) {
        // Set the top card image
        ImageIcon topCardIcon = currentHand.getCardImageIcon(
                topCardModel.getColor().toString().toLowerCase(),
                topCardModel.getType().toString().toLowerCase()
        );
        Image image = topCardIcon.getImage().getScaledInstance(100, 150, Image.SCALE_SMOOTH);
        this.topCard.setIcon(new ImageIcon(image));
        this.topCard.setText(""); // Clear any text

        // Check if the top card is a wild card and if the target color has been chosen
        if (topCardModel.getType() == CardModel.Type.WILD || topCardModel.getType() == CardModel.Type.WILD_DRAW_TWO) {
            // If targetColor has been set, use it. Otherwise, display "Choose a color".
            String colorText = unomodel.getTargetColor() != CardModel.Color.NONE ? unomodel.getTargetColor().toString() : "Choose a color";
            this.targetColour.setText(colorText);
        } else {
            // For non-wild cards, just use the card's color
            this.targetColour.setText(topCardModel.getColor().toString());
        }

        // Reset the panel to display the current player's hand
        this.currentHand.resetCards(cards);
        this.revalidate();
        this.repaint();
    }

    /**
     * updateMessage method updates the instructions for the current player
     * @param message  instructions need to display
     */
    public void updateMessage(String message){
        if (!this.errorMessage.toString().equals(message)){
            this.errorMessage.setText(message);
        }
    }

    /**
     * setHandEnable calls the hand panel to enable or disable all displayed card buttons.
     * @param isEnable
     */
    public void setHandEnable(boolean isEnable){
        this.currentHand.enableCards(isEnable);
    }

    /**
     * playerDrawCard calls the hand panel to add new card button
     * @param newCard
     */
    public void playerDrawCard(CardModel newCard){
        this.currentHand.addNewCard(newCard);
    }

    /**
     * playerRemoveCard calls the hand panel to remove the specific card
     * @param cardIndex
     */
    public void playerRemoveCard(int cardIndex){

        this.currentHand.removeCard(cardIndex);
    }
}
