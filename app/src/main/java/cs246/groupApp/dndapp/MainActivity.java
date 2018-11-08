package cs246.groupApp.dndapp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    public static File characterDir;
    public static File presetDir;
    public static Context context;
    public ProgressBar progressBar;

    public MainActivity() {
        context = this;
        characterDir = context.getDir("characters", 0);
        presetDir = context.getDir("presets", 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    public static void deserialize(File dir, String json) {
        Gson gson = new Gson();
        if (dir == characterDir) {
            Character character = gson.fromJson(json, Character.class);
        }
        else if (dir == presetDir) {
            //set up with presets
        }
        else {
            Toast.makeText(context, "Error, incompatible file.", Toast.LENGTH_SHORT).show();
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
