package Milestone2.View;

import Milestone2.Model.CardModel;

import java.io.IOException;
import java.net.URL;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import java.io.File;

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

        ImageIcon cardIcon = loadCardImageIcon(card.getColor().toString().toLowerCase(), card.getType().toString().toLowerCase());
        JButton cardButton = new JButton(); // Declare cardButton once
        if (cardIcon != null) {
            cardButton.setIcon(cardIcon); // Set the icon if image loaded successfully
        } else {
            cardButton.setText(card.toString()); // Fallback to text if the image fails to load
        }

        cardButton.addActionListener(this.buttonController);
        cardButton.setActionCommand(Integer.toString(this.cards.size()));
        this.cards.add(cardButton);
        this.handPanel.add(cardButton);
        this.handPanel.revalidate();
        this.handPanel.repaint();
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
    private ImageIcon loadCardImageIcon(String color, String value) {
        String fileName; // Declare the fileName variable at the beginning of the method.

        // The leading slash is important, it tells Java to look from the root of the classpath.
        if ("WILD".equals(value) || "WILD_DRAW_TWO".equals(value)) {
            // For wild cards, the file name does not include a color
            fileName = "/" + value + ".png";
        } else {
            // For non-wild cards, include the color and type in the file name
            fileName = "/" + color + "_" + value + ".png";
        }

        URL resource = getClass().getResource(fileName);
        if (resource == null) {
            System.err.println("Resource not found: " + fileName);
            return null;
        } else {
            ImageIcon icon = new ImageIcon(resource);
            // Resize the icon to a fixed size, for example, 100x150 pixels
            Image image = icon.getImage().getScaledInstance(100, 150, Image.SCALE_SMOOTH);
            return new ImageIcon(image);
        }
    }
    public ImageIcon getCardImageIcon(String color, String value) {
        return loadCardImageIcon(color, value);
    }

}
