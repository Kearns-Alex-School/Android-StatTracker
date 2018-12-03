package cs246.groupApp.dndapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    // used to help us call methods in this class with all of the current variables while we are in our custom layout menu
    private static MainActivity instance;

    // these are our preference names
    public static final String DICE_SIDES = "numSides";
    public static final String DICE_ROLLS = "numRolls";
    public static final String DICE_ROLLONLOAD = "autoRoll";
    public static final String DICE_AUTOSCROLL = "autoScroll";
    public static final String LOAD_CONTENT = "loadContent";
    public static final String DIST_UNIT = "distUnit";

    //directories
    public static File characterDir;
    public static File presetDir;

    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = MainActivity.this;
        instance = this;

        // set the file path. Create it if it does not exist
        // https://stackoverflow.com/questions/16237950/android-check-if-file-exists-without-creating-a-new-one
        //      this will look at the file directory for a child called "" either dir or file
        // https://developer.android.com/reference/java/io/File.html#mkdir()
        //      this is why we use the mkdir() function
        characterDir = new File(this.getFilesDir(), "characters");
        if (!characterDir.exists()) {
            if (!characterDir.mkdir())
                CommonMethods.showCenterTopToast(context,"Error creating Character Directory.", 0);
        }

        presetDir = new File(this.getFilesDir(), "presets");
        if (!presetDir.exists()) {
            if(!presetDir.mkdir())
                CommonMethods.showCenterTopToast(context,"Error creating Preset Directory.", 0);
        }

        // AJK: debugging to make sure we have files/folders in our ROOT
        // https://blog.cindypotvin.com/saving-data-to-a-file-in-your-android-application/ w/ alterations to create dir and local not external
        /*{
            // the path to our ROOT location
            String pathRoot = context.getFilesDir().getPath();

            // holds all of our files in the ROOT
            ArrayList<File> inFiles = new ArrayList<>();

            // holds all of our dirs in the ROOT
            ArrayList<File> inDirs = new ArrayList<>();

            // grab all of the files/dirs in the ROOT location
            File[] files = new File(pathRoot).listFiles();
            for (File file : files)
            {
                // separate what is what
                if (file.isDirectory())
                    inDirs.add(file);
                else
                    inFiles.add(file);
            }
        }*/

        //Generate all of the preset files, if they don't already exist. KM: this is synchronous, so keep it fast.
        PresetGenerator presetGen = new PresetGenerator();
        presetGen.generatePresets(presetDir);

        // load the characters that we have so far
        toLoad();
    }

    // since we will be loading more than once in different ways, this method does it all
    public void toLoad() {
        // grab the listview
        ListView listView = findViewById(R.id.characterListView);

        // create the arraylist for the adapter
        final ArrayList<CharacterDataModel> characterModels = new ArrayList<>();

        // create the adapter
        CharacterAdapter characterAdapter = new CharacterAdapter(characterModels, context);

        // grab all of the files in the character directory and add each character
        File[] files = new File(characterDir.getPath()).listFiles();

        for (File file : files)
            characterModels.add(new CharacterDataModel(readFile(file.getName())));

        // set the listview to our newly created adapter
        listView.setAdapter(characterAdapter);

        //https://stackoverflow.com/questions/6703390/listview-setonitemclicklistener-not-working-by-adding-button
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                CharacterDataModel dataModel= characterModels.get(position);

                LoadCharacter(dataModel.getCharacter().fileName);
            }
        });

        // Inform the user that the task is complete
        CommonMethods.showCenterTopToast(context, "Finished Loading Characters", 0);
    }

    public Character readFile(String filename) {
        // KM: not doing async right now since files are small.
        File file = new File(characterDir, filename);
        String contentJson = null;

        //create character object
        Character character;

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

        // if file has contents, deserialize, otherwise create new character
        if (contentJson != null && !contentJson.equals("")) {
            Gson gson = new Gson();
            character = gson.fromJson(contentJson, Character.class);
        }
        else
            character = new Character();

        //save filename for later
        character.fileName = filename;

        return character;
    }

    // another way to use a custom layout [preferred way]
    // https://stackoverflow.com/questions/4016313/how-to-keep-an-alertdialog-open-after-button-onclick-is-fired
    public void addCharacter(View view) {
        // create our popup dialog
        final Dialog dialog = new Dialog(context);

        // have the keyboard show up once we have the ability to add a name
        //  https://stackoverflow.com/questions/4258623/show-soft-keyboard-for-dialog
        Objects.requireNonNull(dialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        // remove the title bar MUST BE CALLED BEFORE SETTING THE CONTENT VIEW!!
        // https://stackoverflow.com/questions/2644134/android-how-to-create-a-dialog-without-a-title
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.new_character);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);

        // set up the EditText behavior
        final EditText inputName = dialog.findViewById(R.id.Message);
        final EditText inputNotes = dialog.findViewById(R.id.notesValue);

        // set up our drop down menu
        final Spinner presetValues = dialog.findViewById(R.id.PresetValues);

        List<String> list = new ArrayList<>();
        list.add("None");

        // grab all of the files in the preset directory and add them
        File[] files = new File(presetDir.getPath()).listFiles();

        for (File file : files)
            list.add(file.getName().replace(".txt", ""));

        // set out dropdown to the list.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, list);
        presetValues.setAdapter(adapter);

        // AJK: My very first easter egg that I have ever coded. 11/11/2018 23:45
        String[] hints = {"Kevin_Marsh","Jeffery_Hooker","LegendOfTechno"};
        inputName.setHint(hints[new Random().nextInt(hints.length)]);

        // set up the Create button behavior
        Button b_Create = dialog.findViewById(R.id.Create);
        b_Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // assign our input to our character variable
                String characterName = inputName.getText().toString();
                String characterNotes = inputNotes.getText().toString();

                // check to see if we have an empty input
                if (characterName.length() != 0) {
                    // create the file name that we are checking
                    String fileName = characterName + ".txt";

                    // check to see if the file exists
                    File file = new File(characterDir, fileName);
                    if (!file.exists()) {
                        // create our new character
                        Character newCharacter = new Character();
                        newCharacter.fileName = fileName;
                        newCharacter.name = characterName;
                        newCharacter.notes = characterNotes;

                        String text = presetValues.getSelectedItem().toString();

                        if (!text.equals("None"))
                            newCharacter.loadPreset(text, presetDir);

                        writeFile(fileName, newCharacter);

                        // close the screen
                        dialog.dismiss();
                    } else
                        // Inform the user that the file already exists
                        CommonMethods.showCenterTopToast(context, characterName + " already exists.", 0);
                }
            }
        });

        // set up the Cancel button
        Button b_Cancel = dialog.findViewById(R.id.Cancel);
        b_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // close the screen
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void deleteCharacter(Character character) {
        // check to see if the file exists
        File file = new File(characterDir, character.fileName);
        if (file.exists()) {
            // try to create the file
            if(!file.delete())
            {
                CommonMethods.showCenterTopToast(context, "Error: Could not delete file", 0);
                return;
            }

            // reload our character list
            toLoad();
        } else
            // Inform the user that the file does not exist
            CommonMethods.showCenterTopToast(context, character.name + " does not exist.", 0);
    }

    public void writeFile(String filename, Character character) {
        Gson gson = new Gson();
        String json = gson.toJson(character);

        File outputFile = new File(characterDir, filename);

        try {
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            outputStream.write(json.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // reload our character list
        toLoad();
    }

    public void about(View view) {
        // create our popup dialog
        final Dialog dialog = new Dialog(context);

        // remove the title bar MUST BE CALLED BEFORE SETTING THE CONTENT VIEW!!
        // https://stackoverflow.com/questions/2644134/android-how-to-create-a-dialog-without-a-title
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.legal);
        Objects.requireNonNull(dialog.getWindow()).setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);

        // set up the Cancel button
        Button b_Close = dialog.findViewById(R.id.close);
        b_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // close the screen
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void openPreferences(View view) {
        Intent intent = new Intent(this, PreferencesActivity.class);
        startActivity(intent);
    }

    public void LoadCharacter(String filename) {
        Intent intent = new Intent(this, CharacterDetailsActivity.class);
        intent.putExtra("filename", filename);
        intent.putExtra("charDir", characterDir);
        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        toLoad();
    }

    public static MainActivity getInstance() {
        return instance;
    }
}
