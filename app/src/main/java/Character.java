import org.w3c.dom.ls.LSException;

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
}
