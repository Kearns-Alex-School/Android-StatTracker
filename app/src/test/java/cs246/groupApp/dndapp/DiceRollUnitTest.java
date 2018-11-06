package cs246.groupApp.dndapp;

import org.junit.Test;

import cs246.groupApp.dndapp.DiceRoll;

public class DiceRollUnitTest {
    @Test
    public void DiceRollerTest() {
        DiceRoll dice = new DiceRoll(1,6,1,0);
        Integer roll = dice.roll();
//        System.out.println(roll);
        assert(roll >= 1 && roll <= 6);
    }
}
