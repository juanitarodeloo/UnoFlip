package Milestone2.View;

import Milestone2.Model.CardModel;
import Milestone2.UnoController;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GamePanel extends JPanel {
    // This class is the panel for the actual game part - the place shows top card, target color, and player's hand

    private JLabel topCard;  //TODO: change to actual card
    private JLabel targetColour;
    private JLabel errorMessage;
    private PlayerHandPanel currentHand;

    public GamePanel(UnoController controller){
        this.setLayout(new BorderLayout());
        this.topCard = new JLabel();
        this.targetColour = new JLabel();
        this.errorMessage = new JLabel();
        this.currentHand = new PlayerHandPanel(controller);
        JPanel gameCenter = new JPanel();
        gameCenter.setBorder(BorderFactory.createLineBorder(Color.black, 3));  // for test
        gameCenter.setLayout(new GridLayout(3, 1));
        gameCenter.add(this.topCard, 0);
        gameCenter.add(this.targetColour, 1);
        gameCenter.add(this.errorMessage, 2);
        this.add(gameCenter, BorderLayout.CENTER);
        this.add(this.currentHand, BorderLayout.SOUTH);
    }

    /**
     * beforeEachTurn method updates the displayed top card, target colour for the current player
     * @param topCard
     * @param targetColour
     * @param cards
     */
    public void beforeEachTurn(String topCard, CardModel.Color targetColour, ArrayList<CardModel> cards){
        if (!this.topCard.toString().equals(topCard)){  // If top card has changed
            this.topCard.setText(topCard);
        }
        if(!this.targetColour.toString().equals(targetColour.toString())){  // If target color has changed
            this.targetColour.setText(targetColour.toString());
        }
        this.currentHand.resetCards(cards);  // Reset the panel to display current player's cards
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
     * plaerRemoveCard calls the hand panel to remove the specific card
     * @param cardIndex
     */
    public void playerRemoveCard(int cardIndex){
        this.currentHand.removeCard(cardIndex);
    }
}
