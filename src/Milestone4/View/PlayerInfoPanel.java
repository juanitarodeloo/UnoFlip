/**
 * PlayerInfoPanel contains a label displays the player's name and a label display the player's score
 * @Authors: Rebecca Li
 */
package Milestone4.View;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;


public class PlayerInfoPanel extends JPanel{
    private JLabel playerName = new JLabel();
    private JLabel score = new JLabel();

    public PlayerInfoPanel(String name){
        this.setLayout(new GridLayout(2,1));
        this.playerName.setText("Player: "  + name);
        this.updateScore(0);  // All players have no score at beginning
        this.add(this.playerName);
        this.add(this.score);
    }

    /**
     * updateScore updates the displayed text of the score label
     * @param newScore
     */
    public void updateScore(int newScore){
        this.score.setText("Score: " + newScore);
    }
}
