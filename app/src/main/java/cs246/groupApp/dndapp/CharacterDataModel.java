package cs246.groupApp.dndapp;

public class CharacterDataModel {
    private Character character;

    CharacterDataModel(Character character) {
        this.character = character;
    }

    /*********\
    * GETTERS *
    \*********/
    public Character getCharacter() {
        return character;
    }
    /*********\
    * SETTERS *
    \*********/
    public void setCharacter(Character character) {
        this.character = character;
    }
}