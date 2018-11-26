package cs246.groupApp.dndapp;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

class PresetGenerator {

    //KM:TODO update this as presets are added
    void generatePresets(File dir) {
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

    private static final String SPECIAL =
"[\n" +
"    {\n" +
"        \"bonus\": 0,\n" +
"        \"name\": \"Strength\",\n" +
"        \"value\": 0\n" +
"    },\n" +
"    {\n" +
"        \"bonus\": 0,\n" +
"        \"name\": \"Perception\",\n" +
"        \"value\": 0\n" +
"    },\n" +
"    {\n" +
"        \"bonus\": 0,\n" +
"        \"name\": \"Endurance\",\n" +
"        \"value\": 0\n" +
"    },\n" +
"    {\n" +
"        \"bonus\": 0,\n" +
"        \"name\": \"Charisma\",\n" +
"        \"value\": 0\n" +
"    },\n" +
"    {\n" +
"        \"bonus\": 0,\n" +
"        \"name\": \"Intelligence\",\n" +
"        \"value\": 0\n" +
"    },\n" +
"    {\n" +
"        \"bonus\": 0,\n" +
"        \"name\": \"Agility\",\n" +
"        \"value\": 0\n" +
"    },\n" +
"    {\n" +
"        \"bonus\": 0,\n" +
"        \"name\": \"Luck\",\n" +
"        \"value\": 0\n" +
"    }\n" +
"]";

    private static final String DnD =
"[\n" +
"    {\n" +
"        \"bonus\": 0,\n" +
"        \"name\": \"Strength\",\n" +
"        \"value\": 0\n" +
"    },\n" +
"    {\n" +
"        \"bonus\": 0,\n" +
"        \"name\": \"Dexterity\",\n" +
"        \"value\": 0\n" +
"    },\n" +
"    {\n" +
"        \"bonus\": 0,\n" +
"        \"name\": \"Constitution\",\n" +
"        \"value\": 0\n" +
"    },\n" +
"    {\n" +
"        \"bonus\": 0,\n" +
"        \"name\": \"Intelligence\",\n" +
"        \"value\": 0\n" +
"    },\n" +
"    {\n" +
"        \"bonus\": 0,\n" +
"        \"name\": \"Wisdom\",\n" +
"        \"value\": 0\n" +
"    },\n" +
"    {\n" +
"        \"bonus\": 0,\n" +
"        \"name\": \"Charisma\",\n" +
"        \"value\": 0\n" +
"    }\n" +
"]";
}
