/**
 * UnoController is the controller component that connects the view and model. It holds the actions that happen when a
 * user interacts with the view. It processes all the logic and incoming requests.
 * @Authors: Rebecca Li, Juanita Rodelo
 */
package Milestone4;

import Milestone4.Model.CardModel;
import Milestone4.Model.CardSideModel;
import Milestone4.Model.PlayerModel;
import Milestone4.Model.UnoModel;
import Milestone4.View.UnoView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class UnoController implements ActionListener{
    private UnoModel model;
    private int numOfPlayers;

    private UnoView view;

    public UnoController(UnoModel model, UnoView view){
        this.model = model;
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //save selected item
        Object source = e.getSource();
        if(source instanceof JComboBox){
            JComboBox<Integer> jComboBox = (JComboBox<Integer>) source;
            numOfPlayers = (Integer) jComboBox.getSelectedItem();
            if(e.getActionCommand().equals("human")){
                model.saveNumOfHumanPlayers(numOfPlayers);
            }else if(e.getActionCommand().equals("AI")){
                model.saveNumOfAIPlayers(numOfPlayers);
            }
        }else if(source instanceof JButton){
            switch (e.getActionCommand()){
                case "Start":  // If click to start the game.
                    this.model.initGame();
                    break;
                case "Draw One":  // If the player click the button to draw a card
                    this.model.playerAction(-1);
                    break;
                case "Next Player":  // If the player clicks Next Player button
                    this.model.nextPlayer();
                    break;
                case "Replay":
                    System.out.println("replay was clicked");
                    this.view.confirmReplay();
                    break;
                case "Undo":  // If the player clicks Undo
                    System.out.println("click undo");
                    this.model.playerUndo();
                    break;
                case "Redo":  // If the player clicks Undo
                    System.out.println("click redo");
                    this.model.playerRedo();
                    break;
                default:  // If the player click a card button to draw a card
                    this.model.playerAction(Integer.parseInt(e.getActionCommand()));
            }

        }

    }

    /**
     * handleChallengeAccepted interrupts the users decision from the JOptionPane from the view
     * @param decision
     */
    public void handleChallengeAccepted(int decision){
        if(decision == 0){
            this.model.challengeAccepted();
        }
    }

    public void handleReplayGame(int decision){
        if(decision == 0){
            this.model.replayGame();
        }
    }

    /**
     * newColor interrupts the users selected color from the view
     * @param newColor
     */
    public void newColor(CardSideModel.Color newColor){
        this.model.setTargetColor(newColor);
    }

    public void saveGame(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<UnoGame>\n");

            writer.write("\t<ListOfPlayers>\n");
            for (PlayerModel player : model.getPlayers()) {
                writer.write("\t\t<Player>\n");
                writer.write("\t\t\t<name>" + escapeXml(player.getName()) + "</name>\n");
                writer.write("\t\t\t<Hand>\n");
                for (CardModel card : player.getHand()) {
                    writer.write("\t\t\t\t<Card>\n");
                    writer.write("\t\t\t\t\t<lightside>\n");
                    writer.write("\t\t\t\t\t\t<Color>" + card.getLightSide().getColor() + "</Color>\n");
                    writer.write("\t\t\t\t\t\t<Type>" + card.getLightSide().getType() + "</Type>\n");
                    writer.write("\t\t\t\t\t</lightside>\n");
                    writer.write("\t\t\t\t\t<darkside>\n");
                    writer.write("\t\t\t\t\t\t<Color>" + card.getDarkSide().getColor() + "</Color>\n");
                    writer.write("\t\t\t\t\t\t<Type>" + card.getDarkSide().getType() + "</Type>\n");
                    writer.write("\t\t\t\t\t</darkside>\n");
                    writer.write("\t\t\t\t</Card>\n");
                }
                writer.write("\t\t\t</Hand>\n");
                writer.write("\t\t\t<Score>" + player.getScore() + "</Score>\n");
                writer.write("\t\t\t<isHuman>" + player.isHuman() + "</isHuman>\n");
                writer.write("\t\t</Player>\n");
            }
            writer.write("\t</ListOfPlayers>\n");

            writer.write("\t<Deck>\n");
            writer.write("\t\t<listOfCards>\n");
            for (CardModel card : model.getMyDeck().getCards()) {
                writer.write("\t\t\t<Card>\n");
                writer.write("\t\t\t\t<lightside>\n");
                writer.write("\t\t\t\t\t<Color>" + card.getLightSide().getColor() + "</Color>\n");
                writer.write("\t\t\t\t\t<Type>" + card.getLightSide().getType() + "</Type>\n");
                writer.write("\t\t\t\t</lightside>\n");
                writer.write("\t\t\t\t<darkside>\n");
                writer.write("\t\t\t\t\t<Color>" + card.getDarkSide().getColor() + "</Color>\n");
                writer.write("\t\t\t\t\t<Type>" + card.getDarkSide().getType() + "</Type>\n");
                writer.write("\t\t\t\t</darkside>\n");
                writer.write("\t\t\t</Card>\n");
            }
            writer.write("\t\t</listOfCards>\n");
            writer.write("\t</Deck>\n");

            writer.write("\t<currentPlayer>" + model.getCurrentPlayer().getName() + "</currentPlayer>\n");
            writer.write("\t<roundNum>" + model.getRoundNum() + "</roundNum>\n");
            writer.write("\t<isLight>" + model.isLight() + "</isLight>\n");
            writer.write("\t<isClockWise>" + model.getIsClockWise() + "</isClockWise>\n");
            writer.write("\t<targetColor>" + model.getTargetColor() + "</targetColor>\n");
            writer.write("\t<prevColor>" + model.getPrevColor() + "</prevColor>\n");

            writer.write("\t<TopCard>\n");
            writer.write("\t\t<lightside>\n");
            writer.write("\t\t\t<Color>" + model.getTopCard().getLightSide().getColor() + "</Color>\n");
            writer.write("\t\t\t<Type>" + model.getTopCard().getLightSide().getType() + "</Type>\n");
            writer.write("\t\t</lightside>\n");
            writer.write("\t\t<darkside>\n");
            writer.write("\t\t\t<Color>" + model.getTopCard().getDarkSide().getColor() + "</Color>\n");
            writer.write("\t\t\t<Type>" + model.getTopCard().getDarkSide().getType() + "</Type>\n");
            writer.write("\t\t</darkside>\n");
            writer.write("\t</TopCard>\n");

            writer.write("\t<drawUntilColor>" + model.isDrawUntilColor() + "</drawUntilColor>\n");
            writer.write("\t<numSkip>" + model.getNumSkip() + "</numSkip>\n");
            writer.write("\t<isLight>" + model.isLight() + "</isLight>\n");
            writer.write("\t<isClockWise>" + model.getIsClockWise() + "</isClockWise>\n");
            writer.write("\t<currentPlayer>" + model.getCurrentPlayer().getName() + "</currentPlayer>\n");
            writer.write("\t<roundNum>" + model.getRoundNum() + "</roundNum>\n");
            writer.write("\t<needToDraw>" + model.getNeedToDraw() + "</needToDraw>\n");
            writer.write("\t<nextMessage>" + escapeXml(model.getNextMessage()) + "</nextMessage>\n");

            // Assuming getRoundWinner() returns a PlayerModel. If it's just an index or name, adjust accordingly.
            String roundWinnerName = model.getRoundWinner() != null ? model.getRoundWinner().getName() : "None";
            writer.write("\t<roundWinner>" + escapeXml(roundWinnerName) + "</roundWinner>\n");

            // Assuming this is a boolean or similar simple type.
            writer.write("\t<valid_wild_draw_two_or_color>" + model.isValidWildDrawTwoOrColor() + "</valid_wild_draw_two_or_color>\n");

            writer.write("</UnoGame>");
            writer.close(); // Make sure to close the writer to flush everything to the file

        } catch (IOException e) {
            e.printStackTrace();
            // Handle exceptions
        }
    }

    private String escapeXml(String input) {
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }

}