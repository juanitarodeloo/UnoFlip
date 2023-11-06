package Milestone2.View;

import Milestone2.Model.CardModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class PlayerHandPanel extends JScrollPane{
    // PlayerHandPanel contains all cards in a player's hand.

    private ActionListener buttonController;
    private ArrayList<JButton> cards;
    private JPanel handPanel;

    public PlayerHandPanel(ActionListener buttonController){ //TODO: this panel should be bigger
        this.buttonController = buttonController;
        this.cards = new ArrayList<JButton>();
        this.handPanel = new JPanel(new FlowLayout());
        this.setBorder(BorderFactory.createLineBorder(Color.black, 3));  // for test
        this.setViewportView(this.handPanel);
    }

    /**
     * addNewCard creates new card button depend on the actual card and adds it to the panel.
     * @param card
     */
    public void addNewCard(CardModel card){
        JButton cardButton = new JButton(card.toString());
        cardButton.setSize(100, 100);
        cardButton.addActionListener(this.buttonController);
        this.cards.add(cardButton);  // Add buttons to the button list -> easy to find later
        // Set the action command of this button to the index of it in the list
        cardButton.setActionCommand(Integer.toString(this.cards.indexOf(cardButton)));
        this.handPanel.add(cardButton);
    }

    /**
     * removeCard removes specific card button from the panel depend on its index
     * @param removedIndex
     */
    public void removeCard(int removedIndex){
        this.cards.remove(removedIndex);
        this.handPanel.remove(removedIndex);
        this.handPanel.revalidate();
        this.handPanel.repaint();
        for (JButton cardButton: this.cards){  // Reset action command for each button
            cardButton.setActionCommand(Integer.toString(this.cards.indexOf(cardButton)));
        }
    }

    /**
     * resetCards removes all card buttons in the panel and adds new buttons
     * @param cards
     */
    public void resetCards(ArrayList<CardModel> cards){
        if (this.cards.size() != 0) {
            this.cards.removeAll(this.cards);  // remove all item in the list
            this.handPanel.removeAll();  // remove all items in this menu
            this.handPanel.revalidate();
            this.handPanel.repaint();
        }
        for (CardModel card: cards){
            this.addNewCard(card);
        }
    }

    /**
     * enableCards disables or enables all card button in the panel
     * @param isEnable
     */
    public void enableCards(boolean isEnable){
        // Enable or Disable all card buttons
        for (JButton card: this.cards){
            card.setEnabled(isEnable);
        }
    }

}
