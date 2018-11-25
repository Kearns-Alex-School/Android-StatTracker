package cs246.groupApp.dndapp;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Character {
    public String name;
    public Integer HPMax;
    public Integer HPCurrent;
    public Integer EXP;
    public Integer MP;
    public Integer ArmrRating;
    public Integer speed;
    public String fileName;
    public List<Stat> CharacterStatsList;
    public List<Stat> statList;
    public List<Item> inventory;

    public Character(String name, Integer HP, Integer EXP, Integer MP, Integer ArmrRating) {
        this.name = name;
        this.HPMax = HP;
        this.EXP = EXP;
        this.MP = MP;
        this.ArmrRating = ArmrRating;
    }

    public Character() {
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

//KM: filling in preset.
    // takes a preset name (without .txt), and the presetDir
    public void loadPreset(String presetName, File dir) {
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
        StatList stats = gson.fromJson(contentJson, StatList.class);
        this.statList = stats.getStatList();
    }

}
