package Milestone1;

public class PlayerTest {

    public static void main(String[] args){

        Player player = new Player("Ayman");

        System.out.println("Test 1 - Player's name: " + player.getName());

        Card card1 = new Card(Card.Color.RED, Card.Type.ONE);
        Card card2 = new Card(Card.Color.BLUE, Card.Type.SKIP);
        Card card3 = new Card(Card.Color.GREEN, Card.Type.THREE);

        player.pickUpCard(card1);
        player.pickUpCard(card2);
        player.pickUpCard(card3);
        System.out.println("Test 2 - Players hand: " + player.getHand());

        player.playCard(card1);

        System.out.println("Test 3 - Players hand afer playing a card: " + player.getHand());

        System.out.println("Test 4 - Players state:\n" + player);
        System.out.println("Test 5 - printHand method ");
        player.printHand();






    }




}
