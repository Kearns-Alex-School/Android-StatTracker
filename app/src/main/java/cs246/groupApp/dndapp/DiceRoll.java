package cs246.groupApp.dndapp;

import java.util.ArrayList;
import java.util.List;

//DiceRoll is a wrapper class for the die class.
public class DiceRoll {
    public Integer numDie;
    public Integer dieSides;
    public Integer numRolls;
    public Integer bonus;
    public List<Die> dice;

    DiceRoll(int numDie, int dieSides, int numRolls, int bonus) {
        this.numDie = numDie;
        this.dieSides = dieSides;
        this.numRolls = numRolls;
        this.bonus=bonus;

        dice = new ArrayList<Die>();
        for (int i = 0; i < numDie * numRolls; i++) {
            dice.add(new Die(dieSides));
        }
    }

    // random number generator dice roller
    public Integer roll() {
        int totalRoll = 0;

        for (int i = 0; i < dice.size(); i++) {
            totalRoll += dice.get(i).roll();
        }

        return totalRoll + bonus;

    }

    public List<Die> getDice() {
        return dice;
    }
}


class Die {
    public Integer numSides;
    public Integer value;

    Die(int numSides) {
        this.numSides = numSides;
    }

    public Integer roll() {
        double roll = (Math.random() * ((numSides - 1) + 1)) + 1;
        value = (int) roll;
        return value;
    }
}