package cs246.groupApp.dndapp;

/**
 * Data model for substats. Establishes getters and setters.
 * @see CharacterDataModel
 * @author Alex Kearns
 */
public class SubStatDataModel {
    private Character character;
    private String name;
    private String statName;
    private int bonus;

    private Boolean showBonus;
    private Boolean showName;
    private Boolean showStatName;
    private Boolean showDelete;

    private int mainTextSize;
    private int subTextSize;

    //https://www.journaldev.com/10416/android-listview-with-custom-adapter-example-tutorial
    SubStatDataModel() {
        this.character = new Character();
        this.name = "";
        this.statName = "";
        this.bonus = 0;

        this.showBonus = true;
        this.showName = true;
        this.showStatName = true;
        this.showDelete = true;

        this.mainTextSize = 15;
        this.subTextSize = 10;
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

    String getStatName() {
        return statName;
    }

    public int getBonus() {
        return bonus;
    }

    Boolean getShowBonus() {
        return showBonus;
    }

    Boolean getShowName() {
        return showName;
    }

    Boolean getShowStatName() {
        return showStatName;
    }

    Boolean getShowDelete() {
        return showDelete;
    }

    int getMainTextSize() {
        return mainTextSize;
    }

    int getSubTextSize() {
        return subTextSize;
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

    void setStatName(String statName) {
        this.statName = statName;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public void setShowBonus(Boolean showBonus) {
        this.showBonus = showBonus;
    }

    public void setShowName(Boolean showName) {
        this.showName = showName;
    }

    public void setShowStatName(Boolean showStatName) {
        this.showStatName = showStatName;
    }

    public void setShowDelete(Boolean showDelete) {
        this.showDelete = showDelete;
    }

    public void setMainTextSize(int mainTextSize) {
        this.mainTextSize = mainTextSize;
    }

    public void setSubTextSize(int subTextSize) {
        this.subTextSize = subTextSize;
    }
}
