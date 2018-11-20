package cs246.groupApp.dndapp;

import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class PresetGenerator {
    private List<Stat> statList;

    //KM:TODO update this as presets are added
    public void generatePresets(File dir) {
        writeFile(dir,"special.txt",SPECIAL);
        writeFile(dir, "dnd.txt", DnD);

    }

    private void writeFile(File dir, String fileName, String content) {
        File outputFile = new File(dir, fileName);

        if (!outputFile.exists()) {
            Log.i("presetCreate", "Created file " + fileName);

            try {
                FileOutputStream outputStream = new FileOutputStream(outputFile);
                outputStream.write(content.getBytes());
            } catch (IOException e) {
                Log.e("presetCreate", "Failed to create file " + fileName);
                e.printStackTrace();
            }
        }
        else {
            Log.i("presetCreate", "Preset file " + fileName + " exists.");
        }
    }

    private static final String SPECIAL = "[\n" +
            "    {\n" +
            "        \"bonus\": 0,\n" +
            "        \"name\": \"strength\",\n" +
            "        \"value\": 0\n" +
            "    },\n" +
            "    {\n" +
            "        \"bonus\": 0,\n" +
            "        \"name\": \"perception\",\n" +
            "        \"value\": 0\n" +
            "    },\n" +
            "    {\n" +
            "        \"bonus\": 0,\n" +
            "        \"name\": \"endurance\",\n" +
            "        \"value\": 0\n" +
            "    },\n" +
            "    {\n" +
            "        \"bonus\": 0,\n" +
            "        \"name\": \"charisma\",\n" +
            "        \"value\": 0\n" +
            "    },\n" +
            "    {\n" +
            "        \"bonus\": 0,\n" +
            "        \"name\": \"intelligence\",\n" +
            "        \"value\": 0\n" +
            "    },\n" +
            "    {\n" +
            "        \"bonus\": 0,\n" +
            "        \"name\": \"agility\",\n" +
            "        \"value\": 0\n" +
            "    },\n" +
            "    {\n" +
            "        \"bonus\": 0,\n" +
            "        \"name\": \"luck\",\n" +
            "        \"value\": 0\n" +
            "    }\n" +
            "]";

    private static final String DnD = "[\n" +
            "    {\n" +
            "        \"bonus\": 0,\n" +
            "        \"name\": \"strength\",\n" +
            "        \"value\": 0\n" +
            "    },\n" +
            "    {\n" +
            "        \"bonus\": 0,\n" +
            "        \"name\": \"dexterity\",\n" +
            "        \"value\": 0\n" +
            "    },\n" +
            "    {\n" +
            "        \"bonus\": 0,\n" +
            "        \"name\": \"constitution\",\n" +
            "        \"value\": 0\n" +
            "    },\n" +
            "    {\n" +
            "        \"bonus\": 0,\n" +
            "        \"name\": \"intelligence\",\n" +
            "        \"value\": 0\n" +
            "    },\n" +
            "    {\n" +
            "        \"bonus\": 0,\n" +
            "        \"name\": \"wisdom\",\n" +
            "        \"value\": 0\n" +
            "    },\n" +
            "    {\n" +
            "        \"bonus\": 0,\n" +
            "        \"name\": \"charisma\",\n" +
            "        \"value\": 0\n" +
            "    }\n" +
            "]";
}

class StatList {
    List<Stat> statList;
    List<Stat> getStatList() { return statList; }
    void setStatList(List<Stat> statList) { this.statList = statList; }
}
