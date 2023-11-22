/**
 * GamePanel is the panel for the actual game part - the place shows top card, target color, and player's hand
 * @Authors: Rebecca Li, Juanita Rodelo, Adham Elmahi
 */
package Milestone3.View;

import Milestone3.Model.CardModel;
import Milestone3.Model.CardSideModel;
import Milestone3.Model.UnoModel;
import Milestone3.UnoController;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class GamePanel extends JPanel {
    private JLabel topCard;
    private JLabel targetColour;
    private JLabel errorMessage;
    private PlayerHandPanel currentHand;
//    private UnoModel unomodel;

    public GamePanel(UnoController controller, UnoModel model){
//        this.unomodel = model;
        this.setLayout(new BorderLayout());
        this.topCard = new JLabel();
        this.targetColour = new JLabel();
        this.errorMessage = new JLabel();
        this.currentHand = new PlayerHandPanel(controller);
        JPanel gameCenter = new JPanel();
        gameCenter.setBackground(new Color(238, 132, 132)); // A more vibrant color
        gameCenter.setLayout(new GridLayout(3, 1));
        gameCenter.add(this.topCard, 0);
        gameCenter.add(this.targetColour, 1);
        gameCenter.add(this.errorMessage, 2);
        this.add(gameCenter, BorderLayout.CENTER);
        this.currentHand.setBackground(new Color(238, 132, 132)); // A more vibrant color
        this.add(this.currentHand, BorderLayout.SOUTH);
    }

    /**
     * updateColor updates the information about target color
     * @param targetColour  new target color
     */
    public void updateColor(CardSideModel.Color targetColour){
        if(!this.targetColour.toString().equals(targetColour.toString())){  // If target color has changed
            this.targetColour.setText(targetColour.toString());
        }
    }

    /**
     * updateTopCard updates the top card information
     * @param topCard  new played (top) card
     */
    public void updateTopCard(CardSideModel topCard){
        if (!this.topCard.toString().equals(topCard.toString())){  // If top card has changed
            // Set the top card image
            ImageIcon topCardIcon = currentHand.getCardImageIcon(
                    topCard.getColor().toString().toLowerCase(),
                    topCard.getType().toString().toLowerCase()
            );
//            Image image = topCardIcon.getImage().getScaledInstance(100, 150, Image.SCALE_SMOOTH);
//            if (image != null) {
//                this.topCard.setIcon(new ImageIcon(image));
//            }
            this.topCard.setText(topCard.toString()); // Clear any text
        }
    }

    /**
     * beforeEachTurn method updates the displayed top card, target colour for the current player
     * @param topCardModel
     * @param cards
     */
    public void beforeEachHumanTurn(CardSideModel topCardModel, ArrayList<CardModel> cards, boolean isLight, CardSideModel.Color targetColor) {
        this.updateTopCard(topCardModel);  // Update top card

        // Check if the top card is a wild card and if the target color has been chosen
        if (topCardModel.getType() == CardSideModel.Type.WILD ||
                topCardModel.getType() == CardSideModel.Type.WILD_DRAW_TWO ||
                topCardModel.getType() == CardSideModel.Type.WILD_DRAW_COLOR) {

            // If targetColor has been set, use it. Otherwise, display "Choose a color".
            String colorText = targetColor != CardSideModel.Color.NONE ? targetColor.toString() : "Choose a color"; //TODO: change message if AI player played the wild
            this.targetColour.setText(colorText);
        } else {
            // For non-wild cards, just use the card's color
            this.targetColour.setText(topCardModel.getColor().toString());
        }

        // Reset the panel to display the current player's hand
        this.currentHand.resetCards(cards, isLight);
        this.revalidate();
        this.repaint();
    }

    public void beforeEachAITurn(CardSideModel topCardModel, ArrayList<CardModel> cards, boolean isLight, CardSideModel.Color targetColor) {
        this.updateTopCard(topCardModel);  // Update top card

        // Check if the top card is a wild card and if the target color has been chosen
        if (topCardModel.getType() == CardSideModel.Type.WILD ||
                topCardModel.getType() == CardSideModel.Type.WILD_DRAW_TWO ||
                topCardModel.getType() == CardSideModel.Type.WILD_DRAW_COLOR) {

            // If targetColor has been set, use it. Otherwise, display "Choose a color".
            String colorText = targetColor != CardSideModel.Color.NONE ? targetColor.toString() : "AI player chose a color!";
            this.targetColour.setText(colorText);
        } else {
            // For non-wild cards, just use the card's color
            this.targetColour.setText(topCardModel.getColor().toString());
        }

        // Reset the panel to display the current player's hand
        this.currentHand.resetCards(cards, isLight);
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
    public void playerDrawCard(CardSideModel newCard){
        this.currentHand.addNewCard(newCard);
    }

    /**
     * playerRemoveCard calls the hand panel to remove the specific card
     * @param cardIndex
     */
    public void playerRemoveCard(int cardIndex){

        this.currentHand.removeCard(cardIndex);
    }

    /**
     * flipCards flip the current player's hand
     * @param isLight  new card side
     * @param hands cards in player's hand
     */
    public void flipCards(boolean isLight, ArrayList<Milestone3.Model.CardModel> hands){
        this.currentHand.resetCards(hands, isLight);
    }
}
