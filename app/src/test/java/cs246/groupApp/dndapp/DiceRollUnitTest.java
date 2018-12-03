package cs246.groupApp.dndapp;

import org.junit.Test;

/**
 * Unit test for die class.
 * @author Kevin Marsh
 */
public class DiceRollUnitTest {
    @Test
    public void DiceRollerTest() {
        Die die = new Die(6);
        int roll = die.roll();
        assert (roll >= 1 && roll <= 6);

        die.numSides = 20;
        roll = die.roll();
        assert (roll >= 1 && roll <= 20);

        die.numSides = 4;
        roll = die.roll();
        assert (roll >= 1 && roll <= 4);
    }
}
