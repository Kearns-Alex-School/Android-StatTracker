package cs246.groupApp.dndapp;

import java.util.ArrayList;
import java.util.List;

/**
 * Die class encapsulates the concept of a single configurable die with an adjustable numbe of sides.
 * @author Kevin Marsh
 */
public class Die {
    public Integer numSides;
    public Integer value;

    /**
     * Constructor. Allows the number of sides to be set on object creation.
     * @param numSides int containing the number of sides for the die.
     * @author Kevin Marsh
     */
    Die(int numSides) {
        this.numSides = numSides;
    }

    /**
     * Rolls the die. Generates a random number inside of the range of the sides of the die.
     * @return Integer object containing the result of the roll. Result is also accessible through the
     *          value member variable.
     * @author Kevin Marsh
     */
    public Integer roll() {
        double roll = (Math.random() * ((numSides - 1) + 1)) + 1;
        value = (int) roll;
        return value;
    }
}