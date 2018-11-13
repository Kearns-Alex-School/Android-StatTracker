package cs246.groupApp.dndapp;

import org.junit.Test;

import cs246.groupApp.dndapp.DiceRoll;

public class DiceRollUnitTest {
    @Test
    public void DiceRollerTest() {
        //test 1 die
        DiceRoll dice = new DiceRoll(1,6,1,0);
        Integer roll = dice.roll();
        assert(roll >= 1 && roll <= 6);

        //test 3 dice
        dice = new DiceRoll(3,6,1,0);
        roll = dice.roll();
        assert (roll >= 3 && roll <= 18);

        //test bonus
        dice = new DiceRoll(1,6,1,1);
        roll = dice.roll();
        assert (roll >= 2 && roll <= 7);

        //test 3 rolls
        dice = new DiceRoll(1,6,3,0);
        roll = dice.roll();
        assert (roll >= 3 && roll <= 18);
    }
}
