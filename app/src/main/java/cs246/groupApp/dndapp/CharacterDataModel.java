package cs246.groupApp.dndapp;

/**
 * Character data model class. Allows for getters/setters for the character.
 * @author Alex Kearns
 */
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