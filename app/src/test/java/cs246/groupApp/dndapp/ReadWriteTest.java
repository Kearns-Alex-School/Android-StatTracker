package cs246.groupApp.dndapp;

import android.widget.ProgressBar;

import com.google.gson.Gson;

import org.junit.Test;

import java.io.File;
import java.util.concurrent.ExecutionException;

import static cs246.groupApp.dndapp.MainActivity.context;

public class ReadWriteTest {

    //this test doesn't work...
    @Test
    public void ReadWrite() {
        ProgressBar progressBar = null;
//        File dir = context.getDir("characters", 0);
        File dir = new File("/");
        //write known object to JSON, write to file, read from file, convert to JSON, test strings

        Character character = new Character("test",10,10,10,10);
        Gson gson = new Gson();
        String jsonOrig = gson.toJson(character);

        new WriteFileTask(context, progressBar, dir).execute(jsonOrig);

        String jsonAfter = null;
        try {
            jsonAfter = new ReadFileTask(context,progressBar, dir).execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        assert jsonAfter != null;
        assert (jsonAfter.equals(jsonOrig));

    }
}
