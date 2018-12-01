package cs246.groupApp.dndapp;

public class SubStatDataModel
{
    String bonus;
    String name;
    String statname;

    private Boolean showBonus;
    private Boolean showName;
    private Boolean showStatName;
    private Boolean showDelete;

    private int mainTextSize;
    private int subTextSize;

    private Character character;

    //https://www.journaldev.com/10416/android-listview-with-custom-adapter-example-tutorial
    SubStatDataModel() {
        this.bonus = "";
        this.name = "";
        this.statname = "";

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
    public String getBonus() {
        return bonus;
    }

    public String getName() {
        return name;
    }

    public String getStatname() {
        return statname;
    }

    public Boolean getShowBonus() {
        return showBonus;
    }

    public Boolean getShowName() {
        return showName;
    }

    public Boolean getShowStatName() {
        return showStatName;
    }

    public Boolean getShowDelete() {
        return showDelete;
    }

    public int getMainTextSize() {
        return mainTextSize;
    }

    public int getSubTextSize() {
        return subTextSize;
    }

    public Character getCharacter() {
        return character;
    }

    /*********\
    * SETTERS *
    \*********/
    public void setBonus(String bonus) {
        this.bonus = bonus;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatname(String statname) {
        this.statname = statname;
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

    public void setCharacter(Character character) {
        this.character = character;
    }
}
