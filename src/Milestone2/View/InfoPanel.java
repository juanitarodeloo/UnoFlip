package Milestone2.View;

import Milestone1.Player;
import Milestone2.Model.DeckModel;
import Milestone2.Model.PlayerModel;
import Milestone2.UnoController;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;

public class InfoPanel extends JPanel{
    // This IndoPanel contains three part in the info panel -> start part, player info part and control game part

    private ArrayList<PlayerInfoPanel> playerInfoPanels;
    private JComboBox<Integer> playerNums;
    private JButton startButton = new JButton("Start");
    private JLabel roundLabel = new JLabel();
    private JLabel cardCount = new JLabel();
    private JLabel direction = new JLabel();
    private JLabel currentPlayer = new JLabel();
    private JButton drawOne = new JButton("Draw One");
    private JButton nextPlayer = new JButton("Next Player");
    private UnoController controller;

    private Image backgroundImage;
    public InfoPanel(UnoController controller){
        this.backgroundImage = loadBackgroundImage("UNO").getImage();
        this.controller = controller;
        this.playerInfoPanels = new ArrayList<PlayerInfoPanel>();
        this.setLayout(new GridLayout(3, 1));
        //this.setBorder(BorderFactory.createLineBorder(Color.black, 3)); // for test


        initStart();
    }

    private ImageIcon loadBackgroundImage(String imageName) {
        String fileName = "/" + imageName + ".png";

        URL resource = getClass().getResource(fileName);
        if (resource == null) {
            System.err.println("Failed to load resource: " + fileName);
            System.err.println("Current directory: " + System.getProperty("user.dir"));
            System.err.println("Classpath: " + System.getProperty("java.class.path"));
            return null;
        } else {
            return new ImageIcon(resource);
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Paint the panel's background

        // Only draw the image if it has been loaded successfully
        if (backgroundImage != null) {
            Graphics2D g2d = (Graphics2D) g.create();

            // Set rendering hints for better image quality
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw the image to fit the panel size
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);

            g2d.dispose(); // Dispose of the graphics object to release resources
        }
    }
    /**
     * initStart creates panel for start part which contains the combobox for choosing the number of players
     */
    public void initStart(){
        startButton = new JButton("Start");
        startButton.setFont(new Font("Tahoma", Font.BOLD, 18));
        startButton.setForeground(Color.WHITE);
        startButton.setBackground(new Color(0, 153, 76)); // A more vibrant color
        startButton.setBorder(BorderFactory.createRaisedBevelBorder());
        startButton.addActionListener(controller);


        JLabel welcomeLabel = new JLabel("Welcome to Uno!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Tahoma", Font.BOLD, 36)); // Larger font size for title
        welcomeLabel.setForeground(Color.WHITE); // White color for better contrast

        JLabel selectLabel = new JLabel("Please select the number of players!", SwingConstants.CENTER);
        selectLabel.setFont(new Font("Tahoma", Font.PLAIN, 18)); // Larger font size for subtext
        selectLabel.setForeground(Color.WHITE);

        JLabel clickStartLabel = new JLabel("Click start to begin the game!", SwingConstants.CENTER);
        clickStartLabel.setFont(new Font("Tahoma", Font.PLAIN, 18)); // Consistent font size
        clickStartLabel.setForeground(Color.WHITE);

        Integer[] choices = {2, 3, 4};
        playerNums = new JComboBox<>(choices);
        playerNums.setFont(new Font("Tahoma", Font.BOLD, 18));
        playerNums.setForeground(Color.BLACK);
        playerNums.setBackground(Color.WHITE);
        playerNums.addActionListener(controller);

        // Create a transparent panel to hold the components
        JPanel startPanel = new JPanel();
        startPanel.setLayout(new GridBagLayout()); // Use GridBagLayout for better control
        startPanel.setOpaque(false); // Make it transparent

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0); // Add some padding

        // Add components with GridBagConstraints
        startPanel.add(welcomeLabel, gbc);
        startPanel.add(selectLabel, gbc);
        startPanel.add(playerNums, gbc);
        startPanel.add(clickStartLabel, gbc);
        startPanel.add(startButton, gbc);

        // Add the start panel to the InfoPanel
        this.add(startPanel);
    }

    /**
     * initPlayerInfo creates a panel that contains the PlayerInfoPanel.
     * The number of PlayerInfoPanel is the same as the number of players
     * @param players
     */
    public void initPlayerInfo(ArrayList<PlayerModel> players){
        JPanel playerInfo = new JPanel();
        playerInfo.setLayout(new GridLayout(players.size(), 1));
        playerInfo.setBackground(new Color(51, 204, 255)); // Ensuring the panel background matches
        for (PlayerModel player : players) { // Create player info panel for each player
            PlayerInfoPanel currentPlayer = new PlayerInfoPanel(player.getName());
            playerInfo.add(currentPlayer);
            this.playerInfoPanels.add(currentPlayer);
        }
        this.add(playerInfo);
    }

    /**
     * initGameInfo creates a panel that contains the game information like round number, turns order,
     * and buttons for drawing a card and pass the game to the next player
     */
    public void initGameInfo(){
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(6, 1));

        //gamePanel.setBackground(Color.WHITE); // Ensuring the panel background matches

        // Add the components to the gamePanel
        gamePanel.add(this.roundLabel);
        gamePanel.add(this.direction);
        gamePanel.add(this.cardCount);
        gamePanel.add(this.currentPlayer);
        this.drawOne.addActionListener(this.controller);
        gamePanel.add(this.drawOne);
        this.nextPlayer.addActionListener(this.controller);
        gamePanel.add(this.nextPlayer);
        this.add(gamePanel);
    }

    /**
     * startGame is called after the player click start button,
     * It disables the start button and the combo box used for choosing the number of players
     * @param players
     */
    public void startGame(ArrayList<PlayerModel> players){
        this.startButton.setEnabled(false);  // Disable start button
        this.playerNums.setEnabled(false);  // Disable combo box used for selecting number of players
        this.initPlayerInfo(players);
        this.initGameInfo();
    }

    /**
     * updateRound updates the displayed round label text
     * @param newRound
     */
    public void updateRound(int newRound){
        this.roundLabel.setText("Round " + newRound +" --------");
    }

    /**
     * updateDirection updates the displayed direction label text
     * @param direction
     */
    public void updateDirection(String direction){
        this.direction.setText("Game order: " + direction);
    }

    /**
     * updateCurrPlayer updates the displayed current player label text
     * @param player
     */
    public void updateCurrPlayer(String player){
        this.currentPlayer.setText("Current player: " + player);
    }

    /**
     * setDrawOneState disables or enables the Draw One button
     * @param isEnable
     */
    public void setDrawOneState(boolean isEnable){
        this.drawOne.setEnabled(isEnable);
    }

    /**
     * setNextPlayerState disables or enables the Next Player button
     * @param isEnable
     */
    public void setNextPlayerState(boolean isEnable){
        this.nextPlayer.setEnabled(isEnable);
    }

    /**
     * updateScore calls the method to update the displayed score in specific playerInfoPanel
     * @param playerIndex
     * @param newScore
     */
    public void updateScore(int playerIndex, int newScore){
        this.playerInfoPanels.get(playerIndex).updateScore(newScore);
    }
}
