package cs246.groupApp.dndapp;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Preset Generator Class. Uses static strings found in this file to generate the preset JSON
 * files.
 * @author Kevin Marsh
 */
class PresetGenerator {

    /**
     * Generate presets function. Abstracts the act of generating the preset files into one function
     * call. This function should be updated if/when new presets are added to the file.
     * @param dir A File object containing the directory in which the preset files should be stored.
     * @author Kevin Marsh
     */
    //KM:TODO update this as presets are added
    void generatePresets(File dir) {
        writeFile(dir,"special.txt",SPECIAL);
        writeFile(dir, "dnd.txt", DnD);
    }

    /**
     * WriteFile function. Called only by GeneratePresets. Writes the specified file and content.
     * @see #generatePresets(File)
     * @param dir A File object containing the directory to write to.
     * @param fileName A String containing the name of the file to be written.
     * @param content A String containing the text to be written.
     * @author Kevin Marsh
     */
    private void writeFile(File dir, String fileName, String content) {
        File outputFile = new File(dir, fileName);

        Log.i("presetCreate", "Created file " + fileName);

        try {
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            outputStream.write(content.getBytes());
        } catch (IOException e) {
            Log.e("presetCreate", "Failed to create file " + fileName);
            e.printStackTrace();
        }
    }

    /**
     * The SPECIAL preset is used in the Interplay/Bethesda series Fallout.
     */
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

    /**
     * This is the traditional Dungeons and Dragons preset.
     */
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
