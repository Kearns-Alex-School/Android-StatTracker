package cs246.groupApp.dndapp;

public class StatDataModel {
    private Stat stat;

    private boolean showPrimary;
    private boolean showBonus;

    StatDataModel() {
        this.stat = new Stat();
        this.showPrimary = true;
        this.showBonus = true;
    }

    /*********\
    * GETTERS *
    \*********/
    public Stat getStat() {
        return stat;
    }

    public boolean isShowPrimary() {
        return showPrimary;
    }

    public boolean isShowBonus() {
        return showBonus;
    }

    /*********\
    * SETTERS *
    \*********/
    public void setStat(Stat stat) {
        this.stat = stat;
    }

    public void setShowPrimary(boolean showPrimary) {
        this.showPrimary = showPrimary;
    }

    public void setShowBonus(boolean showBonus) {
        this.showBonus = showBonus;
    }
}
