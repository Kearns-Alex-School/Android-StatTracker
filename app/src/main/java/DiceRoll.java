public class DiceRoll {
    public Integer lastRoll;
    public Integer numDie;
    public Integer dieSides;
    public Integer numRolls;
    public Integer bonus;

    public DiceRoll(int numDie, int dieSides, int numRolls, int bonus) {
        this.numDie = numDie;
        this.dieSides = dieSides;
        this.numRolls = numRolls;
        this.bonus=bonus;
        lastRoll = null;
    }

    // random number generator dice roller
    public Integer roll() {
        Integer totalRoll = 0;
        if (lastRoll == null) {
            lastRoll = 0;
        }
        // generate a random number between the num of sides and 1. Repeat for each die. Repeat for num of rolls.
        for (int i = 0; i < numRolls; i++) {
            double roll = 0;
            for (int j = 0; j < numDie; j++) {
                roll += (Math.random() * ((dieSides - 1) + 1)) + 1;
            }
            totalRoll = (int) roll + lastRoll + bonus;
            lastRoll = (int) roll;
        }
        
        return totalRoll;
    }
}
