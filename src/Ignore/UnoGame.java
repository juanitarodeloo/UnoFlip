//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package Ignore;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class UnoGame {

    private Deck deck = new Deck();
    private List<Card> discardPile = new ArrayList();
    private List<Player> players = this.initializePlayers();
    private static Side currentSide;
    private int currentPlayerIndex = 0;
    private Color temporaryChosenColor = null;
    //private static final int INITIAL_NUM_CARDS = 7;

    public UnoGame() {
        Card startingCard;
        //starting card is the card at the top/bottom of the deck
        //if the card drawn is a wild card or a pick up two card, add it to the bottom and keep drawing
        for(startingCard = this.deck.draw(); startingCard.getLightValue() == Value.WILD || startingCard.getLightValue() == Value.WILD_DRAW_TWO; startingCard = this.deck.draw()) {
            this.deck.addCardToBottom(startingCard);
        }
        //once the starting card is valid, add it to the discard pile
        this.discardPile.add(startingCard);
        System.out.println("Starting card: " + this.discardPile.get(0));
    }

    public void start() {
        //rotate between players to control whos turn it is
        while(true) {
            this.takeTurn((Player)this.players.get(this.currentPlayerIndex));
            this.currentPlayerIndex = (this.currentPlayerIndex + 1) % this.players.size();
        }
    }

    private List<Player> initializePlayers() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of players (2-4): ");
        int playerCount = sc.nextInt();
        sc.nextLine();
        List<Player> players = new ArrayList();

        for(int i = 0; i < playerCount; ++i) {
            System.out.print("Enter name for Player " + (i + 1) + ": ");
            String playerName = sc.nextLine();
            Player player = new Player(playerName);

            for(int j = 0; j < 7; ++j) {
                player.drawCard(this.deck.draw());
            }

            players.add(player);
        }

        return players;
    }

    public static Side getCurrentSide() {

        return currentSide;
    }

    private void takeTurn(Player player) {
        PrintStream var10000 = System.out;
        String var10001 = player.getName();
        var10000.println(var10001 + "'s Turn.");
        System.out.println("Current side: " + (currentSide == Side.LIGHT ? "Light" : "Dark"));
        System.out.println("Your cards: \n" + player);
        var10000 = System.out;
        List var8 = this.discardPile;
        int var10002 = this.discardPile.size();
        var10000.println("Top card: " + var8.get(var10002 - 1));
        System.out.println("Enter card index to play or 0 to draw a card: ");
        Scanner sc = new Scanner(System.in);

        while(true) {
            int choice;
            while(true) {
                try {
                    choice = sc.nextInt();
                    break;
                } catch (InputMismatchException var7) {
                    System.out.println("Invalid input. Please enter a valid number.");
                    sc.next();
                }
            }

            Card topCard;
            if (choice == 0) {
                if (this.deck.isEmpty()) {
                    this.refillDeckFromDiscard();
                }

                topCard = this.deck.draw();
                player.drawCard(topCard);
                System.out.println("Drew a card: " + topCard);
                break;
            }

            if (choice > 0 && choice <= player.getHand().size()) {
                topCard = (Card)this.discardPile.get(this.discardPile.size() - 1);
                Card chosenCard = (Card)player.getHand().get(choice - 1);
                if (this.isValidPlay(chosenCard, topCard)) {
                    Card playedCard = player.playCard(choice - 1);
                    this.discardPile.add(playedCard);
                    this.temporaryChosenColor = null;
                    System.out.println("Played: " + playedCard);
                    this.handleSpecialCard(playedCard);
                    if (player.getHand().isEmpty()) {
                        System.out.println(player.getName() + " has won!");
                        this.calculateScores(player);
                        return;
                    }
                    break;
                }

                System.out.println("Card doesn't match the top card. Try again.");
            } else {
                System.out.println("Invalid choice. Try again.");
            }
        }

    }

    private boolean isValidPlay(Card chosenCard, Card topCard) {
        if (currentSide == Side.LIGHT) {
            if (chosenCard.getLightValue() != Value.WILD && chosenCard.getLightValue() != Value.WILD_DRAW_TWO) {
                if (topCard.getLightValue() != Value.WILD && topCard.getLightValue() != Value.WILD_DRAW_TWO) {
                    return chosenCard.getLightColor() == topCard.getLightColor() || chosenCard.getLightValue() == topCard.getLightValue();
                } else {
                    return chosenCard.getLightColor() == this.temporaryChosenColor;
                }
            } else {
                return true;
            }
        } else if (chosenCard.getDarkValue() != Value.WILD && chosenCard.getDarkValue() != Value.WILD_DRAW_TWO) {
            if (topCard.getDarkValue() != Value.WILD && topCard.getDarkValue() != Value.WILD_DRAW_TWO) {
                return chosenCard.getDarkColor() == topCard.getDarkColor() || chosenCard.getDarkValue() == topCard.getDarkValue();
            } else {
                return chosenCard.getDarkColor() == this.temporaryChosenColor;
            }
        } else {
            return true;
        }
    }

    private void calculateScores(Player winningPlayer) {
        int totalScore = 0;
        Iterator var3 = this.players.iterator();

        while(true) {
            Player player;
            do {
                if (!var3.hasNext()) {
                    PrintStream var10000 = System.out;
                    String var10001 = winningPlayer.getName();
                    var10000.println(var10001 + " scored " + totalScore + " points this round.");
                    return;
                }

                player = (Player)var3.next();
            } while(player == winningPlayer);

            Iterator var5 = player.getHand().iterator();

            while(var5.hasNext()) {
                Card card = (Card)var5.next();
                Value value = getCurrentSide() == Side.LIGHT ? card.getLightValue() : card.getDarkValue();
                switch(value) {
                    case ONE:
                    case TWO:
                    case THREE:
                    case FOUR:
                    case FIVE:
                    case SIX:
                    case SEVEN:
                    case EIGHT:
                    case NINE:
                        totalScore += value.ordinal() + 1;
                        break;
                    case DRAW_ONE:
                        totalScore += 10;
                        break;
                    case DRAW_FIVE:
                    case REVERSE:
                    case SKIP:
                    case FLIP:
                        totalScore += 20;
                        break;
                    case SKIP_EVERYONE:
                        totalScore += 30;
                        break;
                    case WILD:
                        totalScore += 40;
                        break;
                    case WILD_DRAW_TWO:
                        totalScore += 50;
                        break;
                    case WILD_DRAW_COLOR:
                        totalScore += 60;
                }
            }
        }
    }

    private void refillDeckFromDiscard() {
        Card topCard = (Card)this.discardPile.remove(this.discardPile.size() - 1);
        this.deck.refill(this.discardPile);
        this.discardPile.clear();
        this.discardPile.add(topCard);
    }

    private void toggleSide() {
        if (currentSide == Side.LIGHT) {
            currentSide = Side.DARK;
        } else {
            currentSide = Side.LIGHT;
        }

    }

    private void handleSpecialCard(Card card) {
        Value currentValue = currentSide == Side.LIGHT ? card.getLightValue() : card.getDarkValue();
        Card drawnCard;
        PrintStream var10000;
        String var10001;
        switch(currentValue) {
            case DRAW_ONE:
                this.currentPlayerIndex = (this.currentPlayerIndex + 1) % this.players.size();
                Player targetForDrawOne = (Player)this.players.get(this.currentPlayerIndex);
                drawnCard = this.deck.draw();
                targetForDrawOne.drawCard(drawnCard);
                var10000 = System.out;
                var10001 = targetForDrawOne.getName();
                var10000.println(var10001 + " has to draw one card due to Draw One: " + drawnCard);
                break;
            case DRAW_FIVE:
                this.currentPlayerIndex = (this.currentPlayerIndex + 1) % this.players.size();
                Player targetForDrawFive = (Player)this.players.get(this.currentPlayerIndex);
                StringBuilder drawnCards = new StringBuilder();

                for(int i = 0; i < 5; ++i) {
                    drawnCard = this.deck.draw();
                    targetForDrawFive.drawCard(drawnCard);
                    drawnCards.append(drawnCard).append(i < 4 ? ", " : "");
                }

                var10000 = System.out;
                var10001 = targetForDrawFive.getName();
                var10000.println(var10001 + " has to draw five cards due to Draw Five: " + drawnCards);
                break;
            case REVERSE:
                Collections.reverse(this.players);
                this.currentPlayerIndex = this.players.size() - 1 - this.currentPlayerIndex;
                break;
            case SKIP:
                this.currentPlayerIndex = (this.currentPlayerIndex + 1) % this.players.size();
                break;
            case FLIP:
                this.toggleSide();
            case SKIP_EVERYONE:
            default:
                break;
            case WILD:
                this.chooseColorForWild();
                break;
            case WILD_DRAW_TWO:
                this.chooseColorForWild();
                this.currentPlayerIndex = (this.currentPlayerIndex + 1) % this.players.size();
                Player nextPlayer = (Player)this.players.get(this.currentPlayerIndex);
                Card firstDrawnCard = this.deck.draw();
                Card secondDrawnCard = this.deck.draw();
                nextPlayer.drawCard(firstDrawnCard);
                nextPlayer.drawCard(secondDrawnCard);
                System.out.println(nextPlayer.getName() + " has to draw two cards due to Wild Draw Two: " + firstDrawnCard + ", " + secondDrawnCard);
        }

    }

    private void chooseColorForWild() {
        System.out.println("Choose a color (RED, YELLOW, GREEN, BLUE): ");
        Scanner sc = new Scanner(System.in);

        while(true) {
            try {
                String chosenColorStr = sc.nextLine().toUpperCase();
                if (Arrays.asList("RED", "YELLOW", "GREEN", "BLUE").contains(chosenColorStr)) {
                    this.temporaryChosenColor = Color.valueOf(chosenColorStr);
                    System.out.println(chosenColorStr + " has been chosen.");
                    return;
                }

                System.out.println("Invalid color. Choose again.");
            } catch (Exception var3) {
                System.out.println("Error in choosing color. Try again.");
            }
        }
    }

    public static void main(String[] args) {
        UnoGame game = new UnoGame();
        game.start();
    }

    static {
        currentSide = Side.LIGHT;
    }
}
