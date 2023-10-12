//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package Milestone1;

public class Card {
    private final Color lightColor;
    private final Value lightValue;
    private final Color darkColor;
    private final Value darkValue;

    public Card(Color lightColor, Value lightValue, Color darkColor, Value darkValue) {
        this.lightColor = lightColor;
        this.lightValue = lightValue;
        this.darkColor = darkColor;
        this.darkValue = darkValue;
    }

    public Color getLightColor() {
        return this.lightColor;
    }

    public Value getLightValue() {
        return this.lightValue;
    }

    public Color getDarkColor() {
        return this.darkColor;
    }

    public Value getDarkValue() {
        return this.darkValue;
    }

    public String toString() {
        return UnoGame.getCurrentSide() == Side.LIGHT ? this.lightColor + " " + this.lightValue : this.darkColor + " " + this.darkValue;
    }
}
