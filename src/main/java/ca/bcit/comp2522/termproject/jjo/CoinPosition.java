package ca.bcit.comp2522.termproject.jjo;

import java.io.Serializable;

/**
 * Holds a coins X and Y position.
 *
 * @param x the x position.
 * @param y the y position.
 * @author adedejitoki & vasilyshorin
 * @version 1.0
 */
public record CoinPosition(double x, double y) implements Serializable {
    /**
     * Constructs a CoinPosition object.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public CoinPosition {
    }

    /**
     * Gets the x coordinate.
     *
     * @return the x coordinate
     */
    public double getX() {
        return x;
    }

    /**
     * Gets the y coordinate.
     *
     * @return the y coordinate
     */
    public double getY() {
        return y;
    }

}
