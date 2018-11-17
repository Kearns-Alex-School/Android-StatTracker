package cs246.groupApp.dndapp;

import java.util.List;

class Character implements CharacterPreset {
    public String name;
    public Integer HP;
    public Integer EXP;
    public Integer MP;
    public Integer DMGResist;
    public String fileName;
    public List<Stat> CharacterStatsList;
    public List<Stat> statList;
    public List<Item> inventory;

    public Character(String name, Integer HP, Integer EXP, Integer MP, Integer DMGResist) {
        this.name = name;
        this.HP = HP;
        this.EXP = EXP;
        this.MP = MP;
        this.DMGResist = DMGResist;
        loadPreset();

    }

    public Character() {
        this.name = "";
        this.HP = 0;
        this.EXP = 0;
        this.MP = 0;
        this.DMGResist = 0;
    }


    public void loadPreset(/*somehow indicate whether to load a preset or a custom*/) {
//logic to load preset/call load custom from file
//        this.CharacterStatsList = DnDStats;
    }

    @Override
    public List<Stat> LoadCustomPreset(String fileName) {
//        this should be async
        return null;
    }

    @Override
    public void SaveCustomPreset(String fileName) {
//this should be async
//        save this.statList
    }


}
