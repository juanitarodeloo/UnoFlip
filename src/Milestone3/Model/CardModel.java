package Milestone3.Model;

public class CardModel {

    private final CardSideModel lightSide;
    private final CardSideModel darkSide;

    public CardModel(CardSideModel lightSide, CardSideModel darkSide){
        this.lightSide = lightSide;
        this.darkSide = darkSide;

    }

    public CardSideModel getCard(boolean isLight){
        if (isLight){
            return this.lightSide;
        }else {
            return this.darkSide;
        }
    }

    public CardSideModel.Color getColor(boolean isLight){
        if (isLight){
            return this.lightSide.getColor();
        }else {
            return this.darkSide.getColor();
        }
    }

    public CardSideModel.Type getType(boolean isLight){
        if (isLight){
            return this.lightSide.getType();
        }else {
            return this.darkSide.getType();
        }
    }

    public String toString(boolean isLight){
        if (isLight){
            return this.lightSide.toString();
        }else {
            return this.darkSide.toString();
        }
    }
}
