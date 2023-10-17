package Milestone1;

public class CardTest {

    public static void main(String[] args){


        /**Test 1: Creating and printing a Card
         *
         */
        Card card1 = new Card(Card.Color.RED, Card.Type.FIVE);
        System.out.println("Test 1 - created Card: " + card1);

        /** Test 2: Testing getColor and getType
         *
         */
        if(card1.getColor() == Card.Color.RED && card1.getType() == Card.Type.FIVE){
            System.out.println("Test 2 - Getters work.");
        }else{
            System.out.println("Test 2 - Error in Getters");
        }


        /**Test 3: Testing setColor and setType
         *
         */
        card1.setColor(Card.Color.BLUE);
        card1.setType(Card.Type.WILD);
        if(card1.getColor() == Card.Color.BLUE && card1.getType() == Card.Type.WILD){
            System.out.println("Test 3 - Setters work.");
        }else{
            System.out.println("Test 2 - Error in Setters");
        }

        /**Test 4: Testing toString after modifying card
         *
         */
        System.out.println("Test 4 - Modified Card: " + card1);





    }
}
