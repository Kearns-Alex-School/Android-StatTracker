package cs246.groupApp.dndapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
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

    // This list will store the strings for our ListView
    private List<String> characterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = MainActivity.this;

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
        {
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
        }

        //Generate all of the preset files, if they don't already exist. KM: this is synchronous, so keep it fast.
        PresetGenerator presetGen = new PresetGenerator();
        presetGen.generatePresets(presetDir);

        // load the characters that we have so far
        toLoad();
    }

    // since we will be loading more than once in different ways, this method does it all
    public void toLoad() {
        // First create the List and the ArrayAdapter
        characterList = new ArrayList<>();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.character_list, characterList);

        // Now connect the ArrayAdapter to the ListView
        ListView listView = findViewById(R.id.characterListView);
        listView.setAdapter(arrayAdapter);

        // listen for clicks on each item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                String selectedItem = (String) parent.getItemAtPosition(position);

                LoadCharacter(selectedItem);
            }
        });

        // Reset the progress bar
        ProgressBar progressBar = findViewById(R.id.mainProgressBar);
        characterList.clear();

        // execute the async load to grab all of our characters
        new LoadList(progressBar, (ListView) findViewById(R.id.characterListView), context).execute();
    }

    // async task that will load our characters into our list
    @SuppressLint("StaticFieldLeak")
    private class LoadList extends AsyncTask<Void, Integer, Void> {
        ProgressBar updatingBar;
        ListView listView;
        Context thisContext;

        // Constructor Method
        LoadList(ProgressBar updatingBar, ListView tempList, Context context) {
            this.updatingBar = updatingBar;
            this.listView = tempList;
            this.thisContext = context;
        }

        @Override
        protected void onPreExecute() {
            updatingBar.setProgress(0);
            updatingBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayAdapter<String> adapter = (ArrayAdapter<String>) listView.getAdapter();

            // grab all of the files in the dir and go through them all
            File[] files = new File(characterDir.getPath()).listFiles();
            for (File file : files)
            {
                // check to make sure the item is not a folder
                if (!file.isDirectory())
                {
                    // for now just add the whole file name.
                    characterList.add(file.getName().replace(".txt", ""));

                    // See how many things are in the adapter, and update the ProgressBar
                    int adapterCountPercent = (adapter.getCount() % files.length) * 100;

                    // call our onProgressUpdate and pass in the current percentage
                    publishProgress(adapterCountPercent);
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... value) {
            // Update the progress bar
            updatingBar.setProgress(value[0]);
        }

        @Override
        protected void onPostExecute(Void result) {
            // Update the progress bar to be complete
            updatingBar.setProgress(100);
            updatingBar.setVisibility(View.INVISIBLE);

            // set our listView's adapter to the one that we have created
            ArrayAdapter<String> adapter = (ArrayAdapter<String>) listView.getAdapter();
            adapter.notifyDataSetChanged();

            // Inform the user that the task is complete
            CommonMethods.showCenterTopToast(thisContext, "Finished Loading Characters", 0);
        }
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
        final EditText input = dialog.findViewById(R.id.input);

        // set up our drop down menu
        final Spinner presetValues = dialog.findViewById(R.id.PresetValues);

        List<String> list = new ArrayList<>();
        list.add("None");

        // grab all of the files in the dir and go through them all
        File[] files = new File(presetDir.getPath()).listFiles();
        for (File file : files)
        {
            // check to make sure the item is not a folder
            if (!file.isDirectory())
                list.add(file.getName().replace(".txt", ""));
        }

        // set out dropdown to the list.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, list);
        presetValues.setAdapter(adapter);

        // AJK: My very first easter egg that I have ever coded. 11/11/2018 23:45
        String[] hints = {"Kevin_Marsh","Jeffery_Hooker","Alex_Kearns"};
        input.setHint(hints[new Random().nextInt(hints.length)]);

        // set up the Create button behavior
        Button b_Create = dialog.findViewById(R.id.Create);
        b_Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // assign our input to our character variable
                String characterName = input.getText().toString();

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

                        String text = presetValues.getSelectedItem().toString();

                        if (!text.equals("None"))
                            newCharacter.loadPreset(text, presetDir);

                        writeFile(fileName, newCharacter);

                        // reload our character list
                        toLoad();

                        // close the screen
                        dialog.dismiss();
                    } else
                        // Inform the user that the file already exists
                        CommonMethods.showCenterTopToast(context, characterName + " already exists.", 0);
                }
            }
        });

        // set up the Delete button
        Button b_Delete = dialog.findViewById(R.id.Delete);
        b_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // assign our input to our character variable
                String newCharacter = input.getText().toString();

                // check to see if we have an empty input
                if (newCharacter.length() != 0) {
                    // create the file name that we are checking
                    String fileName = newCharacter + ".txt";

                    // check to see if the file exists
                    File file = new File(characterDir, fileName);
                    if (file.exists()) {
                        // try to create the file
                        if(!file.delete())
                        {
                            CommonMethods.showCenterTopToast(context, "Error: Could not delete file", 0);
                            return;
                        }

                        // reload our character list
                        toLoad();

                        // close the screen
                        dialog.dismiss();
                    } else {
                        // Inform the user that the file does not exist
                        CommonMethods.showCenterTopToast(context, newCharacter + " does not exist.", 0);
                    }
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

    public void LoadCharacter(String name) {
        // create the name + ".txt" == filename
        String filename = name + ".txt";

        Intent intent = new Intent(this, CharacterDetailsActivity.class);
        intent.putExtra("filename", filename);
        intent.putExtra("charDir", characterDir);
        intent.putExtra("presetDir", presetDir);
        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        toLoad();
    }
}
