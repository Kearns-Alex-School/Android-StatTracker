package cs246.groupApp.dndapp;

public class InventoryDataModel {
    private Character character;
    private String name;
    private int DMG;
    private int AMR;
    private Stat statBonus;
    private int bonus1;
    private int bonus2;
    private String notes;

    private Boolean isAddNew;

    InventoryDataModel() {
        this.character = new Character();
        this.name = "";
        this.DMG = 0;
        this.AMR = 0;
        this.statBonus = new Stat();
        this.bonus1 = 0;
        this.bonus2 = 0;
        this.notes = "";

        this.isAddNew = false;
    }

    /*********\
    * GETTERS *
    \*********/
    public Character getCharacter() {
        return character;
    }

    public String getName() {
        return name;
    }

    public int getDMG() {
        return DMG;
    }

    public int getAMR() {
        return AMR;
    }

    public Stat getStatBonus() {
        return statBonus;
    }

    public int getBonus1() {
        return bonus1;
    }

    public int getBonus2() {
        return bonus2;
    }

    public String getNotes() {
        return notes;
    }

    public Boolean getAddNew() {
        return isAddNew;
    }

    /*********\
    * SETTERS *
    \*********/
    public void setCharacter(Character character) {
        this.character = character;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDMG(int DMG) {
        this.DMG = DMG;
    }

    public void setAMR(int AMR) {
        this.AMR = AMR;
    }

    public void setStatBonus(Stat statBonus) {
        this.statBonus = statBonus;
    }

    public void setBonus1(int bonus1) {
        this.bonus1 = bonus1;
    }

    public void setBonus2(int bonus2) {
        this.bonus2 = bonus2;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setAddNew(Boolean addNew) {
        isAddNew = addNew;
    }
}