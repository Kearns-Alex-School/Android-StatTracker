package cs246.groupApp.dndapp;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public static File characterDir;
    public static File presetDir;
    public Context context;
    public ProgressBar progressBar;

    // This list will store the strings for our ListView
    private List<String> characterList;

    // This will help us connect the List to the ListView
    private ArrayAdapter<String> arrayAdapter;

    public MainActivity() {
        context = MainActivity.this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set the file path. Create it if it does not exist
        // https://stackoverflow.com/questions/16237950/android-check-if-file-exists-without-creating-a-new-one
        //      this will look at the file directory for a child called "" either dir or file
        // https://developer.android.com/reference/java/io/File.html#mkdir()
        //      this is why we use the mkdir() function
        characterDir = new File(this.getFilesDir(), "characters");
        if (!characterDir.exists())
            characterDir.mkdir();

        presetDir = new File(this.getFilesDir(), "presets");
        if (!presetDir.exists())
            presetDir.mkdir();


        // AJK: debugging to make sure we have files/folders in our ROOT
        // https://blog.cindypotvin.com/saving-data-to-a-file-in-your-android-application/ w/ alterations to create dir and local not external
        {
            // the path to our ROOT location
            String pathRoot = context.getFilesDir().getPath();

            // holds all of our files in the ROOT
            ArrayList<File> inFiles = new ArrayList<File>();

            // holds all of our dirs in the ROOT
            ArrayList<File> inDirs = new ArrayList<File>();

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

        // load the characters that we have so far
        toLoad();
    }

    // since we will be loading more than once in different ways, this method does it all
    public void toLoad() {
        // First create the List and the ArrayAdapter
        characterList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.character_list, characterList);

        // Now connect the ArrayAdapter to the ListView
        ListView listView = findViewById(R.id.characterListView);
        listView.setAdapter(arrayAdapter);

        // Reset the progress bar
        ProgressBar progressBar = findViewById(R.id.mainProgressBar);
        progressBar.setProgress(0);
        characterList.clear();

        new LoadList(progressBar, (ListView) findViewById(R.id.characterListView), context).execute();
    }

    // async task that will load our characters into our list
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

        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayAdapter<String> adapter = (ArrayAdapter<String>) listView.getAdapter();

            //File[] files = characterDir.listFiles();
            File[] files = new File(characterDir.getPath()).listFiles();
            for (File file : files)
            {
                // check to make sure the item is not a folder
                if (!file.isDirectory())
                {
                    // for now just add the whole file name.
                    // AJK: Need to make a substring without the .txt extension
                    characterList.add(file.getName());

                    // See how many things are in the adapter, and update the ProgressBar
                    int adapterCount = (adapter.getCount() % files.length) * 100;

                    // Simulate a more difficult task by sleeping for 1/4 second
                    //Thread.sleep(250);
                    publishProgress(adapterCount);
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

            ArrayAdapter<String> adapter = (ArrayAdapter<String>) listView.getAdapter();
            adapter.notifyDataSetChanged();

            // Inform the user that the task is complete
            Toast.makeText(thisContext, "Finished Loading Characters", Toast.LENGTH_SHORT).show();
        }
    }

    // used to help us create a new character
    private String newCharacter = "";

    // alert window that pops up and asks the user to enter a new character name
    // https://stackoverflow.com/questions/10903754/input-text-dialog-android w/ alterations to our own need
    public void addCharacter(View view) {
        // we need to make a layout that can be placed on top of our current screen
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Enter your character's name");

        // create the view that will become the overlay on the current activity
        View viewInflated = LayoutInflater.from(context).inflate(R.layout.new_character, (ViewGroup) findViewById(android.R.id.content), false);

        // Set up the input
        final EditText input = viewInflated.findViewById(R.id.input);

        // AJK: My very first easter egg that I have ever coded. 11/11/2018 23:45
        String[] hints = {"Kevin_Marsh","Jeffery_Hooker","Alex_Kearns"};
        input.setHint(hints[new Random().nextInt(hints.length)]);

        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        builder.setView(viewInflated);

        // Set up the buttons
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // assign out input to our character variable
                newCharacter = input.getText().toString();

                // check to see if we have an empty input
                if (newCharacter.length() != 0)
                {
                    // create the file path that we are checking

                    String path = newCharacter + ".txt";

                    // AJK: CHECK TO SEE IF THE FILE EXISTS ALREADY
                    File file = new File(characterDir, path);
                    if(!file.exists())
                    {
                        try {
                            file.createNewFile();

                            // reload our character list
                            toLoad();

                            // reset our character flag
                            newCharacter = "";
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "An error occurred.", Toast.LENGTH_SHORT).show();
                        }

                        // close the screen
                        dialog.dismiss();
                    }
                    else
                    {
                        // Inform the user that the file already exists
                        Toast.makeText(context, newCharacter + " already exists.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // reset our character flag
                newCharacter = "";

                // close the screen
                dialog.cancel();
            }
        });

        // show our alert to the user
        builder.show();
    }

//    create or load character
//    save character
//async!!
    // unit test read/write files
//    unit test input validation scripts
//    https://byui-cs.github.io/CS246/week-08/teach.html

//    get list of all files in local dir
//    https://developer.android.com/training/data-storage/files

//    files can be deleted by context.deleteFile(fileName);

//    context.getDir(name,mode) can be used to create files. Returns a File object.

    public static void deserialize(File dir, String json, Context contextFromAsync) {
        Gson gson = new Gson();
        if (dir == characterDir) {
            Character character = gson.fromJson(json, Character.class);
        }
        else if (dir == presetDir) {
            //set up with presets
        }
        else {
            Toast.makeText(contextFromAsync, "Error, incompatible file.", Toast.LENGTH_SHORT).show();
        }
    }

    public void serializeCharacter(Character character) {
        Gson gson = new Gson();
        String json = gson.toJson(character);

        new WriteFileTask(this, progressBar, characterDir);
    }

    public void serializePreset() {
        //serialize preset
    }
}
