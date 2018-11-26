package cs246.groupApp.dndapp;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public Character(String name, Integer HP, Integer EXP, Integer MP, Integer ArmrRating) {
        this.name = name;
        this.HPMax = HP;
        this.EXP = EXP;
        this.MP = MP;
        this.ArmrRating = ArmrRating;
    }

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

    // takes a preset name (without .txt), and the presetDir
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