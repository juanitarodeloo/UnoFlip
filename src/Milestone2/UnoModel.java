package Milestone2;

import Milestone1.Card;
import Milestone1.Deck;
import Milestone1.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UnoModel {

    private List<Player> players;
    private Deck myDeck;
    private Card.Color targetColor;
    private List<Card> discardPile;
    private UnoView view;
    public enum Status {GAME_INIT, GAME_PLAY};
    private Status status;
    private int numOfPlayers;


    public UnoModel(){
        myDeck = new Deck();
        discardPile = new ArrayList<>();
        players = new ArrayList<>();
        view = new UnoView(this);
        status = Status.GAME_INIT;
    }

    public void initPlayers(int playersNum){
        numOfPlayers = playersNum;
        view.initPlayers(numOfPlayers);

    }

    public void savePlayerNames(String[] names){
        System.out.println("Names:");
        for(int i = 0; i < names.length; i++){
            System.out.println(names[i]); //for testing
        }
        //System.out.println("numOfPlayers: " + numOfPlayers + "- in model");
        view.gameView(names, numOfPlayers); //TODO: probably need a better way to pass around numOfPlayers, just so it's once
    }

    /**
     * playGame() initiate the game by prompting the user with number of players and their names,
     * initializing the players,
     * repeat the game (different rounds) until someone wins (one player has more than 500 score)
     */
    public void startGame(){
        //TODO: frame for playing game
        status = Status.GAME_PLAY;


    }



    /**
     * initPlayers() will initialize players based on the user input
     * @return a list of players
     */
//    private void initPlayers(){
//        int numOfPlayers = 0;
//        boolean validNumOfPlayers = false;
//        String playerName;
//        Player currPlayer;
//        Scanner sc = new Scanner(System.in);
//
//        while(!validNumOfPlayers){
//            System.out.print("Enter number of players (2-4): ");
//            try{
//                numOfPlayers = sc.nextInt();
//                sc.nextLine();
//
//                if(numOfPlayers >= 2 && numOfPlayers <= 4){
//                    validNumOfPlayers = true;
//                }else{
//                    System.out.println("Invalid # of players. Please enter a number between 2 and 4: ");
//                }
//            }catch(Exception e){
//                System.out.println("Invalid input. Try again: ");
//                sc.nextLine();
//            }
//        }
//
//        for(int i = 0; i < numOfPlayers; i++) {
//            System.out.print("Enter name for Player " + (i+1) + ": ");
//            playerName = sc.nextLine();
//            currPlayer = new Player(playerName);
//            players.add(currPlayer);
//        }
//
//        return players;
//    }






}
