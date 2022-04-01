package ca.bcit.comp2522.termproject.jjo;

import java.io.Serializable;

public class CoinPosition implements Serializable {
    private double x;
    private double y;


    public CoinPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

}
