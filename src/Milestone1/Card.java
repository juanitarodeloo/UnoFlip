package Milestone1;

public class Card {

    public enum Color{
        RED, YELLOW, GREEN, BLUE, PINK, TEAL, ORANGE, PURPLE,
    }

    public enum Type{
        REVERSE, SKIP, WILD, WILD_DRAW_TWO

        }

    private Color color;
    private Type type;

    public Card(Color color, Type type){
        this.color = color;
        this.type = type;

    }

    public Color getColor(){
        return color;

    }
    public void setColor(){
        this.color = color;
    }
    public Type getType(){
        return type;
    }

    public void setType(){
        this.type = type;
    }
    public String toString(){
        return color + " " + type;
    }



}
