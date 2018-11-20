package cs246.groupApp.dndapp;

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
        loadPreset();

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


    public void loadPreset(/*somehow indicate whether to load a preset or a custom*/) {
//logic to load preset/call load custom from file
//        this.CharacterStatsList = DnDStats;
    }

}
