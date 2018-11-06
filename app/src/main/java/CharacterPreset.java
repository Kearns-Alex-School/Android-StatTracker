import java.util.List;

public interface CharacterPreset {
//basic character preset stats
    public List<Stat> DnDStats = null;
    public List<Stat> SPECIAL = null;

    public List<Stat> LoadCustomPreset(String fileName);
    public void SaveCustomPreset(String fileName);
}
