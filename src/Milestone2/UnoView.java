package Milestone2;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class UnoView extends JFrame {

    UnoModel model;
    UnoController controller;
    private JPanel playersInitPanel;
    private JComboBox<Integer> playerNums;
    //TODO: have a list of playerPanels that hold their card panels so that dynamically changes as the game is played

    public UnoView(UnoModel model){
        this.model = model;
        controller = new UnoController(model);

        //init frame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setTitle("UNO GAME");
        JLabel welcomeLabel = new JLabel("Welcome to our UNO Game!");
        this.add(welcomeLabel, BorderLayout.PAGE_START); //TODO: center this

        //create frame components
        playersInitPanel = new JPanel(new FlowLayout());
        JLabel playersInitLabel = new JLabel("Enter the number of players:");
        Integer[] choices = {2, 3, 4};
        playerNums  = new JComboBox<Integer>(choices);
        playerNums.setEditable(false);
        playerNums.addActionListener(controller);


        //add sub-components to components
        playersInitPanel.add(playersInitLabel);
        playersInitPanel.add(playerNums);
        this.add(playersInitPanel, BorderLayout.CENTER);

        //last steps
        this.pack();
        this.setSize(600,300);
        //TODO: make frame open in the center of the screen
        //TODO: make text on labels bigger and nicer looking
        this.setVisible(true);

    }

    public void initPlayers(int numOfPlayers){
        System.out.println("in initPlayers in model"); //for testing
        playerNums.setSelectedItem(numOfPlayers); //sets the combo box label to display what the user selects
        List<JTextField> playerNames = new ArrayList<>();

        //create player name prompt panel
        JPanel playerNamePrompts = new JPanel();
        playerNamePrompts.setLayout(new BoxLayout(playerNamePrompts, BoxLayout.Y_AXIS));
        JButton saveButton = new JButton("Save");

        //add text field to each
        for(int i = 0; i < numOfPlayers; i++){
            String playerNameLabelText = "Player " + (i+1) + " Name: ";
            JLabel playerNameLabel = new JLabel(playerNameLabelText, JLabel.TRAILING);
            playerNamePrompts.add(playerNameLabel);
            JTextField playerNameField = new JTextField(); //TODO: make textfield smaller
            playerNameLabel.setLabelFor(playerNameField);
            playerNamePrompts.add(playerNameField);
            playerNames.add(playerNameField);
        }
        playerNamePrompts.add(saveButton);
        controller.savePlayerNames(playerNames);
        saveButton.addActionListener(controller);


        //add components to frame: //TODO: stop from creating a new window
        this.add(playerNamePrompts, BorderLayout.SOUTH);
        this.revalidate();
        this.setVisible(true);
    }

    public void gameView(String[] playerNames, int numOfPlayers){
        System.out.println("in game view"); //for testing

        //System.out.println("numOfPlayers: " + numOfPlayers + "- in view");

        //remove everything from player init frame
        this.getContentPane().removeAll(); //TODO: figure out way to close old window

        //init new frame:
        this.setLayout(new BorderLayout(5, 5));
        this.setTitle("UNO GAME");

        //frame is split into 2 panels:
        JPanel infoPanel = new JPanel(new BorderLayout()); //the right one that displays player info
        JPanel gamePanel = new JPanel(new BorderLayout()); //the left one that displays the game //TODO: center this in the screen
        JPanel gameCenterPanel = new JPanel(); //the one in the middle of the left one that shows target card and discard pile

        gameCenterPanel.add(new JLabel("TARGET CARD"));
        gameCenterPanel.add(new JLabel("DISCARD PILE"));
        infoPanel.add(new JLabel("Player Info Panel"), BorderLayout.NORTH);
        //gamePanel.add(new JLabel("Game Play Panel"), BorderLayout.NORTH); //TODO: temp

        this.add(infoPanel, BorderLayout.EAST); //TODO: put a border around this panel
        gamePanel.add(gameCenterPanel, BorderLayout.CENTER);

        setUpPlayerPanels(gamePanel, numOfPlayers);
        this.add(gamePanel, BorderLayout.WEST); //TODO: idk if this should be center or west

        //last steps
        this.pack(); //TODO: idk if we need this
        this.setSize(800,800);
        this.revalidate();
        this.setVisible(true);

    }

    private void setUpPlayerPanels(JPanel playerGamePanel, int numOfPlayers){
        //set up player areas:
        //there will be at least one player:
        JPanel player1Panel = new JPanel();
        player1Panel.setLayout(new FlowLayout());
        player1Panel.setBorder(new LineBorder(Color.BLACK)); //just for testing
        List<JPanel> player1cardsPanels = new ArrayList<>(); //not sure if we need this
        for(int i=0; i < 7; i++){
            JPanel currCard = new JPanel(new BorderLayout());
            currCard.setBorder(new LineBorder(Color.BLACK)); //just for testing
            currCard.add(new JLabel("p1: CARD " + (i+1)));
            player1cardsPanels.add(currCard);
            player1Panel.add(currCard);
        }
        playerGamePanel.add(player1Panel, BorderLayout.SOUTH);

        //if there are at least two players:
        if(numOfPlayers > 1){
            System.out.println("There are 2 players");
            JPanel player2Panel = new JPanel();
            player2Panel.setLayout(new FlowLayout());
            player2Panel.setBorder(new LineBorder(Color.BLACK)); //just for testing
            List<JPanel> player2cardsPanels = new ArrayList<>(); //not sure if we need this
            for(int i=0; i < 7; i++){
                JPanel currCard = new JPanel(new BorderLayout());
                currCard.setBorder(new LineBorder(Color.BLACK)); //just for testing
                currCard.add(new JLabel("p2: CARD " + (i+1)));
                player1cardsPanels.add(currCard);
                player2Panel.add(currCard);
            }
            playerGamePanel.add(player1Panel, BorderLayout.NORTH);
        }
        //if there are at least three players:
        if(numOfPlayers > 2){
            System.out.println("There are 3 players");
            JPanel player3Panel = new JPanel();
            player3Panel.setLayout(new BoxLayout(player3Panel, BoxLayout.Y_AXIS));
            player3Panel.setBorder(new LineBorder(Color.BLACK)); //just for testing
            List<JPanel> player3cardsPanels = new ArrayList<>(); //not sure if we need this
            for(int i=0; i < 7; i++){
                JPanel currCard = new JPanel(new BorderLayout());
                currCard.setBorder(new LineBorder(Color.BLACK)); //just for testing
                currCard.add(new JLabel("p3: CARD " + (i+1)));
                player3cardsPanels.add(currCard);
                player3Panel.add(currCard);
            }
            playerGamePanel.add(player3Panel, BorderLayout.WEST);
        }
        //if there are at least three players:
        if(numOfPlayers == 4){
            System.out.println("There are 4 players");
            JPanel player4Panel = new JPanel();
            player4Panel.setLayout(new BoxLayout(player4Panel, BoxLayout.Y_AXIS));
            player4Panel.setBorder(new LineBorder(Color.BLACK)); //just for testing
            List<JPanel> player4cardsPanels = new ArrayList<>(); //not sure if we need this
            for(int i=0; i < 7; i++){
                JPanel currCard = new JPanel(new BorderLayout());
                currCard.setBorder(new LineBorder(Color.BLACK)); //just for testing
                currCard.add(new JLabel("p4: CARD " + (i+1)));
                player4cardsPanels.add(currCard);
                player4Panel.add(currCard);
            }
            playerGamePanel.add(player4Panel, BorderLayout.EAST);
        }
    }

    public void handleTicTacToeStatusUpdate(UnoGameEvent e){
        System.out.println("next view!");
        //gameView();
    }

    public static void main(String[] args){
        UnoModel model = new UnoModel();
        new UnoView(model);
    }
}
