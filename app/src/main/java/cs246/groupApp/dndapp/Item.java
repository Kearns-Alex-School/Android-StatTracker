package cs246.groupApp.dndapp;

/**
 * Item class. Encapsulates the concept of an item in an inventory, with a name, a Damage, a Damage
 * resistance, two bonus values, and a set of notes.
 * @author Kevin Marsh
 */
class Item {
    public String name;
    public Integer DMG;
    public Integer AMR;
    Stat statBonus;
    public Integer bonus1;
    public Integer bonus2;
    public String notes;
}
