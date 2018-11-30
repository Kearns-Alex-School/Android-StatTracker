package cs246.groupApp.dndapp;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Character class. Holds all of the information about a character.
 * @author Kevin Marsh
 */
class Character {
    public String name;
    Integer HPMax;
    Integer HPCurrent;
    Integer EXP;
    Integer MP;
    Integer ArmrRating;
    public Integer speed;
    String fileName;
    List<Stat> CharacterStatsList;
    List<Stat> statList;
    List<Item> inventory;

    /**
     * Constructor. Takes no parameters. Allocates List objects, and sets all member variables to 0.
     */
    Character() {
        this.name = "";
        this.HPMax = 0;
        this.HPCurrent = 0;
        this.EXP = 0;
        this.MP = 0;
        this.speed = 0;
        this.ArmrRating = 0;
        this.CharacterStatsList = new ArrayList<>();
        this.statList = new ArrayList<>();
        this.inventory = new ArrayList<>();
    }


    /**
     * This function loads a specified preset from a file into the character statList variable.
     * @param presetName: A string containing the name of the preset, expected to be in lower-case
     *                  and not including a file extension (.txt).
     * @param dir: A File object for the path to the directory where preset files are stored.
     * @see PresetGenerator for information regarding the presets themselves.
     * @author Kevin Marsh, Alex Kearns
     * */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    void loadPreset(String presetName, File dir) {
        File file = new File(dir, presetName + ".txt");
        String contentJson = null;

        FileInputStream fis;
        try {
            fis = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            fis.close();

            contentJson = new String(data, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(contentJson);

        Gson gson = new Gson();

        // get all of the stats out of the file
        Stat[] temp =  gson.fromJson(contentJson, Stat[].class);

        // add the stats to the list in our character
        statList.addAll(Arrays.asList(temp));
    }

}